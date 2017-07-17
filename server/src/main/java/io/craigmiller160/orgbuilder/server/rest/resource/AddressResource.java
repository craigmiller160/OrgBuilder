package io.craigmiller160.orgbuilder.server.rest.resource;

import io.craigmiller160.orgbuilder.server.OrgApiException;
import io.craigmiller160.orgbuilder.server.dto.AddressDTO;
import io.craigmiller160.orgbuilder.server.dto.AddressListDTO;
import io.craigmiller160.orgbuilder.server.dto.ErrorDTO;
import io.craigmiller160.orgbuilder.server.rest.OrgApiInvalidRequestException;
import io.craigmiller160.orgbuilder.server.rest.ResourceFilterBean;
import io.craigmiller160.orgbuilder.server.rest.Role;
import io.craigmiller160.orgbuilder.server.service.AddressService;
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

/**
 * RESTful API for handling the Address resource.
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
@Api (tags = "member/addresses", authorizations = @Authorization(value = "orgapiToken"))
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/members/{memberId}/addresses")
public class AddressResource {

    private final ServiceFactory factory = ServiceFactory.newInstance();

    @Context
    private SecurityContext securityContext;

    @Context
    private UriInfo uriInfo;

    @ApiParam(value = "The ID of the member that will own all the addresses being interacted with via this resource.", required = true)
    @PathParam("memberId")
    private long memberId;

    @ApiOperation(value = "Get All Addresses",
            notes = "Retrieve all addresses for the specified member.\n" +
                    "ACCESS:\n" +
                    "Role: READ",
            response = AddressListDTO.class)
    @ApiResponses(value = @ApiResponse(code = 204, message = "No addresses were found to return."))
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

    @ApiOperation(value = "Add New Address",
            notes = "Add a new address for the specified member.\n" +
                    "ACCESS:\n" +
                    "Role: WRITE",
            response = AddressDTO.class,
            code = 201)
    @POST
    @RolesAllowed(Role.WRITE)
    public Response addAddress(@ApiParam(value = "The address to add.", required = true) AddressDTO address) throws OrgApiException{
        AddressService addressService = factory.newAddressService(securityContext);
        address = addressService.addAddress(address, memberId);

        return Response
                .created(URI.create(uriInfo.getPath() + "/" + address.getElementId()))
                .entity(address)
                .build();
    }

    @ApiOperation(value = "Update Address",
            notes = "Update an existing address for a member.\n" +
                    "ACCESS:\n" +
                    "Role: WRITE",
            response = AddressDTO.class,
            code = 202)
    @ApiResponses(value = @ApiResponse(code = 204, message = "If no address already exists to be updated."))
    @PUT
    @Path("/{addressId}")
    @RolesAllowed(Role.WRITE)
    public Response updateAddress(@ApiParam(value = "The ID of the address to update", required = true) @PathParam("addressId") long addressId,
                                  @ApiParam(value = "The updated address", required = true) AddressDTO address) throws OrgApiException{
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

    @ApiOperation(value = "Delete Address",
            notes = "Delete an existing address for a member.\n" +
                    "ACCESS:\n" +
                    "Role: WRITE",
            response = AddressDTO.class,
            code = 202)
    @ApiResponses(value = @ApiResponse(code = 204, message = "If the address to be deleted didn't exist."))
    @DELETE
    @Path("/{addressId}")
    @RolesAllowed(Role.WRITE)
    public Response deleteAddress(@ApiParam(value = "The ID of the address to delete", required = true) @PathParam("addressId") long addressId) throws OrgApiException{
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

    @ApiOperation(value = "Get Address",
            notes = "Get an address for a member.\n" +
                    "ACCESS:\n" +
                    "Role: READ",
            response = AddressDTO.class,
            code = 200)
    @ApiResponses(value = @ApiResponse(code = 204, message = "No address with the specified ID existed to return."))
    @GET
    @Path("/{addressId}")
    @RolesAllowed(Role.READ)
    public Response getAddress(@ApiParam(value = "The ID of the address to retrieve", required = true) @PathParam("addressId") long addressId) throws OrgApiException{
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
