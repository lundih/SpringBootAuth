package com.lundih.authorizationserver.services

import com.lundih.authorizationserver.dtos.request.EditUserRequest
import com.lundih.authorizationserver.entities.Role
import com.lundih.authorizationserver.entities.User
import com.lundih.authorizationserver.enums.ERole
import com.lundih.authorizationserver.exceptions.DuplicateEntryException
import com.lundih.authorizationserver.exceptions.UserNotFoundException
import com.lundih.authorizationserver.mappers.UserMapper
import com.lundih.authorizationserver.repositories.UserRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.kotlin.any
import java.time.Instant
import java.util.*
import kotlin.test.assertNotNull

class UserServiceTest {
    private lateinit var userRepository: UserRepository
    private lateinit var userMapper: UserMapper
    private lateinit var authService: AuthService

    private lateinit var userService: UserService

    private val validEmployeeNumber = "12345"
    private val invalidEmployeeNumber = "000000"
    private val duplicateEmail = "duplicateemail@test.com"
    private lateinit var user: User

    @BeforeEach
    fun setUp() {
        userRepository = mock(UserRepository::class.java)
        userMapper = mock(UserMapper::class.java)
        authService = mock(AuthService::class.java)

        userService = UserService(userRepository, userMapper, authService)

        user = User()
        user.employeeNumber = validEmployeeNumber
        user.firstName = "firstName"
        user.middleName = "middleName"
        user.lastName = "lastName"
        user.idNumber = "12345"
        user.dob = Instant.EPOCH
        user.shouldLogin = false
        user.enabled = true
        user.email = "test@test.com"
        user.createdOn = Instant.now()
        user.updatedOn = Instant.now()
        user.password = "testPassword"
        user.roles = setOf(Role(ERole.ROLE_USER))

        `when`(userRepository.findByEmployeeNumber(validEmployeeNumber)).thenReturn(Optional.of(user))
        `when`(userRepository.findByEmployeeNumber(invalidEmployeeNumber)).thenReturn(Optional.empty())
        `when`(userRepository.existsByEmail(duplicateEmail)).thenReturn(true)
        `when`(userRepository.save(any())).thenReturn(user)
    }

    @Test
    fun throw_exception_if_user_is_not_found_when_editUSer_is_called() {
        assertThrows<UserNotFoundException> {
            userService.editUser(invalidEmployeeNumber, EditUserRequest(null, null, null, null, null))
        }
    }

    @Test
    fun throw_exception_if_a_duplicate_email_is_found_when_editUser_is_called() {
        assertThrows<DuplicateEntryException> {
            userService.editUser(validEmployeeNumber, EditUserRequest(null, null, null, duplicateEmail, null))
        }
    }

    @Test
    fun check_that_edited_values_are_saved_when_editUser_is_called() {
        val response = userService.editUser(validEmployeeNumber,
                EditUserRequest("editedFirstName", null, "editedLastName", "editedEmail@test.com", null))

        verify(userRepository, times(1)).save(any())
        assertNotNull(response)
    }

    @Test
    fun invoke_userRepository_save_method_when_editUser_is_called() {
        val response = userService.editUser(validEmployeeNumber,
                EditUserRequest("editedFirstName", null, "editedLastName", "editedEmail@test.com", null))

        verify(userRepository, times(1)).save(any())
        assertNotNull(response)
    }

    @Test
    fun throw_exception_if_user_is_not_found_when_invalidateUserTokens_is_called() {
        assertThrows<UserNotFoundException> {
            userService.invalidateUserTokens(invalidEmployeeNumber)
        }
    }

    @Test
    fun invoke_userRepository_save_method_when_invalidateUserTokens_is_called() {
        val response = userService.invalidateUserTokens(validEmployeeNumber)

        verify(userRepository, times(1)).save(any())
        assertNotNull(response)
    }

    @Test
    fun throw_exception_if_user_is_not_found_when_lockUserAccount_is_called() {
        assertThrows<UserNotFoundException> {
            userService.lockUserAccount(invalidEmployeeNumber)
        }
    }

    @Test
    fun invoke_userRepository_save_method_when_lockUserAccount_is_called() {
        val response = userService.lockUserAccount(validEmployeeNumber)

        verify(userRepository, times(1)).save(any())
        assertNotNull(response)
    }

    @Test
    fun throw_exception_if_user_is_not_found_when_unlockUserAccount_is_called() {
        assertThrows<UserNotFoundException> {
            userService.unlockUserAccount(invalidEmployeeNumber)
        }
    }

    @Test
    fun invoke_userRepository_save_method_when_unlockUserAccount_is_called() {
        val response = userService.unlockUserAccount(validEmployeeNumber)

        verify(userRepository, times(1)).save(any())
        assertNotNull(response)
    }

    @Test
    fun throw_exception_if_user_is_not_found_when_deleteUser_is_called() {
        assertThrows<UserNotFoundException> {
            userService.deleteUser(invalidEmployeeNumber)
        }
    }

    @Test
    fun invoke_userRepository_delete_method_when_deleteUser_is_called() {
        val response = userService.deleteUser(validEmployeeNumber)

        verify(userRepository, times(1)).delete(any())
        assertNotNull(response)
    }
}