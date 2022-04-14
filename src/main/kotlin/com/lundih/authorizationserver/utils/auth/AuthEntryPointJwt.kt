package com.lundih.authorizationserver.utils.auth

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * Custom authentication entry point for exception handling
 *
 * @author lundih
 * @since 0.0.1
 */
@Component
class AuthEntryPointJwt: AuthenticationEntryPoint {
    override fun commence(request: HttpServletRequest, response: HttpServletResponse, authException: AuthenticationException) {
        logger.error("Unauthorized error: ${authException.message}")
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Error: Unauthorized")
    }

    companion object {
        val logger: Logger = LoggerFactory.getLogger(AuthEntryPointJwt::class.java)
    }
}