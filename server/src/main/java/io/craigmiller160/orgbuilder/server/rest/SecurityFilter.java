package io.craigmiller160.orgbuilder.server.rest;

import io.craigmiller160.orgbuilder.server.data.jdbc.SchemaManager;

import javax.annotation.Priority;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

/**
 * Created by craig on 9/4/16.
 */
@Provider
@Priority(Priorities.AUTHENTICATION)
public class SecurityFilter implements ContainerRequestFilter{

    private static final String POST_METHOD = "POST";
    private static final String LOGIN_URI = "/orgapi/auth";

    @Context
    HttpServletRequest request;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        //TODO perform authentication... REAL authentication

        String method = requestContext.getMethod();
        String uri = request.getRequestURI();
        if(POST_METHOD.equals(method) && LOGIN_URI.equals(uri)){
            OrgApiSecurityContext securityContext = new OrgApiSecurityContext();
            OrgApiPrincipal principal = new OrgApiPrincipal();
            principal.setName("LoginPrincipal");
            principal.setSchema(SchemaManager.DEFAULT_APP_SCHEMA_NAME);
            securityContext.setUserPrincipal(principal);
            requestContext.setSecurityContext(securityContext);
            //Allow call to proceed to AuthResource to authenticate credentials
            return;
        }
        //TODO validate the token otherwise
    }
}
