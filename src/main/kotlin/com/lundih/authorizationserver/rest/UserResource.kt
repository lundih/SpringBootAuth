package com.lundih.authorizationserver.rest

import com.lundih.authorizationserver.dtos.request.EditUserRequest
import com.lundih.authorizationserver.dtos.response.PageResponse
import com.lundih.authorizationserver.dtos.response.UserResponse
import com.lundih.authorizationserver.services.UserService
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

/**
 * Rest controller to handle requests that involve users
 *
 * @author lundih
 * @since 0.0.1
 */
@Tag(name = "User resource", description = "Endpoints for managing users")
@RestController
@RequestMapping("/api/v1/users")
class UserResource(private val userService: UserService) {

    /**
     * Endpoint for fetching all users
     *
     * @param pageNumber Page number of items to be retrieved
     * @param pageSize Number of items to be retrieved per page
     * @return A [PageResponse][com.lundih.authorizationserver.dtos.response.PageResponse] of users
     */
    @GetMapping("/all")
    fun getUsers(@Validated @RequestParam pageNumber: Int, @Validated @RequestParam pageSize: Int): PageResponse<UserResponse> {
        return userService.getUsers(pageNumber, pageSize)
    }

    /**
     * Endpoint for editing a user
     *
     * @param employeeNumber ID of the user whose details are to be edited
     * @param editUserRequest [EditUserRequest][com.lundih.authorizationserver.dtos.request.EditUserRequest]
     * with details to be edited
     * @return A map with key of type String and value of type String confirming that the user has been edited
     */
    @PatchMapping("{employeeNumber}")
    fun editUser(@Validated @PathVariable employeeNumber: String,
                 @Validated @RequestBody editUserRequest: EditUserRequest, ): Map<String, String> {
        return userService.editUser(employeeNumber, editUserRequest)
    }

    /**
     * Endpoint for invalidating a user's tokens
     *
     * @param employeeNumber ID of the user who is required to log in
     * @return A map with key of type String and value of type String confirming that the user will be required to log in
     */
    @PostMapping("invalidate-tokens/{employeeNumber}")
    fun invalidateUserTokens(@Validated @PathVariable employeeNumber: String): Map<String, String> {
        return userService.invalidateUserTokens(employeeNumber)
    }

    /**
     * Endpoint for disabling a user's account
     *
     * @param employeeNumber ID of the user whose account is to be locked
     * @return A map with key of type String and value of type String confirming that the user's account is disabled
     */
    @PostMapping("lock-account/{employeeNumber}")
    fun lockUserAccount(@Validated @PathVariable employeeNumber: String): Map<String, String> {
        return userService.lockUserAccount(employeeNumber)
    }

    /**
     * Endpoint for enabling a user's account
     *
     * @param employeeNumber ID of the user whose account is to be unlocked
     * @return A map with key of type String and value of type String confirming that the user's account is enabled
     */
    @PostMapping("unlock-account/{employeeNumber}")
    fun unlockUserAccount(@Validated @PathVariable employeeNumber: String): Map<String, String> {
        return userService.unlockUserAccount(employeeNumber)
    }

    /**
     * Endpoint for deleting a user
     *
     * @param employeeNumber ID of the user who is to be deleted
     * @return A map with key of type String and value of type String confirming that the user is deleted
     */
    @DeleteMapping("{employeeNumber}")
    fun deleteUser(@Validated @PathVariable employeeNumber: String): Map<String, String> {
        return userService.deleteUser(employeeNumber)
    }
}