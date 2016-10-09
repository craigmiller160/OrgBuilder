package io.craigmiller160.orgbuilder.server.rest.resource;

import io.craigmiller160.orgbuilder.server.OrgApiException;
import io.craigmiller160.orgbuilder.server.dto.UserDTO;
import io.craigmiller160.orgbuilder.server.dto.UserListDTO;
import io.craigmiller160.orgbuilder.server.rest.OrgApiInvalidRequestException;
import io.craigmiller160.orgbuilder.server.rest.ResourceFilterBean;
import io.craigmiller160.orgbuilder.server.rest.Role;
import io.craigmiller160.orgbuilder.server.service.ServiceFactory;
import io.craigmiller160.orgbuilder.server.service.UserService;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;
import java.net.URI;

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
    //TODO need to restrict this by org, the org of the user adding/modifying the users
    //TODO deleting a user should also delete their refresh token
    //TODO get and update and delete should also be accessible by that user

    @GET
    @RolesAllowed({Role.MASTER, Role.ADMIN})
    public Response getAllUsers(@BeanParam ResourceFilterBean resourceFilterBean) throws OrgApiException {
        resourceFilterBean.validateFilterParams();
        //TODO add search for user by name
        //TODO admin role can only get users in org
        UserService service = factory.newUserService(securityContext);
        UserListDTO results = service.getAllUsers(resourceFilterBean.getOffset(), resourceFilterBean.getSize());

        if(results != null){
            return Response
                    .ok(results)
                    .build();
        }

        return Response
                .noContent()
                .build();
    }

    @POST
    @RolesAllowed({Role.MASTER, Role.ADMIN})
    public Response addUser(UserDTO user) throws OrgApiException{
        validateUser(user);
        //TODO admin role can only add user for org
        UserService service = factory.newUserService(securityContext);
        UserDTO result = service.addUser(user);

        return Response
                .created(URI.create(uriInfo.getPath() + "/" + user.getElementId()))
                .entity(result)
                .build();
    }

    @PUT
    @Path("/{userId}")
    @PermitAll
    public Response updateUser(@PathParam("userId") long userId, UserDTO user) throws OrgApiException{
        //TODO restrict this to either an admin or the specific user involved
        validateUser(user);
        UserService service = factory.newUserService(securityContext);
        UserDTO result = service.updateUser(user, userId);

        return Response
                .accepted(result)
                .build();
    }

    @DELETE
    @Path("/{userId}")
    @PermitAll
    public Response deleteUser(@PathParam("userId") long userId) throws OrgApiException{
        //TODO restrict this to either an admin or the specific user involved
        UserService service = factory.newUserService(securityContext);
        UserDTO result = service.deleteUser(userId);

        if(result != null){
            return Response
                    .accepted(result)
                    .build();
        }

        return Response
                .noContent()
                .build();
    }

    @GET
    @Path("/{userId}")
    public Response getUser(@PathParam("userId") long userId) throws OrgApiException{
        //TODO finish this
        return null;
    }

    private void validateUser(UserDTO user) throws OrgApiInvalidRequestException{
        if(StringUtils.isEmpty(user.getUserEmail())){
            throw new OrgApiInvalidRequestException("User is invalid, missing email field");
        }

        if(StringUtils.isEmpty(user.getPassword())){
            throw new OrgApiInvalidRequestException("User is invalid, missing password field");
        }
    }

}
