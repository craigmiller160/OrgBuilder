package io.craigmiller160.orgbuilder.server.dto;

/**
 * Created by craig on 8/13/16.
 */
public class EmailDTO implements Comparable<EmailDTO>, JoinedWithMemberDTO {

    private long emailId;
    private EmailType emailType;
    private String emailAddress;
    private long memberId;
    private boolean preferred;

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
    public long getMemberId() {
        return memberId;
    }

    @Override
    public void setMemberId(long memberId) {
        this.memberId = memberId;
    }

    @Override
    public boolean isPreferred() {
        return preferred;
    }

    @Override
    public void setPreferred(boolean preferred) {
        this.preferred = preferred;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EmailDTO emailDTO = (EmailDTO) o;

        if (emailId != emailDTO.emailId) return false;
        if (memberId != emailDTO.memberId) return false;
        if (preferred != emailDTO.preferred) return false;
        if (emailType != emailDTO.emailType) return false;
        return emailAddress != null ? emailAddress.equals(emailDTO.emailAddress) : emailDTO.emailAddress == null;

    }

    @Override
    public int hashCode() {
        int result = (int) (emailId ^ (emailId >>> 32));
        result = 31 * result + (emailType != null ? emailType.hashCode() : 0);
        result = 31 * result + (emailAddress != null ? emailAddress.hashCode() : 0);
        result = 31 * result + (int) (memberId ^ (memberId >>> 32));
        result = 31 * result + (preferred ? 1 : 0);
        return result;
    }

    @Override
    public String toString(){
        return emailType + ": " + emailAddress;
    }

    @Override
    public int compareTo(EmailDTO o) {
        return new Long(this.emailId).compareTo(o.emailId);
    }

    public enum EmailType {

        PERSONAL,
        WORK,
        OTHER;

    }
}
