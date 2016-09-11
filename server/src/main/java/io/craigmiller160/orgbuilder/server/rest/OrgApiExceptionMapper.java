package io.craigmiller160.orgbuilder.server.rest;

import io.craigmiller160.orgbuilder.server.OrgApiException;
import io.craigmiller160.orgbuilder.server.data.OrgApiDataException;
import io.craigmiller160.orgbuilder.server.dto.ErrorDTO;
import io.craigmiller160.orgbuilder.server.logging.OrgApiLogger;
import io.craigmiller160.orgbuilder.server.service.OrgApiSecurityException;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Created by craigmiller on 9/8/16.
 */
@Provider
public class OrgApiExceptionMapper implements ExceptionMapper<OrgApiException> {

    @Context
    private HttpServletRequest request;

    @Override
    public Response toResponse(OrgApiException e) {
        String method = request.getMethod();
        String path = request.getRequestURI();
        int status = getStatusCodeForExceptionType(e);
        OrgApiLogger.getRestLogger().error("Request error: " + status + " " + method + " " + path, e);

        ErrorDTO error = new ErrorDTO();
        error.setExceptionName(e.getClass().getSimpleName());
        error.setErrorMessage(e.getMessage());
        error.setStatusCode(status);

        return Response
                .status(error.getStatusCode())
                .entity(error)
                .build();
    }

    private int getStatusCodeForExceptionType(Throwable t){
        if(t instanceof OrgApiDataException){
            return Response.Status.INTERNAL_SERVER_ERROR.getStatusCode();
        }
        else if(t instanceof OrgApiSecurityException){
            return Response.Status.UNAUTHORIZED.getStatusCode();
        }
        else if(t instanceof OrgApiInvalidRequestException){
            return Response.Status.BAD_REQUEST.getStatusCode();
        }
        return Response.Status.INTERNAL_SERVER_ERROR.getStatusCode();
    }
}
