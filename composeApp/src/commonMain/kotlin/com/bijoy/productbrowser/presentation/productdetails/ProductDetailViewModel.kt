package com.bijoy.productbrowser.presentation.productdetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bijoy.productbrowser.domain.Resource
import com.bijoy.productbrowser.domain.usecase.GetSingleProductUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProductDetailViewModel(
    private val getSingleProductUseCase: GetSingleProductUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(ProductDetailState())
    val state: StateFlow<ProductDetailState> = _state.asStateFlow()

    fun loadProduct(id: Int) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            when (val result = getSingleProductUseCase(id)) {
                is Resource.Success -> _state.update {
                    it.copy(isLoading = false, product = result.data)
                }
                is Resource.Error -> _state.update {
                    it.copy(isLoading = false, error = result.message)
                }
                is Resource.Loading -> Unit
            }
        }
    }
}