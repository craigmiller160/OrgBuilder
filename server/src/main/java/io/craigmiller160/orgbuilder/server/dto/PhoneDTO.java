package io.craigmiller160.orgbuilder.server.dto;

import org.apache.commons.lang3.StringUtils;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by craig on 8/13/16.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class PhoneDTO implements Comparable<PhoneDTO>, JoinedWithMemberDTO<Long>, DTO<Long>{

    private long phoneId;
    private PhoneType phoneType;
    private String areaCode;
    private String prefix;
    private String lineNumber;
    private String extension;
    private long memberId;
    private boolean preferred;

    public PhoneDTO(){}

    public PhoneDTO(String areaCode, String prefix, String lineNumber, String extension) {
        this.areaCode = areaCode;
        this.prefix = prefix;
        this.lineNumber = lineNumber;
        this.extension = extension;
    }

    @Override
    public Long getElementId() {
        return phoneId;
    }

    @Override
    public void setElementId(Long id) {
        this.phoneId = id;
    }

    public PhoneType getPhoneType() {
        return phoneType;
    }

    public void setPhoneType(PhoneType phoneType) {
        this.phoneType = phoneType;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(String lineNumber) {
        this.lineNumber = lineNumber;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getRawNumber(){
        return (areaCode != null ? areaCode : "") +
                (prefix != null ? prefix : "") +
                (lineNumber != null ? lineNumber : "") +
                (extension != null ? extension : "");
    }

    public String getFormattedNumber(){
        StringBuilder builder = new StringBuilder();
        if(!StringUtils.isEmpty(areaCode)){
            builder.append("(").append(areaCode).append(") ");
        }

        if(!StringUtils.isEmpty(prefix)){
            builder.append(prefix);
        }
        builder.append("-");

        if(!StringUtils.isEmpty(lineNumber)){
            builder.append(lineNumber);
        }

        if(!StringUtils.isEmpty(extension)){
            builder.append(" ex").append(extension);
        }

        return builder.toString();
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

        PhoneDTO phoneDTO = (PhoneDTO) o;

        if (phoneId != phoneDTO.phoneId) return false;
        if (memberId != phoneDTO.memberId) return false;
        if (preferred != phoneDTO.preferred) return false;
        if (phoneType != phoneDTO.phoneType) return false;
        if (areaCode != null ? !areaCode.equals(phoneDTO.areaCode) : phoneDTO.areaCode != null) return false;
        if (prefix != null ? !prefix.equals(phoneDTO.prefix) : phoneDTO.prefix != null) return false;
        if (lineNumber != null ? !lineNumber.equals(phoneDTO.lineNumber) : phoneDTO.lineNumber != null) return false;
        return extension != null ? extension.equals(phoneDTO.extension) : phoneDTO.extension == null;

    }

    @Override
    public int hashCode() {
        int result = (int) (phoneId ^ (phoneId >>> 32));
        result = 31 * result + (phoneType != null ? phoneType.hashCode() : 0);
        result = 31 * result + (areaCode != null ? areaCode.hashCode() : 0);
        result = 31 * result + (prefix != null ? prefix.hashCode() : 0);
        result = 31 * result + (lineNumber != null ? lineNumber.hashCode() : 0);
        result = 31 * result + (extension != null ? extension.hashCode() : 0);
        result = 31 * result + (int) (memberId ^ (memberId >>> 32));
        result = 31 * result + (preferred ? 1 : 0);
        return result;
    }

    @Override
    public String toString(){
        return phoneType + ": " + getFormattedNumber();
    }

    @Override
    public int compareTo(PhoneDTO o) {
        return new Long(this.phoneId).compareTo(o.phoneId);
    }

    public enum PhoneType {

        HOME,
        MOBILE,
        FAX,
        WORK,
        OTHER;

    }
}
