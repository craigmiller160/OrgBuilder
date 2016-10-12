package io.craigmiller160.orgbuilder.server.rest;

import io.craigmiller160.orgbuilder.server.dto.ErrorDTO;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Response;

/**
 * Created by craigmiller on 10/12/16.
 */
public class FilterUtils {

    public static void handleAccessRejected(ContainerRequestContext requestContext, Class<?> exceptionClass, String errorMessage){
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
