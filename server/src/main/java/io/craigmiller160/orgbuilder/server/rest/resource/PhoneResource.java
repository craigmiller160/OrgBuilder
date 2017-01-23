package io.craigmiller160.orgbuilder.server.rest.resource;

import io.craigmiller160.orgbuilder.server.OrgApiException;
import io.craigmiller160.orgbuilder.server.dto.PhoneDTO;
import io.craigmiller160.orgbuilder.server.dto.PhoneListDTO;
import io.craigmiller160.orgbuilder.server.rest.OrgApiInvalidRequestException;
import io.craigmiller160.orgbuilder.server.rest.ResourceFilterBean;
import io.craigmiller160.orgbuilder.server.rest.Role;
import io.craigmiller160.orgbuilder.server.service.PhoneService;
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
 * RESTful API for handling the Phones resource.
 *
 * Created by craig on 8/23/16.
 */
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/old.members/{memberId}/phones")
public class PhoneResource {

    private final ServiceFactory factory = ServiceFactory.newInstance();

    @Context
    private SecurityContext securityContext;

    @Context
    private UriInfo uriInfo;

    @PathParam("memberId")
    private long memberId;

    /**
     * RESOURCE: GET /old.members/{memberId}/phones
     *
     * PURPOSE: Retrieve all phones for a member.
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
     * @return the Response, containing all the Phones retrieved by the request.
     * @throws OrgApiException if an error occurs.
     */
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

    /**
     * RESOURCE: POST /old.members/{memberId}/phones
     *
     * PURPOSE: Create a new phone for a member.
     *
     * ACCESS: Users with the WRITE role.
     *
     * BODY: The phone to create.
     *
     * QUERY PARAMS: NONE
     *
     * @param phone the phone to create.
     * @return the Response, containing the phone that was created.
     * @throws OrgApiException if an error occurs.
     */
    @POST
    @RolesAllowed(Role.WRITE)
    public Response addPhone(PhoneDTO phone) throws OrgApiException{
        PhoneService phoneService = factory.newPhoneService(securityContext);
        phone = phoneService.addPhone(phone, memberId);

        return Response
                .created(URI.create(uriInfo.getPath() + "/" + phone.getElementId()))
                .entity(phone)
                .build();
    }

    /**
     * RESOURCE: PUT /old.members/{memberId}/phones/{phoneId}
     *
     * PURPOSE: Update an existing phone for a member.
     *
     * ACCESS: Users with the WRITE role.
     *
     * BODY: The phone to update.
     *
     * QUERY PARAMS: NONE
     *
     * @param phoneId the ID of the phone to update.
     * @param phone the updated phone.
     * @return a Response, containing the updated phone,
     *          or nothing if no phone existed with the specified ID.
     * @throws OrgApiException if an error occurs.
     */
    @PUT
    @Path("/{phoneId}")
    @RolesAllowed(Role.WRITE)
    public Response updatePhone(@PathParam("phoneId") long phoneId, PhoneDTO phone) throws OrgApiException{
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

    /**
     * RESOURCE: DELETE /old.members/{memberId}/phones/{phoneId}
     *
     * PURPOSE: Delete an existing phone for a member.
     *
     * ACCESS: Users with the WRITE role.
     *
     * BODY: NONE
     *
     * QUERY PARAMS: NONE
     *
     * @param phoneId the ID of the phone to delete.
     * @return the Response, containing the phone that was deleted,
     *          or nothing if there was no phone with the specified ID.
     * @throws OrgApiException if an error occurs.
     */
    @DELETE
    @Path("/{phoneId}")
    @RolesAllowed(Role.WRITE)
    public Response deletePhone(@PathParam("phoneId") long phoneId) throws OrgApiException{
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

    /**
     * RESOURCE: GET /old.members/{memberId}/phones/{phoneId}
     *
     * PURPOSE: Retrieve a single phone for a member.
     *
     * ACCESS: Users with the READ role.
     *
     * BODY: NONE
     *
     * QUERY PARAMS: NONE
     *
     * @param phoneId the ID of the phone to retrieve.
     * @return the Response, containing the phone that was retrieved,
     *          or nothing if there was no phone with the specified ID.
     * @throws OrgApiException if an error occurs.
     */
    @GET
    @Path("/{phoneId}")
    @RolesAllowed(Role.READ)
    public Response getPhone(@PathParam("phoneId") long phoneId) throws OrgApiException{
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
