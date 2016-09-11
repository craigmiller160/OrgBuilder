package io.craigmiller160.orgbuilder.server.dto;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Created by craig on 9/11/16.
 */
@XmlRootElement
public class EmailListDTO {

    private List<EmailDTO> emailList;

    public EmailListDTO(){
        this(null);
    }

    public EmailListDTO(List<EmailDTO> emailList) {
        this.emailList = emailList;
    }

    public List<EmailDTO> getEmailList() {
        return emailList;
    }

    public void setEmailList(List<EmailDTO> emailList) {
        this.emailList = emailList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EmailListDTO that = (EmailListDTO) o;

        return emailList != null ? emailList.equals(that.emailList) : that.emailList == null;

    }

    @Override
    public int hashCode() {
        return emailList != null ? emailList.hashCode() : 0;
    }
}
