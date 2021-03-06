package io.craigmiller160.orgbuilder.server;

import io.craigmiller160.orgbuilder.server.rest.AccessAnnotationFilterBindingFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;

import javax.ws.rs.ApplicationPath;

/**
 * Created by craig on 9/18/16.
 */
@ApplicationPath("/")
public class OrgApiResourceConfig extends ResourceConfig {

    public OrgApiResourceConfig() {
        packages(true, "io.craigmiller160.orgbuilder.server.rest");
        packages(true, "io.swagger.jaxrs.listing");
        register(RolesAllowedDynamicFeature.class);
        register(AccessAnnotationFilterBindingFeature.class);
    }
}
