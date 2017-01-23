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
 * RESTful API for handling the Email resource.
 *
 * Created by craig on 8/23/16.
 */
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/old.members/{memberId}/emails")
public class EmailResource {

    private final ServiceFactory factory = ServiceFactory.newInstance();

    @Context
    private SecurityContext securityContext;

    @Context
    private UriInfo uriInfo;

    @PathParam("memberId")
    private long memberId;

    /**
     * RESOURCE: GET /old.members/{memberId}/emails
     *
     * PURPOSE: Retrieve all emails for a specific member.
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
     * @return the Response, containing all the Emails retrieved by the request.
     * @throws OrgApiException if an error occurs.
     */
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

    /**
     * RESOURCE: POST /old.members/{memberId}/emails
     *
     * PURPOSE: Create new email for a member.
     *
     * ACCESS: Users with the WRITE role.
     *
     * BODY: The email to be created.
     *
     * QUERY PARAMS: NONE
     *
     * @param email the email to be created.
     * @return the Response, containing the email that was created.
     * @throws OrgApiException if an error occurs.
     */
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

    /**
     * RESOURCE: PUT /old.members/{memberId}/emails/{emailId}
     *
     * PURPOSE: Update an existing email for a member.
     *
     * ACCESS: Users with the WRITE role.
     *
     * BODY: The email to be updated.
     *
     * QUERY PARAMS: NONE
     *
     * @param emailId the ID of the email to update.
     * @param email the updated email.
     * @return the Response, containing the updated email,
     *          or nothing if no email with the specified ID
     *          existed.
     * @throws OrgApiException if an error occurs.
     */
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

    /**
     * RESOURCE: DELETE /old.members/{memberId}/emails/{emailId}
     *
     * PURPOSE: Delete an existing email for a member.
     *
     * ACCESS: Users with the WRITE role.
     *
     * BODY: NONE
     *
     * QUERY PARAMS: NONE
     *
     * @param emailId the ID of the email to delete.
     * @return the Response, containing the email that was deleted,
     *          or nothing if there was no email to delete.
     * @throws OrgApiException if an error occurs.
     */
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

    /**
     * RESOURCE: GET /old.members/{memberId}/emails/{emailId}
     *
     * PURPOSE: To retrieve a single email for a member.
     *
     * ACCESS: Users with the READ role.
     *
     * BODY: NONE
     *
     * QUERY PARAMS: NONE
     *
     *
     * @param emailId the ID of the email to retrieve.
     * @return a Response containing the email that was retrieved,
     *          or nothing if there was no email with the specified ID.
     * @throws OrgApiException if an error occurs.
     */
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
