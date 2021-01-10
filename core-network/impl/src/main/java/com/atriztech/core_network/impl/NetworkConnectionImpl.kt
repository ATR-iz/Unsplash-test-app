package com.atriztech.core_network.impl

import android.content.Context
import com.atriztech.core_network.api.NetworkConnection
import retrofit2.Retrofit
import javax.inject.Inject

class NetworkConnectionImpl @Inject constructor(val context: Context): NetworkConnection {
    override fun createConnection(url: String): Retrofit {
        return DaggerNetworkComponent.builder()
            .networkModule(NetworkModule(context, url))
            .build()
            .retrofit()
    }
}