package com.lundih.authorizationserver.repositories

import com.lundih.authorizationserver.entities.User
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository
import java.util.Optional

/**
 * Repository for [User]s
 *
 * @author lundih
 * @since 0.0.1
 */
@Repository
interface UserRepository: PagingAndSortingRepository<User, Long> {
    fun findBy(pageable: Pageable): Page<User>

    fun findByEmployeeNumber(username: String): Optional<User>

    fun existsByEmployeeNumber(employeeNumber: String): Boolean

    fun existsByIdNumber(idNumber: String): Boolean

    fun existsByEmail(email: String): Boolean
}