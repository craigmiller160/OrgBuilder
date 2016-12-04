package io.craigmiller160.orgbuilder.server.rest;

import io.craigmiller160.orgbuilder.server.ServerCore;
import io.craigmiller160.orgbuilder.server.ServerProps;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

/**
 * Created by craig on 11/29/16.
 */
@Provider
public class CrossOriginHeaderFilter implements ContainerResponseFilter{

    private static final String origin = ServerCore.getProperty(ServerProps.CLIENT_ORIGIN);

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
        MultivaluedMap<String,Object> headers = responseContext.getHeaders();
        headers.add("Access-Control-Allow-Origin", origin);
        headers.add("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS");
        headers.add("Access-Control-Allow-Headers", "X-Requested-With, Origin, Content-Type, Accept");
        headers.add("Access-Control-Expose-Headers", "Content-Type, Authorization");
        headers.add("Access-Control-Allow-Credentials", true);

    }
}
