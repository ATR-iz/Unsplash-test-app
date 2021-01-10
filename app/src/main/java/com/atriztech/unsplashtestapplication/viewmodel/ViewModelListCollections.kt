package com.atriztech.unsplashtestapplication.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.*
import com.atriztech.unsplashtestapplication.view.collectionlist.PagingSourceCollectionList
import com.atriztech.future_unsplash.api.PhotoCollection
import com.atriztech.future_unsplash.api.UnsplashService
import com.atriztech.unsplashtestapplication.CustomNetwork
import javax.inject.Inject

class ViewModelListCollections @Inject constructor(private val unsplashService: UnsplashService, val customNetwork: CustomNetwork) : ViewModel() {
    var collectionsList: LiveData<PagingData<PhotoCollection>> = MutableLiveData()
    var title = MutableLiveData<String>("")

    fun getCollectionList() {
        collectionsList = Pager(
            config = PagingConfig(
                pageSize = 10,
                prefetchDistance = 10,
                initialLoadSize = 10
            )
        ){
            PagingSourceCollectionList(unsplashService, customNetwork)
        }.liveData
    }

    fun searchCollection(searchString: String) {
        title.postValue(searchString)

        collectionsList = Pager(
            config = PagingConfig(
                pageSize = 10
            )
        ){
            PagingSourceCollectionList(unsplashService, customNetwork, searchString)
        }.liveData
    }
}