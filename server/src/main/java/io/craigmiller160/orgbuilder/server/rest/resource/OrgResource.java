package io.craigmiller160.orgbuilder.server.rest.resource;

import io.craigmiller160.orgbuilder.server.OrgApiException;
import io.craigmiller160.orgbuilder.server.dto.OrgDTO;
import io.craigmiller160.orgbuilder.server.dto.OrgListDTO;
import io.craigmiller160.orgbuilder.server.rest.OrgApiInvalidRequestException;
import io.craigmiller160.orgbuilder.server.service.OrgService;
import io.craigmiller160.orgbuilder.server.service.ServiceFactory;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.List;

/**
 * Created by craig on 8/23/16.
 */
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/orgs")
public class OrgResource {

    private final ServiceFactory factory = ServiceFactory.newInstance();

    @Context
    private SecurityContext securityContext;

    @Context
    private UriInfo uriInfo;

    //TODO need to remove schema name from response

    @GET
    public Response getAllOrgs(@QueryParam("offset") @DefaultValue("-1") long offset,
                               @QueryParam("size") @DefaultValue("-1") long size) throws OrgApiException{
        if((offset != -1 && size == -1) || (offset == -1 && size != -1)){
            throw new OrgApiInvalidRequestException("Invalid offset/size query parameters.");
        }
        OrgService orgService = factory.newOrgService(securityContext);
        OrgListDTO results = orgService.getAllOrgs(offset, size);
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
    public Response addOrg(OrgDTO org) throws OrgApiException{
        //TODO need to create a new schema when this is executed
        OrgService orgService = factory.newOrgService(securityContext);
        org = orgService.addOrg(org);

        return Response
                .created(URI.create(uriInfo.getPath() + "/" + org.getElementId()))
                .entity(org)
                .build();
    }

    @PUT
    @Path("/{orgId}")
    public Response updateOrg(@PathParam("orgId") long orgId, OrgDTO org) throws OrgApiException{
        OrgService orgService = factory.newOrgService(securityContext);
        org = orgService.updateOrg(org, orgId);

        return Response
                .accepted(org)
                .build();
    }

    @DELETE
    @Path("/{orgId}")
    public Response deleteOrg(@PathParam("orgId") long orgId) throws OrgApiException{
        //TODO need to delete schema
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

    @GET
    @Path("/{orgId}")
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
