package io.craigmiller160.orgbuilder.server.rest.resource;

import io.craigmiller160.orgbuilder.server.OrgApiException;
import io.craigmiller160.orgbuilder.server.dto.PhoneDTO;
import io.craigmiller160.orgbuilder.server.dto.PhoneListDTO;
import io.craigmiller160.orgbuilder.server.rest.OrgApiInvalidRequestException;
import io.craigmiller160.orgbuilder.server.service.PhoneService;
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
import java.util.List;

/**
 * Created by craig on 8/23/16.
 */
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/orgs/{orgId}/members/{memberId}/phones")
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

    @GET
    public Response getAllPhones(@QueryParam("offset") @DefaultValue("-1") long offset,
                                       @QueryParam("size") @DefaultValue("-1") long size) throws OrgApiException{
        if((offset != -1 && size == -1) || (offset == -1 && size != -1)){
            throw new OrgApiInvalidRequestException("Invalid offset/size query parameters.");
        }
        PhoneService phoneService = factory.newPhoneService(securityContext);
        PhoneListDTO results = phoneService.getAllPhones(offset, size);
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
    public Response addPhone(PhoneDTO phone) throws OrgApiException{
        PhoneService phoneService = factory.newPhoneService(securityContext);
        phone = phoneService.addPhone(phone);

        return Response
                .created(URI.create(uriInfo.getPath() + "/" + phone.getElementId()))
                .entity(phone)
                .build();
    }

    @PUT
    @Path("/{phoneId}")
    public Response updatePhone(@PathParam("phoneId") long phoneId, PhoneDTO phone) throws OrgApiException{
        PhoneService phoneService = factory.newPhoneService(securityContext);
        phone = phoneService.updatePhone(phone, phoneId);

        return Response
                .accepted(phone)
                .build();
    }

    @DELETE
    @Path("/{phoneId}")
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
    public Response getPhone(@PathParam("phoneId") long phoneId) throws OrgApiException{
        PhoneService phoneService = factory.newPhoneService(securityContext);
        PhoneDTO phone = phoneService.getPhone(phoneId);

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
