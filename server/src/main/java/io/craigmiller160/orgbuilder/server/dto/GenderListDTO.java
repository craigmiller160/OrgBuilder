package io.craigmiller160.orgbuilder.server.dto;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.Arrays;
import java.util.List;

/**
 * Created by craig on 9/18/16.
 */
@XmlRootElement
public class GenderListDTO {

    private List<Gender> genders;

    public GenderListDTO(){

    }

    public GenderListDTO(Gender[] genders) {
        this.genders = genders != null ? Arrays.asList(genders) : null;
    }

    public GenderListDTO(List<Gender> genders){
        this.genders = genders;
    }

    public List<Gender> getGenders() {
        return genders;
    }

    public void setGenders(List<Gender> genders) {
        this.genders = genders;
    }

    public void setGenders(Gender[] genders){
        this.genders = genders != null ? Arrays.asList(genders) : null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GenderListDTO that = (GenderListDTO) o;

        if (genders != null ? !genders.equals(that.genders) : that.genders != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return genders != null ? genders.hashCode() : 0;
    }
}
