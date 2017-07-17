package io.craigmiller160.orgbuilder.server.rest.resource;

import io.craigmiller160.orgbuilder.server.OrgApiException;
import io.craigmiller160.orgbuilder.server.dto.ErrorDTO;
import io.craigmiller160.orgbuilder.server.dto.PhoneDTO;
import io.craigmiller160.orgbuilder.server.dto.PhoneListDTO;
import io.craigmiller160.orgbuilder.server.rest.OrgApiInvalidRequestException;
import io.craigmiller160.orgbuilder.server.rest.ResourceFilterBean;
import io.craigmiller160.orgbuilder.server.rest.Role;
import io.craigmiller160.orgbuilder.server.service.PhoneService;
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
 * RESTful API for handling the Phones resource.
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
@Api (tags = "member/phones", authorizations = @Authorization(value = "orgapiToken"))
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/members/{memberId}/phones")
public class PhoneResource {

    private final ServiceFactory factory = ServiceFactory.newInstance();

    @Context
    private SecurityContext securityContext;

    @Context
    private UriInfo uriInfo;

    @ApiParam(value = "The ID of the member that will own all the phones being interacted with via this resource.", required = true)
    @PathParam("memberId")
    private long memberId;

    @ApiOperation(value = "Get All Phones",
            notes = "Get all phones for a member.\n" +
                    "ACCESS:\n" +
                    "Role: READ",
            response = PhoneListDTO.class)
    @ApiResponses(value = @ApiResponse(code = 204, message = "No phones existed to return."))
    @GET
    @RolesAllowed(Role.READ)
    public Response getAllPhones(@BeanParam ResourceFilterBean resourceFilterBean) throws OrgApiException{
        resourceFilterBean.validateFilterParams();

        PhoneService phoneService = factory.newPhoneService(securityContext);
        PhoneListDTO results = phoneService.getAllPhonesByMember(memberId, resourceFilterBean.getOffset(), resourceFilterBean.getSize());
        if(results != null){
            return Response
                    .ok(results)
                    .build();
        }
        return Response
                .noContent()
                .build();
    }

    @ApiOperation(value = "Add New Phone",
            notes = "Add a new phone for a member.\n" +
                    "ACCESS:\n" +
                    "Role: WRITE",
            response = PhoneDTO.class,
            code = 201)
    @POST
    @RolesAllowed(Role.WRITE)
    public Response addPhone(@ApiParam(value = "The phone to add", required = true) PhoneDTO phone) throws OrgApiException{
        PhoneService phoneService = factory.newPhoneService(securityContext);
        phone = phoneService.addPhone(phone, memberId);

        return Response
                .created(URI.create(uriInfo.getPath() + "/" + phone.getElementId()))
                .entity(phone)
                .build();
    }

    @ApiOperation(value = "Update Phone",
            notes = "Update an existing phone for a member.\n" +
                    "ACCESS:\n" +
                    "Role: WRITE",
            response = PhoneDTO.class,
            code = 202)
    @ApiResponses(value = @ApiResponse(code = 204, message = "Phone to update did not exist."))
    @PUT
    @Path("/{phoneId}")
    @RolesAllowed(Role.WRITE)
    public Response updatePhone(@ApiParam(value = "The ID of the phone to update", required = true) @PathParam("phoneId") long phoneId,
                                @ApiParam(value = "The updated phone", required = true) PhoneDTO phone) throws OrgApiException{
        PhoneService phoneService = factory.newPhoneService(securityContext);
        PhoneDTO result = phoneService.updatePhone(phone, phoneId, memberId);

        if(result != null){
            return Response
                    .accepted(result)
                    .build();
        }

        return Response
                .noContent()
                .build();
    }

    @ApiOperation(value = "Delete Phone",
            notes = "Delete a phone for a member.\n" +
                    "ACCESS:\n" +
                    "Role: WRITE",
            response = PhoneDTO.class,
            code = 202)
    @ApiResponses(value = @ApiResponse(code = 204, message = "Phone to delete did not exist"))
    @DELETE
    @Path("/{phoneId}")
    @RolesAllowed(Role.WRITE)
    public Response deletePhone(@ApiParam(value = "The ID of the phone to delete", required = true) @PathParam("phoneId") long phoneId) throws OrgApiException{
        PhoneService phoneService = factory.newPhoneService(securityContext);
        PhoneDTO phone = phoneService.deletePhone(phoneId);

        if(phone != null){
            return Response
                    .accepted(phone)
                    .build();
        }
        return Response
                .noContent()
                .build();
    }

    @ApiOperation(value = "Get Phone",
            notes = "Get a phone for a member.\n" +
                    "ACCESS:\n" +
                    "Role: READ",
            response = PhoneDTO.class)
    @ApiResponses(value = @ApiResponse(code = 204, message = "Phone to retrieve did not exist."))
    @GET
    @Path("/{phoneId}")
    @RolesAllowed(Role.READ)
    public Response getPhone(@ApiParam(value = "The ID of the phone to retrieve.", required = true) @PathParam("phoneId") long phoneId) throws OrgApiException{
        PhoneService phoneService = factory.newPhoneService(securityContext);
        PhoneDTO phone = phoneService.getPhoneByMember(phoneId, memberId);

        if(phone != null){
            return Response
                    .ok(phone)
                    .build();
        }
        return Response
                .noContent()
                .build();
    }

}
