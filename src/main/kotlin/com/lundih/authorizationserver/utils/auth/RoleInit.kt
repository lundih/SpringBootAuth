package com.lundih.authorizationserver.utils.auth

import com.lundih.authorizationserver.entities.Role
import com.lundih.authorizationserver.enums.ERole
import com.lundih.authorizationserver.repositories.RoleRepository
import org.springframework.context.ApplicationListener
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.stereotype.Component

/**
 * Populate roles into the database during initialization
 *
 * @author lundih
 * @since 0.0.1
 */
@Component
class RoleInit(private val roleRepository: RoleRepository): ApplicationListener<ContextRefreshedEvent> {

    private var initialized = false

    override fun onApplicationEvent(event: ContextRefreshedEvent) {
        if (initialized) return
        val roles = enumValues<ERole>()
        for (role in roles) if (!roleRepository.existsByName(role)) roleRepository.save(Role(role))
        initialized = true
    }
}