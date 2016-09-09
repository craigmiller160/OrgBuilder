package io.craigmiller160.orgbuilder.server.service;

import io.craigmiller160.orgbuilder.server.OrgApiException;

/**
 * Created by craigmiller on 9/8/16.
 */
public class OrgApiSecurityException extends OrgApiException {

    public OrgApiSecurityException() {
    }

    public OrgApiSecurityException(String message) {
        super(message);
    }

    public OrgApiSecurityException(Throwable cause) {
        super(cause);
    }

    public OrgApiSecurityException(String message, Throwable cause) {
        super(message, cause);
    }
}
