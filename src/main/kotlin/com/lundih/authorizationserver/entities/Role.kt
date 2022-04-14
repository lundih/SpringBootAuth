package com.lundih.authorizationserver.entities

import com.lundih.authorizationserver.enums.ERole
import javax.persistence.*

/**
 * Entity that represents a role that a user can be assigned
 *
 * The table contains all the roles that the system recognises
 *
 * @author lundih
 * @since 0.0.1
 *
 * @property id Unique auto-generated value
 * @property name Is an enum [ERole] to ensure role names are standard
 */
@Entity
@Table(name = "roles")
class Role() {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long? = null

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    var name: ERole? = null

    constructor(name: ERole?) : this() {
        this.name = name
    }
}