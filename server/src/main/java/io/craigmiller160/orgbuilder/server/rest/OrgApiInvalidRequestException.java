package io.craigmiller160.orgbuilder.server.rest;

import io.craigmiller160.orgbuilder.server.OrgApiException;

/**
 * Created by craig on 9/10/16.
 */
public class OrgApiInvalidRequestException extends OrgApiException {

    public OrgApiInvalidRequestException() {
    }

    public OrgApiInvalidRequestException(String message) {
        super(message);
    }

    public OrgApiInvalidRequestException(Throwable cause) {
        super(cause);
    }

    public OrgApiInvalidRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}
