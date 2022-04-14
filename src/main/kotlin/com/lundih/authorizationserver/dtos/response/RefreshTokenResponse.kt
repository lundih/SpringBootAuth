package com.lundih.authorizationserver.dtos.response

/**
 * Response body to be returned when a request for new token has been made using a refresh token, and the request is
 * successful
 *
 * @author lundih
 * @since 0.0.1
 */
data class RefreshTokenResponse(val token: String, val refreshToken: String, val type: String = "Bearer")