package com.lundih.authorizationserver.repositories

import com.lundih.authorizationserver.entities.Role
import com.lundih.authorizationserver.enums.ERole
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

/**
 * Repository for [Role]s
 *
 * @author lundih
 * @since 0.0.1
 */
@Repository
interface RoleRepository: JpaRepository<Role, Long> {
    fun findByName(name: ERole): Optional<Role>

    fun existsByName(name: ERole): Boolean
}