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

    @ApiOperation(value = "Add New Org",
            notes = "Add a new org to the application.\n" +
                    "ACCESS:\n" +
                    "Role: MASTER",
            response = OrgDTO.class,
            code = 201)
    @POST
    @RolesAllowed(Role.MASTER)
    public Response addOrg(@ApiParam(value = "The org to add", required = true) OrgDTO org) throws OrgApiException{
        OrgService orgService = factory.newOrgService(securityContext);
        org = orgService.addOrg(org);

        return Response
                .created(URI.create(uriInfo.getPath() + "/" + org.getElementId()))
                .entity(org)
                .build();
    }

    @ApiOperation(value = "Update Org",
            notes = "Update an existing org in the application.\n" +
                    "ACCESS:\n" +
                    "Role: MASTER\n" +
                    "Extra: Users in Org with ADMIN role",
            response = OrgDTO.class,
            code = 202)
    @ApiResponses(value = @ApiResponse(code = 204, message = "Org to update didn't already exist."))
    @PUT
    @Path("/{orgId}")
    @ThisOrgAllowed(inOrgRolesAllowed = Role.ADMIN, outOfOrgRolesAllowed = Role.MASTER)
    public Response updateOrg(@ApiParam(value = "The ID of the org to update", required = true) @PathParam("orgId") long orgId,
                              @ApiParam(value = "The updated org", required = true) OrgDTO org) throws OrgApiException{
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

    @ApiOperation(value = "Delete Org",
            notes = "Delete an existing org from the application.\n" +
                    "ACCESS:\n" +
                    "Role: MASTER\n" +
                    "Extra: Users in Org with ADMIN role",
            response = OrgDTO.class,
            code = 202)
    @ApiResponses(value = @ApiResponse(code = 204, message = "Org to delete didn't already exist."))
    @DELETE
    @Path("/{orgId}")
    @ThisOrgAllowed(inOrgRolesAllowed = Role.ADMIN, outOfOrgRolesAllowed = Role.MASTER)
    public Response deleteOrg(@ApiParam(value = "The ID of the org to delete.", required = true) @PathParam("orgId") long orgId) throws OrgApiException{
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

    @ApiOperation(value = "Get Org",
            notes = "Get a single org from the application.\n" +
                    "ACCESS:\n" +
                    "Role: MASTER\n" +
                    "Extra: Users in Org")
    @ApiResponses(value = @ApiResponse(code = 204, message = "Specified org does not exist to retrieve."))
    @GET
    @Path("/{orgId}")
    @ThisOrgAllowed(outOfOrgRolesAllowed = Role.MASTER)
    public Response getOrg(@ApiParam(value = "The ID of the org to retrieve", required = true) @PathParam("orgId") long orgId) throws OrgApiException{
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
