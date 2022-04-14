package com.lundih.authorizationserver.mappers

import com.lundih.authorizationserver.domain.UserDetailsImpl
import com.lundih.authorizationserver.dtos.request.RegisterUserRequest
import com.lundih.authorizationserver.entities.Role
import com.lundih.authorizationserver.entities.User
import com.lundih.authorizationserver.enums.ERole
import org.junit.jupiter.api.Test
import org.mapstruct.factory.Mappers
import org.springframework.security.core.authority.SimpleGrantedAuthority
import java.time.Instant
import kotlin.test.assertEquals

class UserMapperTest {
    private val userMapper = Mappers.getMapper(UserMapper::class.java)

    @Test
    fun map_registerUserRequest_to_user() {
        val registerUserRequest = RegisterUserRequest("12345",
                "firstName",
                null,
                "lastName",
                "12345",
                Instant.EPOCH,
                "test@test.com",
                null)
        val user = userMapper.registerUserRequestToUser(registerUserRequest)

        assertEquals(registerUserRequest.employeeNumber, user.employeeNumber)
        assertEquals(registerUserRequest.firstName, user.firstName)
        assertEquals(registerUserRequest.middleName, user.middleName)
        assertEquals(registerUserRequest.lastName, user.lastName)
        assertEquals(registerUserRequest.idNumber, user.idNumber)
        assertEquals(registerUserRequest.dob, user.dob)
        assertEquals(registerUserRequest.email, user.email)
    }

    @Test
    fun map_userDetailsImpl_to_response() {
        val userDetailsImpl = UserDetailsImpl(1L,
                "12345",
                "firstName",
                null,
                "lastName",
                "12345",
                Instant.EPOCH,
                shouldLogin = false,
                enabled = true,
                "test@test.com",
                Instant.now(),
                Instant.now(),
                "password",
                listOf(SimpleGrantedAuthority(ERole.ROLE_USER.name), SimpleGrantedAuthority(ERole.ROLE_ADMIN.name)))
        val userResponse = userMapper.userDetailsImplToResponse(userDetailsImpl)

        assertEquals(userDetailsImpl.id, userResponse.id)
        assertEquals(userDetailsImpl.employeeNumber, userResponse.employeeNumber)
        assertEquals(userDetailsImpl.firstName, userResponse.firstName)
        assertEquals(userDetailsImpl.middleName, userResponse.middleName)
        assertEquals(userDetailsImpl.lastName, userResponse.lastName)
        assertEquals(userDetailsImpl.idNumber, userResponse.idNumber)
        assertEquals(userDetailsImpl.dob, userResponse.dob)
        assertEquals(userDetailsImpl.email, userResponse.email)
        assertEquals(userDetailsImpl.createdOn, userResponse.createdOn)
        assertEquals(userDetailsImpl.updatedOn, userResponse.updatedOn)
        assert(userResponse.roles.contains(ERole.ROLE_USER.name))
        assert(userResponse.roles.contains(ERole.ROLE_ADMIN.name))
    }

    @Test
    fun map_user_to_response() {
        val user = User()
        user.id = 1L
        user.employeeNumber = "12345"
        user.firstName = "firstName"
        user.middleName = null
        user.lastName = "lastName"
        user.idNumber = "12345"
        user.dob = Instant.EPOCH
        user.email = "test@test.com"
        user.createdOn = Instant.now()
        user.updatedOn = Instant.now()
        user.roles = setOf(Role(ERole.ROLE_USER), Role(ERole.ROLE_ADMIN))
        val userResponse = userMapper.userToResponse(user)

        assertEquals(user.id, userResponse.id)
        assertEquals(user.employeeNumber, userResponse.employeeNumber)
        assertEquals(user.firstName, userResponse.firstName)
        assertEquals(user.middleName, userResponse.middleName)
        assertEquals(user.lastName, userResponse.lastName)
        assertEquals(user.idNumber, userResponse.idNumber)
        assertEquals(user.dob, userResponse.dob)
        assertEquals(user.email, userResponse.email)
        assertEquals(user.createdOn, userResponse.createdOn)
        assertEquals(user.updatedOn, userResponse.updatedOn)
        assert(userResponse.roles.contains(ERole.ROLE_USER.name))
        assert(userResponse.roles.contains(ERole.ROLE_ADMIN.name))
    }

    @Test
    fun map_userList_to_response() {
        val id = 1L
        val user = User()
        user.id = id
        val userList = listOf(User(), User(), user, User())
        val userListResponse = userMapper.userListToResponse(userList)

        assertEquals(userList.size, userListResponse.size)
        assertEquals(id, userListResponse[2].id)
    }
}