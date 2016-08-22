package io.craigmiller160.orgbuilder.server.dto;

/**
 *
 *
 * Created by craig on 8/13/16.
 */
public class AddressDTO implements Comparable<AddressDTO>{

    private long addressId;
    private AddressType addressType;
    private String address;
    private String unit;
    private String city;
    private State state;
    private String zipCode;
    private long memberId;

    public AddressDTO(){}

    public AddressDTO(AddressType addressType, String address, String unit, String city, State state, String zipCode){
        this.addressType = addressType;
        this.address = address;
        this.unit = unit;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
    }

    public long getAddressId() {
        return addressId;
    }

    public void setAddressId(long addressId) {
        this.addressId = addressId;
    }

    public AddressType getAddressType() {
        return addressType;
    }

    public void setAddressType(AddressType addressType) {
        this.addressType = addressType;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AddressDTO that = (AddressDTO) o;

        if (addressId != that.addressId) return false;
        if (memberId != that.memberId) return false;
        if (addressType != that.addressType) return false;
        if (address != null ? !address.equals(that.address) : that.address != null) return false;
        if (unit != null ? !unit.equals(that.unit) : that.unit != null) return false;
        if (city != null ? !city.equals(that.city) : that.city != null) return false;
        if (state != that.state) return false;
        return zipCode != null ? zipCode.equals(that.zipCode) : that.zipCode == null;

    }

    @Override
    public int hashCode() {
        int result = (int) (addressId ^ (addressId >>> 32));
        result = 31 * result + (addressType != null ? addressType.hashCode() : 0);
        result = 31 * result + (address != null ? address.hashCode() : 0);
        result = 31 * result + (unit != null ? unit.hashCode() : 0);
        result = 31 * result + (city != null ? city.hashCode() : 0);
        result = 31 * result + (state != null ? state.hashCode() : 0);
        result = 31 * result + (zipCode != null ? zipCode.hashCode() : 0);
        result = 31 * result + (int) (memberId ^ (memberId >>> 32));
        return result;
    }

    public long getMemberId() {
        return memberId;
    }

    public void setMemberId(long memberId) {
        this.memberId = memberId;
    }

    @Override
    public String toString(){
        return String.format("%s: %s %s %s, %s %s", addressType, address, (unit != null ? unit : ""), city, state, zipCode);
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
