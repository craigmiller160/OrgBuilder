package io.craigmiller160.orgbuilder.server.rest.resource;

import io.craigmiller160.orgbuilder.server.dto.AddressDTO;

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
@Path("/orgs/{orgId}/members/{memberId}")
public class AddressResource {

    @Context
    private SecurityContext securityContext;

    @PathParam("orgId")
    private long orgId;

    @PathParam("memberId")
    private long memberId;

    @GET
    public List<AddressDTO> getAllAddresses(@QueryParam("offset") long offset, @QueryParam("size") long size){
        //TODO finish this
        return null;
    }

    @POST
    public AddressDTO addAddress(AddressDTO address){
        //TODO finish this
        return null;
    }

    @PUT
    @Path("/{addressId}")
    public AddressDTO updateAddress(@PathParam("addressId") long addressId, AddressDTO address){
        //TODO finish this
        return null;
    }

    @DELETE
    @Path("/{addressId}")
    public AddressDTO deleteAddress(@PathParam("addressId") long addressId){
        //TODO finish this
        return null;
    }

    @GET
    @Path("/{addressId}")
    public AddressDTO getAddress(@PathParam("addressId") long addressId){
        //TODO finish this
        return null;
    }

}
