package com.bijoy.productbrowser.presentation.productlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bijoy.productbrowser.domain.Resource
import com.bijoy.productbrowser.domain.usecase.GetProductsUseCase
import com.bijoy.productbrowser.domain.usecase.SearchProductsUseCase
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class)
class ProductListViewModel(
    private val getProductsUseCase: GetProductsUseCase,
    private val searchProductsUseCase: SearchProductsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(ProductListState())
    val state: StateFlow<ProductListState> = _state.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    private var searchJob: Job? = null

    init {
        loadProducts()
        observeSearchQuery()
    }

    fun loadProducts() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            when (val result = getProductsUseCase()) {
                is Resource.Success -> _state.update {
                    it.copy(isLoading = false, products = result.data)
                }
                is Resource.Error -> _state.update {
                    it.copy(isLoading = false, error = result.message)
                }
                is Resource.Loading -> Unit
            }
        }
    }

    private fun observeSearchQuery() {
        _searchQuery
            .debounce(400L)
            .distinctUntilChanged()
            .onEach { query ->
                if (query.length >= 2) {
                    searchProducts(query)
                } else if (query.isEmpty()) {
                    _state.update { it.copy(searchResults = emptyList(), isSearchActive = false) }
                }
            }
            .launchIn(viewModelScope)
    }

    fun onSearchQueryChange(query: String) {
        _state.update { it.copy(searchQuery = query, isSearchActive = query.isNotBlank()) }
        _searchQuery.value = query
    }

    fun clearSearch() {
        _state.update {
            it.copy(
                searchQuery = "",
                isSearchActive = false,
                searchResults = emptyList()
            )
        }
        _searchQuery.value = ""
    }

    private fun searchProducts(query: String) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            _state.update { it.copy(isSearchLoading = true) }
            when (val result = searchProductsUseCase(query)) {
                is Resource.Success -> _state.update {
                    it.copy(isSearchLoading = false, searchResults = result.data)
                }
                is Resource.Error -> _state.update {
                    it.copy(isSearchLoading = false, searchResults = emptyList())
                }
                is Resource.Loading -> Unit
            }
        }
    }


    fun selectCategory(category: String) {
        _state.update {
            it.copy(selectedCategory = if (it.selectedCategory == category) null else category)
        }
    }

    fun clearCategory() {
        _state.update { it.copy(selectedCategory = null) }
    }
}