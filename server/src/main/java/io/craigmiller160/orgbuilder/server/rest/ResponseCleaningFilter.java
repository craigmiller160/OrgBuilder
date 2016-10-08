package io.craigmiller160.orgbuilder.server.rest;

import io.craigmiller160.orgbuilder.server.dto.OrgDTO;
import io.craigmiller160.orgbuilder.server.dto.OrgListDTO;
import io.craigmiller160.orgbuilder.server.dto.UserDTO;
import io.craigmiller160.orgbuilder.server.dto.UserListDTO;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

/**
 * This filter cleans responses of their
 * contents so that data that shouldn't
 * ever be returned never is.
 *
 * Created by craig on 9/11/16.
 */
@Provider
public class ResponseCleaningFilter implements ContainerResponseFilter{

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
        Object entity = responseContext.getEntity();
        if(entity instanceof UserDTO){
            ((UserDTO) entity).setPassword(null);
        }
        else if(entity instanceof UserListDTO){
            ((UserListDTO) entity).getUserList()
                    .forEach((e) -> e.setPassword(null));
        }
    }
}
