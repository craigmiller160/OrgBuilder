package io.craigmiller160.orgbuilder.server.rest.resource;

import io.craigmiller160.orgbuilder.server.OrgApiException;
import io.craigmiller160.orgbuilder.server.dto.MemberDTO;
import io.craigmiller160.orgbuilder.server.service.MemberService;
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
import javax.ws.rs.core.SecurityContext;
import java.util.List;

/**
 * Created by craig on 8/23/16.
 */
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/orgs/{orgId}/members")
public class MembersResource {

    private final ServiceFactory factory = ServiceFactory.newInstance();

    @Context
    private SecurityContext securityContext;

    @PathParam("orgId")
    private long orgId;

    @GET
    public List<MemberDTO> getAllMembers(@QueryParam("offset") long offset, @QueryParam("size") long size){
        //TODO finish this
        return null;
    }

    @POST
    public MemberDTO addMember(MemberDTO member) throws OrgApiException{
        //TODO how to handle the exception with Jersey... there's a way, just can't remember
        MemberService memberService = factory.newMemberService(securityContext);
        return memberService.addMember(member);
    }

    @PUT
    @Path("/{memberId}")
    public MemberDTO updateMember(@PathParam("memberId") long memberId, MemberDTO member){
        member.setElementId(memberId);
        //TODO finish this
        return null;
    }

    @DELETE
    @Path("/{memberId}")
    public MemberDTO deleteMember(@PathParam("memberId") long memberId){
        //TODO delete this
        return null;
    }

    @GET
    @Path("/{memberId}")
    public MemberDTO getMember(@PathParam("memberId") long memberId, @QueryParam("joins") boolean joins){
        //TODO delete this
        return null;
    }

}
