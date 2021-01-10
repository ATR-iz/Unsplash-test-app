package com.atriztech.unsplashtestapplication.di

import android.app.Application
import android.content.Context
import com.atriztech.core_network.api.NetworkConnection
import com.atriztech.core_network.impl.NetworkConnectionImpl
import com.atriztech.future_unsplash.api.UnsplashService
import com.atriztech.future_unsplash.impl.UnsplashServiceImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(private val app: App) {

    @Provides
    @Singleton
    fun provideApplication(): Application {
        return app
    }

    @Provides
    @Singleton
    fun provideContext(): Context {
        return app.applicationContext
    }

    @Provides
    @Singleton
    fun provideNetworkConnection(): NetworkConnection {
        return NetworkConnectionImpl(app.applicationContext)
    }

    @Provides
    @Singleton
    fun provideUnsplash(networkConnection: NetworkConnection): UnsplashService {
        return UnsplashServiceImpl(networkConnection)
    }
}