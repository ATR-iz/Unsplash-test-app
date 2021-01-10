package com.atriztech.unsplashtestapplication

import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingSource
import retrofit2.HttpException
import java.net.UnknownHostException

abstract class CustomPagingSource <Key : Any, Value: Any>: PagingSource<Key, Value>() {
    open val networkState = MutableLiveData<CustomNetwork.NetworkState>()

    suspend fun networkResponse(getData: suspend () -> LoadResult<Key, Value>): LoadResult<Key, Value> {
        try {
            return getData()
        } catch (e: HttpException) {
            when (e.code()) {
                401 -> networkState.postValue((CustomNetwork.NetworkState.LoadError.Error401))
                402 -> networkState.postValue((CustomNetwork.NetworkState.LoadError.Error402))
                403 -> networkState.postValue(CustomNetwork.NetworkState.LoadError.Error403)
                404 -> networkState.postValue((CustomNetwork.NetworkState.LoadError.Error404))
                else -> networkState.postValue(CustomNetwork.NetworkState.LoadError.Error(e.message.toString()))
            }
            return LoadResult.Error(e)
        } catch (e: UnknownHostException) {
            networkState.postValue(CustomNetwork.NetworkState.LoadError.Error("No internet connection"))
            return LoadResult.Error(e)
        } catch (e: Exception) {
            networkState.postValue(CustomNetwork.NetworkState.LoadError.Error(e.message.toString()))
            return LoadResult.Error(e)
        }
    }
}