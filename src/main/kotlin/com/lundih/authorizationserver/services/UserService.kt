package com.lundih.authorizationserver.services

import com.lundih.authorizationserver.dtos.request.EditUserRequest
import com.lundih.authorizationserver.dtos.response.PageResponse
import com.lundih.authorizationserver.dtos.response.UserResponse
import com.lundih.authorizationserver.exceptions.DuplicateEntryException
import com.lundih.authorizationserver.exceptions.UserNotFoundException
import com.lundih.authorizationserver.mappers.UserMapper
import com.lundih.authorizationserver.repositories.UserRepository
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Service

/**
 * Service to handle requests that deal with users
 *
 * @author lundih
 * @since 0.0.1
 *
 * @param userRepository [UserRepository] gets [Users][com.lundih.authorizationserver.entities.User] from the database
 * @param userMapper [UserMapper] maps between [User][com.lundih.authorizationserver.entities.User] and its DTOs
 */
@Service
class UserService(private val userRepository: UserRepository,
                  private val userMapper: UserMapper,
                  private val authService: AuthService) {
    /**
     * Gets all users that fall under the 'jurisdiction' of the person making the request
     *
     * @return Paginated list of users and the total count of users
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    fun getUsers(pageNumber: Int, pageSize: Int): PageResponse<UserResponse> {
        val page = userRepository.findBy(PageRequest.of(pageNumber, pageSize, Sort.by("employeeNumber")))

        return PageResponse(userMapper.userListToResponse(page.content), page.totalElements)
    }

    /**
     * Edits a user
     *
     * @param employeeNumber ID of the user whose details are to be edited
     * @param editUserRequest [EditUserRequest][com.lundih.authorizationserver.dtos.request.EditUserRequest]
     * with details to be edited
     * @return A map with key of type String and value of type String confirming that the user is deleted
     * @throws [UserNotFoundException][com.lundih.authorizationserver.exceptions.UserNotFoundException]
     * if the user could not be found
     * @throws [DuplicateEntryException] if the email is found in the database
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    fun editUser(employeeNumber: String, editUserRequest: EditUserRequest): Map<String, String> {
        val optionalUser = userRepository.findByEmployeeNumber(employeeNumber)
        if (optionalUser.isEmpty) throw UserNotFoundException(employeeNumber)
        val user = optionalUser.get()
        if (editUserRequest.firstName != null && editUserRequest.firstName != "")
            user.firstName = editUserRequest.firstName
        if (editUserRequest.middleName != null && editUserRequest.middleName != "")
            user.middleName = editUserRequest.middleName
        if (editUserRequest.lastName != null && editUserRequest.lastName != "")
            user.lastName = editUserRequest.lastName
        if (editUserRequest.email != null && editUserRequest.email != "") {
            if (userRepository.existsByEmail(editUserRequest.email))
                throw DuplicateEntryException("Email Address already in use")
            user.email = editUserRequest.email
        }
        if (editUserRequest.roles != null)
            user.roles = authService.userProvidedRolesToSystemRoles(editUserRequest.roles)
        userRepository.save(user)

        return mapOf("message" to "User edited successfully")
    }

    /**
     * Invalidates a user's tokens
     *
     * @param employeeNumber ID of the user whose tokens are to be invalidated
     * @return A map with key of type String and value of type String confirming that the user will be required to log in
     * @throws [UserNotFoundException][com.lundih.authorizationserver.exceptions.UserNotFoundException]
     * if the user could not be found
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    fun invalidateUserTokens(employeeNumber: String): Map<String, String> {
        val optionalUser = userRepository.findByEmployeeNumber(employeeNumber)
        if (optionalUser.isEmpty) throw UserNotFoundException(employeeNumber)
        val user = optionalUser.get()
        user.shouldLogin = true
        userRepository.save(user)

        return mapOf("message" to "User will be required to log in")
    }

    /**
     * Disables a user's account
     *
     * @param employeeNumber ID of the user whose account is to be disabled
     * @return A map with key of type String and value of type String confirming that the user's account is disabled
     * @throws [UserNotFoundException][com.lundih.authorizationserver.exceptions.UserNotFoundException]
     * if the user could not be found
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    fun lockUserAccount(employeeNumber: String): Map<String, String> {
        val optionalUser = userRepository.findByEmployeeNumber(employeeNumber)
        if (optionalUser.isEmpty) throw UserNotFoundException(employeeNumber)
        val user = optionalUser.get()
        user.enabled = false
        userRepository.save(user)

        return mapOf("message" to "User account locked")
    }

    /**
     * Enables a user's account
     *
     * @param employeeNumber ID of the user whose account is to be enabled
     * @return A map with key of type String and value of type String confirming that the user's account is enabled
     * @throws [UserNotFoundException][com.lundih.authorizationserver.exceptions.UserNotFoundException]
     * if the user could not be found
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    fun unlockUserAccount(employeeNumber: String): Map<String, String> {
        val optionalUser = userRepository.findByEmployeeNumber(employeeNumber)
        if (optionalUser.isEmpty) throw UserNotFoundException(employeeNumber)
        val user = optionalUser.get()
        user.enabled = true
        userRepository.save(user)

        return mapOf("message" to "User account unlocked")
    }

    /**
     * Deletes a user
     *
     * @param employeeNumber ID of the user to be deleted
     * @return A map with key of type String and value of type String confirming that the user is deleted
     * @throws [UserNotFoundException][com.lundih.authorizationserver.exceptions.UserNotFoundException]
     * if the user could not be found
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    fun deleteUser(employeeNumber: String): Map<String, String> {
        val optionalUser = userRepository.findByEmployeeNumber(employeeNumber)
        if (optionalUser.isEmpty) throw UserNotFoundException(employeeNumber)
        val user = optionalUser.get()
        userRepository.delete(user)

        return mapOf("message" to "User has been deleted")
    }
}