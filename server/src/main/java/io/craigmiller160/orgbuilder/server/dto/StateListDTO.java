package io.craigmiller160.orgbuilder.server.dto;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.Arrays;
import java.util.List;

/**
 * Created by craig on 9/18/16.
 */
@XmlRootElement
public class StateListDTO {

    private List<State> states;

    public StateListDTO(){

    }

    public StateListDTO(State[] states) {
        this.states = states != null ? Arrays.asList(states) : null;
    }

    public StateListDTO(List<State> states){
        this.states = states;
    }

    public List<State> getStates() {
        return states;
    }

    public void setStates(List<State> states) {
        this.states = states;
    }

    public void setStates(State[] states){
        this.states = states != null ? Arrays.asList(states) : null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StateListDTO that = (StateListDTO) o;

        return states != null ? states.equals(that.states) : that.states == null;

    }

    @Override
    public int hashCode() {
        return states != null ? states.hashCode() : 0;
    }
}
