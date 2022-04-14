package com.lundih.authorizationserver.dtos.request

import java.time.Instant

/**
 * Request body for registering a new [User][com.lundih.authorizationserver.entities.User]
 *
 * @author lundih
 * @since 0.0.1
 *
 * @param employeeNumber Unique identifier provided for the user
 * @param firstName User's first name
 * @param middleName User's middle name
 * @param lastName User's last name
 * @param idNumber User's identity card number. Should not already be in the database
 * @param dob User's date of birth
 * @param email User's email address. Should not already be in the database
 * @param roles Users roles. These are later mapped to the corresponding roles that the system understands
 */
data class RegisterUserRequest(val employeeNumber: String,
                               val firstName: String,
                               val middleName: String?,
                               val lastName: String,
                               val idNumber: String,
                               val dob: Instant,
                               val email: String,
                               val roles: List<String>?)
