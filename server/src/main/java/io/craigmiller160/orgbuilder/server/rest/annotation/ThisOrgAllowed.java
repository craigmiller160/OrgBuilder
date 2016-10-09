package io.craigmiller160.orgbuilder.server.rest.annotation;

import io.craigmiller160.orgbuilder.server.rest.Role;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * An annotation to be used to restrict
 * access to a resource. It works alongside
 * the ThisOrgAllowedFilter.
 *
 * Created by craig on 10/9/16.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ThisOrgAllowed {

    /**
     * The roles that the user with
     * the orgId must have to be
     * granted access. By default,
     * all roles are allowed.
     *
     * @return the roles the user must
     * have to be granted access.
     */
    String[] inOrgRolesAllowed() default {Role.MASTER, Role.ADMIN, Role.READ, Role.WRITE};

    /**
     * The roles that, if the user has,
     * they are granted access regardless
     * of the orgId. By default, no roles
     * have this access.
     *
     * @return the roles that, if the user
     * has, they are granted access regardless
     * of the orgId.
     */
    String[] outOfOrgRolesAllowed() default "";

}
