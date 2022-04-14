package com.lundih.authorizationserver.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import com.lundih.authorizationserver.entities.Role
import com.lundih.authorizationserver.entities.User
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.time.Instant
import java.util.stream.Collectors

/**
 * UserDetails implementation
 *
 * @author lundih
 * @since 0.0.1
 *
 * @param id Unique auto-generated value
 * @param employeeNumber Unique user provided value that can be used to identify users. Crucial in the authentication process
 * @param firstName User's first name
 * @param middleName User's middle name (not required)
 * @param lastName User's last name
 * @param password User's hashed password
 * @param idNumber User's national identity card number
 * @param dob User's date of birth (ISO date)
 * @param email User's email address. Should be unique
 * @param shouldLogin Shows if a user's token to should be considered valid, or if they should be required to log in
 * @param enabled Shows if a user is enabled
 * @param createdOn Auto-generated user creation timestamp
 * @param updatedOn Auto-generated update timestamp
 * @param grantedAuthorities Roles the user is assigned
 */
class UserDetailsImpl(val id: Long,
                      val employeeNumber: String,
                      val firstName: String,
                      val middleName: String?,
                      val lastName: String,
                      val idNumber: String,
                      val dob: Instant,
                      val shouldLogin: Boolean,
                      val enabled: Boolean,
                      val email: String,
                      val createdOn: Instant?,
                      val updatedOn: Instant?,
                      @field:JsonIgnore private val password: String,
                      private val grantedAuthorities: Collection<GrantedAuthority>) : UserDetails {

    override fun getAuthorities(): Collection<GrantedAuthority> {
        return grantedAuthorities
    }

    override fun getPassword(): String {
        return password
    }

    override fun getUsername(): String {
        return employeeNumber
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return enabled
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val user = other as UserDetailsImpl

        return id == user.id
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + username.hashCode()

        return result
    }

    companion object {
        /**
         * Accepts a [User] object to return a UserDetailsImpl object
         * that extends UserDetails
         *
         * Converts [User]'s [Role]s into GrantedAuthorities and
         * creates a new UserDetailsImpl object with populated GrantedAuthorities
         *
         * @param user [User] to be
         * @return UserDetailsImpl object with populated GrantedAuthorities
         */
        fun build(user: User): UserDetailsImpl {
            val authorities: List<GrantedAuthority> = user.roles.stream()
                .map { role -> SimpleGrantedAuthority(role.name?.name) }
                .collect(Collectors.toList())

            return UserDetailsImpl(user.id!!,
                user.employeeNumber!!,
                user.firstName!!,
                user.middleName!!,
                user.lastName!!,
                user.idNumber!!,
                user.dob!!,
                user.shouldLogin,
                user.enabled,
                user.email!!,
                user.createdOn,
                user.updatedOn,
                user.password!!,
                authorities)
        }
    }
}