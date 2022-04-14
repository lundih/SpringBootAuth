package com.lundih.authorizationserver.exceptions.jwt

import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException

/**
 * Exception intended to be thrown when a JWT has empty claims
 *
 * @author lundih
 * @since 0.0.1
 */
class EmptyJwtClaimsException(message: String?): ResponseStatusException(HttpStatus.BAD_REQUEST, message)