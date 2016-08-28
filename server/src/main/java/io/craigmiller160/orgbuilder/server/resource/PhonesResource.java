package io.craigmiller160.orgbuilder.server.resource;

import io.craigmiller160.orgbuilder.server.dto.PhoneDTO;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Created by craig on 8/23/16.
 */
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/orgs/{orgId}/members/{memberId}")
public class PhonesResource {

    @PathParam("orgId")
    private long orgId;

    @PathParam("memberId")
    private long memberId;

    @GET
    public List<PhoneDTO> getAllPhones(@QueryParam("offset") long offset, @QueryParam("size") long size){
        //TODO finish this
        return null;
    }

    @POST
    public PhoneDTO addPhone(PhoneDTO phone){
        //TODO finish this
        return null;
    }

    @PUT
    @Path("/{phoneId}")
    public PhoneDTO updatePhone(@PathParam("phoneId") long phoneId, PhoneDTO phone){
        //TODO finish this
        return null;
    }

    @DELETE
    @Path("/{phoneId}")
    public PhoneDTO deletePhone(@PathParam("phoneId") long phoneId){
        //TODO finish this
        return null;
    }

    @GET
    @Path("/{phoneId}")
    public PhoneDTO getPhone(@PathParam("phoneId") long phoneId){
        //TODO finish this
        return null;
    }

}
