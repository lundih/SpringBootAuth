package com.lundih.authorizationserver.domain

import com.lundih.authorizationserver.entities.User
import com.lundih.authorizationserver.repositories.UserRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

/**
 * UserDetailsService implementation
 *
 * @author lundih
 * @since 0.0.1
 *
 * @param userRepository [UserRepository] provides the datasource to find users by a parameter from
 */
@Service
class UserDetailsServiceImpl(private val userRepository: UserRepository): UserDetailsService {

    /**
     * Loads a [User] by the provided parameter
     *
     * @param employeeNumber Unique value that is used to fetch a [User] from the database
     * @return [UserDetailsImpl] object generated from a [User] loaded from the repository by the provided parameter
     * else an Exception is thrown
     */
    override fun loadUserByUsername(employeeNumber: String): UserDetailsImpl {
        val user: User = userRepository.findByEmployeeNumber(employeeNumber)
            .orElseThrow { UsernameNotFoundException("User Not Found with username: $employeeNumber") }

        return UserDetailsImpl.build(user)
    }
}