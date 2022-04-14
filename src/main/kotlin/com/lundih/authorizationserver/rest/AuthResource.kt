package com.lundih.authorizationserver.rest

import com.lundih.authorizationserver.dtos.request.LoginRequest
import com.lundih.authorizationserver.dtos.request.RefreshTokenRequest
import com.lundih.authorizationserver.dtos.request.RegisterUserRequest
import com.lundih.authorizationserver.dtos.response.JwtResponse
import com.lundih.authorizationserver.dtos.response.RefreshTokenResponse
import com.lundih.authorizationserver.services.AuthService
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

/**
 * Rest controller to handle requests that involve authentication
 *
 * @author lundih
 * @since 0.0.1
 *
 * @param authService Provides the methods for authentication and authorization
 */
@Tag(name = "Auth resource", description = "Endpoints for managing authentication and authorization")
@CrossOrigin(origins = ["*"], maxAge = 3600)
@RestController
@RequestMapping("/api/v1/auth")
class AuthResource(val authService: AuthService) {

    /**
     * Endpoint for logging in
     *
     * @param loginRequest [LoginRequest] with the users credentials
     * @return [JwtResponse] if a user is authenticated else an error is thrown
     */
    @PostMapping("login")
    fun authenticateUser(@Validated @RequestBody loginRequest: LoginRequest): JwtResponse {
        return authService.authenticateUser(loginRequest)
    }

    /**
     * Endpoint for requesting for a new access token using a refresh token
     *
     * @param refreshTokenRequest [RefreshTokenRequest] with the refresh token provided after login or after a previous
     * request for a new access token using a refresh token
     * @return [RefreshTokenResponse] if the presented refresh token is valid, else an Exception is thrown with a message
     * describing the issue
     */
    @PostMapping("refresh-token")
    fun renewAccessToken(@Validated @RequestBody refreshTokenRequest: RefreshTokenRequest): RefreshTokenResponse? {
        return authService.renewAccessToken(refreshTokenRequest)
    }

    /**
     * Endpoint for registering a user
     *
     * @param registerUserRequest [RegisterUserRequest] with details about the user to be registered
     * @return Map with key of type String and value of type String confirming that the user has been registered
     * successfully else an error is thrown
     */
    @PostMapping("register-user")
    fun registerUser(@Validated @RequestBody registerUserRequest: RegisterUserRequest): Map<String, String> {
        return authService.registerUser(registerUserRequest)
    }
}