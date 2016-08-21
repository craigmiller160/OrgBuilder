package io.craigmiller160.orgbuilder.server;

/**
 * Created by craig on 8/21/16.
 */
public class OrgApiException extends Exception{

    public OrgApiException() {
    }

    public OrgApiException(String message) {
        super(message);
    }

    public OrgApiException(Throwable cause) {
        super(cause);
    }

    public OrgApiException(String message, Throwable cause) {
        super(message, cause);
    }
}
