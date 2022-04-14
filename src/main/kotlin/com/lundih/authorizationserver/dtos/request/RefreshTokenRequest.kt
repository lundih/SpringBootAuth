package com.lundih.authorizationserver.dtos.request

/**
 * Request body for login credentials
 *
 * @author lundih
 * @since 0.0.1
 *
 * @param refreshToken The refresh JWT provided alongside the access token during a user's authentication
 */
data class RefreshTokenRequest(val refreshToken: String)
