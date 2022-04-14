package com.lundih.authorizationserver.utils

import com.lundih.authorizationserver.entities.Role
import com.lundih.authorizationserver.enums.ERole
import org.junit.jupiter.api.Test
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import java.util.Collections
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class ConverterTest {
    private val converter = Converter

    @Test
    fun authorityCollectionsToStringHashSet_should_return_a_hashSet_of_role_strings() {
        val roleUserString = "ROLE_USER"
        val roleAdminString = "ROLE_ADMIN"
        val grantedAuthorities = listOf<GrantedAuthority>(SimpleGrantedAuthority(roleUserString),
                SimpleGrantedAuthority(roleAdminString))

        val emptyRoles = converter.authorityCollectionToStringHashSet(Collections.emptySet())
        val populatedRoles = converter.authorityCollectionToStringHashSet(grantedAuthorities)

        assert(emptyRoles.isEmpty())
        assert(populatedRoles.contains(roleUserString))
        assert(populatedRoles.contains(roleAdminString))
        assertEquals(2, populatedRoles.size)
    }

    @Test
    fun roleHashSetToStringHashSet_should_return_a_hashSet_of_role_strings() {
        val emptyRoleStringSet = converter.roleHashSetToStringHashSet(setOf())
        val populatedRolesStringSet = converter.roleHashSetToStringHashSet(
                setOf(Role(ERole.ROLE_USER), Role(ERole.ROLE_ADMIN)))

        assert(emptyRoleStringSet.isEmpty())
        assert(populatedRolesStringSet.contains(ERole.ROLE_USER.toString()))
        assert(populatedRolesStringSet.contains(ERole.ROLE_ADMIN.toString()))
        assertEquals(2, populatedRolesStringSet.size)
    }

    @Test
    fun converter_constructor_should_instantiate_object() {
        val converter = Converter()

        assertNotNull(converter)
    }
}