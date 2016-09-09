package io.craigmiller160.orgbuilder.server.dto;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Created by craig on 9/9/16.
 */
@XmlRootElement
public class MemberListDTO {

    private List<MemberDTO> memberList;

    public MemberListDTO(){
        this(null);
    }

    public MemberListDTO(List<MemberDTO> memberList){
        this.memberList = memberList;
    }

    public List<MemberDTO> getMemberList() {
        return memberList;
    }

    public void setMemberList(List<MemberDTO> memberList) {
        this.memberList = memberList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MemberListDTO that = (MemberListDTO) o;

        return memberList != null ? memberList.equals(that.memberList) : that.memberList == null;

    }

    @Override
    public int hashCode() {
        return memberList != null ? memberList.hashCode() : 0;
    }
}
