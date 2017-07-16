package io.craigmiller160.orgbuilder.server.rest.resource;

import io.craigmiller160.orgbuilder.server.OrgApiException;
import io.craigmiller160.orgbuilder.server.dto.AllInfoDTO;
import io.craigmiller160.orgbuilder.server.dto.AppInfoDTO;
import io.craigmiller160.orgbuilder.server.dto.ContactTypesDTO;
import io.craigmiller160.orgbuilder.server.dto.SexListDTO;
import io.craigmiller160.orgbuilder.server.dto.RoleListDTO;
import io.craigmiller160.orgbuilder.server.dto.StateListDTO;
import io.craigmiller160.orgbuilder.server.service.InfoService;
import io.craigmiller160.orgbuilder.server.service.ServiceFactory;
import io.swagger.annotations.*;

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
@SwaggerDefinition(
        securityDefinition = @SecurityDefinition(
                apiKeyAuthDefinitions = {
                        @ApiKeyAuthDefinition(
                                in = ApiKeyAuthDefinition.ApiKeyLocation.HEADER,
                                key = "orgapiToken",
                                name= "Authorization",
                                description = "The JSON Web Token needed to access the API"
                        )
                }
        )
)
@Api(
        tags = "info",
        authorizations = {
                @Authorization(value = "orgapiToken")
        }
)
@Path("/info")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class InfoResource implements ApiDefinition{

    private final ServiceFactory factory = ServiceFactory.newInstance();

    @Context
    private SecurityContext securityContext;

    @ApiOperation(
            value = "Get all general information data in a single payload",
            notes = "This returns a combination of everything that all the other operations in this resource return",
            response = AllInfoDTO.class
    )
    @GET
    @PermitAll
    public Response getAll() throws OrgApiException{
        InfoService infoService = factory.newInfoService(securityContext);
        AllInfoDTO allInfoDTO = infoService.getAll();

        return Response
                .ok(allInfoDTO)
                .build();
    }

    @ApiOperation(
            value = "Get the accepted values for 'sex' fields.",
            notes = "This returns a collection of values that are the only acceptable choices for fields that define a person's sex.",
            response = SexListDTO.class
    )
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

    @ApiOperation(
            value = "Get the accepted values for 'state' fields.",
            notes = "This returns a collection of the values for all US states.",
            response = StateListDTO.class
    )
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


    @ApiOperation(
            value = "Get the accepted values for user roles.",
            notes = "This returns a collection of values that are the only acceptable choices for working with user access roles.",
            response = RoleListDTO.class
    )
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

    @ApiOperation(
            value = "Get the basic information about this application.",
            notes = "This returns some very basic meta information about this application.",
            response = AppInfoDTO.class
    )
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

    @ApiOperation(
            value = "Get the accepted values for contact info types.",
            notes = "This returns the collections of values for the 'type' fields of various contact info resources.",
            response = ContactTypesDTO.class
    )
    @GET
    @Path("/contact")
    @PermitAll
    public Response getContactTypes() throws OrgApiException{
        InfoService infoService = factory.newInfoService(securityContext);
        ContactTypesDTO contactTypes = infoService.getContactTypes();
        return Response
                .ok(contactTypes)
                .build();
    }

}
