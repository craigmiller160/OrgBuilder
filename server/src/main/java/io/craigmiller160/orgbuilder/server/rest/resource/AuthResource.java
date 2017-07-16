package io.craigmiller160.orgbuilder.server.rest.resource;

import io.craigmiller160.orgbuilder.server.OrgApiException;
import io.craigmiller160.orgbuilder.server.ServerCore;
import io.craigmiller160.orgbuilder.server.ServerProps;
import io.craigmiller160.orgbuilder.server.data.jdbc.SchemaManager;
import io.craigmiller160.orgbuilder.server.dto.*;
import io.craigmiller160.orgbuilder.server.logging.OrgApiLogger;
import io.craigmiller160.orgbuilder.server.rest.JWTUtil;
import io.craigmiller160.orgbuilder.server.rest.OrgApiInvalidRequestException;
import io.craigmiller160.orgbuilder.server.rest.RefreshTokenUtil;
import io.craigmiller160.orgbuilder.server.rest.Role;
import io.craigmiller160.orgbuilder.server.service.OrgService;
import io.craigmiller160.orgbuilder.server.service.ServiceFactory;
import io.craigmiller160.orgbuilder.server.service.TokenService;
import io.craigmiller160.orgbuilder.server.service.UserService;
import io.craigmiller160.orgbuilder.server.util.HashingUtils;
import io.swagger.annotations.*;
import io.swagger.core.filter.SwaggerSpecFilter;
import io.swagger.model.ApiDescription;
import io.swagger.models.Model;
import io.swagger.models.Operation;
import io.swagger.models.parameters.Parameter;
import io.swagger.models.properties.Property;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.security.PermitAll;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * RESTful API for handling the authentication of users.
 *
 * Created by craig on 9/27/16.
 */
@SwaggerDefinition(
        info = @Info(
                title = "OrgBuilder API",
                version = "1.1-ALPHA",
                description = "The API for the data managed by the OrgBuilder application"
        ),
        securityDefinition = @SecurityDefinition(
                apiKeyAuthDefinitions = {
                        @ApiKeyAuthDefinition(
                                in = ApiKeyAuthDefinition.ApiKeyLocation.HEADER,
                                key = "orgapiToken",
                                name= "Authorization",
                                description = "The JSON Web Token needed to access the API"
                        )
                }
        )
)
@ApiResponses(
        value = {
                @ApiResponse(
                        code = 403,
                        message = "Access to resource is forbidden, you are either not logged in or don't have a high enough access level",
                        response = ErrorDTO.class
                )
        }
)
@Api (
        tags = "auth",
        authorizations = {
                @Authorization(value = "orgapiToken")
        }
)
@Path("/auth")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AuthResource {

    private final ServiceFactory factory = ServiceFactory.newInstance();

    @Context
    private SecurityContext securityContext;

    private Response handleInvalidLogin(String errorMessage){
        ErrorDTO error = new ErrorDTO();
        error.setStatusCode(Response.Status.UNAUTHORIZED.getStatusCode());
        if(!StringUtils.isEmpty(errorMessage)){
            error.setErrorMessage(errorMessage);
        }
        return Response
                .status(Response.Status.UNAUTHORIZED)
                .entity(error)
                .build();
    }

    private Response handleInvalidLogin(){
        return handleInvalidLogin(null);
    }

    @ApiOperation(
            value = "Login to API",
            notes = "Send a payload with username/password and receive back an access token for the API."
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            code = 200,
                            message = "The login attempt was successful, the token will be in the Authorization header"
                    ),
                    @ApiResponse(
                            code = 401,
                            message = "The login attempt was unsuccessful",
                            response = ErrorDTO.class
                    )
            }
    )
    @POST
    @PermitAll
    public Response authenticate(
            @ApiParam(value = "User-Agent value is used in providing a unique token key for logins from different devices") @HeaderParam("user-agent") String userAgent,
            @ApiParam(value = "The user information for the login, in this case only username/password are required", required = true) LoginDTO user)
            throws OrgApiException{
        if(user == null || StringUtils.isEmpty(user.getUserEmail()) || StringUtils.isEmpty(user.getPassword())){
            throw new OrgApiInvalidRequestException("Authentication request has incomplete credentials");
        }

        UserService userService = factory.newUserService(securityContext);
        UserDTO foundUser = userService.getUserByName(user.getUserEmail());
        if(foundUser == null){
            OrgApiLogger.getRestLogger().warn("Attempted login by non-existent user: " + user.getUserEmail());
            return handleInvalidLogin();
        }

        OrgService orgService = factory.newOrgService(securityContext);
        TokenService tokenService = factory.newTokenService(securityContext);

        if(!HashingUtils.verifyBCryptHash(user.getPassword(), foundUser.getPassword())){
            OrgApiLogger.getRestLogger().warn("Attempted login with invalid password. User: " + user.getUserEmail());
            return handleInvalidLogin();
        }

        //If a NumberFormatException ever happens here, the property is invalid
        int refreshExpHrs = Integer.parseInt(ServerCore.getProperty(ServerProps.REFRESH_MAX_EXP_HRS));
        LocalDateTime expiration = LocalDateTime.now().plusHours(refreshExpHrs);
        String tokenHash = RefreshTokenUtil.generateRefreshTokenHash(foundUser.getUserEmail(), userAgent);
        RefreshTokenDTO refreshToken = new RefreshTokenDTO(foundUser.getElementId(), foundUser.getOrgId(), tokenHash, expiration);
        refreshToken = tokenService.addRefreshToken(refreshToken);

        OrgDTO foundOrg = orgService.getOrg(foundUser.getOrgId());
        if(foundOrg == null && !foundUser.getRoles().contains(Role.MASTER)){
            OrgApiLogger.getRestLogger().error("User is not assigned to an org and doesn't have master access. UserID: " + foundUser.getElementId() + " UserName: " + foundUser.getUserEmail());
            return handleInvalidLogin("User is improperly configured on server.");
        }

        String token = JWTUtil.generateNewToken(
                refreshToken.getElementId(),
                foundUser.getUserEmail(),
                foundOrg != null ? foundOrg.getOrgName() : "",
                foundUser.getElementId(),
                foundOrg != null ? foundOrg.getElementId() : 0,
                foundOrg != null ? foundOrg.getSchemaName() : SchemaManager.DEFAULT_APP_SCHEMA_NAME,
                foundUser.getRoles()
        );
        return Response
                .ok()
                .header(HttpHeaders.AUTHORIZATION, JWTUtil.BEARER_PREFIX + " " + token)
                .build();
    }

    @ApiOperation(
            value = "Validate the access token",
            notes = "This just validates that the token is still valid, and refreshes it where appropriate. If it's not valid, the SecurityFilter will reject it automatically"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            code = 200,
                            message = "The token is still valid, it has been refreshed if necessary, and is returned in the Authorization header."
                    )
            }
    )
    @GET
    @Path("/check")
    @PermitAll
    public Response checkStillValid() throws OrgApiException{
        return Response
                .ok()
                .build();
    }


    @ApiOperation(
            value = "Check if user name exists",
            notes = "This checks whether or not a given user name already exists in the system."
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            code = 200,
                            message = "A user with that name does not exist"
                    ),
                    @ApiResponse(
                            code = 409,
                            message = "A user with that name already exists",
                            response = UserDTO.class
                    )
            }
    )
    @GET
    @Path("/exists")
    @PermitAll
    public Response userExists(
            @ApiParam(value = "The username to check for", required = true) @QueryParam("userName") String userName) throws OrgApiException{
        UserService userService = factory.newUserService(securityContext);
        UserDTO result = userService.getUserByName(userName);

        if(result != null){
            UserDTO resp = new UserDTO();
            resp.setUserEmail(userName);
            return Response
                    .status(Response.Status.CONFLICT)
                    .entity(resp)
                    .build();
        }

        return Response
                .ok()
                .build();
    }

}
