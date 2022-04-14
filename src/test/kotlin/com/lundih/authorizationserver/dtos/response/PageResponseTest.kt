package com.lundih.authorizationserver.dtos.response

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class PageResponseTest {

    @Test
    fun pageResponse_constructor_should_populate_pageResponse_properties() {
        val userResponse = UserResponse()
        val itemCount = 10L
        val pageResponse = PageResponse(listOf(userResponse), itemCount)

        assert(pageResponse.items.contains(userResponse))
        assertEquals(itemCount, pageResponse.totalItems)
    }

    @Test
    fun getItems_should_return_items() {
        val userResponse = UserResponse()
        val pageResponse = PageResponse(listOf(userResponse), 20L)

        assertEquals(pageResponse.items[0], userResponse)
    }

    @Test
    fun getTotalItems_should_return_totalItems() {
        val itemCount = 30L
        val pageResponse = PageResponse(listOf(UserResponse()), itemCount)

        assertEquals(pageResponse.totalItems, itemCount)
    }
}