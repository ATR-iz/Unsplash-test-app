package com.atriztech.unsplashtestapplication.view.collectionlist

import androidx.paging.PagingSource
import com.atriztech.future_unsplash.api.PhotoCollection
import com.atriztech.future_unsplash.api.UnsplashService
import com.atriztech.unsplashtestapplication.CustomNetwork

class PagingSourceCollectionList(
    private val unsplashService: UnsplashService,
    private val customNetwork: CustomNetwork,
    private val searchString: String? = null
    ): PagingSource<Int, PhotoCollection>() {

    companion object{
        const val FIRST_COLLECTION_PAGE = 1
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PhotoCollection> {
        return try {
            customNetwork.state.postValue(CustomNetwork.NetworkState.Loading)
            val numPage = params.key ?: FIRST_COLLECTION_PAGE

            val response = if (searchString == null){
                unsplashService.collectionsAsync(numPage).await()
            } else {
                unsplashService.searchCollectionsAsync(searchString, numPage).await().results
            }

            customNetwork.state.postValue(CustomNetwork.NetworkState.Loaded)
            LoadResult.Page(
                data = response,
                prevKey = if (numPage > 1) numPage - 1 else null,
                nextKey = numPage + 1
            )
        } catch (e: Exception){
            customNetwork.checkException(e)
            LoadResult.Error(e)
        }
    }
}