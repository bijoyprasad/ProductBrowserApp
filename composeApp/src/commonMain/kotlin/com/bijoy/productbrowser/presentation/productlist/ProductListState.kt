package com.bijoy.productbrowser.presentation.productlist

import com.bijoy.productbrowser.domain.model.Product

data class ProductListState(
    val isLoading: Boolean = false,
    val products: List<Product> = emptyList(),
    val error: String? = null,
    val searchQuery: String = "",
    val isSearchActive: Boolean = false,
    val searchResults: List<Product> = emptyList(),
    val isSearchLoading: Boolean = false,
    val selectedCategory: String? = null
) {
    val availableCategories: List<String>
        get() = products.map { it.category }.distinct().sorted()

    val displayedProducts: List<Product>
        get() {
            val source = if (isSearchActive && searchQuery.isNotBlank()) searchResults else products
            return if (selectedCategory != null) {
                source.filter { it.category == selectedCategory }
            } else {
                source
            }
        }
}