package io.craigmiller160.orgbuilder.server.dto;

import java.time.LocalDate;

/**
 * Created by craig on 8/21/16.
 */
public class OrgDTO implements Comparable<OrgDTO>{

    private long orgId;
    private String orgName;
    private LocalDate createdDate;
    private String schemaName;

    public OrgDTO(){}

    public OrgDTO(long orgId, String orgName, LocalDate createdDate, String schemaName) {
        this.orgId = orgId;
        this.orgName = orgName;
        this.createdDate = createdDate;
        this.schemaName = schemaName;
    }

    public long getOrgId() {
        return orgId;
    }

    public void setOrgId(long orgId) {
        this.orgId = orgId;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public String getSchemaName() {
        return schemaName;
    }

    public void setSchemaName(String schemaName) {
        this.schemaName = schemaName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OrgDTO orgDTO = (OrgDTO) o;

        if (orgId != orgDTO.orgId) return false;
        if (orgName != null ? !orgName.equals(orgDTO.orgName) : orgDTO.orgName != null) return false;
        if (createdDate != null ? !createdDate.equals(orgDTO.createdDate) : orgDTO.createdDate != null) return false;
        return schemaName != null ? schemaName.equals(orgDTO.schemaName) : orgDTO.schemaName == null;

    }

    @Override
    public int hashCode() {
        int result = (int) (orgId ^ (orgId >>> 32));
        result = 31 * result + (orgName != null ? orgName.hashCode() : 0);
        result = 31 * result + (createdDate != null ? createdDate.hashCode() : 0);
        result = 31 * result + (schemaName != null ? schemaName.hashCode() : 0);
        return result;
    }

    @Override
    public String toString(){
        return "Org: " + orgName;
    }

    @Override
    public int compareTo(OrgDTO o) {
        return new Long(this.orgId).compareTo(o.orgId);
    }
}
