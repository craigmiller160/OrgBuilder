package io.craigmiller160.orgbuilder.server.rest;

import io.craigmiller160.orgbuilder.server.dto.ErrorDTO;
import io.craigmiller160.orgbuilder.server.rest.annotation.ThisOrgAllowed;
import org.apache.commons.lang3.StringUtils;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Created by craig on 10/9/16.
 */
public class ThisOrgAllowedFilter implements ContainerRequestFilter {

    @Context
    private UriInfo uriInfo;

    @Context
    private ResourceInfo resourceInfo;

    @Context
    private SecurityContext securityContext;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        long orgId = getOrgIdPathParam();
        if(orgId < 0){
            throw new BadRequestException("No OrgId path param in URI");
        }

        ThisOrgAllowed annotation = resourceInfo.getResourceMethod().getAnnotation(ThisOrgAllowed.class);
        OrgApiPrincipal principal = (OrgApiPrincipal) securityContext.getUserPrincipal();

        long principalOrgId = principal.getOrgId();
        if(principalOrgId == orgId){
            List<String> inOrgRolesAllowed = Arrays.asList(annotation.inOrgRolesAllowed());
            validateRolesAllowed(requestContext, inOrgRolesAllowed, principal);
        }
        else{
            List<String> outOfOrgRolesAllowed = Arrays.asList(annotation.outOfOrgRolesAllowed());
            validateRolesAllowed(requestContext, outOfOrgRolesAllowed, principal);
        }
    }

    private void validateRolesAllowed(ContainerRequestContext requestContext, List<String> rolesAllowed, OrgApiPrincipal principal){
        for(String role : principal.getRoles()){
            if(rolesAllowed.contains(role)){
                return;
            }
        }
        handleAccessRejected(requestContext, ForbiddenException.class, "User does not have access to resource");
    }

    private void handleAccessRejected(ContainerRequestContext requestContext, Class<?> exceptionClass, String errorMessage){
        ErrorDTO error = new ErrorDTO();
        error.setStatusCode(Response.Status.FORBIDDEN.getStatusCode());
        error.setExceptionName(exceptionClass.getSimpleName());
        error.setErrorMessage(errorMessage);
        requestContext.abortWith(
                Response
                        .status(Response.Status.FORBIDDEN)
                        .entity(error)
                        .build()
        );
    }

    private long getOrgIdPathParam() throws BadRequestException{
        long orgId = -1;
        List<String> pathParams = uriInfo.getPathParameters().get("orgId");
        if(pathParams.size() > 0){
            String orgIdString = pathParams.get(0);
            if(!StringUtils.isNumeric(orgIdString)){
                throw new BadRequestException("OrgId path param is not a valid numeric value");
            }
            orgId = Long.parseLong(orgIdString);
        }
        return orgId;
    }
}
