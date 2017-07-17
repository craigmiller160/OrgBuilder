package io.craigmiller160.orgbuilder.server.rest;

import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;

import javax.ws.rs.QueryParam;

/**
 * Created by craigmiller on 10/10/16.
 */
public class UserFilterBean extends ResourceFilterBean {

    @ApiParam(value = "Optional search parameter for the user name.")
    @QueryParam(QueryParamName.USER_NAME)
    private String userName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public boolean isSearch(){
        return !StringUtils.isEmpty(userName);
    }
}
