package com.bijoy.productbrowser.data.mapper

import com.bijoy.productbrowser.data.model.DimensionsDto
import com.bijoy.productbrowser.data.model.ProductDto
import com.bijoy.productbrowser.data.model.ReviewDto
import com.bijoy.productbrowser.domain.model.Dimensions
import com.bijoy.productbrowser.domain.model.Product
import com.bijoy.productbrowser.domain.model.Review

fun ProductDto.toProduct(): Product {
    return Product(
        id = id,
        title = title,
        description = description,
        category = category,
        price = price,
        discountPercentage = discountPercentage,
        rating = rating,
        stock = stock,
        tags = tags,
        brand = brand,
        sku = sku,
        weight = weight,
        dimensions = dimensions?.toDimensions(),
        warrantyInformation = warrantyInformation,
        shippingInformation = shippingInformation,
        availabilityStatus = availabilityStatus,
        reviews = reviews.map { it.toReview() },
        returnPolicy = returnPolicy,
        minimumOrderQuantity = minimumOrderQuantity,
        images = images,
        thumbnail = thumbnail
    )
}

fun DimensionsDto.toDimensions(): Dimensions {
    return Dimensions(
        width = width,
        height = height,
        depth = depth
    )
}

fun ReviewDto.toReview(): Review {
    return Review(
        rating = rating,
        comment = comment,
        date = date,
        reviewerName = reviewerName,
        reviewerEmail = reviewerEmail
    )
}