package io.craigmiller160.orgbuilder.server.rest;

import io.craigmiller160.orgbuilder.server.OrgApiException;
import io.craigmiller160.orgbuilder.server.data.OrgApiDataException;
import io.craigmiller160.orgbuilder.server.dto.ErrorDTO;
import io.craigmiller160.orgbuilder.server.logging.OrgApiLogger;
import io.craigmiller160.orgbuilder.server.service.OrgApiSecurityException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Created by craigmiller on 9/8/16.
 */
@Provider
public class OrgApiExceptionMapper implements ExceptionMapper<OrgApiException> {

    @Override
    public Response toResponse(OrgApiException e) {
        OrgApiLogger.getRestLogger().error("Request error", e);
        ErrorDTO error = new ErrorDTO();
        Throwable cause = e.getCause();
        if(cause != null){
            error.setExceptionName(cause.getClass().getSimpleName());
            error.setErrorMessage(cause.getMessage());
            error.setStatusCode(getStatusCodeForExceptionType(cause));
        }
        else{
            error.setExceptionName(e.getClass().getSimpleName());
            error.setErrorMessage(e.getMessage());
            error.setStatusCode(getStatusCodeForExceptionType(e));
        }

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
        return Response.Status.INTERNAL_SERVER_ERROR.getStatusCode();
    }
}
