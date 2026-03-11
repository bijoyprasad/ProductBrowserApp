package com.bijoy.productbrowser.data.api

object ApiEndpoint {
    private const val BASE_URL = "https://dummyjson.com"

    const val products = "$BASE_URL/products"
    const val search = "$BASE_URL/products/search"

    fun productById(id: Int) = "$products/$id"
}