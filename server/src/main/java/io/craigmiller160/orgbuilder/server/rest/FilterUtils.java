package io.craigmiller160.orgbuilder.server.rest;

import io.craigmiller160.orgbuilder.server.dto.ErrorDTO;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by craigmiller on 10/12/16.
 */
public class FilterUtils {

    public static void handleAccessRejected(ContainerRequestContext requestContext, Class<?> exceptionClass, String errorMessage){
        handleRejection(requestContext, exceptionClass, errorMessage, Response.Status.FORBIDDEN.getStatusCode());
    }

    public static void handleAccessExpired(ContainerRequestContext requestContext, Class<?> exceptionClass, String errorMessage){
        handleRejection(requestContext, exceptionClass, errorMessage, Response.Status.UNAUTHORIZED.getStatusCode());
    }

    private static void handleRejection(ContainerRequestContext requestContext, Class<?> exceptionClass, String errorMessage, int status){
        ErrorDTO error = new ErrorDTO();
        error.setStatusCode(status);
        error.setExceptionName(exceptionClass.getSimpleName());
        error.setErrorMessage(errorMessage);
        requestContext.abortWith(
                Response
                        .status(status)
                        .entity(error)
                        .type(MediaType.APPLICATION_JSON)
                        .build()
        );
    }

}
