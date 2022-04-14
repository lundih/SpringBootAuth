package com.lundih.authorizationserver.entities

import com.lundih.authorizationserver.enums.ERole
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.Instant
import kotlin.test.assertEquals

class UserTest {
    private val id: Long = 1
    private val employeeNumber = "12345"
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
    private val roles = setOf(Role(ERole.ROLE_USER), Role(ERole.ROLE_ADMIN))

    private val user = User()

    @BeforeEach
    fun setUp() {
        user.id = id
        user.employeeNumber = employeeNumber
        user.firstName = firstName
        user.middleName = middleName
        user.lastName = lastName
        user.password = password
        user.idNumber = idNumber
        user.dob = dob
        user.email = email
        user.shouldLogin = shouldLogin
        user.enabled = enabled
        user.createdOn = createdOn
        user.updatedOn = updatedOn
        user.roles = roles
    }

    @Test
    fun getId_should_return_user_id() {
        assertEquals(id, user.id)
    }

    @Test
    fun getEmployeeNumber_should_return_user_employeeNumber() {
        assertEquals(employeeNumber, user.employeeNumber)
    }

    @Test
    fun getFirstName_should_return_user_firstName() {
        assertEquals(firstName, user.firstName)
    }

    @Test
    fun getMiddleName_should_return_user_middleName() {
        assertEquals(middleName, user.middleName)
    }

    @Test
    fun getLastName_should_return_user_lastName() {
        assertEquals(lastName, user.lastName)
    }

    @Test
    fun getPassword_should_return_user_password() {
        assertEquals(password, user.password)
    }

    @Test
    fun getIdNumber_should_return_user_idNumber() {
        assertEquals(idNumber, user.idNumber)
    }

    @Test
    fun getDob_should_return_user_dob() {
        assertEquals(dob, user.dob)
    }

    @Test
    fun getEmail_should_return_user_email() {
        assertEquals(email, user.email)
    }

    @Test
    fun getShouldLogin_should_return_user_shouldLogin() {
        assertEquals(shouldLogin, user.shouldLogin)
    }

    @Test
    fun getEnabled_should_return_user_enabled() {
        assertEquals(enabled, user.enabled)
    }

    @Test
    fun getCreatedOn_should_return_user_createdOn() {
        assertEquals(createdOn, user.createdOn)
    }

    @Test
    fun getUpdatedOn_should_return_user_updatedOn() {
        assertEquals(updatedOn, user.updatedOn)
    }

    @Test
    fun getRoles_should_return_user_roles() {
        assertEquals(roles, user.roles)
    }
}