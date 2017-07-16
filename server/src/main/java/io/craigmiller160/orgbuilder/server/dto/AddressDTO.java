package io.craigmiller160.orgbuilder.server.dto;

import io.swagger.annotations.ApiModelProperty;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 *
 * Created by craig on 8/13/16.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class AddressDTO implements Comparable<AddressDTO>, JoinedWithMemberDTO<Long>, DTO<Long>{

    private long addressId;
    private AddressType addressType;
    private String address1;
    private String address2;
    private String city;
    private State state;
    private String zipCode;
    private long memberId;
    private boolean preferred;

    public AddressDTO(){}

    public AddressDTO(AddressType addressType, String address1, String address2, String city, State state, String zipCode){
        this.addressType = addressType;
        this.address1 = address1;
        this.address2 = address2;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
    }

    @ApiModelProperty(name = "addressId")
    @Override
    public Long getElementId(){
        return addressId;
    }

    @Override
    public void setElementId(Long id) {
        this.addressId = id;
    }

    public AddressType getAddressType() {
        return addressType;
    }

    public void setAddressType(AddressType addressType) {
        this.addressType = addressType;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
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

        AddressDTO that = (AddressDTO) o;

        if (addressId != that.addressId) return false;
        if (memberId != that.memberId) return false;
        if (preferred != that.preferred) return false;
        if (addressType != that.addressType) return false;
        if (address1 != null ? !address1.equals(that.address1) : that.address1 != null) return false;
        if (address2 != null ? !address2.equals(that.address2) : that.address2 != null) return false;
        if (city != null ? !city.equals(that.city) : that.city != null) return false;
        if (state != that.state) return false;
        return zipCode != null ? zipCode.equals(that.zipCode) : that.zipCode == null;

    }

    @Override
    public int hashCode() {
        int result = (int) (addressId ^ (addressId >>> 32));
        result = 31 * result + (addressType != null ? addressType.hashCode() : 0);
        result = 31 * result + (address1 != null ? address1.hashCode() : 0);
        result = 31 * result + (address2 != null ? address2.hashCode() : 0);
        result = 31 * result + (city != null ? city.hashCode() : 0);
        result = 31 * result + (state != null ? state.hashCode() : 0);
        result = 31 * result + (zipCode != null ? zipCode.hashCode() : 0);
        result = 31 * result + (int) (memberId ^ (memberId >>> 32));
        result = 31 * result + (preferred ? 1 : 0);
        return result;
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
    public String toString(){
        return String.format("%s: %s %s %s, %s %s", addressType, address1, (address2 != null ? address2 : ""), city, state, zipCode);
    }

    @Override
    public int compareTo(AddressDTO o) {
        return new Long(this.addressId).compareTo(o.addressId);
    }

    public enum AddressType {

        HOME,
        WORK,
        MAIL,
        OTHER;

    }
}
