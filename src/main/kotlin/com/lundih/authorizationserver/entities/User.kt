package com.lundih.authorizationserver.entities

import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.Instant
import javax.persistence.*

/**
 * Entity that represents a person whose details can be captured
 *
 * @author lundih
 * @since 0.0.1
 *
 * @property id Unique auto-generated value
 * @property employeeNumber Unique user provided value that can be used to identify users. Crucial in the authentication process
 * @property firstName User's first name
 * @property middleName User's middle name (not required)
 * @property lastName User's last name
 * @property password User's hashed password
 * @property idNumber User's national identity card number
 * @property dob User's date of birth (ISO date)
 * @property email User's email address. Should be unique
 * @property shouldLogin Shows if a user's token to should be considered valid, or if they should be required to log in
 * @property enabled Shows if a user is enabled
 * @property createdOn Auto-generated user creation timestamp
 * @property updatedOn Auto-generated update timestamp
 * @property roles Roles the user is assigned. Made from joining [User] and [Role]. Can be considered a 'UserRole'
 */
@Entity
@Table(name = "users",
    uniqueConstraints = [
        UniqueConstraint(columnNames = ["employee_number", "email"])
    ])
class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long? = null

    @Column(name = "employee_number")
    var employeeNumber: String? = null

    @Column(name = "first_name")
    var firstName: String? = null

    @Column(name = "middle_name")
    var middleName: String? = null

    @Column(name = "last_name")
    var lastName: String? = null

    var password: String? = null

    @Column(name = "id_number")
    var idNumber: String? = null

    @Column(name = "dob")
    var dob: Instant? = null

    var email: String? = null

    @Column(name = "should_login")
    var shouldLogin: Boolean = false

    var enabled: Boolean = true

    @Column(name="created_on")
    @CreationTimestamp
    var createdOn: Instant? = null

    @Column(name = "updated_on")
    @UpdateTimestamp
    var updatedOn: Instant? = null

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",
        joinColumns = [JoinColumn(name = "user_id")],
        inverseJoinColumns = [JoinColumn(name = "role_id")])
    var roles: Set<Role> = HashSet()
}