package com.bijoy.productbrowser.data.repository

import com.bijoy.productbrowser.data.api.ApiEndpoint
import com.bijoy.productbrowser.data.api.apiClient
import com.bijoy.productbrowser.data.mapper.toProduct
import com.bijoy.productbrowser.data.model.ProductDto
import com.bijoy.productbrowser.data.model.ProductsResponseDto
import com.bijoy.productbrowser.domain.model.Product
import com.bijoy.productbrowser.domain.repository.ProductRepository
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class ProductRepositoryImpl : ProductRepository {

    override suspend fun getAllProducts(limit: Int, skip: Int): List<Product> {
        val response: ProductsResponseDto = apiClient
            .get(ApiEndpoint.products) {
                parameter("limit", limit)
                parameter("skip", skip)
            }.body()

        return response.products.map { it.toProduct() }
    }

    override suspend fun getProductById(id: Int): Product {
        val response: ProductDto = apiClient
            .get(ApiEndpoint.productById(id))
            .body()

        return response.toProduct()
    }

    override suspend fun searchProducts(query: String): List<Product> {
        val response: ProductsResponseDto = apiClient
            .get(ApiEndpoint.search) {
                parameter("q", query)
            }.body()

        return response.products.map { it.toProduct() }
    }
}