package com.atriztech.unsplashtestapplication.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.atriztech.future_unsplash.api.Photo
import com.atriztech.future_unsplash.api.UnsplashService
import com.atriztech.unsplashtestapplication.CustomNetwork
import javax.inject.Inject

class ViewModelRandomPhoto @Inject constructor(val unsplashService: UnsplashService, val customNetwork: CustomNetwork) : ViewModel() {
    var randomPhoto = MutableLiveData<List<Photo>>()

    suspend fun getRandomPhoto(){
        try {
            customNetwork.state.postValue(CustomNetwork.NetworkState.Loading)
            randomPhoto.postValue(unsplashService.randomPhotoAsync(16).await())
            customNetwork.state.postValue(CustomNetwork.NetworkState.Loaded)
        } catch (e: Exception){
            customNetwork.checkException(e)
        }
    }
}