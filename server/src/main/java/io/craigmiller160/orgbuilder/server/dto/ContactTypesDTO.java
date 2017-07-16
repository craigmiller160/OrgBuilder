package io.craigmiller160.orgbuilder.server.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Created by craig on 3/18/17.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ContactTypesDTO {

    private List<AddressDTO.AddressType> addressTypes;
    private List<PhoneDTO.PhoneType> phoneTypes;
    private List<EmailDTO.EmailType> emailTypes;

    public ContactTypesDTO(){}

    public ContactTypesDTO(List<AddressDTO.AddressType> addressTypes, List<PhoneDTO.PhoneType> phoneTypes, List<EmailDTO.EmailType> emailTypes){
        this.addressTypes = addressTypes;
        this.phoneTypes = phoneTypes;
        this.emailTypes = emailTypes;
    }

    public List<AddressDTO.AddressType> getAddressTypes() {
        return addressTypes;
    }

    public void setAddressTypes(List<AddressDTO.AddressType> addressTypes) {
        this.addressTypes = addressTypes;
    }

    public List<PhoneDTO.PhoneType> getPhoneTypes() {
        return phoneTypes;
    }

    public void setPhoneTypes(List<PhoneDTO.PhoneType> phoneTypes) {
        this.phoneTypes = phoneTypes;
    }

    public List<EmailDTO.EmailType> getEmailTypes() {
        return emailTypes;
    }

    public void setEmailTypes(List<EmailDTO.EmailType> emailTypes) {
        this.emailTypes = emailTypes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ContactTypesDTO that = (ContactTypesDTO) o;

        if (addressTypes != null ? !addressTypes.equals(that.addressTypes) : that.addressTypes != null) return false;
        if (phoneTypes != null ? !phoneTypes.equals(that.phoneTypes) : that.phoneTypes != null) return false;
        return emailTypes != null ? emailTypes.equals(that.emailTypes) : that.emailTypes == null;
    }

    @Override
    public int hashCode() {
        int result = addressTypes != null ? addressTypes.hashCode() : 0;
        result = 31 * result + (phoneTypes != null ? phoneTypes.hashCode() : 0);
        result = 31 * result + (emailTypes != null ? emailTypes.hashCode() : 0);
        return result;
    }
}
