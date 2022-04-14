package com.lundih.authorizationserver.domain

import com.lundih.authorizationserver.entities.Role
import com.lundih.authorizationserver.entities.User
import com.lundih.authorizationserver.enums.ERole
import com.lundih.authorizationserver.repositories.UserRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.springframework.security.core.userdetails.UsernameNotFoundException
import java.time.Instant
import java.util.*
import kotlin.test.assertEquals

class UserDetailsServiceImplTest {
    private lateinit var userRepository: UserRepository

    private  lateinit var userDetailsServiceImpl: UserDetailsServiceImpl

    private val employeeNumber = "12345"
    private val invalidEmployeeNumber = "54321"
    private val user = User()

    @BeforeEach
    fun setUp() {
        userRepository = mock(UserRepository::class.java)
        userDetailsServiceImpl = UserDetailsServiceImpl(userRepository)
        user.id = 1
        user.employeeNumber = employeeNumber
        user.firstName = "firstName"
        user.middleName = "middleName"
        user.lastName = "lastName"
        user.password = "password"
        user.idNumber = "12345"
        user.dob = Instant.EPOCH
        user.shouldLogin = false
        user.enabled = true
        user.email = "test@test.com"
        user.createdOn = Instant.now()
        user.updatedOn = Instant.now()
        user.roles = setOf(Role(ERole.ROLE_USER), Role(ERole.ROLE_ADMIN))

        `when`(userRepository.findByEmployeeNumber(employeeNumber)).thenReturn(Optional.of(user))
        `when`(userRepository.findByEmployeeNumber(invalidEmployeeNumber)).thenReturn(Optional.empty())
    }

    @Test
    fun loadByUsername_should_return_a_built_userDetailsImpl_with_provided_username() {
        val userDetailsImpl = UserDetailsImpl.build(user)
        val userDetailsImplFromRepository = userDetailsServiceImpl.loadUserByUsername(employeeNumber)

        assertEquals(userDetailsImpl, userDetailsImplFromRepository)
        assertEquals(2, userDetailsImplFromRepository.authorities.size)
    }

    @Test
    fun loadByUsername_throws_Exception_when_user_with_provided_username_is_not_found() {
        assertThrows<UsernameNotFoundException> {
            userDetailsServiceImpl.loadUserByUsername(invalidEmployeeNumber)
        }
    }
}