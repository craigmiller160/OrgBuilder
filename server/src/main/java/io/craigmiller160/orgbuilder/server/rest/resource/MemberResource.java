package io.craigmiller160.orgbuilder.server.rest.resource;

import io.craigmiller160.orgbuilder.server.OrgApiException;
import io.craigmiller160.orgbuilder.server.data.OrgApiDataException;
import io.craigmiller160.orgbuilder.server.dto.MemberDTO;
import io.craigmiller160.orgbuilder.server.service.MemberService;
import io.craigmiller160.orgbuilder.server.service.OrgApiSecurityException;
import io.craigmiller160.orgbuilder.server.service.ServiceFactory;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
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
import java.net.URISyntaxException;
import java.util.List;

/**
 * Created by craig on 8/23/16.
 */
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/orgs/{orgId}/members")
public class MemberResource {

    private final ServiceFactory factory = ServiceFactory.newInstance();

    @Context
    private SecurityContext securityContext;

    @Context
    private UriInfo uriInfo;

    @PathParam("orgId")
    private long orgId;

    @GET
    public Response getAllMembers(@QueryParam("offset") long offset, @QueryParam("size") long size) throws OrgApiException{
        try{
            MemberService memberService = factory.newMemberService(securityContext);
            List<MemberDTO> results = memberService.getAllMembers(offset, size);
            return Response
                    .ok(results)
                    .build();
        }
        catch(OrgApiDataException | OrgApiSecurityException ex){
            throw new OrgApiException("Unable to process request: GET " + uriInfo.getPath(), ex);
        }
    }

    @POST
    public Response addMember(MemberDTO member) throws OrgApiException{
        try{
            MemberService memberService = factory.newMemberService(securityContext);
            member = memberService.addMember(member);
            return Response
                    .created(new URI(uriInfo.getPath() + "/" + member.getElementId()))
                    .entity(member)
                    .build();
        }
        catch(OrgApiDataException | OrgApiSecurityException | URISyntaxException ex){
            throw new OrgApiException("Unable to process request: POST " + uriInfo.getPath(), ex);
        }
    }

    @PUT
    @Path("/{memberId}")
    public Response updateMember(@PathParam("memberId") long memberId, MemberDTO member) throws OrgApiException{
        try{
            MemberService memberService = factory.newMemberService(securityContext);
            member = memberService.updateMember(member, memberId);
            return Response
                    .accepted(member)
                    .build();
        }
        catch(OrgApiDataException | OrgApiSecurityException ex){
            throw new OrgApiException("Unable to process request: PUT " + uriInfo.getPath(), ex);
        }
    }

    @DELETE
    @Path("/{memberId}")
    public Response deleteMember(@PathParam("memberId") long memberId) throws OrgApiException{
        try{
            MemberService memberService = factory.newMemberService(securityContext);
            MemberDTO member = memberService.deleteMember(memberId);
            if(member != null){
                return Response
                        .accepted(member)
                        .build();
            }
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .build();
        }
        catch(OrgApiDataException | OrgApiSecurityException ex){
            throw new OrgApiException("Unable to process request: DELETE " + uriInfo.getPath(), ex);
        }
    }

    @GET
    @Path("/{memberId}")
    public Response getMember(@PathParam("memberId") long memberId) throws OrgApiException{
        try{
            MemberService memberService = factory.newMemberService(securityContext);
            MemberDTO member = memberService.getMember(memberId);
            return Response
                    .ok(member)
                    .build();
        }
        catch(OrgApiDataException | OrgApiSecurityException ex){
            throw new OrgApiException("Unable to process request: GET " + uriInfo.getPath(), ex);
        }
    }

}
