package com.bijoy.productbrowser.presentation.productdetails

import com.bijoy.productbrowser.domain.model.Product


data class ProductDetailState(
    val isLoading: Boolean = false,
    val product: Product? = null,
    val error: String? = null
)