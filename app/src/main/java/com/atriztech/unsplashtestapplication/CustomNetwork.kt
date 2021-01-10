package com.atriztech.unsplashtestapplication

import androidx.lifecycle.MutableLiveData
import retrofit2.HttpException
import java.net.UnknownHostException
import javax.inject.Inject

class CustomNetwork @Inject constructor(){
    val state = MutableLiveData<NetworkState>()

    fun checkException(e: Exception){
        when(e){
            is HttpException ->{
                when (e.code()) {
                    401 -> state.postValue((NetworkState.LoadError.Error401))
                    402 -> state.postValue((NetworkState.LoadError.Error402))
                    403 -> state.postValue(NetworkState.LoadError.Error403)
                    404 -> state.postValue((NetworkState.LoadError.Error404))
                    else -> state.postValue(NetworkState.LoadError.Error(e.message.toString()))
                }
            }
            is UnknownHostException -> state.postValue(NetworkState.LoadError.Error("No internet connection"))
            else -> state.postValue(NetworkState.LoadError.Error(e.message.toString()))
        }
    }

    sealed class NetworkState{
        object Loading: NetworkState()
        object Loaded: NetworkState()

        sealed class LoadError(val msg: String) : NetworkState(){
            object Error401: LoadError("The access token is invalid")
            object Error402: LoadError("Error 402")
            object Error403: LoadError("Rate Limit Exceeded")
            object Error404: LoadError("Error 404")
            class Error(msg: String): LoadError(msg)
        }
    }
}
