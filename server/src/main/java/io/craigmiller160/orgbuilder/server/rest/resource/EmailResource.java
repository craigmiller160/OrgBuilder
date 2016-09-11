package io.craigmiller160.orgbuilder.server.rest.resource;

import io.craigmiller160.orgbuilder.server.OrgApiException;
import io.craigmiller160.orgbuilder.server.dto.EmailDTO;
import io.craigmiller160.orgbuilder.server.dto.EmailListDTO;
import io.craigmiller160.orgbuilder.server.rest.OrgApiInvalidRequestException;
import io.craigmiller160.orgbuilder.server.service.EmailService;
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
@Path("/orgs/{orgId}/members/{memberId}/emails")
public class EmailResource {

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
    public Response getAllEmails(@QueryParam("offset") @DefaultValue("-1") long offset,
                                       @QueryParam("size") @DefaultValue("-1") long size) throws OrgApiException{
        if((offset != -1 && size == -1) || (offset == -1 && size != -1)){
            throw new OrgApiInvalidRequestException("Invalid offset/size query parameters.");
        }

        EmailService emailService = factory.newEmailService(securityContext);
        EmailListDTO results = emailService.getAllEmails(offset, size);
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
    public Response addEmail(EmailDTO email) throws OrgApiException{
        EmailService emailService = factory.newEmailService(securityContext);
        email = emailService.addEmail(email);

        return Response
                .created(URI.create(uriInfo.getPath() + "/" + email.getElementId()))
                .entity(email)
                .build();
    }

    @PUT
    @Path("/{emailId}")
    public Response updateEmail(@PathParam("emailId") long emailId, EmailDTO email) throws OrgApiException{
        EmailService emailService = factory.newEmailService(securityContext);
        email = emailService.updateEmail(email, emailId);

        return Response
                .accepted(email)
                .build();
    }

    @DELETE
    @Path("/{emailId}")
    public Response deleteEmail(@PathParam("emailId") long emailId) throws OrgApiException{
        EmailService emailService = factory.newEmailService(securityContext);
        EmailDTO email = emailService.deleteEmail(emailId);

        if(email != null){
            return Response
                    .accepted(email)
                    .build();
        }
        return Response
                .noContent()
                .build();
    }

    @GET
    @Path("/{emailId}")
    public Response getEmail(@PathParam("emailId") long emailId) throws OrgApiException{
        EmailService emailService = factory.newEmailService(securityContext);
        EmailDTO email = emailService.getEmail(emailId);

        if(email != null){
            return Response
                    .ok(email)
                    .build();
        }
        return Response
                .noContent()
                .build();
    }

}
