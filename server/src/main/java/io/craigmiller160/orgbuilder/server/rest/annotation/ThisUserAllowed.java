package io.craigmiller160.orgbuilder.server.rest.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * An annotation to be used to restrict
 * access to a resource. It works alongside
 * the ThisUserAllowedFilter.
 *
 * It restricts access in the following ways:
 *
 * 1) A user that is the same as the targeted
 * resource is always allowed.
 *
 * 2) A user with a role in the otherUserRolesAllowed
 * array is allowed.
 *
 * NOTE: A final restriction, allowing a user with
 * Admin access in the same org as the user accessing
 * the resource to have access, could not be achieved
 * with this annotation. That restriction will
 * be handled at the service layer.
 *
 * Created by craig on 10/9/16.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ThisUserAllowed {

    /**
     * The roles that, if the user has,
     * they are granted access regardless
     * of the userId. By default, no roles
     * have this access.
     *
     * @return the roles that, if the user
     * has, they are granted access regardless
     * of the userId.
     */
    String[] otherUserRolesAllowed() default "";

}
