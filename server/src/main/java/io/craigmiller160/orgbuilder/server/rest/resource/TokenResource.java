package io.craigmiller160.orgbuilder.server.rest.resource;

import io.craigmiller160.orgbuilder.server.OrgApiException;
import io.craigmiller160.orgbuilder.server.dto.RefreshTokenDTO;
import io.craigmiller160.orgbuilder.server.dto.RefreshTokenListDTO;
import io.craigmiller160.orgbuilder.server.rest.ResourceFilterBean;
import io.craigmiller160.orgbuilder.server.rest.Role;
import io.craigmiller160.orgbuilder.server.service.ServiceFactory;
import io.craigmiller160.orgbuilder.server.service.TokenService;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

/**
 * The resource to handle access to RefreshTokens.
 *
 * For security reasons, this class should NEVER
 * add or update tokens. It simply allows tokens
 * to be viewed or deleted by the webmaster.
 *
 * Created by craig on 10/17/16.
 */
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/tokens")
public class TokenResource {

    private final ServiceFactory factory = ServiceFactory.newInstance();

    @Context
    private SecurityContext securityContext;

    /**
     * RESOURCE: GET /tokens
     *
     * PURPOSE: Retrieve all refresh tokens currently stored in the server.
     *
     * ACCESS: Users with the MASTER role.
     *
     * BODY: NONE
     *
     * QUERY PARAMS:
     * offset: the number of records to skip over before starting retrieval.
     * size: the total number of records to retrieve.
     *
     * @param resourceFilterBean the filter bean with the Query Params.
     * @return the Response, containing all the tokens retrieved by the request.
     * @throws OrgApiException if an error occurs.
     */
    @GET
    @RolesAllowed(Role.MASTER)
    public Response getAllTokens(@BeanParam ResourceFilterBean resourceFilterBean) throws OrgApiException{
        resourceFilterBean.validateFilterParams();

        TokenService tokenService = factory.newTokenService(securityContext);
        RefreshTokenListDTO results = tokenService.getAllRefreshTokens(resourceFilterBean.getOffset(), resourceFilterBean.getSize());
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
     * RESOURCE: GET /tokens/{tokenId}
     *
     * PURPOSE: Retrieve a single refresh token from the server.
     *
     * ACCESS: Users with the MASTER role.
     *
     * BODY: NONE
     *
     * QUERY PARAMS: NONE
     *
     * @param tokenId the ID of the token to retrieve.
     * @return the Response, containing the retrieved token, or
     *          nothing if there's no token with the specified ID.
     * @throws OrgApiException if an error occurs.
     */
    @GET
    @Path("/{tokenId}")
    @RolesAllowed(Role.MASTER)
    public Response getToken(@PathParam("tokenId") long tokenId) throws OrgApiException{
        TokenService tokenService = factory.newTokenService(securityContext);
        RefreshTokenDTO result = tokenService.getRefreshToken(tokenId);

        if(result != null){
            return Response
                    .ok(result)
                    .build();
        }
        return Response
                .noContent()
                .build();
    }

    /**
     * RESOURCE: DELETE /tokens/{tokenId}
     *
     * PURPOSE: Delete the specified refresh token.
     *
     * ACCESS: Users with the MASTER role.
     *
     * BODY: NONE
     *
     * QUERY PARAMS: NONE
     *
     * @param tokenId the ID of the token to delete.
     * @return the Response, containing the deleted token, or
     *          nothing if there was no token with the specified ID.
     * @throws OrgApiException if an error occurs.
     */
    @DELETE
    @Path("/{tokenId}")
    @RolesAllowed(Role.MASTER)
    public Response deleteToken(@PathParam("tokenId") long tokenId) throws OrgApiException{
        TokenService tokenService = factory.newTokenService(securityContext);
        RefreshTokenDTO result = tokenService.deleteRefreshToken(tokenId);

        if(result != null){
            return Response
                    .ok(result)
                    .build();
        }
        return Response
                .noContent()
                .build();
    }

}
