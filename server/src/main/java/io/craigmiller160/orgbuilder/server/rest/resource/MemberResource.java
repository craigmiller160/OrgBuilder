package io.craigmiller160.orgbuilder.server.rest.resource;

import io.craigmiller160.orgbuilder.server.OrgApiException;
import io.craigmiller160.orgbuilder.server.dto.ErrorDTO;
import io.craigmiller160.orgbuilder.server.dto.MemberDTO;
import io.craigmiller160.orgbuilder.server.dto.MemberListDTO;
import io.craigmiller160.orgbuilder.server.rest.MemberFilterBean;
import io.craigmiller160.orgbuilder.server.rest.Role;
import io.craigmiller160.orgbuilder.server.service.MemberService;
import io.craigmiller160.orgbuilder.server.service.ServiceFactory;
import io.swagger.annotations.*;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;
import java.net.URI;

/**
 * RESTful API for accessing the Members resource.
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
@Api (tags = "members", authorizations = @Authorization(value = "orgapiToken"))
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/members")
public class MemberResource {

    private final ServiceFactory factory = ServiceFactory.newInstance();

    @Context
    private SecurityContext securityContext;

    @Context
    private UriInfo uriInfo;

    @ApiOperation(value = "Get All Members (w/ Optional Search)",
            notes = "Get all members, with optional parameters to filter the results to specific search terms.\n" +
                    "ACCESS:\n" +
                    "Role: READ",
            response = MemberListDTO.class)
    @ApiResponses(value = @ApiResponse(code = 204, message = "No members were found to return."))
    @GET
    @RolesAllowed(Role.READ)
    public Response getAllMembers(@BeanParam MemberFilterBean membersFilterBean) throws OrgApiException{
        membersFilterBean.validateFilterParams();

        MemberService memberService = factory.newMemberService(securityContext);
        MemberListDTO results = memberService.getMemberList(membersFilterBean);
        if(results != null){
            return Response
                    .ok(results)
                    .build();
        }
        return Response
                .noContent()
                .build();
    }

    @ApiOperation(value = "Add New Member",
            notes = "Add a new member.\n" +
                    "ACCESS:\n" +
                    "Role: WRITE",
            response = MemberDTO.class,
            code = 201)
    @POST
    @RolesAllowed(Role.WRITE)
    public Response addMember(@ApiParam(value = "The member to add.", required = true) MemberDTO member) throws OrgApiException{
        MemberService memberService = factory.newMemberService(securityContext);
        member = memberService.addMember(member);

        return Response
                .created(URI.create(uriInfo.getPath() + "/" + member.getElementId()))
                .entity(member)
                .build();
    }

    @ApiOperation(value = "Update Member",
            notes = "Update an existing member.\n" +
                    "ACCESS:\n" +
                    "Role: WRITE",
            response = MemberDTO.class,
            code = 202)
    @ApiResponses(value = @ApiResponse(code = 204, message = "Member did not exist to update."))
    @PUT
    @Path("/{memberId}")
    @RolesAllowed(Role.WRITE)
    public Response updateMember(@ApiParam(value = "The ID of the member to update.", required = true) @PathParam("memberId") long memberId,
                                 @ApiParam(value = "The updated member.", required = true) MemberDTO member) throws OrgApiException{
        MemberService memberService = factory.newMemberService(securityContext);
        MemberDTO result = memberService.updateMember(member, memberId);

        if(result != null){
            return Response
                    .accepted(result)
                    .build();
        }

        return Response
                .noContent()
                .build();
    }

    @ApiOperation(value = "Delete Member",
            notes = "Delete an existing member.\n" +
                    "ACCESS:\n" +
                    "Role: WRITE",
            response = MemberDTO.class,
            code = 202)
    @ApiResponses(value = @ApiResponse(code = 204, message = "Member did not exist to delete."))
    @DELETE
    @Path("/{memberId}")
    @RolesAllowed(Role.WRITE)
    public Response deleteMember(@ApiParam(value = "The ID of the member to delete.", required = true) @PathParam("memberId") long memberId) throws OrgApiException{
        MemberService memberService = factory.newMemberService(securityContext);
        MemberDTO member = memberService.deleteMember(memberId);
        if(member != null){
            return Response
                    .accepted(member)
                    .build();
        }
        return Response
                .noContent()
                .build();
    }

    @ApiOperation(value = "Get Member",
            notes = "Retrieve a single member.\n" +
                    "ACCESS:\n" +
                    "Role: READ",
            response = MemberDTO.class)
    @ApiResponses(value = @ApiResponse(code = 204, message = "Member to retrieve did not exist."))
    @GET
    @Path("/{memberId}")
    @RolesAllowed(Role.READ)
    public Response getMember(@PathParam("memberId") long memberId) throws OrgApiException{
        MemberService memberService = factory.newMemberService(securityContext);
        MemberDTO member = memberService.getMember(memberId);

        if(member != null){
            return Response
                    .ok(member)
                    .build();
        }
        return Response
                .noContent()
                .build();
    }

}
