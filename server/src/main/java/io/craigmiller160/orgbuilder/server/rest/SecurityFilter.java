package io.craigmiller160.orgbuilder.server.rest;

import com.nimbusds.jwt.SignedJWT;
import io.craigmiller160.orgbuilder.server.OrgApiException;
import io.craigmiller160.orgbuilder.server.ServerCore;
import io.craigmiller160.orgbuilder.server.ServerProps;
import io.craigmiller160.orgbuilder.server.data.jdbc.SchemaManager;
import io.craigmiller160.orgbuilder.server.dto.RefreshTokenDTO;
import io.craigmiller160.orgbuilder.server.logging.OrgApiLogger;
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
    private static final String GET_METHOD = "GET";
    private static final String OPTIONS_METHOD = "OPTIONS";
    private static final String LOGIN_URI = "/orgapi/api/auth";
    private static final String USER_EXISTS_URI = "/orgapi/api/auth/exists";
    private static final String SWAGGER_URI = "/orgapi/api/swagger.json";
    private static final ServiceFactory factory = ServiceFactory.newInstance();

    @Context
    HttpServletRequest request;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        OrgApiPrincipal principal = null;

        String method = requestContext.getMethod();
        String uri = request.getRequestURI();

        if((POST_METHOD.equals(method) && LOGIN_URI.equals(uri)) ||
                (GET_METHOD.equals(method) && USER_EXISTS_URI.equals(uri))){
            //Allow call to proceed to AuthResource to authenticate credentials
            OrgApiLogger.getRestLogger().trace("Creating principal for authenticating login credentials");
            principal = createAuthPrincipal();
        }
        else if(OPTIONS_METHOD.equals(method)){
            //Do nothing and return
            return;
        }
        else if(GET_METHOD.equals(method) && SWAGGER_URI.equals(uri)){
            //Do nothing and return
            return;
        }
        else{
            OrgApiLogger.getRestLogger().trace("Validating token to create principal for API access");
            principal = handleTokenValidation(requestContext);
        }

        //If principal is null, authentication was rejected or it is an options request
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
            FilterUtils.handleAccessRejected(requestContext, OrgApiSecurityException.class, "Missing access token");
            return null;
        }

        try{
            SignedJWT jwt = JWTUtil.parseAndValidateTokenSignature(authString);
            if(!JWTUtil.isTokenIssuedByOrgApi(jwt)){
                FilterUtils.handleAccessRejected(requestContext, OrgApiSecurityException.class, "Token not issued by OrgBuilder API");
                return null;
            }

            if(JWTUtil.isTokenExpired(jwt)){
                if(!isRefreshAllowed(requestContext, jwt)){
                    FilterUtils.handleAccessExpired(requestContext, OrgApiSecurityException.class, "Token is expired");
                    return null;
                }

                refreshToken(requestContext, jwt);
            }

            OrgApiPrincipal principal = new OrgApiPrincipal();
            principal.setName(JWTUtil.getTokenSubjectClaim(jwt));
            principal.setSchema(JWTUtil.getTokenSchemaClaim(jwt));
            principal.setRoles(JWTUtil.getTokenRolesClaim(jwt));
            principal.setOrgId(JWTUtil.getTokenOrgIdClaim(jwt));
            principal.setUserId(JWTUtil.getTokenUserIdClaim(jwt));

            return principal;
        }
        catch(OrgApiException ex){
            FilterUtils.handleAccessRejected(requestContext, ex.getClass(), "Access Denied: " + ex.getMessage());
        }

        return null;
    }

    private boolean isRefreshAllowed(ContainerRequestContext requestContext, SignedJWT jwt) throws OrgApiException{
        String userAgent = requestContext.getHeaderString(HttpHeaders.USER_AGENT);
        TokenService service = factory.newTokenService(createTokenSecurityContext());
        RefreshTokenDTO refreshToken = service.getRefreshToken(JWTUtil.getTokenIdClaim(jwt));
        LocalDateTime accessExpTime = JWTUtil.getTokenExpirationClaim(jwt);
        LocalDateTime now = LocalDateTime.now();
        //If a NumberFormatException occurs, then the property is invalid
        int refreshExpMins = Integer.parseInt(ServerCore.getProperty(ServerProps.REFRESH_EXP_MINS));
        LocalDateTime cantRefreshAfter = accessExpTime.plusMinutes(refreshExpMins);

        //Refresh token if: token != null, token has valid hash, token max expiration hasn't passed, and the max minutes after access expires hasn't passed
        return refreshToken != null &&
                RefreshTokenUtil.isValidRefreshToken(jwt, userAgent, refreshToken.getTokenHash()) &&
                refreshToken.getExpiration().compareTo(now) >= 0 &&
                cantRefreshAfter.compareTo(now) >= 0;
    }

    private void refreshToken(ContainerRequestContext requestContext, SignedJWT jwt) throws OrgApiException{
        String newToken = JWTUtil.generateNewToken(jwt);
        requestContext.getHeaders().remove(HttpHeaders.AUTHORIZATION);
        requestContext.getHeaders().add(HttpHeaders.AUTHORIZATION, JWTUtil.BEARER_PREFIX + " " + newToken);
    }

    private OrgApiPrincipal createAuthPrincipal(){
        OrgApiPrincipal principal = new OrgApiPrincipal();
        principal.setName("Auth");
        principal.setSchema(SchemaManager.DEFAULT_APP_SCHEMA_NAME);
        return principal;
    }

    private OrgApiSecurityContext createTokenSecurityContext(){
        OrgApiPrincipal principal = new OrgApiPrincipal();
        principal.setName("Token");
        principal.setSchema(SchemaManager.DEFAULT_APP_SCHEMA_NAME);
        principal.getRoles().add(Role.MASTER);
        OrgApiSecurityContext securityContext = new OrgApiSecurityContext();
        securityContext.setUserPrincipal(principal);
        return securityContext;
    }
}
