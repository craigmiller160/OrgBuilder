package io.craigmiller160.orgbuilder.server.rest;

import io.craigmiller160.orgbuilder.server.OrgApiException;
import io.craigmiller160.orgbuilder.server.data.OrgApiDataException;
import io.craigmiller160.orgbuilder.server.dto.ErrorDTO;
import io.craigmiller160.orgbuilder.server.logging.OrgApiLogger;
import io.craigmiller160.orgbuilder.server.service.OrgApiSecurityException;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Created by craigmiller on 9/8/16.
 */
@Provider
public class OrgApiExceptionMapper implements ExceptionMapper<Throwable> {

    @Context
    private HttpServletRequest request;

    @Override
    public Response toResponse(Throwable t) {
        String method = request.getMethod();
        String path = request.getRequestURI();
        int status = getStatusCodeForExceptionType(t);
        OrgApiLogger.getRestLogger().error("Request error: " + status + " " + method + " " + path, t);

        ErrorDTO error = new ErrorDTO();
        error.setExceptionName(t.getClass().getSimpleName());
        error.setErrorMessage(t.getMessage());
        error.setStatusCode(status);

        return Response
                .status(error.getStatusCode())
                .entity(error)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }

    private int getStatusCodeForExceptionType(Throwable t){
        if(t instanceof OrgApiSecurityException){
            return Response.Status.UNAUTHORIZED.getStatusCode();
        }
        else if(t instanceof OrgApiInvalidRequestException | t instanceof BadRequestException){
            return Response.Status.BAD_REQUEST.getStatusCode();
        }
        else if(t instanceof OrgApiDataException || t instanceof OrgApiException){
            return Response.Status.INTERNAL_SERVER_ERROR.getStatusCode();
        }
        else if(t instanceof WebApplicationException){
            return ((WebApplicationException) t).getResponse().getStatus();
        }
        return Response.Status.INTERNAL_SERVER_ERROR.getStatusCode();
    }
}
