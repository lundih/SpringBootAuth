package com.lundih.authorizationserver.domain

import com.lundih.authorizationserver.entities.Role
import com.lundih.authorizationserver.entities.User
import com.lundih.authorizationserver.enums.ERole
import org.junit.jupiter.api.Test
import org.springframework.security.core.authority.SimpleGrantedAuthority
import java.time.Instant
import java.util.*
import kotlin.test.assertEquals

class UserDetailsImplTest {
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

    private val userDetailsImpl = UserDetailsImpl(id,
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

    @Test
    fun getId_should_return_id() {
        assertEquals(id, userDetailsImpl.id)
    }

    @Test
    fun getEmployeeNumber_should_return_employeeNumber() {
        assertEquals(employeeNumber, userDetailsImpl.employeeNumber)
    }

    @Test
    fun getFirstName_should_return_firstName() {
        assertEquals(firstName, userDetailsImpl.firstName)
    }

    @Test
    fun getMiddleName_should_return_user_middleName() {
        assertEquals(middleName, userDetailsImpl.middleName)
    }

    @Test
    fun getLastName_should_return_lastName() {
        assertEquals(lastName, userDetailsImpl.lastName)
    }

    @Test
    fun getPassword_should_return_password() {
        assertEquals(password, userDetailsImpl.password)
    }

    @Test
    fun getIdNumber_should_return_idNumber() {
        assertEquals(idNumber, userDetailsImpl.idNumber)
    }

    @Test
    fun getDob_should_return_dob() {
        assertEquals(dob, userDetailsImpl.dob)
    }

    @Test
    fun getEmail_should_return_email() {
        assertEquals(email, userDetailsImpl.email)
    }

    @Test
    fun getShouldLogin_should_return_shouldLogin() {
        assertEquals(shouldLogin, userDetailsImpl.shouldLogin)
    }

    @Test
    fun getEnabled_should_return_enabled() {
        assertEquals(enabled, userDetailsImpl.enabled)
    }

    @Test
    fun getCreatedOn_should_return_createdOn() {
        assertEquals(createdOn, userDetailsImpl.createdOn)
    }

    @Test
    fun getUpdatedOn_should_return_updatedOn() {
        assertEquals(updatedOn, userDetailsImpl.updatedOn)
    }

    @Test
    fun getAuthorities_should_return_grantedAuthorities() {
        assertEquals(Collections.emptyList(), userDetailsImpl.authorities)
    }

    @Test
    fun getUsername_should_return_employeeNumber() {
        assertEquals(employeeNumber, userDetailsImpl.username)
    }

    @Test
    fun isEnabled_should_return_enabled() {
        assertEquals(enabled, userDetailsImpl.isEnabled)
    }

    @Test
    fun isAccountNonExpiredShould_return_true() {
        assertEquals(true, userDetailsImpl.isAccountNonExpired)
    }

    @Test
    fun isAccountNonLocked_should_return_true() {
        assertEquals(true, userDetailsImpl.isAccountNonLocked)
    }

    @Test
    fun isCredentialsNonExpired() {
        assertEquals(true, userDetailsImpl.isCredentialsNonExpired)
    }

    @Test
    fun build_should_generate_userDetailsImpl_with_grantedAuthorities_from_user_roles() {
        val user = User()
        user.id = id
        user.employeeNumber = employeeNumber
        user.firstName = firstName
        user.middleName = middleName
        user.lastName = lastName
        user.idNumber = idNumber
        user.dob = dob
        user.shouldLogin = shouldLogin
        user.enabled = enabled
        user.email = email
        user.createdOn = createdOn
        user.updatedOn = updatedOn
        user.roles = roles
        user.password = password
        val userDetailsImpl = UserDetailsImpl.build(user)

        assertEquals(id, userDetailsImpl.id)
        assertEquals(employeeNumber, userDetailsImpl.employeeNumber)
        assertEquals(firstName, userDetailsImpl.firstName)
        assertEquals(middleName, userDetailsImpl.middleName)
        assertEquals(lastName, userDetailsImpl.lastName)
        assertEquals(idNumber, userDetailsImpl.idNumber)
        assertEquals(dob, userDetailsImpl.dob)
        assertEquals(shouldLogin, userDetailsImpl.shouldLogin)
        assertEquals(enabled, userDetailsImpl.enabled)
        assertEquals(email, userDetailsImpl.email)
        assertEquals(createdOn, userDetailsImpl.createdOn)
        assertEquals(updatedOn, userDetailsImpl.updatedOn)
        assertEquals(password, userDetailsImpl.password)
        assert(userDetailsImpl.authorities.contains(SimpleGrantedAuthority(ERole.ROLE_USER.name)))
        assert(userDetailsImpl.authorities.contains(SimpleGrantedAuthority(ERole.ROLE_ADMIN.name)))
        assertEquals(2, userDetailsImpl.authorities.size)
    }

    @Test
    fun equals_should_return_true_if_referencing_the_same_item_in_memory() {
        val userDetailsImpl2 = userDetailsImpl

        assertEquals(true, userDetailsImpl.equals(userDetailsImpl2))
    }

    @Test
    fun equals_should_return_false_if_comparing_objects_of_different_classes() {
        assertEquals(false,  userDetailsImpl.equals(User()))
    }

    @Test
    fun equals_should_return_false_if_comparing_to_null() {
        assertEquals(false,  userDetailsImpl.equals(null))
    }

    @Test
    fun equals_should_return_true_if_type_and_id_of_the_objects_are_equal() {
        val userDetailsImpl2 = UserDetailsImpl(id,
                "54321",
                "differentFirstName",
                null,
                "differentLastName",
                "54321",
                Instant.ofEpochMilli(1000),
                !shouldLogin,
                !enabled,
                "differentEmail",
                null,
                null,
                "differentPassword",
                Collections.emptyList())

        assertEquals(true,  userDetailsImpl.equals(userDetailsImpl2))
    }

    @Test
    fun equals_should_return_false_if_id_of_the_objects_are_not_equal() {
        val userDetailsImpl2 = UserDetailsImpl(id + 1,
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

        assertEquals(false,  userDetailsImpl.equals(userDetailsImpl2))
    }

    @Test
    fun hashCode_should_generate_hashCode() {
        assertEquals(46792786, userDetailsImpl.hashCode())
    }
}