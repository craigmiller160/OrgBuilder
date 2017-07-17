package io.craigmiller160.orgbuilder.server.rest.resource;

import io.craigmiller160.orgbuilder.server.OrgApiException;
import io.craigmiller160.orgbuilder.server.dto.EmailDTO;
import io.craigmiller160.orgbuilder.server.dto.EmailListDTO;
import io.craigmiller160.orgbuilder.server.dto.ErrorDTO;
import io.craigmiller160.orgbuilder.server.rest.OrgApiInvalidRequestException;
import io.craigmiller160.orgbuilder.server.rest.ResourceFilterBean;
import io.craigmiller160.orgbuilder.server.rest.Role;
import io.craigmiller160.orgbuilder.server.service.EmailService;
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
 * RESTful API for handling the Email resource.
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
@Api (tags = "member/emails", authorizations = @Authorization(value = "orgapiToken"))
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/members/{memberId}/emails")
public class EmailResource {

    private final ServiceFactory factory = ServiceFactory.newInstance();

    @Context
    private SecurityContext securityContext;

    @Context
    private UriInfo uriInfo;

    @ApiParam(value = "The ID of the member that will own all the emails being interacted with via this resource.", required = true)
    @PathParam("memberId")
    private long memberId;

    @ApiOperation(value = "Get All Emails",
            notes = "Retrieve all emails for the specified member.\n" +
                    "ACCESS:\n" +
                    "Role: READ",
            response = EmailListDTO.class)
    @ApiResponses(value = @ApiResponse(code = 204, message = "No emails were found to return."))
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

    @ApiOperation(value = "Add New Email",
            notes = "Add a new email for a member.\n" +
                    "ACCESS:\n" +
                    "Role: WRITE",
            response = EmailDTO.class,
            code = 201)
    @POST
    @RolesAllowed(Role.WRITE)
    public Response addEmail(@ApiParam(value = "The email to add.", required = true) EmailDTO email) throws OrgApiException{
        EmailService emailService = factory.newEmailService(securityContext);
        email = emailService.addEmail(email, memberId);

        return Response
                .created(URI.create(uriInfo.getPath() + "/" + email.getElementId()))
                .entity(email)
                .build();
    }

    @ApiOperation(value = "Update Email",
            notes = "Update an existing email for a member.\n" +
                    "ACCESS:\n" +
                    "Role: WRITE",
            response = EmailDTO.class,
            code = 202)
    @ApiResponses(value = @ApiResponse(code = 204, message = "No email existed to update."))
    @PUT
    @Path("/{emailId}")
    @RolesAllowed(Role.WRITE)
    public Response updateEmail(@ApiParam(value = "The ID of the email to update", required = true) @PathParam("emailId") long emailId,
                                @ApiParam(value = "The updated email.", required = true) EmailDTO email) throws OrgApiException{
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

    @ApiOperation(value = "Delete Email",
            notes = "Delete an email for a member.\n" +
                    "ACCESS:\n" +
                    "Role: WRITE",
            response = EmailDTO.class,
            code = 202)
    @ApiResponses(value = @ApiResponse(code = 204, message = "No email existed to delete."))
    @DELETE
    @Path("/{emailId}")
    @RolesAllowed(Role.WRITE)
    public Response deleteEmail(@ApiParam(value = "The ID of the email to delete.", required = true) @PathParam("emailId") long emailId) throws OrgApiException{
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

    @ApiOperation(value = "Get Email",
            notes = "Retrieve an email for a member.\n" +
                    "ACCESS:\n" +
                    "Role: READ",
            response = EmailDTO.class,
            code = 200)
    @ApiResponses(value = @ApiResponse(code = 204, message = "No email existed to retrieve."))
    @GET
    @Path("/{emailId}")
    @RolesAllowed(Role.READ)
    public Response getEmail(@ApiParam(value = "The ID of the email to retrieve.", required = true) @PathParam("emailId") long emailId) throws OrgApiException{
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
