package com.lundih.authorizationserver.exceptions.jwt

import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException

/**
 * Exception intended to be thrown when a refresh token is used to request for an access token while the access token
 * corresponding to the refresh token is still valid
 *
 * @author lundih
 * @since 0.0.1
 */
class AttemptToRefreshValidTokenException: ResponseStatusException(HttpStatus.FORBIDDEN,
        "The access token that is to be refreshed by the provided refresh token still appears to be valid")