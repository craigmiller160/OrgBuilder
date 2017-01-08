package io.craigmiller160.orgbuilder.server.rest.resource;

import io.craigmiller160.orgbuilder.server.OrgApiException;
import io.craigmiller160.orgbuilder.server.dto.EmailDTO;
import io.craigmiller160.orgbuilder.server.dto.EmailListDTO;
import io.craigmiller160.orgbuilder.server.rest.OrgApiInvalidRequestException;
import io.craigmiller160.orgbuilder.server.rest.ResourceFilterBean;
import io.craigmiller160.orgbuilder.server.rest.Role;
import io.craigmiller160.orgbuilder.server.service.EmailService;
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
@Path("/members/{memberId}/emails")
public class EmailResource {

    private final ServiceFactory factory = ServiceFactory.newInstance();

    @Context
    private SecurityContext securityContext;

    @Context
    private UriInfo uriInfo;

    @PathParam("memberId")
    private long memberId;

    @GET
    @RolesAllowed(Role.READ)
    public Response getAllEmails(@BeanParam ResourceFilterBean resourceFilterBean) throws OrgApiException{
        resourceFilterBean.validateFilterParams();

        EmailService emailService = factory.newEmailService(securityContext);
        EmailListDTO results = emailService.getAllEmailsByMember(memberId, resourceFilterBean.getOffset(), resourceFilterBean.getSize());
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
    public Response addEmail(EmailDTO email) throws OrgApiException{
        EmailService emailService = factory.newEmailService(securityContext);
        email = emailService.addEmail(email, memberId);

        return Response
                .created(URI.create(uriInfo.getPath() + "/" + email.getElementId()))
                .entity(email)
                .build();
    }

    @PUT
    @Path("/{emailId}")
    @RolesAllowed(Role.WRITE)
    public Response updateEmail(@PathParam("emailId") long emailId, EmailDTO email) throws OrgApiException{
        EmailService emailService = factory.newEmailService(securityContext);
        EmailDTO result = emailService.updateEmail(email, emailId, memberId);

        if(result != null){
            return Response
                    .accepted(result)
                    .build();
        }

        return Response
                .noContent()
                .build();
    }

    @DELETE
    @Path("/{emailId}")
    @RolesAllowed(Role.WRITE)
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
    @RolesAllowed(Role.READ)
    public Response getEmail(@PathParam("emailId") long emailId) throws OrgApiException{
        EmailService emailService = factory.newEmailService(securityContext);
        EmailDTO email = emailService.getEmailByMember(emailId, memberId);

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
