package com.lundih.authorizationserver.dtos.request

/**
 * Request body for editing a [User][com.lundih.authorizationserver.entities.User]
 *
 * @author lundih
 * @since 0.0.1
 *
 * @param firstName User's first name
 * @param middleName User's middle name
 * @param lastName User's last name
 * @param email User's email address. Should not already be in the database
 * @param roles Users roles. These are later mapped to the corresponding roles that the system understands
 */
data class EditUserRequest(val firstName: String?,
                           val middleName: String?,
                           val lastName: String?,
                           val email: String?,
                           val roles: List<String>?)