package com.lundih.authorizationserver.utils.auth

import com.lundih.authorizationserver.domain.UserDetailsImpl
import com.lundih.authorizationserver.domain.UserDetailsServiceImpl
import com.lundih.authorizationserver.exceptions.UserAccountLockedException
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.kotlin.any
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import java.time.Instant
import java.util.*
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import kotlin.test.assertEquals

class AuthTokenFilterTest {
    private lateinit var userDetailsService: UserDetailsServiceImpl
    private lateinit var jwtUtils: JwtUtils

    private lateinit var authTokenFilter: AuthTokenFilter
    private lateinit var authTokenFilterWrapper: AuthTokenFilterWrapper

    private val validJwt = "testJwt"
    private val invalidJwt = "invalidTestJwt"
    private val lockedUserJwt = "lockedUserJwt"
    private val shouldLoginJwt = "shouldLoginJwt"
    private val authorizationHeaderKey = "Authorization"

    private val id: Long = 1
    private val employeeNumber = "12345"
    private val lockedEmployeeNumber  = "54321"
    private val shouldLoginEmployeeNumber = "12321"
    private val firstName = "firstName"
    private val middleName = "middleName"
    private val lastName = "lastName"
    private val password = "password"
    private val idNumber ="idNumber"
    private val dob = Instant.EPOCH
    private val email = "test@test.com"
    private val shouldLogin = false
    private val enabled = true
    private val createdOn = Instant.now()
    private val updatedOn = Instant.now()

    @BeforeEach
    fun setUp() {
        jwtUtils = mock(JwtUtils::class.java)
        userDetailsService = mock(UserDetailsServiceImpl::class.java)

        authTokenFilter = AuthTokenFilter(jwtUtils, userDetailsService)
        authTokenFilterWrapper = AuthTokenFilterWrapper(jwtUtils, userDetailsService)

        val userDetailsImpl = UserDetailsImpl(id,
                employeeNumber,
                firstName,
                middleName,
                lastName,
                idNumber,
                dob,
                shouldLogin,
                enabled,
                email,
                createdOn,
                updatedOn,
                password,
                Collections.emptyList())

        val lockedUserDetailsImpl = UserDetailsImpl(id,
                lockedEmployeeNumber,
                firstName,
                middleName,
                lastName,
                idNumber,
                dob,
                shouldLogin,
                false,
                email,
                createdOn,
                updatedOn,
                password,
                Collections.emptyList())

        val shouldLoginUserDetailsImpl = UserDetailsImpl(id,
                shouldLoginEmployeeNumber,
                firstName,
                middleName,
                lastName,
                idNumber,
                dob,
                true,
                enabled,
                email,
                createdOn,
                updatedOn,
                password,
                Collections.emptyList())

        `when`(jwtUtils.validateJwt(validJwt)).thenReturn(true)
        `when`(jwtUtils.validateJwt(invalidJwt)).thenReturn(false)
        `when`(jwtUtils.validateJwt(lockedUserJwt)).thenReturn(true)
        `when`(jwtUtils.validateJwt(shouldLoginJwt)).thenReturn(true)
        `when`(jwtUtils.getSubjectFromJwt(validJwt)).thenReturn(employeeNumber)
        `when`(jwtUtils.getSubjectFromJwt(lockedUserJwt)).thenReturn(lockedEmployeeNumber)
        `when`(jwtUtils.getSubjectFromJwt(shouldLoginJwt)).thenReturn(shouldLoginEmployeeNumber)

        `when`(userDetailsService.loadUserByUsername(employeeNumber)).thenReturn(userDetailsImpl)
        `when`(userDetailsService.loadUserByUsername(lockedEmployeeNumber)).thenReturn(lockedUserDetailsImpl)
        `when`(userDetailsService.loadUserByUsername(shouldLoginEmployeeNumber)).thenReturn(shouldLoginUserDetailsImpl)
    }

    @Test
    fun parseJwt_should_return_jwt_from_request_header_with_authorization_header_of_type_bearer() {
        val request = mock(HttpServletRequest::class.java)
        `when`(request.getHeader(authorizationHeaderKey)).thenReturn("Bearer $validJwt")

        assertEquals(validJwt, authTokenFilter.parseJwt(request))
    }

    @Test
    fun parseJwt_should_return_null_if_no_authorization_header_is_found_in_request_header() {
        val request = mock(HttpServletRequest::class.java)
        `when`(request.getHeader(authorizationHeaderKey)).thenReturn(null)

        assertEquals(null, authTokenFilter.parseJwt(request))
    }

    @Test
    fun parseJwt_should_return_null_if_authorization_header_is_not_of_type_bearer() {
        val request = mock(HttpServletRequest::class.java)
        `when`(request.getHeader(authorizationHeaderKey)).thenReturn("NotBearer $validJwt")

        assertEquals(null, authTokenFilter.parseJwt(request))
    }

    @Test
    fun run_authTokenFilterWrapper_tests() {
        authTokenFilterWrapper.doFilterInternal_should_not_attempt_to_get_subject_from_null_jwt()
        authTokenFilterWrapper.doFilterInternal_should_not_attempt_to_get_subject_from_invalid_jwt()
        authTokenFilterWrapper.doFilterInternal_should_throw_Exception_if_user_is_not_enabled()
        authTokenFilterWrapper.doFilterInternal_should_throw_Exception_if_user_shouldLogin_is_true()
    }

    // Wrapper class to test protected method in AuthTokenFilter
    inner class AuthTokenFilterWrapper(private val jwtUtils: JwtUtils, userDetailsService: UserDetailsServiceImpl):
            AuthTokenFilter(jwtUtils, userDetailsService) {
        private val response = mock(HttpServletResponse::class.java)
        private val filterChain = mock(FilterChain::class.java)

        fun doFilterInternal_should_not_attempt_to_get_subject_from_null_jwt() {
            val request = mock(HttpServletRequest::class.java)
            `when`(parseJwt(request)).thenReturn(null)

            doFilterInternal(request, response, filterChain)

            verify(jwtUtils, never()).getSubjectFromJwt(any())
        }

        fun doFilterInternal_should_not_attempt_to_get_subject_from_invalid_jwt() {
            val request = mock(HttpServletRequest::class.java)
            `when`(request.getHeader(authorizationHeaderKey)).thenReturn("Bearer $invalidJwt")

            doFilterInternal(request, response, filterChain)

            verify(jwtUtils, never()).getSubjectFromJwt(any())
        }

        fun doFilterInternal_should_throw_Exception_if_user_is_not_enabled() {
            val request = mock(HttpServletRequest::class.java)
            `when`(request.getHeader(authorizationHeaderKey)).thenReturn("Bearer $lockedUserJwt")

            assertThrows<UserAccountLockedException> {
                doFilterInternal(request, response, filterChain)
            }
        }

        fun doFilterInternal_should_throw_Exception_if_user_shouldLogin_is_true() {
            val request = mock(HttpServletRequest::class.java)
            `when`(request.getHeader(authorizationHeaderKey)).thenReturn("Bearer $shouldLoginJwt")

            assertThrows<Exception> {
                doFilterInternal(request, response, filterChain)
            }
        }
    }
}