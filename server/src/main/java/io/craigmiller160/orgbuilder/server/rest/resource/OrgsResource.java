package io.craigmiller160.orgbuilder.server.rest.resource;

import io.craigmiller160.orgbuilder.server.dto.OrgDTO;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
import java.util.List;

/**
 * Created by craig on 8/23/16.
 */
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/orgs")
public class OrgsResource {

    @Context
    private SecurityContext securityContext;

    @GET
    public List<OrgDTO> getAllOrgs(@QueryParam("offset") long offset, @QueryParam("size") long size){
        //TODO finish this
        return null;
    }

    @POST
    public OrgDTO addOrg(OrgDTO org){
        //TODO finish this
        return null;
    }

    @PUT
    @Path("/{orgId}")
    public OrgDTO updateOrg(@PathParam("orgId") long orgId, OrgDTO org){
        org.setElementId(orgId);
        //TODO finish this
        return null;
    }

    @DELETE
    @Path("/{orgId}")
    public OrgDTO deleteOrg(@PathParam("orgId") long orgId){
        //TODO finish this
        return null;
    }

    @GET
    @Path("/{orgId}")
    public OrgDTO getOrg(@PathParam("orgId") long orgId){
        //TODO finish this
        return null;
    }

}
