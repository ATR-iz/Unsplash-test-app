package com.atriztech.unsplashtestapplication.view.photolist

import androidx.paging.PagingSource
import com.atriztech.future_unsplash.api.Photo
import com.atriztech.future_unsplash.api.UnsplashService
import com.atriztech.unsplashtestapplication.CustomNetwork

class PagingSourcePhotoList(
    private val unsplashService: UnsplashService,
    private val customNetwork: CustomNetwork,
    private val idCollection: Int? = null,
    private val searchString: String? = null
    ): PagingSource<Int, Photo>() {

    companion object{
        const val FIRST_PHOTO_PAGE = 1
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Photo> {
        return try {
            customNetwork.state.postValue(CustomNetwork.NetworkState.Loading)
            val numPage = params.key ?: FIRST_PHOTO_PAGE

            val response = if (idCollection != null && searchString == null){
                unsplashService.photoInCollectionAsync(idCollection, numPage).await()
            }
            else if (idCollection == null && searchString != null){
                unsplashService.searchPhotoAsync(searchString, numPage).await().results
            }
            else {
                unsplashService.searchPhotosInCollectionsAsync(searchString!!, idCollection!!, numPage).await().results
            }

            customNetwork.state.postValue(CustomNetwork.NetworkState.Loaded)
            LoadResult.Page(
                data = response,
                prevKey = if (numPage > 1) numPage - 1 else null,
                nextKey = if (response.size != 0) numPage + 1 else null
            )
        } catch (e: Exception){
            customNetwork.checkException(e)
            LoadResult.Error(e)
        }
    }
}