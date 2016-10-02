package io.craigmiller160.orgbuilder.server.rest;

import io.craigmiller160.orgbuilder.server.service.OrgApiSecurityException;

/**
 * Created by craig on 10/2/16.
 */
public class OrgApiInvalidTokenException extends OrgApiSecurityException {

    public OrgApiInvalidTokenException() {
    }

    public OrgApiInvalidTokenException(String message) {
        super(message);
    }

    public OrgApiInvalidTokenException(Throwable cause) {
        super(cause);
    }

    public OrgApiInvalidTokenException(String message, Throwable cause) {
        super(message, cause);
    }
}
