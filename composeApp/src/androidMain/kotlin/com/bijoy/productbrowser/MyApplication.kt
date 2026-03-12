package com.bijoy.productbrowser

import android.app.Application
import com.bijoy.productbrowser.data.di.networkModule
import com.bijoy.productbrowser.data.di.repositoryModule
import com.bijoy.productbrowser.data.di.useCaseModule
import com.bijoy.productbrowser.data.di.viewModelModule
import org.koin.core.context.startKoin

class MyApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            modules(
                networkModule,
                repositoryModule,
                useCaseModule,
                viewModelModule
            )
        }
    }

}