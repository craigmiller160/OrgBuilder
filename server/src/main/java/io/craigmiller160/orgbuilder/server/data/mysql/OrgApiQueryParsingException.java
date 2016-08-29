package io.craigmiller160.orgbuilder.server.data.mysql;

import io.craigmiller160.orgbuilder.server.OrgApiException;

/**
 * Created by craig on 8/27/16.
 */
public class OrgApiQueryParsingException extends OrgApiException {

    public OrgApiQueryParsingException() {
    }

    public OrgApiQueryParsingException(String message) {
        super(message);
    }

    public OrgApiQueryParsingException(Throwable cause) {
        super(cause);
    }

    public OrgApiQueryParsingException(String message, Throwable cause) {
        super(message, cause);
    }
}
