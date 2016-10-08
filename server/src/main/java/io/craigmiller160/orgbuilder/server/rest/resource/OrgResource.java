package io.craigmiller160.orgbuilder.server.rest.resource;

import io.craigmiller160.orgbuilder.server.OrgApiException;
import io.craigmiller160.orgbuilder.server.dto.OrgDTO;
import io.craigmiller160.orgbuilder.server.dto.OrgListDTO;
import io.craigmiller160.orgbuilder.server.rest.OrgApiInvalidRequestException;
import io.craigmiller160.orgbuilder.server.rest.ResourceFilterBean;
import io.craigmiller160.orgbuilder.server.rest.Role;
import io.craigmiller160.orgbuilder.server.service.OrgService;
import io.craigmiller160.orgbuilder.server.service.ServiceFactory;

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

    @PUT
    @Path("/{orgId}")
    @RolesAllowed({Role.MASTER, Role.ADMIN})
    public Response updateOrg(@PathParam("orgId") long orgId, OrgDTO org) throws OrgApiException{
        //TODO need an additional restriction so that only the admin of the org can do this
        OrgService orgService = factory.newOrgService(securityContext);
        org = orgService.updateOrg(org, orgId);

        return Response
                .accepted(org)
                .build();
    }

    @DELETE
    @Path("/{orgId}")
    @RolesAllowed(Role.MASTER)
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

    @GET
    @Path("/{orgId}")
    @RolesAllowed({Role.MASTER, Role.ADMIN})
    public Response getOrg(@PathParam("orgId") long orgId) throws OrgApiException{
        //TODO need to add a restriction to only the same org
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
