package io.craigmiller160.orgbuilder.server.rest.resource;

import io.swagger.annotations.ApiKeyAuthDefinition;
import io.swagger.annotations.Info;
import io.swagger.annotations.SecurityDefinition;
import io.swagger.annotations.SwaggerDefinition;

/**
 * An empty interface to provide useful metadata
 * for Swagger.
 *
 * Created by craig on 7/16/17.
 */
@SwaggerDefinition(
        info = @Info(
                title = "OrgBuilder API",
                version = "1.1-ALPHA",
                description = "The API for the data managed by the OrgBuilder application"
        ),
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
public interface ApiDefinition {
}
