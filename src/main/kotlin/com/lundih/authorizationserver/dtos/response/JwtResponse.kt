package com.lundih.authorizationserver.dtos.response

/**
 * Response body for when a user has successfully been authenticated, and they have been given a JWT
 *
 * @author lundih
 * @since 0.0.1
 *
 * @param token JWT (access token) to be returned to the user which they should use for subsequent requests
 * @param refreshToken JWT to be used to request for a new access token in the case of an expired access token without
 * requiring the user to provide their login credentials. It should ideally have a significantly longer lifespan than
 * the access token
 * @param type Authorization type the auth server expects when the JWT is used in a request. The default value is 'Bearer'
 * @param user [UserResponse] with the details of the authenticated user
 */
data class JwtResponse(val token: String, val refreshToken: String, val type: String = "Bearer", val user: UserResponse)
