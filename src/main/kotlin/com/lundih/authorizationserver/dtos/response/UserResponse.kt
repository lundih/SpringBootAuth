package com.lundih.authorizationserver.dtos.response

import java.time.Instant

/**
 * Request body for login credentials
 *
 * @author lundih
 * @since 0.0.1
 *
 * @property id Unique auto-generated value
 * @property employeeNumber Unique user provided value that can be used to identify users
 * @property firstName User's first name
 * @property middleName User's middle name (not required)
 * @property lastName User's last name
 * @property idNumber User's national identity card number
 * @property dob User's date of birth (ISO date)
 * @property email User's email address
 * @property createdOn Auto-generated user creation timestamp
 * @property updatedOn Auto-generated update timestamp
 * @property roles Roles the user is assigned.
 */
class UserResponse {
    var id: Long? = null

    var employeeNumber: String? = null

    var firstName: String? = null

    var middleName: String? = null

    var lastName: String? = null

    var idNumber: String? = null

    var dob: Instant? = null

    var email: String? = null

    var roles: Set<String> = HashSet()

    var createdOn: Instant? = null

    var updatedOn: Instant? = null
}