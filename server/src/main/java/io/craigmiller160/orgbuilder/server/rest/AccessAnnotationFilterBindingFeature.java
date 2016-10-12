package io.craigmiller160.orgbuilder.server.rest;

import io.craigmiller160.orgbuilder.server.rest.annotation.ThisOrgAllowed;
import io.craigmiller160.orgbuilder.server.rest.annotation.ThisUserAllowed;

import javax.ws.rs.container.DynamicFeature;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.FeatureContext;

/**
 * Created by craig on 10/9/16.
 */
public class AccessAnnotationFilterBindingFeature implements DynamicFeature {

    @Override
    public void configure(ResourceInfo resourceInfo, FeatureContext featureContext) {
        if(resourceInfo.getResourceMethod().isAnnotationPresent(ThisOrgAllowed.class)){
            featureContext.register(ThisOrgAllowedFilter.class);
        }
        else if(resourceInfo.getResourceMethod().isAnnotationPresent(ThisUserAllowed.class)){
            featureContext.register(ThisUserAllowedFilter.class);
        }
    }
}
