package com.bijoy.productbrowser.domain.usecase

import com.bijoy.productbrowser.domain.Resource
import com.bijoy.productbrowser.domain.model.Product
import com.bijoy.productbrowser.domain.repository.ProductRepository

class SearchProductsUseCase(
    private val repository: ProductRepository
) {
    suspend operator fun invoke(query: String): Resource<List<Product>> {
        return when {
            query.isBlank() -> Resource.Error("Search query cannot be empty")
            query.length < 2 -> Resource.Error("Search query must be at least 2 characters")
            else ->
                try {
                    val result = repository.searchProducts(query)
                    Resource.Success(result)
                } catch (e: Exception) {
                    Resource.Error(e.message ?: "Unknown error")
                }
        }
    }
}