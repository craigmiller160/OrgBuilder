package io.craigmiller160.orgbuilder.server.dto;

import java.time.LocalDateTime;

/**
 * Created by craig on 9/27/16.
 */
public class RefreshTokenDTO implements Comparable<RefreshTokenDTO>{

    private long tokenId;
    private long userId;
    private String tokenHash;
    private LocalDateTime timestamp;

    public long getTokenId() {
        return tokenId;
    }

    public void setTokenId(long tokenId) {
        this.tokenId = tokenId;
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

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RefreshTokenDTO that = (RefreshTokenDTO) o;

        if (tokenId != that.tokenId) return false;
        if (userId != that.userId) return false;
        if (tokenHash != null ? !tokenHash.equals(that.tokenHash) : that.tokenHash != null) return false;
        return timestamp != null ? timestamp.equals(that.timestamp) : that.timestamp == null;

    }

    @Override
    public int hashCode() {
        int result = (int) (tokenId ^ (tokenId >>> 32));
        result = 31 * result + (int) (userId ^ (userId >>> 32));
        result = 31 * result + (tokenHash != null ? tokenHash.hashCode() : 0);
        result = 31 * result + (timestamp != null ? timestamp.hashCode() : 0);
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
