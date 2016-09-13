package io.craigmiller160.orgbuilder.server.rest;

/**
 * Created by craig on 9/4/16.
 */
public enum Role{

    MASTER ("The webmaster. Has master access to the application and all organizations"),
    ADMIN ("The administrator of an organization. Has read/write access to only the data of that organization, as well as the ability to create new user accounts for that organization."),
    WRITE("A regular user of an organization. Has read/write access to only the data of that organization."),
    READ("A regular user of an organization. Has read only access to only the data of that organization.");

    private final String description;

    Role(String description){
        this.description = description;
    }

    public String getDescription(){
        return description;
    }

}
