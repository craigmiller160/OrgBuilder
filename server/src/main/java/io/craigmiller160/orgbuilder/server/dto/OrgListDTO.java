package io.craigmiller160.orgbuilder.server.dto;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Created by craig on 9/11/16.
 */
@XmlRootElement
public class OrgListDTO {

    private List<OrgDTO> orgList;

    public OrgListDTO(){
        this(null);
    }

    public OrgListDTO(List<OrgDTO> orgList) {
        this.orgList = orgList;
    }

    public List<OrgDTO> getOrgList() {
        return orgList;
    }

    public void setOrgList(List<OrgDTO> orgList) {
        this.orgList = orgList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OrgListDTO that = (OrgListDTO) o;

        return orgList != null ? orgList.equals(that.orgList) : that.orgList == null;

    }

    @Override
    public int hashCode() {
        return orgList != null ? orgList.hashCode() : 0;
    }
}
