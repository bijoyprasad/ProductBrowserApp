package com.bijoy.productbrowser.domain.usecase

import com.bijoy.productbrowser.domain.Resource
import com.bijoy.productbrowser.domain.model.Product
import com.bijoy.productbrowser.domain.repository.ProductRepository

class GetSingleProductUseCase(
    private val repository: ProductRepository
) {
    suspend operator fun invoke(id: Int): Resource<Product> {
        return when {
            id < 0 -> Resource.Error("Id must be greater than 0")
            else ->
                try {
                    val result = repository.getProductById(id)
                    Resource.Success(result)
                } catch (e: Exception) {
                    Resource.Error(e.message ?: "Unknown error")
                }
        }
    }
}