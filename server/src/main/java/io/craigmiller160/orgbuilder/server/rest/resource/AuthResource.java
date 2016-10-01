package io.craigmiller160.orgbuilder.server.rest.resource;

import io.craigmiller160.orgbuilder.server.OrgApiException;
import io.craigmiller160.orgbuilder.server.ServerCore;
import io.craigmiller160.orgbuilder.server.dto.OrgDTO;
import io.craigmiller160.orgbuilder.server.dto.UserDTO;
import io.craigmiller160.orgbuilder.server.rest.OrgApiInvalidRequestException;
import io.craigmiller160.orgbuilder.server.rest.Role;
import io.craigmiller160.orgbuilder.server.service.OrgService;
import io.craigmiller160.orgbuilder.server.service.ServiceFactory;
import io.craigmiller160.orgbuilder.server.service.UserService;
import io.craigmiller160.orgbuilder.server.util.HashingUtils;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

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
    public Response authenticate(UserDTO user) throws OrgApiException{
        if(StringUtils.isEmpty(user.getUserEmail()) || StringUtils.isEmpty(user.getPassword())){
            throw new OrgApiInvalidRequestException("Authentication request has incomplete credentials");
        }

        UserService userService = factory.newUserService(securityContext);
        OrgService orgService = factory.newOrgService(securityContext);
        UserDTO foundUser = userService.getUserByName(user.getUserEmail());
        if(foundUser != null){
            OrgDTO foundOrg = orgService.getOrg(foundUser.getOrgId());
            if(HashingUtils.verifyBCryptHash(user.getPassword(), foundUser.getPassword())){
                String token = ServerCore.getJWTManager().generateNewToken(user.getUserEmail(), user.getRoles(), foundOrg.getSchemaName());
                return Response
                        .ok()
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token) //TODO ensure that the Bearer part is formatted properly
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
