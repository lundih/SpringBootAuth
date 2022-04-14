package com.lundih.authorizationserver.exceptions.jwt

import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException

/**
 * Exception intended to be thrown when a JWT is expired
 *
 * @author lundih
 * @since 0.0.1
 */
class ExpiredJwtException(message: String?): ResponseStatusException(HttpStatus.UNAUTHORIZED, message)