package io.craigmiller160.orgbuilder.server.rest.resource;

import io.craigmiller160.orgbuilder.server.OrgApiException;
import io.craigmiller160.orgbuilder.server.dto.UserDTO;
import io.craigmiller160.orgbuilder.server.dto.UserListDTO;
import io.craigmiller160.orgbuilder.server.rest.OrgApiInvalidRequestException;
import io.craigmiller160.orgbuilder.server.rest.ResourceFilterBean;
import io.craigmiller160.orgbuilder.server.rest.Role;
import io.craigmiller160.orgbuilder.server.rest.UserFilterBean;
import io.craigmiller160.orgbuilder.server.rest.annotation.ThisUserAllowed;
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
 * The resource for retrieving and manipulating
 * user records.
 *
 * NOTE: This resource has more complex authentication
 * than others. Some final authentication is done in
 * the service layer for certain methods, and ForbiddenExceptions are thrown
 * from it.
 *
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

    //TODO test access for ThisUserAllowed annotation, and the service layer restrictions for those methods and admin accounts
    //TODO test that admin role calling getAllUsers() can only get users in their org
    //TODO test that admin role can only add users for their own org, and can't update users to another org
    //TODO test new users table trigger restricting the insert of master users

    @GET
    @RolesAllowed({Role.MASTER, Role.ADMIN})
    public Response getAllUsers(@BeanParam UserFilterBean userFilterBean) throws OrgApiException {
        userFilterBean.validateFilterParams();
        //TODO add search for user by name
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

        UserService service = factory.newUserService(securityContext);
        UserDTO result = service.addUser(user);

        return Response
                .created(URI.create(uriInfo.getPath() + "/" + user.getElementId()))
                .entity(result)
                .build();
    }

    @PUT
    @Path("/{userId}")
    @ThisUserAllowed(otherUserRolesAllowed = {Role.ADMIN,Role.MASTER})
    public Response updateUser(@PathParam("userId") long userId, UserDTO user) throws OrgApiException{
        validateUser(user);
        UserService service = factory.newUserService(securityContext);
        UserDTO result = service.updateUser(user, userId);

        return Response
                .accepted(result)
                .build();
    }

    @DELETE
    @Path("/{userId}")
    @ThisUserAllowed(otherUserRolesAllowed = {Role.ADMIN, Role.MASTER})
    public Response deleteUser(@PathParam("userId") long userId) throws OrgApiException{
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
    @ThisUserAllowed(otherUserRolesAllowed = {Role.ADMIN, Role.MASTER})
    public Response getUser(@PathParam("userId") long userId) throws OrgApiException{
        UserService service = factory.newUserService(securityContext);
        UserDTO result = service.getUser(userId);

        if(result != null){
            return Response
                    .ok(result)
                    .build();
        }

        return Response
                .noContent()
                .build();
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
