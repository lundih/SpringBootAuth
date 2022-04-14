package com.lundih.authorizationserver.dtos.request

/**
 * Request body for login credentials
 *
 * @author lundih
 * @since 0.0.1
 *
 * @param employeeNumber A unique number that is in the database representing the user
 * @param password User's raw password
 */
data class LoginRequest(val employeeNumber: String, val password: String)
