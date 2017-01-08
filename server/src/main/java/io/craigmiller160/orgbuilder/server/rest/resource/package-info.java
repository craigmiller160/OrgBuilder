/**
 * ORGBUILDER API SPECIFICATION
 *
 * INTRO
 *
 * This is the specification for the RESTful API that the OrgBuilder
 * server exposes through the resource classes in this package. All
 * resource classes conform to these guidelines.
 *
 * RESPONSE STATUSES
 *
 * 200 OK
 * - Request was executed successfully by the server.
 * - Usually includes a return payload.
 * - The only case where nothing is returned is /authenticate, where this indicates a successful login.
 *
 * 201 CREATED
 * - Used only for POST requests that create new resources.
 * - Returns the URI for the created resource.
 * - Returns a payload containing the contents currently on the server.
 *
 * 204 NO CONTENT
 * - Request was valid, but no data on the server matches the request to be returned.
 * - Nothing is returned in the payload.
 *
 * 400 BAD REQUEST
 * - The request lacked necessary content to execute against the server.
 * - An error message is usually returned in the payload.
 *
 * 401 UNAUTHORIZED
 * - Only returned by /auth resource, to indicate that the login credentials are invalid.
 * - Not used for any other access-related errors.
 * - An error message is usually returned in the payload.
 *
 * 403 FORBIDDEN
 * - Used for any access-related errors other than for the initial authentication.
 * - An error message is usually returned in the payload.
 *
 * 409 CONFLICT
 * - Only used for a single resource, /auth/exists.
 * - Returned when testing if a user name already exists, and it does exist, to indicate that there is a conflict.
 *
 * 500 INTERNAL SERVER ERROR
 * - An exception happened on the server
 * - An error message is usually returned in the payload.
 *
 *
 * Created by craig on 1/8/17.
 */
package io.craigmiller160.orgbuilder.server.rest.resource;