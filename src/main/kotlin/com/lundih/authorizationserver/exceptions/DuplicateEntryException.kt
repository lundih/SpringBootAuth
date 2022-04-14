package com.lundih.authorizationserver.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException

/**
 * Exception intended to be thrown for Conflicting entries
 *
 * @author lundih
 * @since 0.0.1
 */
class DuplicateEntryException(message: String?): ResponseStatusException(HttpStatus.CONFLICT, message)