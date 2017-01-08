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
 * RESTful API for handling the Address resource.
 *
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

    /**
     * RESOURCE: GET /members/{memberId}/addresses
     *
     * PURPOSE: Retrieve all addresses for a specific member.
     *
     * ACCESS: Users with the READ role.
     *
     * BODY: NONE
     *
     * QUERY PARAMS:
     * offset: the number of records to skip over before starting retrieval.
     * size: the total number of records to retrieve.
     *
     * @param resourceFilterBean the filter bean with the Query Params.
     * @return the Response, containing all the Addresses retrieved by the request.
     * @throws OrgApiException if an error occurs.
     */
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

    /**
     * RESOURCE: POST /members/{memberId}/addresses
     *
     * PURPOSE: Create a new address for a member.
     *
     * ACCESS: Users with the WRITE role.
     *
     * BODY: The address to create.
     *
     * QUERY PARAMS: NONE
     *
     * @param address the address to create.
     * @return a Response with the newly created address.
     * @throws OrgApiException if an error occurs.
     */
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

    /**
     * RESOURCE: PUT /members/{memberId}/addresses/{addressId}
     *
     * PURPOSE: To update an existing address for a member.
     *
     * ACCESS: Users with the WRITE role.
     *
     * BODY: The address to be updated.
     *
     * QUERY PARAMS: NONE
     *
     * @param addressId the ID of the address to be updated.
     * @param address the address to be updated.
     * @return a Response containing the new state of the address that was updated,
     *          or nothing if no address existed with the specified ID.
     * @throws OrgApiException if an error occurs.
     */
    @PUT
    @Path("/{addressId}")
    @RolesAllowed(Role.WRITE)
    public Response updateAddress(@PathParam("addressId") long addressId, AddressDTO address) throws OrgApiException{
        AddressService addressService = factory.newAddressService(securityContext);
        AddressDTO result = addressService.updateAddress(address, addressId, memberId);

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
     * RESOURCE: DELETE /members/{memberId}/addresses/{addressId}
     *
     * PURPOSE: Delete an existing address from a member.
     *
     * ACCESS: Users with the WRITE role.
     *
     * BODY: NONE
     *
     * QUERY PARAMS: NONE
     *
     * @param addressId the ID of the address to be deleted.
     * @return the Response, containing the address that was deleted,
     *          or nothing if no address matching the ID existed.
     * @throws OrgApiException if an error occurs.
     */
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

    /**
     * RESOURCE: GET /members/{memberId}/addresses/{addressId}
     *
     * PURPOSE: Retrieve a single address for a member.
     *
     * ACCESS: Any user with READ access
     *
     * BODY: NONE
     *
     * QUERY PARAMS: NONE
     *
     * @param addressId the ID of the address to retrieve.
     * @return the Response, containing the address that was retrieved,
     *          or nothing if no address existed with the specified ID.
     * @throws OrgApiException if an error occurs.
     */
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
