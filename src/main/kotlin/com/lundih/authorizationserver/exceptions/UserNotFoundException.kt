package com.lundih.authorizationserver.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException

class UserNotFoundException(employeeNumber: String):
        ResponseStatusException(HttpStatus.NOT_FOUND, "User with employee number $employeeNumber was not found")