package io.craigmiller160.orgbuilder.server.rest.resource;

import io.craigmiller160.orgbuilder.server.OrgApiException;
import io.craigmiller160.orgbuilder.server.dto.AppInfoDTO;
import io.craigmiller160.orgbuilder.server.dto.SexListDTO;
import io.craigmiller160.orgbuilder.server.dto.RoleListDTO;
import io.craigmiller160.orgbuilder.server.dto.StateListDTO;
import io.craigmiller160.orgbuilder.server.service.InfoService;
import io.craigmiller160.orgbuilder.server.service.ServiceFactory;

import javax.annotation.security.PermitAll;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

/**
 * RESTful API for handling generic information
 * for the application. Nothing here is of a
 * restricted nature, and anyone with credentials
 * can access it.
 *
 * Created by craig on 9/18/16.
 */
@Path("/info")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class InfoResource {

    private final ServiceFactory factory = ServiceFactory.newInstance();

    @Context
    private SecurityContext securityContext;

    /**
     * RESOURCE: GET /info/sexes
     *
     * PURPOSE: Get all the accepted values for sexes for this application.
     *
     * ACCESS: Everyone.
     *
     * BODY: NONE
     *
     * QUERY PARAMS: NONE
     *
     * @return a Response containing the list of the sexes.
     * @throws OrgApiException if an error occurs.
     */
    @GET
    @Path("/sexes")
    @PermitAll
    public Response getSexes() throws OrgApiException{
        InfoService infoService = factory.newInfoService(securityContext);
        SexListDTO sexesList = infoService.getSexes();

        return Response
                .ok(sexesList)
                .build();
    }

    /**
     * RESOURCE: GET /info/states
     *
     * PURPOSE: Get a list of all the values for US states for this application.
     *
     * ACCESS: Everyone.
     *
     * BODY: NONE
     *
     * QUERY PARAMS: NONE
     *
     * @return a Response containing the list of the states.
     * @throws OrgApiException if an error occurs.
     */
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

    /**
     * RESOURCE: GET /info/roles
     *
     * PURPOSE: Get a list of all supported roles for the application.
     *
     * ACCESS: Everyone.
     *
     * BODY: NONE
     *
     * QUERY PARAMS: NONE
     *
     * @return a Response containing the list of the roles.
     * @throws OrgApiException if an error occurs.
     */
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

    /**
     * RESOURCE: GET /info/app
     *
     * PURPOSE: Get basic info about this application.
     *
     * ACCESS: Everyone
     *
     * BODY: NONE
     *
     * QUERY PARAMS: NONE
     *
     * @return
     * @throws OrgApiException
     */
    @GET
    @Path("/app")
    @PermitAll
    public Response getAppInfo() throws OrgApiException{
        InfoService infoService = factory.newInfoService(securityContext);
        AppInfoDTO appInfo = infoService.getAppInfo();
        return Response
                .ok(appInfo)
                .build();
    }

}
