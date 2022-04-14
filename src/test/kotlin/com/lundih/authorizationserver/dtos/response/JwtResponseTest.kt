package com.lundih.authorizationserver.dtos.response

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class JwtResponseTest {
    private val tokenType = "Bearer"
    private val tokenUser = UserResponse()

    private val jwtResponse = JwtResponse("testToken", "testRefreshToken", tokenType, tokenUser)

    @Test
    fun getType_should_return_token_type() {
        assertEquals(tokenType, jwtResponse.type)
    }

    @Test
    fun getUser_should_return_token_user() {
        assertEquals(tokenUser, jwtResponse.user)
    }
}