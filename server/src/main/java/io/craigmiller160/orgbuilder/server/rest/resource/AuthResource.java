package io.craigmiller160.orgbuilder.server.rest.resource;

import io.craigmiller160.orgbuilder.server.rest.Role;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

/**
 * Created by craig on 9/27/16.
 */
@Path("/auth")
public class AuthResource {

    @POST
    @PermitAll
    public Response authenticate(){
        //TODO finish this
        return null;
    }

    @DELETE
    @RolesAllowed(Role.MASTER)
    public Response invalidate(){
        //TODO finish this
        return null;
    }

}
