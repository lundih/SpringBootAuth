package com.lundih.authorizationserver.services

import com.lundih.authorizationserver.domain.UserDetailsImpl
import com.lundih.authorizationserver.dtos.request.LoginRequest
import com.lundih.authorizationserver.dtos.request.RefreshTokenRequest
import com.lundih.authorizationserver.dtos.request.RegisterUserRequest
import com.lundih.authorizationserver.dtos.response.UserResponse
import com.lundih.authorizationserver.entities.Role
import com.lundih.authorizationserver.entities.User
import com.lundih.authorizationserver.enums.ERole
import com.lundih.authorizationserver.exceptions.DuplicateEntryException
import com.lundih.authorizationserver.mappers.UserMapper
import com.lundih.authorizationserver.repositories.RoleRepository
import com.lundih.authorizationserver.repositories.UserRepository
import com.lundih.authorizationserver.utils.auth.JwtUtils
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.any
import org.mockito.Mockito.mock
import org.mockito.Mockito.times
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import java.time.Instant
import java.util.*
import kotlin.test.assertEquals

class AuthServiceTest {
    private lateinit var authenticationManager: AuthenticationManager
    private lateinit var userRepository: UserRepository
    private lateinit var roleRepository: RoleRepository
    private lateinit var userMapper: UserMapper
    private lateinit var passwordEncoder: PasswordEncoder
    private lateinit var jwtUtils: JwtUtils

    private lateinit var authService: AuthService

    private val testAccessJwt = "testAccessJwt"
    private val testRefreshJwt = "testRefreshJwt"
    private val validRefreshJwt = "validRefreshJwt"
    private val invalidRefreshJwt = "invalidRefreshJwt"
    private val duplicateEmployeeNumber = "duplicateEmployeeNumber"
    private val duplicateIdNumber = "duplicateIdNumber"
    private val duplicateEmail = "duplicateEmail"
    private val roleUser = Role(ERole.ROLE_USER)
    private val roleAdmin = Role(ERole.ROLE_ADMIN)

    @BeforeEach
    fun setUp() {
        authenticationManager = mock(AuthenticationManager::class.java)
        userRepository = mock(UserRepository::class.java)
        roleRepository = mock(RoleRepository::class.java)
        userMapper = mock(UserMapper::class.java)
        passwordEncoder = mock(PasswordEncoder::class.java)
        jwtUtils = mock(JwtUtils::class.java)

        authService =
                AuthService(authenticationManager, userRepository, roleRepository, userMapper, passwordEncoder, jwtUtils)

        `when`(authenticationManager.authenticate(any())).thenReturn(UsernamePasswordAuthenticationToken(
                UserDetailsImpl(id = 1L,
                        employeeNumber ="12345",
                        firstName = "firstName",
                        middleName = "middleName",
                        lastName = "lastName",
                        idNumber = "12345",
                        dob = Instant.EPOCH,
                        shouldLogin = true,
                        enabled = true,
                        email = "test@test.com",
                        createdOn = Instant.now(),
                        updatedOn = Instant.now(),
                        password = "testPassword",
                        grantedAuthorities = Collections.emptyList()),
                null))

        `when`(userRepository.findByEmployeeNumber(any())).thenReturn(Optional.of(User()))
        `when`(userRepository.save(any())).thenReturn(User())
        `when`(userRepository.existsByEmployeeNumber(duplicateEmployeeNumber)).thenReturn(true)
        `when`(userRepository.existsByIdNumber(duplicateIdNumber)).thenReturn(true)
        `when`(userRepository.existsByEmail(duplicateEmail)).thenReturn(true)

        `when`(roleRepository.findByName(ERole.ROLE_USER)).thenReturn(Optional.of(roleUser))
        `when`(roleRepository.findByName(ERole.ROLE_ADMIN)).thenReturn(Optional.of(roleAdmin))

        `when`(userMapper.userDetailsImplToResponse(any())).thenReturn(UserResponse())

        `when`(jwtUtils.generateAccessJwt(any())).thenReturn(testAccessJwt)
        `when`(jwtUtils.generateRefreshJwt(any())).thenReturn(testRefreshJwt)
        `when`(jwtUtils.validateRefreshJwt(validRefreshJwt)).thenReturn(true)
        `when`(jwtUtils.validateRefreshJwt(invalidRefreshJwt)).thenReturn(false)
        `when`(jwtUtils.getAccessTokenOwnerFromRefreshToken(validRefreshJwt)).thenReturn("12345")
    }

    @Test
    fun generate_tokens_when_user_is_authenticated() {
        val jwtResponse = authService.authenticateUser(LoginRequest("12345", "password"))

        verify(authenticationManager, times(1)).authenticate(any())
        verify(jwtUtils, times(1)).generateAccessJwt(any())
        verify(jwtUtils, times(1)).generateRefreshJwt(any())
        assertEquals(testAccessJwt, jwtResponse.token)
        assertEquals(testRefreshJwt, jwtResponse.refreshToken)
    }

    @Test
    fun reset_shouldLogin_after_authentication() {
        authService.authenticateUser(LoginRequest("12345", "password"))

        verify(userRepository, times(1)).findByEmployeeNumber(any())
    }

    @Test
    fun generate_tokens_when_valid_refreshToken_is_provided() {
        val refreshTokenResponse = authService.renewAccessToken(RefreshTokenRequest(validRefreshJwt))

        verify(jwtUtils, times(1)).validateRefreshJwt(validRefreshJwt)
        assertEquals(testAccessJwt, refreshTokenResponse!!.token)
        assertEquals(testRefreshJwt, refreshTokenResponse.refreshToken)
    }

    @Test
    fun return_null_when_invalid_refreshToken_is_used_to_generate_tokens() {
        val refreshTokenResponse = authService.renewAccessToken(RefreshTokenRequest(invalidRefreshJwt))

        verify(jwtUtils, times(1)).validateRefreshJwt(invalidRefreshJwt)
        assertEquals(null, refreshTokenResponse)
    }

    @Test
    fun throw_exception_when_registering_with_duplicate_employeeNumber() {
        assertThrows<DuplicateEntryException> {
            authService.registerUser(RegisterUserRequest(duplicateEmployeeNumber, "firstName", null, "lastName", "12345", Instant.EPOCH, "test@test.com", null))
        }
    }

    @Test
    fun throw_exception_when_registering_with_duplicate_idNumber() {
        assertThrows<DuplicateEntryException> {
            authService.registerUser(RegisterUserRequest("12345", "firstName", null, "lastName", duplicateIdNumber, Instant.EPOCH, "test@test.com", null))
        }
    }

    @Test
    fun throw_exception_when_registering_with_duplicate_email() {
        assertThrows<DuplicateEntryException> {
            authService.registerUser(RegisterUserRequest("12345", "firstName", null, "lastName", "12345", Instant.EPOCH, duplicateEmail, null))
        }
    }

    @Test
    fun assign_user_ROLE_USER_when_no_roles_are_provided_during_registration() {
        val roles = authService.userProvidedRolesToSystemRoles(null)

        assertThat(roles.contains(roleUser))
    }

    @Test
    fun convert_client_provided_roles_into_system_roles() {
        val roles = authService.userProvidedRolesToSystemRoles(listOf("ADMIN", "USER", "IMPOSTOR"))

        assertThat(roles.contains(roleUser))
        assertThat(roles.contains(roleAdmin))
        assertEquals(2, roles.size)
    }
}