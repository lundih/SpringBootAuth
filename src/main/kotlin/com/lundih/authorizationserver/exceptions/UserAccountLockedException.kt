package com.lundih.authorizationserver.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException

class UserAccountLockedException: ResponseStatusException(HttpStatus.LOCKED, "User's account is locked")