package io.craigmiller160.orgbuilder.server.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Created by craig on 9/11/16.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class PhoneListDTO {

    private List<PhoneDTO> phoneList;

    public PhoneListDTO(){
        this(null);
    }

    public PhoneListDTO(List<PhoneDTO> phoneList) {
        this.phoneList = phoneList;
    }

    public List<PhoneDTO> getPhoneList() {
        return phoneList;
    }

    public void setPhoneList(List<PhoneDTO> phoneList) {
        this.phoneList = phoneList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PhoneListDTO that = (PhoneListDTO) o;

        return phoneList != null ? phoneList.equals(that.phoneList) : that.phoneList == null;

    }

    @Override
    public int hashCode() {
        return phoneList != null ? phoneList.hashCode() : 0;
    }
}
