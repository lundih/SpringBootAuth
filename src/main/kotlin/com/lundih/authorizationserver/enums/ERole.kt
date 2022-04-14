package com.lundih.authorizationserver.enums

/**
 * Used to ensure all roles are standard
 *
 * When the service is initialised, the enums in this class are used by
 * [RoleInit][com.lundih.authorizationserver.utils.auth.RoleInit] to populate the database with all possible
 * [Roles][com.lundih.authorizationserver.entities.Role] to allow users to have the list to choose from
 *
 * @author lundih
 * @since 0.0.1
 */
enum class ERole {
    ROLE_USER,
    ROLE_ADMIN
}