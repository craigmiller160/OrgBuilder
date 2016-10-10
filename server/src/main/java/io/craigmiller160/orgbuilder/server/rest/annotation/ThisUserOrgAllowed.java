package io.craigmiller160.orgbuilder.server.rest.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * An annotation to be used to restrict
 * access to a resource. It works alongside
 * the ThisUserOrgAllowedFilter.
 *
 * It restricts access in the following ways:
 *
 * 1) A user that is the same as the targeted
 * resource is always allowed.
 *
 * 2) Users in the same org as the TODO figure this out
 *
 * Created by craig on 10/9/16.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ThisUserOrgAllowed {

    String[] inOrgRolesAllowed() default "";

    String[] outOfOrgRolesAllowed() default "";

}
