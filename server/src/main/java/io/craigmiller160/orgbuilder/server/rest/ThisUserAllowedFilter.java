package io.craigmiller160.orgbuilder.server.rest;

import io.craigmiller160.orgbuilder.server.rest.annotation.ThisUserAllowed;
import org.apache.commons.lang3.StringUtils;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Created by craig on 10/9/16.
 */
public class ThisUserAllowedFilter implements ContainerRequestFilter {

    @Context
    private UriInfo uriInfo;

    @Context
    private ResourceInfo resourceInfo;

    @Context
    private SecurityContext securityContext;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        long userId = getUserIdPathParam();
        if(userId < 0){
            throw new BadRequestException("No UserId path param in URI");
        }

        ThisUserAllowed annotation = resourceInfo.getResourceMethod().getAnnotation(ThisUserAllowed.class);
        OrgApiPrincipal principal = (OrgApiPrincipal) securityContext.getUserPrincipal();

        long principalUserId = principal.getUserId();
        if(principalUserId != userId){
            List<String> otherUserRolesAllowed = Arrays.asList(annotation.otherUserRolesAllowed());
            for(String role : principal.getRoles()){
                if(otherUserRolesAllowed.contains(role)){
                    return;
                }
            }
            FilterUtils.handleAccessRejected(requestContext, ForbiddenException.class, "User does not have access to resource");
        }
    }

    private long getUserIdPathParam() throws BadRequestException {
        long userId = -1;
        List<String> pathParams = uriInfo.getPathParameters().get("userId");
        if(pathParams.size() > 0){
            String userIdString = pathParams.get(0);
            if(!StringUtils.isNumeric(userIdString)){
                throw new BadRequestException("UserId path param is not a valid numeric value");
            }
            userId = Long.parseLong(userIdString);
        }
        return userId;
    }
}
