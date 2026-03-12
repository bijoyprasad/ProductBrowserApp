package com.bijoy.productbrowser.data.model

import kotlinx.serialization.Serializable

@Serializable
data class ProductsResponseDto(
    val products: List<ProductDto>,
    val total: Int,
    val skip: Int,
    val limit: Int
)