package io.craigmiller160.orgbuilder.server.rest.resource;

import io.craigmiller160.orgbuilder.server.OrgApiException;
import io.craigmiller160.orgbuilder.server.dto.AddressDTO;
import io.craigmiller160.orgbuilder.server.dto.AddressListDTO;
import io.craigmiller160.orgbuilder.server.rest.OrgApiInvalidRequestException;
import io.craigmiller160.orgbuilder.server.rest.ResourceFilterBean;
import io.craigmiller160.orgbuilder.server.rest.Role;
import io.craigmiller160.orgbuilder.server.service.AddressService;
import io.craigmiller160.orgbuilder.server.service.ServiceFactory;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
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
@Path("/members/{memberId}/addresses")
public class AddressResource {

    private final ServiceFactory factory = ServiceFactory.newInstance();

    @Context
    private SecurityContext securityContext;

    @Context
    private UriInfo uriInfo;

    @PathParam("memberId")
    private long memberId;

    //TODO need to add an additional restriction for the org related to the user

    @GET
    @RolesAllowed(Role.READ)
    public Response getAllAddresses(@BeanParam ResourceFilterBean resourceFilterBean) throws OrgApiException{
        resourceFilterBean.validateFilterParams();

        AddressService addressService = factory.newAddressService(securityContext);
        AddressListDTO results = addressService.getAllAddressesByMember(memberId, resourceFilterBean.getOffset(), resourceFilterBean.getSize());
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
    @RolesAllowed(Role.WRITE)
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
    @RolesAllowed(Role.WRITE)
    public Response updateAddress(@PathParam("addressId") long addressId, AddressDTO address) throws OrgApiException{
        AddressService addressService = factory.newAddressService(securityContext);
        address = addressService.updateAddress(address, addressId, memberId);

        return Response
                .accepted(address)
                .build();
    }

    @DELETE
    @Path("/{addressId}")
    @RolesAllowed(Role.WRITE)
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
    @RolesAllowed(Role.READ)
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
