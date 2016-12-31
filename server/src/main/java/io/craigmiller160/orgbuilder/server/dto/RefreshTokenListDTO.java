package io.craigmiller160.orgbuilder.server.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Created by craig on 10/17/16.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class RefreshTokenListDTO {

    private List<RefreshTokenDTO> tokenList;

    public RefreshTokenListDTO(){}

    public RefreshTokenListDTO(List<RefreshTokenDTO> tokenList){
        this.tokenList = tokenList;
    }

    public List<RefreshTokenDTO> getTokenList() {
        return tokenList;
    }

    public void setTokenList(List<RefreshTokenDTO> tokenList) {
        this.tokenList = tokenList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RefreshTokenListDTO that = (RefreshTokenListDTO) o;

        return tokenList != null ? tokenList.equals(that.tokenList) : that.tokenList == null;

    }

    @Override
    public int hashCode() {
        return tokenList != null ? tokenList.hashCode() : 0;
    }
}
