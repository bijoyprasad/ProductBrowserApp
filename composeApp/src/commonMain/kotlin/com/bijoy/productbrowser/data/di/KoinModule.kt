package com.bijoy.productbrowser.data.di

import com.bijoy.productbrowser.data.api.apiClient
import com.bijoy.productbrowser.data.repository.ProductRepositoryImpl
import com.bijoy.productbrowser.domain.repository.ProductRepository
import com.bijoy.productbrowser.domain.usecase.GetProductsUseCase
import com.bijoy.productbrowser.domain.usecase.GetSingleProductUseCase
import com.bijoy.productbrowser.domain.usecase.SearchProductsUseCase
import com.bijoy.productbrowser.presentation.productdetails.ProductDetailViewModel
import com.bijoy.productbrowser.presentation.productlist.ProductListViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val networkModule = module {
    single { apiClient }
}

val repositoryModule = module {
    single<ProductRepository> { ProductRepositoryImpl() }
}

val useCaseModule = module {
    singleOf(::GetProductsUseCase)
    singleOf(::SearchProductsUseCase)
    singleOf(::GetSingleProductUseCase)
}

val viewModelModule = module {
    viewModelOf(::ProductListViewModel)
    viewModelOf(::ProductDetailViewModel)
}

