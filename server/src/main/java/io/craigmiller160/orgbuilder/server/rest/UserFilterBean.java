package io.craigmiller160.orgbuilder.server.rest;

import javax.ws.rs.QueryParam;

/**
 * Created by craigmiller on 10/10/16.
 */
public class UserFilterBean extends ResourceFilterBean {

    @QueryParam(QueryParamName.USER_NAME)
    private String userName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
