package io.craigmiller160.orgbuilder.server.rest;

import org.apache.commons.lang3.StringUtils;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

/**
 * A filter that ensures that the token
 * is always returned in the response.
 *
 * Created by craig on 12/4/16.
 */
@Provider
public class ResponseTokenFilter implements ContainerResponseFilter{

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
        String requestAuthHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
        String responseAuthHeader = responseContext.getHeaderString(HttpHeaders.AUTHORIZATION);

        //Remove the Authorization header/token if the status is 401 UNAUTHORIZED or 403 FORBIDDEN. Otherwise, ensure it is returned if it isn't there already
        int status = responseContext.getStatus();
        if(status == 401 || status == 403){
            responseContext.getHeaders().remove(HttpHeaders.AUTHORIZATION);
        }
        else{
            if(!StringUtils.isEmpty(requestAuthHeader) && StringUtils.isEmpty(responseAuthHeader)){
                responseContext.getHeaders().putSingle(HttpHeaders.AUTHORIZATION, requestAuthHeader);
            }
        }
    }
}
