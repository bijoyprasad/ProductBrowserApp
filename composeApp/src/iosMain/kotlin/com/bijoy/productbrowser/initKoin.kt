package com.bijoy.productbrowser

import com.bijoy.productbrowser.data.di.networkModule
import com.bijoy.productbrowser.data.di.repositoryModule
import com.bijoy.productbrowser.data.di.useCaseModule
import org.koin.core.context.startKoin

fun initKoin() {
    startKoin {
        modules(
            networkModule,
            repositoryModule,
            useCaseModule
        )
    }
}