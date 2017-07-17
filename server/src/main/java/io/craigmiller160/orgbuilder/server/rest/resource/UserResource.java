package io.craigmiller160.orgbuilder.server.rest.resource;

import io.craigmiller160.orgbuilder.server.OrgApiException;
import io.craigmiller160.orgbuilder.server.dto.ErrorDTO;
import io.craigmiller160.orgbuilder.server.dto.UserDTO;
import io.craigmiller160.orgbuilder.server.dto.UserListDTO;
import io.craigmiller160.orgbuilder.server.rest.OrgApiInvalidRequestException;
import io.craigmiller160.orgbuilder.server.rest.OrgApiPrincipal;
import io.craigmiller160.orgbuilder.server.rest.Role;
import io.craigmiller160.orgbuilder.server.rest.UserFilterBean;
import io.craigmiller160.orgbuilder.server.rest.annotation.ThisUserAllowed;
import io.craigmiller160.orgbuilder.server.service.ServiceFactory;
import io.craigmiller160.orgbuilder.server.service.UserService;
import io.swagger.annotations.*;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;
import java.net.URI;

/**
 * The resource for retrieving and manipulating
 * user records.
 *
 * NOTE: This resource has more complex authentication
 * than others. In addition to the annotations, a great
 * deal of authentication is done in this class as well.
 * In addition, some UserService methods have their own,
 * internal validation that affects what kind of results
 * are returned, based on the principal's access.
 *
 * Created by craig on 9/15/16.
 */
@SwaggerDefinition(info = @Info(title = "OrgBuilder API", version = "1.1-ALPHA", description = "The API for the data managed by the OrgBuilder application"),
        securityDefinition = @SecurityDefinition(
                apiKeyAuthDefinitions = @ApiKeyAuthDefinition(
                        in = ApiKeyAuthDefinition.ApiKeyLocation.HEADER,
                        key = "orgapiToken",
                        name= "Authorization",
                        description = "The JSON Web Token needed to access the API"
                )
        )
)
@ApiResponses(value = {
        @ApiResponse(code = 403, message = "Access to resource is forbidden, you are either not logged in or don't have a high enough access level", response = ErrorDTO.class),
        @ApiResponse(code = 500, message = "Server error while processing request", response = ErrorDTO.class)
})
@Api (tags = "users", authorizations = @Authorization(value = "orgapiToken"))
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/users")
public class UserResource {

    private final ServiceFactory factory = ServiceFactory.newInstance();

    @Context
    private SecurityContext securityContext;

    @Context
    private UriInfo uriInfo;

    @ApiOperation(value = "Get All Users",
            notes = "Get all users, either for a specific org or the entire application.\n" +
                    "ACCESS:\n" +
                    "Role: MASTER, ADMIN\n" +
                    "Extra: ADMIN users can only get users in their own org.",
            response = UserListDTO.class)
    @ApiResponses(value = @ApiResponse(code = 204, message = "No users were found to return."))
    @GET
    @RolesAllowed({Role.MASTER, Role.ADMIN})
    public Response getAllUsers(@BeanParam UserFilterBean userFilterBean) throws OrgApiException {
        userFilterBean.validateFilterParams();
        //TODO FC-13
        UserService service = factory.newUserService(securityContext);

        //If the principal has an orgId, they must have an Admin role (due to annotation restriction)
        //The service will restrict results to only users in their org.
        UserListDTO results = service.getAllUsers(getPrincipalOrgId(), userFilterBean.getOffset(), userFilterBean.getSize());

        if(results != null){
            return Response
                    .ok(results)
                    .build();
        }

        return Response
                .noContent()
                .build();
    }

    @ApiOperation(value = "Add New User",
            notes = "Add a new user, either to the application or a specific org.\n" +
                    "ACCESS:\n" +
                    "Role: MASTER, ADMIN\n" +
                    "Extra: ADMIN users can only add to their own org.",
            response = UserDTO.class,
            code = 201)
    @POST
    @RolesAllowed({Role.MASTER, Role.ADMIN})
    public Response addUser(@ApiParam(value = "The user to add", required = true) UserDTO user) throws OrgApiException{
        validateUser(user, true);
        ensureMasterCreationRestriction(user);
        ensureAdminAccessRestriction(user);

        UserService service = factory.newUserService(securityContext);
        UserDTO result = service.addUser(user);

        return Response
                .created(URI.create(uriInfo.getPath() + "/" + user.getElementId()))
                .entity(result)
                .build();
    }

    @ApiOperation(value = "Update User",
            notes = "Update an existing user, either for the application or a specific org.\n" +
                    "ACCESS:\n" +
                    "Role: MASTER, ADMIN\n" +
                    "Extra: ADMIN users can only update for their own org.",
            response = UserDTO.class,
            code = 202)
    @ApiResponses(value = @ApiResponse(code = 204, message = "User to update did not exist."))
    @PUT
    @Path("/{userId}")
    @ThisUserAllowed(otherUserRolesAllowed = {Role.ADMIN,Role.MASTER})
    public Response updateUser(@ApiParam(value = "The ID of the user to update", required = true) @PathParam("userId") long userId,
                               @ApiParam(value = "The updated user.", required = true)  UserDTO user) throws OrgApiException{
        validateUser(user, false);
        ensureMasterCreationRestriction(user);
        UserService service = factory.newUserService(securityContext);

        UserDTO result = service.updateUser(user, userId, (u) -> {
            ensureAdminAccessRestriction(u);
            blockRegularUserRoleChanging(user, u);
        });

        if(result != null){
            return Response
                    .accepted(result)
                    .build();
        }

        return Response
                .noContent()
                .build();
    }

    @ApiOperation(value = "Delete User",
            notes = "Delete an existing user, either from the application or a specific org.\n" +
                    "ACCESS:\n" +
                    "Role: MASTER, ADMIN\n" +
                    "Extra: ADMIN users can only delete from their own org.",
            response = UserDTO.class,
            code = 202)
    @ApiResponses(value = @ApiResponse(code = 204, message = "User to delete didn't exist."))
    @DELETE
    @Path("/{userId}")
    @ThisUserAllowed(otherUserRolesAllowed = {Role.ADMIN, Role.MASTER})
    public Response deleteUser(@ApiParam(value = "The ID of the user to delete", required = true) @PathParam("userId") long userId) throws OrgApiException{
        UserService service = factory.newUserService(securityContext);

        UserDTO result = service.deleteUser(userId, this::ensureAdminAccessRestriction);

        if(result != null){
            return Response
                    .accepted(result)
                    .build();
        }

        return Response
                .noContent()
                .build();
    }

    @ApiOperation(value = "Get User",
            notes = "Retrieve a user, either from the application or a specific org.\n" +
                    "ACCESS:\n" +
                    "Role: MASTER, ADMIN\n" +
                    "Extra: ADMIN users can only get users from their own org.",
            response = UserDTO.class)
    @ApiResponses(value = @ApiResponse(code = 204, message = "User to retrieve does not exist."))
    @GET
    @Path("/{userId}")
    @ThisUserAllowed(otherUserRolesAllowed = {Role.ADMIN, Role.MASTER})
    public Response getUser(@ApiParam(value = "The ID of the user to retrieve", required = true) @PathParam("userId") long userId) throws OrgApiException{
        UserService service = factory.newUserService(securityContext);
        UserDTO result = service.getUser(userId);
        ensureAdminAccessRestriction(result);

        if(result != null){
            return Response
                    .ok(result)
                    .build();
        }

        return Response
                .noContent()
                .build();
    }

    private void validateUser(UserDTO user, boolean requirePassword) throws OrgApiInvalidRequestException{
        if(StringUtils.isEmpty(user.getUserEmail())){
            throw new OrgApiInvalidRequestException("User is invalid, missing email field");
        }

        if(StringUtils.isEmpty(user.getPassword()) && requirePassword){
            throw new OrgApiInvalidRequestException("User is invalid, missing password field");
        }

        if(user.getOrgId() <= 0 && !(user.getRoles() != null && user.getRoles().contains(Role.MASTER))){
            throw new OrgApiInvalidRequestException("User is invalid, doesn't have an org assignment");
        }
    }

    private void ensureMasterCreationRestriction(UserDTO user) {
        if(user.getRoles().contains(Role.MASTER) && !isPrincipalMaster()){
            throw new ForbiddenException("Only users with Master access can give a user Master access");
        }

        if(user.getRoles().contains(Role.MASTER) && (user.getRoles().size() != 1 || user.getOrgId() > 0)){
            throw new ForbiddenException("Cannot add a user with Master role that has other roles or an Org assignment.");
        }
    }

    private long getPrincipalOrgId(){
        OrgApiPrincipal principal = (OrgApiPrincipal) securityContext.getUserPrincipal();
        return principal.getOrgId();
    }

    private boolean isPrincipalAdmin(){
        OrgApiPrincipal principal = (OrgApiPrincipal) securityContext.getUserPrincipal();
        return principal.isUserInRole(Role.ADMIN);
    }

    private boolean isPrincipalMaster(){
        OrgApiPrincipal principal = (OrgApiPrincipal) securityContext.getUserPrincipal();
        return principal.isUserInRole(Role.MASTER);
    }

    private void ensureAdminAccessRestriction(UserDTO user){
        if(isPrincipalAdmin() && getPrincipalOrgId() != user.getOrgId()){
            throw new ForbiddenException("Admin user cannot access a user outside of their own org");
        }
    }

    private void blockRegularUserRoleChanging(UserDTO newUser, UserDTO existingUser){
        if((!isPrincipalAdmin() && !isPrincipalMaster()) && !newUser.getRoles().equals(existingUser.getRoles())){
            throw new ForbiddenException("Non-Admin user cannot change user roles");
        }
    }

}
