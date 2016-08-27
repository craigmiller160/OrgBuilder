package io.craigmiller160.orgbuilder.server.dto;

import java.util.Arrays;

/**
 * Enumeration of US States.
 *
 * Created by craig on 8/13/16.
 */
public enum State {

    AL ("Alabama"),
    AK ("Alaska"),
    AZ ("Arizona"),
    AR ("Arkansas"),
    CA ("California"),
    CO ("Colorado"),
    CT ("Connecticut"),
    DC ("District of Columbia"),
    DE ("Delaware"),
    FL ("Florida"),
    GA ("Georgia"),
    HI ("Hawaii"),
    ID ("Idaho"),
    IL ("Illinois"),
    IN ("Indiana"),
    IA ("Iowa"),
    KS ("Kansas"),
    KY ("Kentucky"),
    LA ("Louisiana"),
    ME ("Maine"),
    MD ("Maryland"),
    MA ("Massachusetts"),
    MI ("Michigan"),
    MN ("Minnesota"),
    MS ("Mississippi"),
    MO ("Missouri"),
    MT ("Montana"),
    NE ("Nebraska"),
    NV ("Nevada"),
    NH ("New Hampshire"),
    NJ ("New Jersey"),
    NY ("New York"),
    NC ("North Carolina"),
    ND ("North Dakota"),
    OH ("Ohio"),
    OK ("Oklahoma"),
    OR ("Oregon"),
    PA ("Pennsylvania"),
    RI ("Rhode Island"),
    SC ("South Carolina"),
    SD ("South Dakota"),
    TN ("Tennessee"),
    TX ("Texas"),
    UT ("Utah"),
    VT ("Vermont"),
    VA ("Virginia"),
    WA ("Washington"),
    WV ("West Virginia"),
    WI ("Wisconsin"),
    WY ("Wyoming");

    /**
     * The name of the state.
     */
    private String name;

    /**
     * Private constructor builds a
     * <tt>State</tt> with a full name
     * assigned to it.
     *
     * @param name the name of the state
     */
    State(String name){
        this.name = name;
    }

    /**
     * Get the <tt>String</tt> name of the
     * state.
     *
     * @return the name of the state
     */
    public String getName(){
        return name;
    }

    /**
     * Get the <tt>State</tt> enum from the
     * provided <tt>String</tt> name.
     *
     * @param name the name of the state.
     * @return the <tt>State</tt> enum.
     */
    public static State getStateForName(String name){
        return Arrays.stream(State.values())
                .filter((s) -> s.getName().equalsIgnoreCase(name))
                .findFirst().orElse(null);
    }

}
