package io.craigmiller160.orgbuilder.server.dto;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Created by craig on 9/11/16.
 */
@XmlRootElement
public class AddressListDTO {

    private List<AddressDTO> addressList;

    public AddressListDTO(){
        this(null);
    }

    public AddressListDTO(List<AddressDTO> addressList){
        this.addressList = addressList;
    }

    public List<AddressDTO> getAddressList() {
        return addressList;
    }

    public void setAddressList(List<AddressDTO> addressList) {
        this.addressList = addressList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AddressListDTO that = (AddressListDTO) o;

        return addressList != null ? addressList.equals(that.addressList) : that.addressList == null;

    }

    @Override
    public int hashCode() {
        return addressList != null ? addressList.hashCode() : 0;
    }
}
