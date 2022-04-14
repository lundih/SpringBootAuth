package com.lundih.authorizationserver.mappers

import com.lundih.authorizationserver.domain.UserDetailsImpl
import com.lundih.authorizationserver.dtos.request.RegisterUserRequest
import com.lundih.authorizationserver.dtos.response.UserResponse
import com.lundih.authorizationserver.entities.User
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings

/**
 * Mapper for converting between [User] and its DTOs
 *
 * @author lundih
 * @since 0.0.1
 */
@Mapper(componentModel = "spring")
interface UserMapper {
    @Mappings(Mapping(target = "id", ignore = true),
        Mapping(target = "password", ignore = true),
        Mapping(target = "shouldLogin", ignore = true),
        Mapping(target = "enabled", ignore = true),
        Mapping(target = "roles", ignore = true),
        Mapping(target = "createdOn", ignore = true),
        Mapping(target = "updatedOn", ignore = true))
    fun registerUserRequestToUser(registerUserRequest: RegisterUserRequest): User

    @Mapping(target = "roles", expression = "java(com.lundih.authorizationserver.utils.Converter.Companion.authorityCollectionToStringHashSet(userDetailsImpl.getAuthorities()))")
    fun userDetailsImplToResponse(userDetailsImpl: UserDetailsImpl): UserResponse

    @Mapping(target = "roles", expression = "java(com.lundih.authorizationserver.utils.Converter.Companion.roleHashSetToStringHashSet(user.getRoles()))")
    fun userToResponse(user: User): UserResponse

    fun userListToResponse(users: List<User>): List<UserResponse>
}