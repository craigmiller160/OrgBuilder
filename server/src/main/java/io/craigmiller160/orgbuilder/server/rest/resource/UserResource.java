package io.craigmiller160.orgbuilder.server.rest.resource;

import io.craigmiller160.orgbuilder.server.OrgApiException;
import io.craigmiller160.orgbuilder.server.dto.UserDTO;
import io.craigmiller160.orgbuilder.server.rest.ResourceFilterBean;
import io.craigmiller160.orgbuilder.server.service.ServiceFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;

/**
 * Created by craig on 9/15/16.
 */
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/users")
public class UserResource {

    private final ServiceFactory factory = ServiceFactory.newInstance();

    @Context
    private SecurityContext securityContext;

    @Context
    private UriInfo uriInfo;

    //TODO need to decide if this should be a sub-resource of orgs, or if it needs a separate parameter passed for the org

    @GET
    public Response getAllUsers(@BeanParam ResourceFilterBean resourceFilterBean) throws OrgApiException {
        resourceFilterBean.validateFilterParams();
        //TODO finish this
        return null;
    }

    @POST
    public Response addUser(UserDTO user) throws OrgApiException{
        //TODO finish this
        return null;
    }

    @PUT
    @Path("/{userId}")
    public Response updateUser(@PathParam("userId") long userId, UserDTO user) throws OrgApiException{
        //TODO finish this
        return null;
    }

    @DELETE
    @Path("/{userId}")
    public Response deleteUser(@PathParam("userId") long userId, UserDTO user) throws OrgApiException{
        //TODO finish this
        return null;
    }

    @GET
    @Path("/{userId}")
    public Response getUser(@PathParam("userId") long userId) throws OrgApiException{
        //TODO finish this
        return null;
    }

}
