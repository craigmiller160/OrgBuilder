package io.craigmiller160.orgbuilder.server.rest;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.QueryParam;

/**
 * Created by craig on 9/17/16.
 */
public class GetMembersFilterBean {

    @QueryParam("offset")
    @DefaultValue("-1")
    private long offset;

    @QueryParam("size")
    @DefaultValue("-1")
    private long size;

    @QueryParam("firstName")
    private String firstName;

    @QueryParam("middleName")
    private String middleName;

    @QueryParam("lastName")
    private String lastName;

    @QueryParam("gender")
    private String gender;

    @QueryParam("address")
    private String address;

    @QueryParam("unit")
    private String unit;

    @QueryParam("city")
    private String city;

    @QueryParam("state")
    private String state;

    @QueryParam("zipCode")
    private String zipCode;

    @QueryParam("areaCode")
    private String areaCode;

    @QueryParam("prefix")
    private String prefix;

    @QueryParam("lineNumber")
    private String lineNumber;

    @QueryParam("emailAddress")
    private String emailAddress;

    public long getOffset() {
        return offset;
    }

    public void setOffset(long offset) {
        this.offset = offset;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
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

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public boolean hasFilterParams(){
        return (offset >= 0 && size >= 0);
    }

    public void validateFilterParams() throws OrgApiInvalidRequestException{
        if((offset != -1 && size == -1) || (offset == -1 && size != -1)){
            throw new OrgApiInvalidRequestException("Invalid offset/size query parameters.");
        }
    }
}
