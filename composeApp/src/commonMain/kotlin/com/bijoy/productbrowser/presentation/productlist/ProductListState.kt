package com.bijoy.productbrowser.presentation.productlist

import com.bijoy.productbrowser.domain.model.Product

data class ProductListState(
    val isLoading: Boolean = false,
    val products: List<Product> = emptyList(),
    val error: String? = null,
    val searchQuery: String = "",
    val isSearchActive: Boolean = false,
    val searchResults: List<Product> = emptyList(),
    val isSearchLoading: Boolean = false
) {
    val displayedProducts: List<Product>
        get() = if (isSearchActive && searchQuery.isNotBlank()) searchResults else products
}