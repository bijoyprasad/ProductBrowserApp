package com.bijoy.productbrowser.presentation.productlist

import com.bijoy.productbrowser.domain.model.Product

data class ProductListState(
    val isLoading: Boolean = false,
    val products: List<Product> = emptyList(),
    val error: String? = null,
    // ── Search ────────────────────────────────────────────────────────────
    val searchQuery: String = "",
    val isSearchActive: Boolean = false,
    val searchResults: List<Product> = emptyList(),
    val isSearchLoading: Boolean = false,
    // ── Category filter (local, no API call) ──────────────────────────────
    val selectedCategory: String? = null   // null → "All"
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