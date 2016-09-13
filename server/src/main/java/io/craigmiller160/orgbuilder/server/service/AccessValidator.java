package io.craigmiller160.orgbuilder.server.service;

import io.craigmiller160.orgbuilder.server.rest.Role;

import javax.ws.rs.core.SecurityContext;

/**
 * Created by craigmiller on 9/8/16.
 */
public class AccessValidator {

    public static boolean hasReadAccess(SecurityContext securityContext){
        return securityContext.isUserInRole(Role.READ.toString()) || securityContext.isUserInRole(Role.WRITE.toString()) ||
                securityContext.isUserInRole(Role.ADMIN.toString()) || securityContext.isUserInRole(Role.MASTER.toString());
    }

    public static boolean hasWriteAccess(SecurityContext securityContext){
        return securityContext.isUserInRole(Role.WRITE.toString()) || securityContext.isUserInRole(Role.ADMIN.toString()) ||
                securityContext.isUserInRole(Role.MASTER.toString());
    }

    public static boolean hasAdminAccess(SecurityContext securityContext){
        return securityContext.isUserInRole(Role.ADMIN.toString()) || securityContext.isUserInRole(Role.MASTER.toString());
    }

    public static boolean hasMasterAccess(SecurityContext securityContext){
        return securityContext.isUserInRole(Role.MASTER.toString());
    }

}
