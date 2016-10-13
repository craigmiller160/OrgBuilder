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
 * Created by craig on 8/23/16.
 */
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/members/{memberId}/phones")
public class PhoneResource {

    private final ServiceFactory factory = ServiceFactory.newInstance();

    @Context
    private SecurityContext securityContext;

    @Context
    private UriInfo uriInfo;

    @PathParam("orgId")
    private long orgId;

    @PathParam("memberId")
    private long memberId;

    //TODO need to add an additional restriction for the org related to the user

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

    @POST
    @RolesAllowed({Role.READ, Role.WRITE})
    public Response addPhone(PhoneDTO phone) throws OrgApiException{
        PhoneService phoneService = factory.newPhoneService(securityContext);
        phone = phoneService.addPhone(phone, memberId);

        return Response
                .created(URI.create(uriInfo.getPath() + "/" + phone.getElementId()))
                .entity(phone)
                .build();
    }

    @PUT
    @Path("/{phoneId}")
    @RolesAllowed({Role.READ, Role.WRITE})
    public Response updatePhone(@PathParam("phoneId") long phoneId, PhoneDTO phone) throws OrgApiException{
        PhoneService phoneService = factory.newPhoneService(securityContext);
        phone = phoneService.updatePhone(phone, phoneId, memberId);

        return Response
                .accepted(phone)
                .build();
    }

    @DELETE
    @Path("/{phoneId}")
    @RolesAllowed({Role.READ, Role.WRITE})
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
