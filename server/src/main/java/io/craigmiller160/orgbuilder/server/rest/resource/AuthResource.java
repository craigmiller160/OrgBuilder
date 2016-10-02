package io.craigmiller160.orgbuilder.server.rest.resource;

import io.craigmiller160.orgbuilder.server.OrgApiException;
import io.craigmiller160.orgbuilder.server.ServerCore;
import io.craigmiller160.orgbuilder.server.ServerProps;
import io.craigmiller160.orgbuilder.server.dto.OrgDTO;
import io.craigmiller160.orgbuilder.server.dto.RefreshTokenDTO;
import io.craigmiller160.orgbuilder.server.dto.UserDTO;
import io.craigmiller160.orgbuilder.server.rest.JWTUtil;
import io.craigmiller160.orgbuilder.server.rest.OrgApiInvalidRequestException;
import io.craigmiller160.orgbuilder.server.rest.RefreshTokenUtil;
import io.craigmiller160.orgbuilder.server.rest.Role;
import io.craigmiller160.orgbuilder.server.service.OrgService;
import io.craigmiller160.orgbuilder.server.service.ServiceFactory;
import io.craigmiller160.orgbuilder.server.service.TokenService;
import io.craigmiller160.orgbuilder.server.service.UserService;
import io.craigmiller160.orgbuilder.server.util.HashingUtils;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.DELETE;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.time.LocalDateTime;

/**
 * Created by craig on 9/27/16.
 */
@Path("/auth")
public class AuthResource {

    private final ServiceFactory factory = ServiceFactory.newInstance();

    @Context
    private SecurityContext securityContext;

    @POST
    @PermitAll
    public Response authenticate(@HeaderParam("user-agent") String userAgent, UserDTO user) throws OrgApiException{
        if(StringUtils.isEmpty(user.getUserEmail()) || StringUtils.isEmpty(user.getPassword())){
            throw new OrgApiInvalidRequestException("Authentication request has incomplete credentials");
        }

        UserService userService = factory.newUserService(securityContext);
        UserDTO foundUser = userService.getUserByName(user.getUserEmail());
        if(foundUser != null){
            OrgService orgService = factory.newOrgService(securityContext);
            TokenService tokenService = factory.newTokenService(securityContext);

            OrgDTO foundOrg = orgService.getOrg(foundUser.getOrgId());
            if(HashingUtils.verifyBCryptHash(user.getPassword(), foundUser.getPassword())){
                //If a NumberFormatException ever happens here, the property is invalid
                int refreshExpHrs = Integer.parseInt(ServerCore.getProperty(ServerProps.REFRESH_MAX_EXP_HRS));
                LocalDateTime expiration = LocalDateTime.now().plusHours(refreshExpHrs);
                String userAgentHash = RefreshTokenUtil.generateRefreshTokenHash(foundUser.getUserEmail(), userAgent);
                RefreshTokenDTO refreshToken = new RefreshTokenDTO(foundUser.getElementId(), userAgentHash, expiration);
                refreshToken = tokenService.addRefreshToken(refreshToken);

                String token = JWTUtil.generateNewToken(refreshToken.getElementId(), foundUser.getUserEmail(), foundUser.getRoles(), foundOrg.getSchemaName());
                return Response
                        .ok()
                        .header(HttpHeaders.AUTHORIZATION, JWTUtil.BEARER_PREFIX + token)
                        .build();
            }
        }

        return Response
                .status(Response.Status.UNAUTHORIZED)
                .build();
    }

    @DELETE
    @RolesAllowed(Role.MASTER)
    public Response invalidate(){
        //TODO finish this
        return null;
    }

}
