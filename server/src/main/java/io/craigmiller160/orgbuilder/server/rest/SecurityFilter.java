package io.craigmiller160.orgbuilder.server.rest;

import com.nimbusds.jwt.SignedJWT;
import io.craigmiller160.orgbuilder.server.OrgApiException;
import io.craigmiller160.orgbuilder.server.ServerCore;
import io.craigmiller160.orgbuilder.server.ServerProps;
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
            principal = handleLogin();
        }
        else{
            principal = handleTokenValidation(requestContext);
        }

        //If principal is null, authentication was rejected
        if(principal == null){
            return;
        }

        OrgApiSecurityContext securityContext = new OrgApiSecurityContext();
        securityContext.setUserPrincipal(principal);
        requestContext.setSecurityContext(securityContext);
    }

    private OrgApiPrincipal handleTokenValidation(ContainerRequestContext requestContext){
        String authString = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
        if(StringUtils.isEmpty(authString)){
            handleAuthRejected(requestContext, OrgApiSecurityException.class, "Missing access token");
            return null;
        }

        try{
            SignedJWT jwt = JWTUtil.parseAndValidateTokenSignature(authString);
            if(!JWTUtil.isTokenIssuedByOrgApi(jwt)){
                handleAuthRejected(requestContext, OrgApiSecurityException.class, "Token not issued by OrgBuilder API");
                return null;
            }

            if(JWTUtil.isTokenExpired(jwt)){
                if(!isRefreshAllowed(requestContext, jwt)){
                    handleAuthRejected(requestContext, OrgApiSecurityException.class, "Token is expired");
                    return null;
                }

                refreshToken(requestContext, jwt);
            }

            String newToken = JWTUtil.generateNewToken(jwt);
            requestContext.getHeaders().remove(HttpHeaders.AUTHORIZATION);
            requestContext.getHeaders().add(HttpHeaders.AUTHORIZATION, newToken);

            OrgApiPrincipal principal = new OrgApiPrincipal();
            principal.setName(JWTUtil.getTokenSubjectClaim(jwt));
            principal.setSchema(JWTUtil.getTokenSchemaClaim(jwt));
            principal.setRoles(JWTUtil.getTokenRolesClaim(jwt));
            return principal;
        }
        catch(OrgApiException ex){
            handleAuthRejected(requestContext, ex.getClass(), ex.getMessage());
        }

        return null;
    }

    private boolean isRefreshAllowed(ContainerRequestContext requestContext, SignedJWT jwt) throws OrgApiException{
        String userAgent = requestContext.getHeaderString(HttpHeaders.USER_AGENT);
        TokenService service = factory.newTokenService(null);
        RefreshTokenDTO refreshToken = service.getRefreshToken(JWTUtil.getTokenIdClaim(jwt));
        LocalDateTime accessExpTime = JWTUtil.getTokenExpirationClaim(jwt);
        LocalDateTime now = LocalDateTime.now();
        //If a NumberFormatException occurs, then the property is invalid
        int refreshExpMins = Integer.parseInt(ServerCore.getProperty(ServerProps.REFRESH_EXP_MINS));
        LocalDateTime cantRefreshAfter = accessExpTime.plusMinutes(refreshExpMins);

        //Refresh token if: token != null, token has valid hash, token max expiration hasn't passed, and the max minutes after access expires hasn't passed
        return refreshToken != null &&
                RefreshTokenUtil.isValidRefreshToken(jwt, userAgent, refreshToken.getTokenHash()) &&
                refreshToken.getExpiration().compareTo(LocalDateTime.now()) <= 0 &&
                cantRefreshAfter.compareTo(now) <= 0;
    }

    private void refreshToken(ContainerRequestContext requestContext, SignedJWT jwt) throws OrgApiException{
        String newToken = JWTUtil.generateNewToken(jwt);
        requestContext.getHeaders().remove(HttpHeaders.AUTHORIZATION);
        requestContext.getHeaders().add(HttpHeaders.AUTHORIZATION, newToken);
    }

    private OrgApiPrincipal handleLogin(){
        OrgApiPrincipal principal = new OrgApiPrincipal();
        principal.setName("Login");
        principal.setSchema(SchemaManager.DEFAULT_APP_SCHEMA_NAME);
        principal.getRoles().add(Role.MASTER);
        return principal;
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
