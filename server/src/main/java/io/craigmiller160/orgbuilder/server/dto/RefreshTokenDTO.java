package io.craigmiller160.orgbuilder.server.dto;

import io.craigmiller160.orgbuilder.server.rest.LocalDateAdapter;
import io.craigmiller160.orgbuilder.server.rest.LocalDateTimeAdapter;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.LocalDateTime;

/**
 * Created by craig on 9/27/16.
 */
@XmlRootElement
public class RefreshTokenDTO implements DTO<Long>, Comparable<RefreshTokenDTO>{

    private long tokenId;
    private long userId;
    private long orgId;
    private String tokenHash;
    @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
    private LocalDateTime expiration;
    private String userEmail;
    private String orgName;

    public RefreshTokenDTO(){}

    public RefreshTokenDTO(long userId, long orgId, String tokenHash, LocalDateTime expiration){
        this.userId = userId;
        this.orgId = orgId;
        this.tokenHash = tokenHash;
        this.expiration = expiration;
    }

    @Override
    public Long getElementId() {
        return tokenId;
    }

    @Override
    public void setElementId(Long id) {
        this.tokenId = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getTokenHash() {
        return tokenHash;
    }

    public void setTokenHash(String tokenHash) {
        this.tokenHash = tokenHash;
    }

    public LocalDateTime getExpiration() {
        return expiration;
    }

    public void setExpiration(LocalDateTime expiration) {
        this.expiration = expiration;
    }

    public long getOrgId() {
        return orgId;
    }

    public void setOrgId(long orgId) {
        this.orgId = orgId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RefreshTokenDTO that = (RefreshTokenDTO) o;

        if (tokenId != that.tokenId) return false;
        if (userId != that.userId) return false;
        if (orgId != that.orgId) return false;
        if (tokenHash != null ? !tokenHash.equals(that.tokenHash) : that.tokenHash != null) return false;
        if (expiration != null ? !expiration.equals(that.expiration) : that.expiration != null) return false;
        if (userEmail != null ? !userEmail.equals(that.userEmail) : that.userEmail != null) return false;
        return orgName != null ? orgName.equals(that.orgName) : that.orgName == null;

    }

    @Override
    public int hashCode() {
        int result = (int) (tokenId ^ (tokenId >>> 32));
        result = 31 * result + (int) (userId ^ (userId >>> 32));
        result = 31 * result + (int) (orgId ^ (orgId >>> 32));
        result = 31 * result + (tokenHash != null ? tokenHash.hashCode() : 0);
        result = 31 * result + (expiration != null ? expiration.hashCode() : 0);
        result = 31 * result + (userEmail != null ? userEmail.hashCode() : 0);
        result = 31 * result + (orgName != null ? orgName.hashCode() : 0);
        return result;
    }

    @Override
    public String toString(){
        return "Refresh Token: " + tokenHash;
    }

    @Override
    public int compareTo(RefreshTokenDTO o) {
        return new Long(this.tokenId).compareTo(o.tokenId);
    }
}
