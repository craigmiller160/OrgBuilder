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
@Path("/old.users")
public class UserResource {

    private final ServiceFactory factory = ServiceFactory.newInstance();

    @Context
    private SecurityContext securityContext;

    @Context
    private UriInfo uriInfo;

    /**
     * RESOURCE: GET /old.users
     *
     * PURPOSE: Get all old.users in the system, restricted based on
     *          their Org if a non-MASTER user calls this.
     *
     * ACCESS: Users with the MASTER or ADMIN roles. The ADMIN role
     *          old.users will only access old.users in their own Org.
     *
     * BODY: NONE
     *
     * QUERY PARAMS:
     * offset: the number of records to skip over before starting retrieval.
     * size: the total number of records to retrieve.
     * {search}: additional params for performing a search function.
     *
     * @param userFilterBean the filter bean with the Query Params.
     * @return the Response, containing all the old.users retrieved.
     * @throws OrgApiException if an error occurs.
     */
    @GET
    @RolesAllowed({Role.MASTER, Role.ADMIN})
    public Response getAllUsers(@BeanParam UserFilterBean userFilterBean) throws OrgApiException {
        userFilterBean.validateFilterParams();
        //TODO add search for user by name
        UserService service = factory.newUserService(securityContext);

        //If the principal has an orgId, they must have an Admin role (due to annotation restriction)
        //The service will restrict results to only old.users in their org.
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

    /**
     * RESOURCE: POST /old.users
     *
     * PURPOSE: Create a new user. If done by an ADMIN user, that user
     *          will be restricted to the creating user's Org.
     *
     * ACCESS: Users with the MASTER or ADMIN roles. The ADMIN role
     *          old.users will only access old.users in their own Org.
     *
     * BODY: The user to create.
     *
     * QUERY PARAMS: NONE
     *
     * @param user the user to create.
     * @return the Response, containing the created user.
     * @throws OrgApiException if an error occurs.
     */
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

    /**
     * RESOURCE: PUT /old.users/{userId}
     *
     * PURPOSE: Update an existing user.
     *
     * ACCESS: Users with the MASTER or ADMIN roles. The ADMIN role
     *          old.users will only access old.users in their own Org.
     *          In addition, if the user calling this resource is
     *          the same as the user being retrieved, they can access it.
     *
     * BODY: The updated user.
     *
     * QUERY PARAMS: NONE
     *
     * @param userId the ID of the user to update.
     * @param user the updated user.
     * @return the Response, containing the updated user, or
     *          nothing if there was no user with the specified ID.
     * @throws OrgApiException if an error occurs.
     */
    @PUT
    @Path("/{userId}")
    @ThisUserAllowed(otherUserRolesAllowed = {Role.ADMIN,Role.MASTER})
    public Response updateUser(@PathParam("userId") long userId, UserDTO user) throws OrgApiException{
        validateUser(user, false);
        ensureMasterCreationRestriction(user);
        UserService service = factory.newUserService(securityContext);

        UserDTO result = service.updateUser(user, userId, (u) -> {
            ensureAdminAccessRestriction(u);
            blockRegularUserRoleChanging(user, u);
        });

        if(result != null){
            return Response
                    .accepted(result)
                    .build();
        }

        return Response
                .noContent()
                .build();
    }

    /**
     * RESOURCE: DELETE /old.users/{userId}
     *
     * PURPOSE: Delete an existing user.
     *
     * ACCESS: Users with the MASTER or ADMIN roles. The ADMIN role
     *          old.users will only access old.users in their own Org.
     *          In addition, if the user calling this resource is
     *          the same as the user being retrieved, they can access it.
     *
     * BODY: NONE
     *
     * QUERY PARAMS: NONE
     *
     * @param userId the ID of the user to delete.
     * @return the Response, containing the deleted user, or
     *          nothing if there was no user with the specified ID.
     * @throws OrgApiException if an error occurs.
     */
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

    /**
     * RESOURCE: GET /old.users/{userId}
     *
     * PURPOSE: Retrieve the specified user.
     *
     * ACCESS: Users with the MASTER or ADMIN roles. The ADMIN role
     *          old.users will only access old.users in their own Org.
     *          In addition, if the user calling this resource is
     *          the same as the user being retrieved, they can access it.
     *
     * BODY: NONE
     *
     * QUERY PARAMS: NONE
     *
     * @param userId the ID of the user to retrieve.
     * @return the Response, containing the user that was retrieved, or
     *          nothing if there was no user with the specified ID.
     * @throws OrgApiException if an error occurs.
     */
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
            throw new ForbiddenException("Only old.users with Master access can give a user Master access");
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

    private void blockRegularUserRoleChanging(UserDTO newUser, UserDTO existingUser){
        if((!isPrincipalAdmin() && !isPrincipalMaster()) && !newUser.getRoles().equals(existingUser.getRoles())){
            throw new ForbiddenException("Non-Admin user cannot change user roles");
        }
    }

}
