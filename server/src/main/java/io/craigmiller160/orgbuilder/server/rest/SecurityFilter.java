package io.craigmiller160.orgbuilder.server.rest;

import com.nimbusds.jwt.SignedJWT;
import io.craigmiller160.orgbuilder.server.data.OrgApiDataException;
import io.craigmiller160.orgbuilder.server.data.jdbc.SchemaManager;
import io.craigmiller160.orgbuilder.server.dto.ErrorDTO;
import io.craigmiller160.orgbuilder.server.dto.RefreshTokenDTO;
import io.craigmiller160.orgbuilder.server.service.OrgApiSecurityException;
import io.craigmiller160.orgbuilder.server.service.ServiceFactory;
import io.craigmiller160.orgbuilder.server.service.TokenService;
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
import java.time.LocalDateTime;

/**
 * Created by craig on 9/4/16.
 */
@Provider
@Priority(Priorities.AUTHENTICATION)
public class SecurityFilter implements ContainerRequestFilter{

    private static final String POST_METHOD = "POST";
    private static final String LOGIN_URI = "/orgapi/auth";
    private static final ServiceFactory factory = ServiceFactory.newInstance();

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
                    //Check for a possible refresh of the token here
                    TokenService service = factory.newTokenService(null);
                    RefreshTokenDTO refreshToken = service.getRefreshToken(JWTUtil.getTokenIdClaim(jwt));
                    String userAgent = requestContext.getHeaderString(HttpHeaders.USER_AGENT);
                    //If a valid refreshToken exists, and it hasn't expired, re-issue the access token.
                    if(refreshToken != null && RefreshTokenUtil.isValidRefreshToken(jwt, userAgent, refreshToken.getTokenHash()) &&
                            refreshToken.getExpiration().compareTo(LocalDateTime.now()) <= 0){
                        String newToken = JWTUtil.generateNewToken(jwt);
                        requestContext.getHeaders().remove(HttpHeaders.AUTHORIZATION);
                        requestContext.getHeaders().add(HttpHeaders.AUTHORIZATION, newToken);
                    }
                    else{
                        handleAuthRejected(requestContext, OrgApiSecurityException.class, "Token is expired");
                        return;
                    }
                }

                if(!JWTUtil.isTokenIssuedByOrgApi(jwt)){
                    handleAuthRejected(requestContext, OrgApiSecurityException.class, "Token not issued by OrgBuilder API");
                    return;
                }

                principal = new OrgApiPrincipal();
                principal.setName(JWTUtil.getTokenSubjectClaim(jwt));
                principal.setSchema(JWTUtil.getTokenSchemaClaim(jwt));
                principal.setRoles(JWTUtil.getTokenRolesClaim(jwt));
            } catch (OrgApiSecurityException | OrgApiDataException e) {
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
