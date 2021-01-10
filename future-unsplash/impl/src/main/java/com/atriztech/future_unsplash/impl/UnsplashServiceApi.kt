package com.atriztech.future_unsplash.impl

import com.atriztech.future_unsplash.api.FoundCollections
import com.atriztech.future_unsplash.api.FoundPhotos
import com.atriztech.future_unsplash.api.Photo
import com.atriztech.future_unsplash.api.PhotoCollection
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface UnsplashServiceApi {

    @GET("photos/random")
    fun getRandomPhotosAsync(
        @Query("client_id") apiKey: String,
        @Query("count") count: Int
    ): Deferred<List<Photo>>

    @GET("collections")
    fun getCollectionsAsync(
        @Query("client_id") apiKey: String,
        @Query("page") page: Int
    ): Deferred<List<PhotoCollection>>

    @GET("collections/{id}/photos")
    fun getCollectionPhotoAsync(
        @Path ("id") idCollection: Int,
        @Query("client_id") apiKey: String,
        @Query("page") page: Int
    ): Deferred<List<Photo>>

    @GET("search/photos/")
    fun searchPhotoAsync(
        @Query("client_id") apiKey: String,
        @Query("query") query: String,
        @Query("page") page: Int
    ): Deferred<FoundPhotos>

    @GET("search/collections/")
    fun searchCollectionsAsync(
        @Query("client_id") apiKey: String,
        @Query("query") query: String,
        @Query("page") page: Int
    ): Deferred<FoundCollections>

    @GET("search/photos/")
    fun searchPhotosInCollectionsAsync(
        @Query("client_id") apiKey: String,
        @Query("query") query: String,
        @Query("collections") collectionId: Int,
        @Query("page") page: Int
    ): Deferred<FoundPhotos>
}