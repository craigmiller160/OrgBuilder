package io.craigmiller160.orgbuilder.server.data;

import io.craigmiller160.orgbuilder.server.OrgApiException;

/**
 * Created by craig on 8/21/16.
 */
public class OrgApiDataException extends OrgApiException {

    public OrgApiDataException() {
    }

    public OrgApiDataException(String message) {
        super(message);
    }

    public OrgApiDataException(Throwable cause) {
        super(cause);
    }

    public OrgApiDataException(String message, Throwable cause) {
        super(message, cause);
    }
}
