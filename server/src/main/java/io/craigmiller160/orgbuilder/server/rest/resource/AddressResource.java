package io.craigmiller160.orgbuilder.server.rest.resource;

import io.craigmiller160.orgbuilder.server.OrgApiException;
import io.craigmiller160.orgbuilder.server.dto.AddressDTO;
import io.craigmiller160.orgbuilder.server.dto.AddressListDTO;
import io.craigmiller160.orgbuilder.server.rest.OrgApiInvalidRequestException;
import io.craigmiller160.orgbuilder.server.service.AddressService;
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

/**
 * Created by craig on 8/23/16.
 */
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/orgs/{orgId}/members/{memberId}/addresses")
public class AddressResource {

    private final ServiceFactory factory = ServiceFactory.newInstance();

    @Context
    private SecurityContext securityContext;

    @Context
    private UriInfo uriInfo;

    @PathParam("orgId")
    private long orgId;

    @PathParam("memberId")
    private long memberId;

    @GET
    public Response getAllAddresses(@QueryParam("offset") @DefaultValue("-1") long offset,
                                            @QueryParam("size") @DefaultValue("-1") long size) throws OrgApiException{
        if((offset != -1 && size == -1) || (offset == -1 && size != -1)){
            throw new OrgApiInvalidRequestException("Invalid offset/size query parameters.");
        }

        AddressService addressService = factory.newAddressService(securityContext);
        AddressListDTO results = addressService.getAllAddressesByMember(memberId, offset, size);
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
    public Response addAddress(AddressDTO address) throws OrgApiException{
        AddressService addressService = factory.newAddressService(securityContext);
        address = addressService.addAddress(address, memberId);

        return Response
                .created(URI.create(uriInfo.getPath() + "/" + address.getElementId()))
                .entity(address)
                .build();
    }

    @PUT
    @Path("/{addressId}")
    public Response updateAddress(@PathParam("addressId") long addressId, AddressDTO address) throws OrgApiException{
        AddressService addressService = factory.newAddressService(securityContext);
        address = addressService.updateAddress(address, addressId, memberId);

        return Response
                .accepted(address)
                .build();
    }

    @DELETE
    @Path("/{addressId}")
    public Response deleteAddress(@PathParam("addressId") long addressId) throws OrgApiException{
        AddressService addressService = factory.newAddressService(securityContext);
        AddressDTO address = addressService.deleteAddress(addressId);

        if(address != null){
            return Response
                    .accepted(address)
                    .build();
        }
        return Response
                .noContent()
                .build();
    }

    @GET
    @Path("/{addressId}")
    public Response getAddress(@PathParam("addressId") long addressId) throws OrgApiException{
        AddressService addressService = factory.newAddressService(securityContext);
        AddressDTO address = addressService.getAddressByMember(addressId, memberId);

        if(address != null){
            return Response
                    .ok(address)
                    .build();
        }
        return Response
                .noContent()
                .build();
    }

}
