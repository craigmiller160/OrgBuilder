package io.craigmiller160.orgbuilder.server.dto;

import io.craigmiller160.orgbuilder.server.rest.Role;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by craig on 2/5/17.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class AllInfoDTO {

    private AppInfoDTO appInfo;
    private List<Sex> sexes;
    private List<State> states;
    private List<String> roles;

    public AllInfoDTO(){

    }

    public AllInfoDTO(AppInfoDTO appInfo, List<Sex> sexes, List<State> states, List<String> roles) {
        this.appInfo = appInfo;
        this.sexes = sexes;
        this.states = states;
        this.roles = roles;
    }

    public AllInfoDTO(AppInfoDTO appInfo, Sex[] sexes, State[] states, String[] roles){
        this(appInfo, Arrays.asList(sexes), Arrays.asList(states), Arrays.asList(roles));
    }

    public AppInfoDTO getAppInfo() {
        return appInfo;
    }

    public void setAppInfo(AppInfoDTO appInfo) {
        this.appInfo = appInfo;
    }

    public List<Sex> getSexes() {
        return sexes;
    }

    public void setSexes(List<Sex> sexes) {
        this.sexes = sexes;
    }

    public List<State> getStates() {
        return states;
    }

    public void setStates(List<State> states) {
        this.states = states;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AllInfoDTO that = (AllInfoDTO) o;

        if (appInfo != null ? !appInfo.equals(that.appInfo) : that.appInfo != null) return false;
        if (sexes != null ? !sexes.equals(that.sexes) : that.sexes != null) return false;
        if (states != null ? !states.equals(that.states) : that.states != null) return false;
        return roles != null ? roles.equals(that.roles) : that.roles == null;
    }

    @Override
    public int hashCode() {
        int result = appInfo != null ? appInfo.hashCode() : 0;
        result = 31 * result + (sexes != null ? sexes.hashCode() : 0);
        result = 31 * result + (states != null ? states.hashCode() : 0);
        result = 31 * result + (roles != null ? roles.hashCode() : 0);
        return result;
    }
}
