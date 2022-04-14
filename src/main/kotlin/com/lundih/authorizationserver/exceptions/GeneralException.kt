package com.lundih.authorizationserver.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException

/**
 * Exception intended to be thrown for internal server errors
 *
 * @author lundih
 * @since 0.0.1
 */
class GeneralException(message: String?): ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, message)