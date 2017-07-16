package io.craigmiller160.orgbuilder.server.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Arrays;
import java.util.List;

/**
 * Created by craig on 9/18/16.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class SexListDTO {

    private List<Sex> sexes;

    public SexListDTO(){

    }

    public SexListDTO(Sex[] sexes) {
        this.sexes = sexes != null ? Arrays.asList(sexes) : null;
    }

    public SexListDTO(List<Sex> sexes){
        this.sexes = sexes;
    }

    public List<Sex> getSexes() {
        return sexes;
    }

    public void setSexes(List<Sex> sexes) {
        this.sexes = sexes;
    }

    public void setGenders(Sex[] sexes){
        this.sexes = sexes != null ? Arrays.asList(sexes) : null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SexListDTO that = (SexListDTO) o;

        if (sexes != null ? !sexes.equals(that.sexes) : that.sexes != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return sexes != null ? sexes.hashCode() : 0;
    }
}
