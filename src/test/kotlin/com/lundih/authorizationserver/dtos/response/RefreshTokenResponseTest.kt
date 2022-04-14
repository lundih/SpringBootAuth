package com.lundih.authorizationserver.dtos.response

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class RefreshTokenResponseTest {
    private val tokenType = "Bearer"

    private val refreshTokenResponse = RefreshTokenResponse("testToken", "testRefreshToken", tokenType)

    @Test
    fun getType_should_return_token_type() {
        assertEquals(tokenType, refreshTokenResponse.type)
    }
}