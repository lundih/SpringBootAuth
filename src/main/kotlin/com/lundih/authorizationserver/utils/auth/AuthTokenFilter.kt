package com.lundih.authorizationserver.utils.auth

import com.lundih.authorizationserver.domain.UserDetailsServiceImpl
import com.lundih.authorizationserver.exceptions.UserAccountLockedException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.util.StringUtils
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * Custom request filter
 *
 * @author lundih
 * @since 0.0.1
 *
 * This class is made open in order to make it possible to inherit from it to test the protected doFilterInternal method.
 * Kotlin does not allow members of the same package to access protected methods (like in Java) but subclasses instead
 *
 * @param jwtUtils [JWT utility][JwtUtils]. Used by the filter to validate the JWT in the request
 * @param userDetailsService UserDetailsImpl object[UserDetailsServiceImpl].Used to load the user whose identifier
 * (could be a username or some number) is received in the JWT
 */
open class AuthTokenFilter(private val jwtUtils: JwtUtils, private val userDetailsService: UserDetailsServiceImpl)
    : OncePerRequestFilter() {

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        val jwt = parseJwt(request)
        if (jwt != null && jwtUtils.validateJwt(jwt)) {
            val username: String = jwtUtils.getSubjectFromJwt(jwt)
            val userDetails = userDetailsService.loadUserByUsername(username)
            // Throw an exception if the user's account has been disabled
            if (!userDetails.enabled) throw UserAccountLockedException()
            // Throw an exception if a user authenticates with a token while their shouldLogin flag is enabled
            if (userDetails.shouldLogin) throw Exception()
            val authentication = UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)
            authentication.details = WebAuthenticationDetailsSource().buildDetails(request)
            SecurityContextHolder.getContext().authentication = authentication
        }
        filterChain.doFilter(request, response)
    }

    /**
     * Extracts JWT from request header
     *
     * @param request Request from client
     * @return Jwt string or null if it could not be retrieved
     */
    fun parseJwt(request: HttpServletRequest): String? {
        val headerAuth = request.getHeader("Authorization")
        return if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            headerAuth.substring(7, headerAuth.length)
        } else null
    }
}