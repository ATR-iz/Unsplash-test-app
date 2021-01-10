package com.atriztech.unsplashtestapplication.viewmodel

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.atriztech.unsplashtestapplication.view.photolist.PagingSourcePhotoList
import com.atriztech.future_unsplash.api.Photo
import com.atriztech.future_unsplash.api.PhotoCollection
import com.atriztech.future_unsplash.api.UnsplashService
import com.atriztech.unsplashtestapplication.CustomNetwork
import com.atriztech.unsplashtestapplication.view.photolist.FragmentPhotoList
import javax.inject.Inject

class ViewModelListPhoto @Inject constructor(val unsplashService: UnsplashService, val customNetwork: CustomNetwork) : ViewModel() {
    var listPhoto: LiveData<PagingData<Photo>> = MutableLiveData()
    var title = MutableLiveData<String>("")
    lateinit var state: State

    private lateinit var photoCollection: PhotoCollection
    private lateinit var searchString: String

    fun setData(bundle: Bundle){
        val type = bundle.getString(FragmentPhotoList.KEY_TYPE) as String
        when (type){
            FragmentPhotoList.KEY_TYPE_SEARCH_PHOTO -> state = State.SearchPhoto
            FragmentPhotoList.KEY_TYPE_COLLECTION_PHOTO -> state = State.CollectionPhoto
            FragmentPhotoList.KEY_TYPE_SEARCH_PHOTO_IN_COLLECTION -> state = State.SearchPhotoInCollection
        }

        when(state){
            is State.CollectionPhoto -> {
                photoCollection = bundle.getParcelable<PhotoCollection>(FragmentPhotoList.KEY_COLLECTION) as PhotoCollection
                getPhotoFromCollection(photoCollection)
            }
            is State.SearchPhoto -> {
                searchString = bundle.getString(FragmentPhotoList.KEY_SEARCH_STRING) as String
                findPhoto(searchString)
            }
            is State.SearchPhotoInCollection -> {
                searchString = bundle.getString(FragmentPhotoList.KEY_SEARCH_STRING) as String
                photoCollection = bundle.getParcelable<PhotoCollection>(FragmentPhotoList.KEY_COLLECTION) as PhotoCollection
                findPhotoFromCollection(photoCollection, searchString)
            }
        }
    }

    fun updateData(){
        when(state){
            is State.CollectionPhoto -> {
                getPhotoFromCollection(photoCollection)
            }
            is State.SearchPhoto -> {
                findPhoto(searchString)
            }
            is State.SearchPhotoInCollection -> {
                findPhotoFromCollection(photoCollection, searchString)
            }
        }
    }

    private fun getPhotoFromCollection(photoCollection: PhotoCollection){
        title.postValue(photoCollection.title)

        listPhoto = Pager(
            config = PagingConfig(
                pageSize = 10,
                prefetchDistance = 10,
                initialLoadSize = 10
            )
        ){
            PagingSourcePhotoList(unsplashService = unsplashService, customNetwork = customNetwork, idCollection = photoCollection.id)
        }.liveData
    }

    fun findPhoto(searchString: String){
        title.postValue(searchString)

        listPhoto = Pager(
            config = PagingConfig(
                pageSize = 10,
                prefetchDistance = 10,
                initialLoadSize = 10
            )
        ){
            PagingSourcePhotoList(unsplashService = unsplashService, customNetwork = customNetwork, searchString = searchString)
        }.liveData
    }

    private fun findPhotoFromCollection(photoCollection: PhotoCollection, searchString: String){
        title.postValue(searchString)

        listPhoto = Pager(
            config = PagingConfig(
                pageSize = 10,
                prefetchDistance = 10,
                initialLoadSize = 10
            )
        ){
            PagingSourcePhotoList(unsplashService = unsplashService, customNetwork = customNetwork, idCollection = photoCollection.id, searchString = searchString)
        }.liveData
    }

    sealed class State{
        object CollectionPhoto: State()
        object SearchPhoto: State()
        object SearchPhotoInCollection: State()
    }
}