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
 * RESTful API for accessing the Members resource.
 *
 * Created by craig on 8/23/16.
 */
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/members")
public class MemberResource {

    private final ServiceFactory factory = ServiceFactory.newInstance();

    @Context
    private SecurityContext securityContext;

    @Context
    private UriInfo uriInfo;

    /**
     * RESOURCE: GET /members
     *
     * PURPOSE: Retrieve all members.
     *
     * ACCESS: Users with the READ role.
     *
     * BODY: NONE
     *
     * QUERY PARAMS:
     * offset: the number of records to skip over before starting retrieval.
     * size: the total number of records to retrieve.
     * {search}: a number of other params for performing a search for a specific member.
     *
     * @param membersFilterBean the filter bean with the Query Params.
     * @return the Response, containing all the members retrieved by the request.
     * @throws OrgApiException if an error occurs.
     */
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

    /**
     * RESOURCE: POST /members
     *
     * PURPOSE: Create a new member.
     *
     * ACCESS: Users with the WRITE role.
     *
     * BODY: The member to create.
     *
     * QUERY PARAMS: NONE
     *
     * @param member the member to create.
     * @return the Response, containing the member that was created.
     * @throws OrgApiException if an error occurs.
     */
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

    /**
     * RESOURCE: PUT /members/{memberId}
     *
     * PURPOSE: Update an existing member.
     *
     * ACCESS: Users with the WRITE role.
     *
     * BODY: The member to update.
     *
     * QUERY PARAMS: NONE
     *
     * @param memberId the ID of the member to update.
     * @param member the updated member.
     * @return the Response, containing the member that was updated,
     *          or nothing if there was no member with the specified ID.
     * @throws OrgApiException if an error occurs.
     */
    @PUT
    @Path("/{memberId}")
    @RolesAllowed(Role.WRITE)
    public Response updateMember(@PathParam("memberId") long memberId, MemberDTO member) throws OrgApiException{
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

    /**
     * RESOURCE: DELETE /members/{memberId}
     *
     * PURPOSE: Delete an existing member.
     *
     * ACCESS: Users with the WRITE role.
     *
     * BODY: NONE
     *
     * QUERY PARAMS: NONE
     *
     * @param memberId the ID of the member to delete.
     * @return the Response, containing the member that was deleted,
     *          or nothing if no member matching the ID existed.
     * @throws OrgApiException if an error occurs.
     */
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

    /**
     * RESOURCE: GET /members/{memberId}
     *
     * PURPOSE: Retrieve a single member and all its details.
     *
     * ACCESS: Users with the READ role.
     *
     * BODY: NONE
     *
     * QUERY PARAMS: NONE
     *
     * @param memberId the ID of the member to retrieve.
     * @return the Response, containing the member retrieved,
     *          or nothing if there's no member matching the specified ID.
     * @throws OrgApiException if an error occurs.
     */
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
