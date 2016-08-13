package io.craigmiller160.orgbuilder.server.dto;

/**
 * Created by craig on 8/13/16.
 */
public class EmailDTO {

    private long emailId;
    private EmailType emailType;
    private String emailAddress;

    public EmailDTO(){}

    public EmailDTO(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public long getEmailId() {
        return emailId;
    }

    public void setEmailId(long emailId) {
        this.emailId = emailId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EmailDTO emailDTO = (EmailDTO) o;

        if (emailId != emailDTO.emailId) return false;
        if (emailType != emailDTO.emailType) return false;
        return emailAddress != null ? emailAddress.equals(emailDTO.emailAddress) : emailDTO.emailAddress == null;

    }

    @Override
    public int hashCode() {
        int result = (int) (emailId ^ (emailId >>> 32));
        result = 31 * result + (emailType != null ? emailType.hashCode() : 0);
        result = 31 * result + (emailAddress != null ? emailAddress.hashCode() : 0);
        return result;
    }

    public EmailType getEmailType() {

        return emailType;
    }

    public void setEmailType(EmailType emailType) {
        this.emailType = emailType;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    @Override
    public String toString(){
        return emailType + ": " + emailAddress;
    }

    public enum EmailType {

        PERSONAL,
        WORK,
        OTHER;

    }
}
