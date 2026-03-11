package com.bijoy.productbrowser.data.model

import kotlinx.serialization.Serializable

@Serializable
data class ProductDto(
    val id: Int,
    val title: String,
    val description: String,
    val category: String,
    val price: Double,
    val discountPercentage: Double,
    val rating: Double,
    val stock: Int,
    val tags: List<String> = emptyList(),
    val brand: String? = null,
    val sku: String = "",
    val weight: Double = 0.0,
    val dimensions: DimensionsDto? = null,
    val warrantyInformation: String = "",
    val shippingInformation: String = "",
    val availabilityStatus: String = "",
    val reviews: List<ReviewDto> = emptyList(),
    val returnPolicy: String = "",
    val minimumOrderQuantity: Int = 1,
    val images: List<String> = emptyList(),
    val thumbnail: String = ""
)

@Serializable
data class DimensionsDto(
    val width: Double,
    val height: Double,
    val depth: Double
)

@Serializable
data class ReviewDto(
    val rating: Int,
    val comment: String,
    val date: String,
    val reviewerName: String,
    val reviewerEmail: String
)