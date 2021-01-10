package com.atriztech.unsplashtestapplication

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.HttpException
import java.net.UnknownHostException

open class CustomViewModel: ViewModel() {
    var networkState = MutableLiveData<CustomNetwork.NetworkState>()

    suspend fun networkResponse(getData: suspend () -> Unit) {
        if (networkState.value !is CustomNetwork.NetworkState.Loading) {
            try {
                getData()
            } catch (e: HttpException) {
                when (e.code()) {
                    401 -> networkState.postValue((CustomNetwork.NetworkState.LoadError.Error401))
                    402 -> networkState.postValue((CustomNetwork.NetworkState.LoadError.Error402))
                    403 -> networkState.postValue(CustomNetwork.NetworkState.LoadError.Error403)
                    404 -> networkState.postValue((CustomNetwork.NetworkState.LoadError.Error404))
                    else -> networkState.postValue(CustomNetwork.NetworkState.LoadError.Error(e.message.toString()))
                }
            } catch (e: UnknownHostException) {
                networkState.postValue(CustomNetwork.NetworkState.LoadError.Error("No internet connection"))
            } catch (e: Exception) {
                networkState.postValue(CustomNetwork.NetworkState.LoadError.Error(e.message.toString()))
            }
        }
    }
}