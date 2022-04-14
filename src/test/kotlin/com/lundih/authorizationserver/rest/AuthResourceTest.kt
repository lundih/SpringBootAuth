package com.lundih.authorizationserver.rest

import com.fasterxml.jackson.databind.ObjectMapper
import com.lundih.authorizationserver.dtos.request.LoginRequest
import com.lundih.authorizationserver.dtos.request.RefreshTokenRequest
import com.lundih.authorizationserver.dtos.request.RegisterUserRequest
import com.lundih.authorizationserver.services.AuthService
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.mockito.kotlin.any
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.MvcResult
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.time.Instant
import kotlin.test.assertNotNull

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class AuthResourceTest @Autowired constructor(private val mockMvc: MockMvc, private val objectMapper: ObjectMapper) {

    @MockBean
    private lateinit var authService: AuthService

    @Test
    fun login_endpoint_should_invoke_authenticateUser_in_service() {
        val mvcResult: MvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(this.objectMapper.writeValueAsBytes(LoginRequest("12345", "password"))))
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andReturn()

        verify(authService, times(1)).authenticateUser(any())
        assertNotNull(mvcResult.response)
    }

    @Test
    fun refresh_token_endpoint_should_invoke_renewAccessToken_in_service() {
        val mvcResult: MvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/refresh-token")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(this.objectMapper.writeValueAsBytes(RefreshTokenRequest("testRefreshToken"))))
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andReturn()

        verify(authService, times(1)).renewAccessToken(any())
        assertNotNull(mvcResult.response)
    }

    @Test
    fun register_user_endpoint_should_invoke_registerUser_in_service() {
        val mvcResult: MvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/register-user")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(this.objectMapper.writeValueAsBytes(RegisterUserRequest("12345",
                            "firstName",
                            null,
                            "lastName",
                            "12345",
                            Instant.EPOCH,
                            "test@test.com",
                            null))))
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andReturn()

        verify(authService, times(1)).registerUser(any())
        assertNotNull(mvcResult.response)
    }
}