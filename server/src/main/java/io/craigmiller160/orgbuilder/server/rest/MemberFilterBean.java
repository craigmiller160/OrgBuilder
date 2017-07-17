package io.craigmiller160.orgbuilder.server.rest;

import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;

import javax.ws.rs.QueryParam;

/**
 * Created by craig on 9/17/16.
 */
public class MemberFilterBean extends ResourceFilterBean{

    @ApiParam(value = "Optional search criteria for a member's first name.")
    @QueryParam(QueryParamName.FIRST_NAME)
    private String firstName;

    @ApiParam(value = "Optional search criteria for a member's middle name.")
    @QueryParam(QueryParamName.MIDDLE_NAME)
    private String middleName;

    @ApiParam(value = "Optional search criteria for a member's last name.")
    @QueryParam(QueryParamName.LAST_NAME)
    private String lastName;

    @ApiParam(value = "Optional search criteria for a member's sex.")
    @QueryParam(QueryParamName.SEX)
    private String sex;

    @ApiParam(value = "Optional search criteria for a member's address.")
    @QueryParam(QueryParamName.ADDRESS)
    private String address;

    @ApiParam(value = "Optional search criteria for a member's address unit (ie, apartment number)")
    @QueryParam(QueryParamName.UNIT)
    private String unit;

    @ApiParam(value = "Optional search criteria for a member's address city.")
    @QueryParam(QueryParamName.CITY)
    private String city;

    @ApiParam(value = "Optional search criteria for a member's address state.")
    @QueryParam(QueryParamName.STATE)
    private String state;

    @ApiParam(value = "Optional search criteria for a member's address zip code.")
    @QueryParam(QueryParamName.ZIP_CODE)
    private String zipCode;

    @ApiParam(value = "Optional search criteria for a member's phone area code.")
    @QueryParam(QueryParamName.AREA_CODE)
    private String areaCode;

    @ApiParam(value = "Optional search criteria for a member's phone prefix.")
    @QueryParam(QueryParamName.PREFIX)
    private String prefix;

    @ApiParam(value = "Optional search criteria for a member's phone line number")
    @QueryParam(QueryParamName.LINE_NUMBER)
    private String lineNumber;

    @ApiParam(value = "Optional search criteria for a member's email address.")
    @QueryParam(QueryParamName.EMAIL_ADDRESS)
    private String emailAddress;

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

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
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

    public boolean isSearch(){
        return !StringUtils.isEmpty(firstName) || !StringUtils.isEmpty(middleName) || !StringUtils.isEmpty(lastName) ||
                !StringUtils.isEmpty(sex) || !StringUtils.isEmpty(address) || !StringUtils.isEmpty(unit) ||
                !StringUtils.isEmpty(city) || !StringUtils.isEmpty(state) || !StringUtils.isEmpty(zipCode) ||
                !StringUtils.isEmpty(areaCode) || !StringUtils.isEmpty(prefix) || !StringUtils.isEmpty(lineNumber) ||
                !StringUtils.isEmpty(emailAddress);
    }
}
