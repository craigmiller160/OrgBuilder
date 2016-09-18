package io.craigmiller160.orgbuilder.server.rest;

import io.craigmiller160.orgbuilder.server.dto.OrgDTO;
import io.craigmiller160.orgbuilder.server.dto.UserDTO;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

/**
 * Created by craig on 9/4/16.
 */
@Provider
@Priority(Priorities.AUTHENTICATION)
public class SecurityFilter implements ContainerRequestFilter{

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        //TODO perform authentication... REAL authentication
        //TODO test the URI to check the specified org_id, make sure it matches the allowed org_id for the user
        UserDTO userDTO = new UserDTO();
        userDTO.setElementId(1L);
        userDTO.setUserName("Craig");
        userDTO.setOrgId(1L);
        userDTO.convertStringToRoles("ADMIN,WRITE,READ");
        OrgDTO orgDTO = new OrgDTO();
        orgDTO.setElementId(1L);
        orgDTO.setOrgName("TestOrg");
        orgDTO.setSchemaName("1TestO");
        requestContext.setSecurityContext(new OrgApiSecurityContext(new UserOrgPrincipal(userDTO, orgDTO)));
    }
}
