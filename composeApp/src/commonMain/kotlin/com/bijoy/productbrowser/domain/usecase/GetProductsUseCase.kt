package com.bijoy.productbrowser.domain.usecase

import com.bijoy.productbrowser.domain.Resource
import com.bijoy.productbrowser.domain.model.Product
import com.bijoy.productbrowser.domain.repository.ProductRepository

class GetProductsUseCase(
    private val repository: ProductRepository
) {
    suspend operator fun invoke(limit: Int = 30, skip: Int = 0): Resource<List<Product>> {
        return when {
            limit <= 0 -> Resource.Error("Limit must be greater than 0")
            else ->
                try {
                    val result = repository.getAllProducts(limit, skip)
                    Resource.Success(result)
                } catch (e: Exception) {
                    Resource.Error(e.message ?: "Unknown error")
                }
        }
    }
}