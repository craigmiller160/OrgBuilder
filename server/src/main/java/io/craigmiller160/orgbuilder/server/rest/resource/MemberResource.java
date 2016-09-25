package io.craigmiller160.orgbuilder.server.rest.resource;

import io.craigmiller160.orgbuilder.server.OrgApiException;
import io.craigmiller160.orgbuilder.server.dto.MemberDTO;
import io.craigmiller160.orgbuilder.server.dto.MemberListDTO;
import io.craigmiller160.orgbuilder.server.rest.MemberFilterBean;
import io.craigmiller160.orgbuilder.server.rest.Role;
import io.craigmiller160.orgbuilder.server.service.MemberService;
import io.craigmiller160.orgbuilder.server.service.ServiceFactory;

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

    //TODO need to add an additional restriction for the org related to the user

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

    @POST
    @RolesAllowed(Role.WRITE)
    public Response addMember(MemberDTO member) throws OrgApiException{
        MemberService memberService = factory.newMemberService(securityContext);
        member = memberService.addMember(member);

        return Response
                .created(URI.create(uriInfo.getPath() + "/" + member.getElementId()))
                .entity(member)
                .build();
    }

    @PUT
    @Path("/{memberId}")
    @RolesAllowed(Role.WRITE)
    public Response updateMember(@PathParam("memberId") long memberId, MemberDTO member) throws OrgApiException{
        MemberService memberService = factory.newMemberService(securityContext);
        member = memberService.updateMember(member, memberId);
        return Response
                .accepted(member)
                .build();
    }

    @DELETE
    @Path("/{memberId}")
    @RolesAllowed(Role.WRITE)
    public Response deleteMember(@PathParam("memberId") long memberId) throws OrgApiException{
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
