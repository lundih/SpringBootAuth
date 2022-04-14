package com.lundih.authorizationserver.rest

import com.fasterxml.jackson.databind.ObjectMapper
import com.lundih.authorizationserver.dtos.request.EditUserRequest
import com.lundih.authorizationserver.services.UserService
import org.junit.jupiter.api.Test
import org.mockito.Mockito
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
import kotlin.test.assertNotNull

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class UserResourceTest @Autowired constructor(private val mockMvc: MockMvc, private val objectMapper: ObjectMapper) {

    @MockBean
    private lateinit var userService: UserService

    @Test
    fun get_all_users_endpoint_should_invoke_getUsers_in_service() {
        val mvcResult: MvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users/all")
                    .param("pageNumber", "0")
                    .param("pageSize", "20"))
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andReturn()

        Mockito.verify(userService, Mockito.times(1)).getUsers(any(), any())
        assertNotNull(mvcResult.response)
    }

    @Test
    fun edit_user_endpoint_should_invoke_editUser_in_service() {
        val mvcResult: MvcResult = mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/users/12345")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsBytes(EditUserRequest("firstName",
                        null,
                        "lastName",
                        "test@test.com",
                        null))))
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andReturn()

        Mockito.verify(userService, Mockito.times(1)).editUser(any(), any())
        assertNotNull(mvcResult.response)
    }

    @Test
    fun invalidate_user_tokens_endpoint_should_invoke_invalidateUserTokens_in_service() {
        val mvcResult: MvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/users/invalidate-tokens/12345"))
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andReturn()

        Mockito.verify(userService, Mockito.times(1)).invalidateUserTokens(any())
        assertNotNull(mvcResult.response)
    }

    @Test
    fun lock_user_account_endpoint_should_invoke_lockUserAccount_in_service() {
        val mvcResult: MvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/users/lock-account/12345"))
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andReturn()

        Mockito.verify(userService, Mockito.times(1)).lockUserAccount(any())
        assertNotNull(mvcResult.response)
    }

    @Test
    fun unlock_user_account_endpoint_should_invoke_unlockUserAccount_in_service() {
        val mvcResult: MvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/users/unlock-account/12345"))
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andReturn()

        Mockito.verify(userService, Mockito.times(1)).unlockUserAccount(any())
        assertNotNull(mvcResult.response)
    }

    @Test
    fun delete_user_endpoint_should_invoke_deleteUser_in_service() {
        val mvcResult: MvcResult = mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/users/12345"))
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andReturn()

        Mockito.verify(userService, Mockito.times(1)).deleteUser(any())
        assertNotNull(mvcResult.response)
    }
}