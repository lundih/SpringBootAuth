package com.lundih.authorizationserver.dtos.response

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.Instant
import kotlin.test.assertEquals

class UserResponseTest {
    private val id: Long = 1
    private val employeeNumber = "12345"
    private val firstName = "firstName"
    private val middleName = "middleName"
    private val lastName = "lastName"
    private val idNumber ="idNumber"
    private val dob = Instant.EPOCH
    private val email = "test@test.com"
    private val createdOn = Instant.now()
    private val updatedOn = Instant.now()
    private val roles = setOf("ROLE_USER")

    private val newId: Long = 2
    private val newEmployeeNumber = "54321"
    private val newFirstName = "newFirstName"
    private val newMiddleName = "newMiddleName"
    private val newLastName = "newLastName"
    private val newIdNumber ="newIdNumber"
    private val newDob = Instant.ofEpochMilli(1000)
    private val newEmail = "newtest@test.com"
    private val newCreatedOn = Instant.now()
    private val newUpdatedOn = Instant.now()
    private val newRoles = setOf("ROLE_ADMIN")

    private val userResponse = UserResponse()

    @BeforeEach
    fun setUp() {
        userResponse.id = id
        userResponse.employeeNumber = employeeNumber
        userResponse.firstName = firstName
        userResponse.middleName = middleName
        userResponse.lastName = lastName
        userResponse.idNumber = idNumber
        userResponse.dob = dob
        userResponse.email = email
        userResponse.createdOn = createdOn
        userResponse.updatedOn = updatedOn
        userResponse.roles = roles
    }

    @Test
    fun getId_should_return_user_id() {
        assertEquals(id, userResponse.id)
    }

    @Test
    fun getEmployeeNumber_should_return_user_employeeNumber() {
        assertEquals(employeeNumber, userResponse.employeeNumber)
    }

    @Test
    fun getFirstName_should_return_user_firstName() {
        assertEquals(firstName, userResponse.firstName)
    }

    @Test
    fun getMiddleName_should_return_user_middleName() {
        assertEquals(middleName, userResponse.middleName)
    }

    @Test
    fun getLastName_should_return_user_lastName() {
        assertEquals(lastName, userResponse.lastName)
    }

    @Test
    fun getIdNumber_should_return_user_idNumber() {
        assertEquals(idNumber, userResponse.idNumber)
    }

    @Test
    fun getDob_should_return_user_dob() {
        assertEquals(dob, userResponse.dob)
    }

    @Test
    fun getEmail_should_return_user_email() {
        assertEquals(email, userResponse.email)
    }

    @Test
    fun getCreatedOn_should_return_user_createdOn() {
        assertEquals(createdOn, userResponse.createdOn)
    }

    @Test
    fun getUpdatedOn_should_return_user_updatedOn() {
        assertEquals(updatedOn, userResponse.updatedOn)
    }

    @Test
    fun getRoles_should_return_user_roles() {
        assertEquals(roles, userResponse.roles)
    }

    @Test
    fun setId_should_set_user_id() {
        userResponse.id = newId

        assertEquals(newId, userResponse.id)
    }

    @Test
    fun setEmployeeNumber_should_set_user_employeeNumber() {
        userResponse.employeeNumber = newEmployeeNumber

        assertEquals(newEmployeeNumber, userResponse.employeeNumber)
    }

    @Test
    fun setFirstName_should_set_user_firstName() {
        userResponse.firstName = newFirstName

        assertEquals(newFirstName, userResponse.firstName)
    }

    @Test
    fun setMiddleName_should_set_user_middleName() {
        userResponse.middleName = newMiddleName

        assertEquals(newMiddleName, userResponse.middleName)
    }

    @Test
    fun setLastName_should_set_user_lastName() {
        userResponse.lastName = newLastName

        assertEquals(newLastName, userResponse.lastName)
    }

    @Test
    fun setIdNumber_should_set_user_idNumber() {
        userResponse.idNumber = newIdNumber

        assertEquals(newIdNumber, userResponse.idNumber)
    }

    @Test
    fun setDob_should_set_user_dob() {
        userResponse.dob = newDob

        assertEquals(newDob, userResponse.dob)
    }

    @Test
    fun setEmail_should_set_user_email() {
        userResponse.email = newEmail

        assertEquals(newEmail, userResponse.email)
    }

    @Test
    fun setCreatedOn_should_set_user_createdOn() {
        userResponse.createdOn = newCreatedOn

        assertEquals(newCreatedOn, userResponse.createdOn)
    }

    @Test
    fun setUpdatedOn_should_set_user_updatedOn() {
        userResponse.updatedOn = newUpdatedOn

        assertEquals(newUpdatedOn, userResponse.updatedOn)
    }

    @Test
    fun setRoles_should_set_user_roles() {
        userResponse.roles = newRoles

        assertEquals(newRoles, userResponse.roles)
    }
}