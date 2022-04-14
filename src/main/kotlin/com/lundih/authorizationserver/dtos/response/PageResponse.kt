package com.lundih.authorizationserver.dtos.response

/**
 * Generic response for items that should be returned in pages
 *
 * @param T Type of the items to be returned
 * @param items List of items to be returned
 * @param totalItems Count of items in the database
 */
data class PageResponse<T>(val items: List<T>, val totalItems: Long)