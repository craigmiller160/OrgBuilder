package io.craigmiller160.orgbuilder.server.rest.resource;

import io.craigmiller160.orgbuilder.server.OrgApiException;
import io.craigmiller160.orgbuilder.server.dto.UserDTO;
import io.craigmiller160.orgbuilder.server.dto.UserListDTO;
import io.craigmiller160.orgbuilder.server.rest.OrgApiInvalidRequestException;
import io.craigmiller160.orgbuilder.server.rest.OrgApiPrincipal;
import io.craigmiller160.orgbuilder.server.rest.Role;
import io.craigmiller160.orgbuilder.server.rest.UserFilterBean;
import io.craigmiller160.orgbuilder.server.rest.annotation.ThisUserAllowed;
import io.craigmiller160.orgbuilder.server.service.ServiceFactory;
import io.craigmiller160.orgbuilder.server.service.UserService;
import org.apache.commons.lang3.StringUtils;

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
 * than others. In addition to the annotations, a great
 * deal of authentication is done in this class as well.
 * In addition, some UserService methods have their own,
 * internal validation that affects what kind of results
 * are returned, based on the principal's access.
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

    @GET
    @RolesAllowed({Role.MASTER, Role.ADMIN})
    public Response getAllUsers(@BeanParam UserFilterBean userFilterBean) throws OrgApiException {
        userFilterBean.validateFilterParams();
        //TODO add search for user by name
        UserService service = factory.newUserService(securityContext);

        //If the principal has an orgId, they must have an Admin role (due to annotation restriction)
        //The service will restrict results to only users in their org.
        UserListDTO results = service.getAllUsers(getPrincipalOrgId(), userFilterBean.getOffset(), userFilterBean.getSize());

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
        validateUser(user, true);
        ensureMasterCreationRestriction(user);
        ensureAdminAccessRestriction(user);

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
        validateUser(user, false);
        ensureMasterCreationRestriction(user);
        UserService service = factory.newUserService(securityContext);

        ensureAdminAccessRestriction(user);

        UserDTO result = service.updateUser(user, userId);

        if(result != null){
            return Response
                    .accepted(result)
                    .build();
        }

        return Response
                .noContent()
                .build();
    }

    @DELETE
    @Path("/{userId}")
    @ThisUserAllowed(otherUserRolesAllowed = {Role.ADMIN, Role.MASTER})
    public Response deleteUser(@PathParam("userId") long userId) throws OrgApiException{
        UserService service = factory.newUserService(securityContext);

        UserDTO result = service.deleteUser(userId, this::ensureAdminAccessRestriction);

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
        ensureAdminAccessRestriction(result);

        if(result != null){
            return Response
                    .ok(result)
                    .build();
        }

        return Response
                .noContent()
                .build();
    }

    private void validateUser(UserDTO user, boolean requirePassword) throws OrgApiInvalidRequestException{
        if(StringUtils.isEmpty(user.getUserEmail())){
            throw new OrgApiInvalidRequestException("User is invalid, missing email field");
        }

        if(StringUtils.isEmpty(user.getPassword()) && requirePassword){
            throw new OrgApiInvalidRequestException("User is invalid, missing password field");
        }

        if(user.getOrgId() <= 0 && !(user.getRoles() != null && user.getRoles().contains(Role.MASTER))){
            throw new OrgApiInvalidRequestException("User is invalid, doesn't have an org assignment");
        }
    }

    private void ensureMasterCreationRestriction(UserDTO user) {
        if(user.getRoles().contains(Role.MASTER) && !isPrincipalMaster()){
            throw new ForbiddenException("Only users with Master access can give a user Master access");
        }

        if(user.getRoles().contains(Role.MASTER) && (user.getRoles().size() != 1 || user.getOrgId() > 0)){
            throw new ForbiddenException("Cannot add a user with Master role that has other roles or an Org assignment.");
        }
    }

    private long getPrincipalOrgId(){
        OrgApiPrincipal principal = (OrgApiPrincipal) securityContext.getUserPrincipal();
        return principal.getOrgId();
    }

    private boolean isPrincipalAdmin(){
        OrgApiPrincipal principal = (OrgApiPrincipal) securityContext.getUserPrincipal();
        return principal.isUserInRole(Role.ADMIN);
    }

    private boolean isPrincipalMaster(){
        OrgApiPrincipal principal = (OrgApiPrincipal) securityContext.getUserPrincipal();
        return principal.isUserInRole(Role.MASTER);
    }

    private void ensureAdminAccessRestriction(UserDTO user){
        if(isPrincipalAdmin() && getPrincipalOrgId() != user.getOrgId()){
            throw new ForbiddenException("Admin user cannot access a user outside of their own org");
        }
    }

}
