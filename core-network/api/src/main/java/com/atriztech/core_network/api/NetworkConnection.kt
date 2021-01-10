package com.atriztech.core_network.api

import retrofit2.Retrofit

interface NetworkConnection {
    fun createConnection(url: String): Retrofit
}