package io.craigmiller160.orgbuilder.server.rest;

import io.craigmiller160.orgbuilder.server.logging.OrgApiLogger;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

/**
 * Created by craig on 9/10/16.
 */
@Provider
public class LoggingFilter implements ContainerRequestFilter, ContainerResponseFilter {

    @Context
    HttpServletRequest request;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String method = requestContext.getMethod();
        String path = requestContext.getUriInfo().getPath();
        String remoteAddress = request.getRemoteAddr();
        OrgApiLogger.getRestLogger().info("Request received from " + remoteAddress + " | " + method + " " + path);
    }

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
        String method = requestContext.getMethod();
        String path = requestContext.getUriInfo().getPath();
        String remoteAddress = request.getRemoteAddr();
        int status = responseContext.getStatus();
        OrgApiLogger.getRestLogger().info("Response sent to " + remoteAddress + " | " + status + " " + method + " " + path);
    }
}
