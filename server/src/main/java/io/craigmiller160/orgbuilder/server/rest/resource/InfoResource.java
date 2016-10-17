package io.craigmiller160.orgbuilder.server.rest.resource;

import io.craigmiller160.orgbuilder.server.OrgApiException;
import io.craigmiller160.orgbuilder.server.dto.GenderListDTO;
import io.craigmiller160.orgbuilder.server.dto.RoleListDTO;
import io.craigmiller160.orgbuilder.server.dto.StateListDTO;
import io.craigmiller160.orgbuilder.server.service.InfoService;
import io.craigmiller160.orgbuilder.server.service.ServiceFactory;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

/**
 * Created by craig on 9/18/16.
 */
@Path("/info")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class InfoResource {

    private final ServiceFactory factory = ServiceFactory.newInstance();

    @Context
    private SecurityContext securityContext;

    @GET
    @Path("/genders")
    @PermitAll
    public Response getGenders() throws OrgApiException{
        InfoService infoService = factory.newInfoService(securityContext);
        GenderListDTO genderList = infoService.getGenders();

        return Response
                .ok(genderList)
                .build();
    }

    @GET
    @Path("/states")
    @PermitAll
    public Response getStates() throws OrgApiException{
        InfoService infoService = factory.newInfoService(securityContext);
        StateListDTO stateList = infoService.getStates();
        return Response
                .ok(stateList)
                .build();
    }

    @GET
    @Path("/roles")
    @PermitAll
    public Response getRoles() throws OrgApiException{
        InfoService infoService = factory.newInfoService(securityContext);
        RoleListDTO roleList = infoService.getRoles();
        return Response
                .ok(roleList)
                .build();
    }

}
