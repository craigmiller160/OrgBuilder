package io.craigmiller160.orgbuilder.server.rest.resource;

import io.craigmiller160.orgbuilder.server.OrgApiException;
import io.craigmiller160.orgbuilder.server.dto.ErrorDTO;
import io.craigmiller160.orgbuilder.server.dto.OrgDTO;
import io.craigmiller160.orgbuilder.server.dto.OrgListDTO;
import io.craigmiller160.orgbuilder.server.rest.OrgApiInvalidRequestException;
import io.craigmiller160.orgbuilder.server.rest.ResourceFilterBean;
import io.craigmiller160.orgbuilder.server.rest.Role;
import io.craigmiller160.orgbuilder.server.rest.annotation.ThisOrgAllowed;
import io.craigmiller160.orgbuilder.server.service.OrgService;
import io.craigmiller160.orgbuilder.server.service.ServiceFactory;
import io.swagger.annotations.*;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.List;

/**
 * RESTful API for handling the Orgs resource.
 *
 * Created by craig on 8/23/16.
 */
@SwaggerDefinition(info = @Info(title = "OrgBuilder API", version = "1.1-ALPHA", description = "The API for the data managed by the OrgBuilder application"),
        securityDefinition = @SecurityDefinition(
                apiKeyAuthDefinitions = @ApiKeyAuthDefinition(
                        in = ApiKeyAuthDefinition.ApiKeyLocation.HEADER,
                        key = "orgapiToken",
                        name= "Authorization",
                        description = "The JSON Web Token needed to access the API"
                )
        )
)
@ApiResponses(value = {
        @ApiResponse(code = 403, message = "Access to resource is forbidden, you are either not logged in or don't have a high enough access level", response = ErrorDTO.class),
        @ApiResponse(code = 500, message = "Server error while processing request", response = ErrorDTO.class)
})
@Api (tags = "orgs", authorizations = @Authorization(value = "orgapiToken"))
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/orgs")
public class OrgResource {

    private final ServiceFactory factory = ServiceFactory.newInstance();

    @Context
    private SecurityContext securityContext;

    @Context
    private UriInfo uriInfo;

    @ApiOperation(value = "Get All Orgs",
            notes = "Get all orgs in the application.\n" +
                    "ACCESS:\n" +
                    "Role: MASTER",
            response = OrgListDTO.class)
    @ApiResponses(value = @ApiResponse(code = 204, message = "No orgs existed to return."))
    @GET
    @RolesAllowed(Role.MASTER)
    public Response getAllOrgs(@BeanParam ResourceFilterBean resourceFilterBean) throws OrgApiException{
        resourceFilterBean.validateFilterParams();

        OrgService orgService = factory.newOrgService(securityContext);
        OrgListDTO results = orgService.getAllOrgs(resourceFilterBean.getOffset(), resourceFilterBean.getSize());
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
     * RESOURCE: POST /orgs
     *
     * PURPOSE: Add a new Org.
     *
     * ACCESS: Only users with the MASTER role.
     *
     * BODY: A JSON payload with the Org to be added.
     *
     * QUERY PARAMS: NONE
     *
     * @param org the Org to be added.
     * @return the Response, containing the created Org.
     * @throws OrgApiException if an error occurs.
     */
    @POST
    @RolesAllowed(Role.MASTER)
    public Response addOrg(OrgDTO org) throws OrgApiException{
        OrgService orgService = factory.newOrgService(securityContext);
        org = orgService.addOrg(org);

        return Response
                .created(URI.create(uriInfo.getPath() + "/" + org.getElementId()))
                .entity(org)
                .build();
    }

    /**
     * RESOURCE: PUT /orgs/{orgId}
     *
     * PURPOSE: Update an existing Org.
     *
     * ACCESS: Only users with the MASTER role, or users
     *          with the ADMIN role who are a part of the
     *          Org being updated.
     *
     * BODY: A JSON payload with the Org to be updated.
     *
     * QUERY PARAMS: None
     *
     * @param orgId the Org ID of the resource to update.
     * @param org the Org to update.
     * @return the Response, containing the updated Org,
     *          or nothing if no Org existed with the specified ID.
     * @throws OrgApiException if an error occurs.
     */
    @PUT
    @Path("/{orgId}")
    @ThisOrgAllowed(inOrgRolesAllowed = Role.ADMIN, outOfOrgRolesAllowed = Role.MASTER)
    public Response updateOrg(@PathParam("orgId") long orgId, OrgDTO org) throws OrgApiException{
        OrgService orgService = factory.newOrgService(securityContext);
        OrgDTO result = orgService.updateOrg(org, orgId);

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
     * RESOURCE: DELETE /orgs/{orgId}
     *
     * PURPOSE: Delete an existing Org.
     *
     * ACCESS: Only users with the MASTER role, or users
     *          with the ADMIN role who are a part of the
     *          Org being updated.
     *
     * BODY: NONE
     *
     * QUERY PARAMS: NONE
     *
     * @param orgId the Org ID of the resource to delete.
     * @return the Response, containing the Org that was deleted,
     *          or nothing if no Org existed with the specified ID.
     * @throws OrgApiException if an error occurs.
     */
    @DELETE
    @Path("/{orgId}")
    @ThisOrgAllowed(inOrgRolesAllowed = Role.ADMIN, outOfOrgRolesAllowed = Role.MASTER)
    public Response deleteOrg(@PathParam("orgId") long orgId) throws OrgApiException{
        OrgService orgService = factory.newOrgService(securityContext);
        OrgDTO result = orgService.deleteOrg(orgId);

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
     * RESOURCE: GET /orgs/{orgId}
     *
     * PURPOSE: Retrieve a single Org and all its details.
     *
     * ACCESS: Only users with the MASTER role, or users
     *          with the ADMIN role who are a part of the
     *          Org being updated.
     *
     * BODY: NONE
     *
     * QUERY PARAMS: NONE
     *
     * @param orgId the Org ID of the resource to retrieve.
     * @return the Response, containing the Org that was retrieved,
     *          or nothing if no Org existed with the specified ID.
     * @throws OrgApiException if an error occurs.
     */
    @GET
    @Path("/{orgId}")
    @ThisOrgAllowed(outOfOrgRolesAllowed = Role.MASTER)
    public Response getOrg(@PathParam("orgId") long orgId) throws OrgApiException{
        OrgService orgService = factory.newOrgService(securityContext);
        OrgDTO result = orgService.getOrg(orgId);

        if(result != null){
            return Response
                    .ok(result)
                    .build();
        }
        return Response
                .noContent()
                .build();
    }

}
