package com.lundih.authorizationserver.exceptions.jwt

import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException

/**
 * Exception intended to be thrown when a JWT is unsupported
 *
 * @author lundih
 * @since 0.0.1
 */
class UnsupportedJwtException(message: String?): ResponseStatusException(HttpStatus.BAD_REQUEST, message)