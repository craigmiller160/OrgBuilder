package io.craigmiller160.orgbuilder.server.rest;

import com.nimbusds.jwt.SignedJWT;
import io.craigmiller160.orgbuilder.server.data.jdbc.SchemaManager;
import io.craigmiller160.orgbuilder.server.dto.ErrorDTO;
import io.craigmiller160.orgbuilder.server.service.OrgApiSecurityException;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Priority;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
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
        OrgApiPrincipal principal = null;

        String method = requestContext.getMethod();
        String uri = request.getRequestURI();
        if(POST_METHOD.equals(method) && LOGIN_URI.equals(uri)){
            //Allow call to proceed to AuthResource to authenticate credentials
            principal = new OrgApiPrincipal();
            principal.setName("LoginPrincipal");
            principal.setSchema(SchemaManager.DEFAULT_APP_SCHEMA_NAME);
        }
        else{
            String authString = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
            if(StringUtils.isEmpty(authString)){
                handleAuthRejected(requestContext, OrgApiSecurityException.class, "Missing access token");
                return;
            }

            try {
                SignedJWT jwt = JWTUtil.parseAndValidateTokenSignature(authString);
                if(JWTUtil.isTokenExpired(jwt)){
                    //TODO attempt to refresh the token here
                    handleAuthRejected(requestContext, OrgApiSecurityException.class, "Token is expired");
                    return;
                }

                principal = new OrgApiPrincipal();
                principal.setName(JWTUtil.getTokenSubjectClaim(jwt));
                principal.setSchema(JWTUtil.getTokenSchemaClaim(jwt));
                principal.setRoles(JWTUtil.getTokenRolesClaim(jwt));
            } catch (OrgApiSecurityException e) {
                handleAuthRejected(requestContext, e.getClass(), e.getMessage());
            }
        }


        OrgApiSecurityContext securityContext = new OrgApiSecurityContext();
        securityContext.setUserPrincipal(principal);
        requestContext.setSecurityContext(securityContext);
    }

    private void handleAuthRejected(ContainerRequestContext requestContext, Class<?> exceptionClass, String errorMessage){
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
}
