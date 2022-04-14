package com.lundih.authorizationserver.services

import com.lundih.authorizationserver.domain.UserDetailsImpl
import com.lundih.authorizationserver.dtos.request.LoginRequest
import com.lundih.authorizationserver.dtos.request.RefreshTokenRequest
import com.lundih.authorizationserver.dtos.request.RegisterUserRequest
import com.lundih.authorizationserver.dtos.response.JwtResponse
import com.lundih.authorizationserver.dtos.response.RefreshTokenResponse
import com.lundih.authorizationserver.entities.Role
import com.lundih.authorizationserver.enums.ERole
import com.lundih.authorizationserver.exceptions.DuplicateEntryException
import com.lundih.authorizationserver.exceptions.GeneralException
import com.lundih.authorizationserver.mappers.UserMapper
import com.lundih.authorizationserver.repositories.RoleRepository
import com.lundih.authorizationserver.repositories.UserRepository
import com.lundih.authorizationserver.utils.auth.JwtUtils
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.util.function.Consumer

/**
 * Service to handle requests that deal with authentication
 *
 * @author lundih
 * @since 0.0.1
 *
 * @param authenticationManager Authenticates users attempting to log in
 * @param userRepository [UserRepository] gets [Users][com.lundih.authorizationserver.entities.User] from the database
 * @param roleRepository [RoleRepository] gets [Roles][Role] from the database
 * @param userMapper [UserMapper] maps between [User][com.lundih.authorizationserver.entities.User] and its DTOs
 * @param passwordEncoder encodes user-provided passwords for comparison with their stored password hashes
 * @param jwtUtils [JwtUtils] generates JWTs to return to authenticated users
 */
@Service
class AuthService(private val authenticationManager: AuthenticationManager,
                  private val userRepository: UserRepository,
                  private val roleRepository: RoleRepository,
                  private val userMapper: UserMapper,
                  private val passwordEncoder: PasswordEncoder,
                  private val jwtUtils: JwtUtils) {

    /**
     * Handles log-in requests
     *
     * The framework will check if the user is enabled and use that as a parameter to determine if the login is
     * successful
     *
     * @param loginRequest [LoginRequest] with the users credentials
     * @return [JwtResponse] if a user is authenticated else an Exception is thrown
     */
    fun authenticateUser(loginRequest: LoginRequest): JwtResponse {
        val authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(loginRequest.employeeNumber, loginRequest.password))
        SecurityContextHolder.getContext().authentication = authentication
        val userDetails = authentication.principal as UserDetailsImpl
        val accessJwt = jwtUtils.generateAccessJwt(userDetails.employeeNumber)
        val refreshJwt = jwtUtils.generateRefreshJwt(accessJwt)
        // If a user has their shouldLogin in flag set to true, then set it to false
        if (userDetails.shouldLogin) {
            val user = userRepository.findByEmployeeNumber(userDetails.employeeNumber).get()
            user.shouldLogin = false
            userRepository.save(user)
        }

        return JwtResponse(accessJwt, refreshJwt, user = userMapper.userDetailsImplToResponse(userDetails))
    }

    /**
     * Handles requests to renew access tokens using refresh tokens
     *
     * @param refreshTokenRequest [RefreshTokenRequest] with the refresh token provided after login or after a previous
     * request for a new access token using a refresh token
     * @return [RefreshTokenResponse] if the presented refresh token is valid, else an Exception is thrown with a message
     * describing the issue
     */
    fun renewAccessToken(refreshTokenRequest: RefreshTokenRequest): RefreshTokenResponse? {
        val refreshToken = refreshTokenRequest.refreshToken
        return if (jwtUtils.validateRefreshJwt(refreshToken)) {
            val employeeNumber = jwtUtils.getAccessTokenOwnerFromRefreshToken(refreshToken)
            val accessJwt = jwtUtils.generateAccessJwt(employeeNumber)
            val refreshJwt = jwtUtils.generateRefreshJwt(accessJwt)

            RefreshTokenResponse(accessJwt, refreshJwt)
        } else null
    }

    /**
     * Handles requests to register users
     *
     * @param registerUserRequest [RegisterUserRequest] with details about the user to be registered
     * @return Map with key of type String and value of type String confirming that the user has been registered
     * successfully else an Exception is thrown
     */
    fun registerUser(registerUserRequest: RegisterUserRequest): Map<String, String> {
        if (userRepository.existsByEmployeeNumber(registerUserRequest.employeeNumber))
            throw DuplicateEntryException("Employee number already in use")
        if (userRepository.existsByIdNumber(registerUserRequest.idNumber))
            throw DuplicateEntryException("ID number already in use")
        if (userRepository.existsByEmail(registerUserRequest.email))
            throw DuplicateEntryException("Email address already in use")

        val user = userMapper.registerUserRequestToUser(registerUserRequest)
        user.password = passwordEncoder.encode("555555")
        user.roles = userProvidedRolesToSystemRoles(registerUserRequest.roles)
        userRepository.save(user)

        return mapOf("message" to "New user registered successfully")
    }

    /**
     * Converts roles provided by the client into the format of accepted roles in the system
     *
     * @param userProvidedRoles List of roles in an easy-to-read format for users
     * @return Roles that are understood by the system
     */
    fun userProvidedRolesToSystemRoles(userProvidedRoles: List<String>?): Set<Role> {
        val roles: MutableSet<Role> = HashSet()
        if (userProvidedRoles == null) {
            val userRole: Role = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow { GeneralException("Role [${ERole.ROLE_USER}] is not found.") }
            roles.add(userRole)
        } else {
            userProvidedRoles.forEach(Consumer { role: String? ->
                when (role) {
                    "ADMIN" -> {
                        val adminRole: Role = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow { GeneralException("Role [$role] is not found.") }
                        roles.add(adminRole)
                    }
                    "USER" -> {
                        val userRole: Role = roleRepository.findByName(ERole.ROLE_USER)
                                .orElseThrow { GeneralException("Role [$role] is not found.") }
                        roles.add(userRole)
                    }
                    else -> { }
                }
            })
        }

        return roles
    }
}