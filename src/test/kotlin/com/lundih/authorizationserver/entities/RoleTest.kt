package com.lundih.authorizationserver.entities

import com.lundih.authorizationserver.enums.ERole
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class RoleTest {
    private val roleId = 1L
    private val roleName = ERole.ROLE_USER

    private val role = Role()

    @BeforeEach
    fun setUp() {
        role.id = 0L
        role.name = ERole.ROLE_ADMIN
    }

    @Test
    fun setId_should_set_role_id() {
        role.id = roleId

        assertEquals(roleId, role.id)
    }

    @Test
    fun setName_should_set_role_name() {
        role.name = roleName

        assertEquals(roleName, role.name)
    }
}