package io.craigmiller160.orgbuilder.server.dto;

import io.craigmiller160.orgbuilder.server.ServerCore;
import io.craigmiller160.orgbuilder.server.ServerProps;

/**
 * Created by craigmiller on 9/8/16.
 */
public class ErrorDTO implements Comparable<ErrorDTO>{

    private int statusCode;
    private String errorMessage;
    private String exceptionName;
    private final String contactMessage = "If this error persists, please contact the webmaster at " + ServerCore.getProperty(ServerProps.DEV_EMAIL);

    public ErrorDTO(){}

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getExceptionName() {
        return exceptionName;
    }

    public void setExceptionName(String exceptionName) {
        this.exceptionName = exceptionName;
    }

    public String getContactMessage() {
        return contactMessage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ErrorDTO errorDTO = (ErrorDTO) o;

        if (statusCode != errorDTO.statusCode) return false;
        if (errorMessage != null ? !errorMessage.equals(errorDTO.errorMessage) : errorDTO.errorMessage != null)
            return false;
        if (exceptionName != null ? !exceptionName.equals(errorDTO.exceptionName) : errorDTO.exceptionName != null)
            return false;
        return contactMessage != null ? contactMessage.equals(errorDTO.contactMessage) : errorDTO.contactMessage == null;

    }

    @Override
    public int hashCode() {
        int result = statusCode;
        result = 31 * result + (errorMessage != null ? errorMessage.hashCode() : 0);
        result = 31 * result + (exceptionName != null ? exceptionName.hashCode() : 0);
        result = 31 * result + (contactMessage != null ? contactMessage.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Error: " + errorMessage;
    }

    @Override
    public int compareTo(ErrorDTO o) {
        return new Integer(this.hashCode()).compareTo(o.hashCode());
    }
}
