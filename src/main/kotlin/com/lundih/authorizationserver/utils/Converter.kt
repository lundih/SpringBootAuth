package com.lundih.authorizationserver.utils

import com.lundih.authorizationserver.entities.Role
import org.springframework.security.core.GrantedAuthority
import java.util.stream.Collectors

/**
 * Utility class for conversions. Primarily for mapper expressions
 *
 * @author lundih
 * @since 0.0.1
 */
class Converter {
    companion object {
        /**
         *  Converts a Collection of type GrantedAuthority to a Set of type String
         *
         *  @param authorities Collection of type GrantedAuthority
         *  @return Hashset of type String
         */
        fun authorityCollectionToStringHashSet(authorities: Collection<GrantedAuthority>): Set<String> {
            val roles: List<String> = authorities.stream()
                .map { grantedAuthority -> grantedAuthority.toString() }
                .collect(Collectors.toList())

            return HashSet(roles)
        }

        /**
         * Converts a Set of type Role to a Set Of type String
         *
         * @param roles Set of type Role
         * @return Set of type String
         */
        fun roleHashSetToStringHashSet(roles: Set<Role>): Set<String> {
            return roles.stream()
                .map { role -> role.name.toString() }
                .collect(Collectors.toSet())
        }
    }
}