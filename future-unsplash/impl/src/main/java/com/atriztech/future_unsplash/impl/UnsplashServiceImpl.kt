package com.atriztech.future_unsplash.impl

import com.atriztech.core_network.api.NetworkConnection
import com.atriztech.future_unsplash.api.UnsplashService
import javax.inject.Inject

class UnsplashServiceImpl @Inject constructor(val networkConnection: NetworkConnection): UnsplashService {
    private val url = "https://api.unsplash.com"
    private val apiKey = ""
    private val unsplashServiceApi: UnsplashServiceApi = createUnsplashService()

    fun createUnsplashService(): UnsplashServiceApi{
        return networkConnection.createConnection(url).create(UnsplashServiceApi::class.java)
    }

    override fun randomPhotoAsync(count: Int) = unsplashServiceApi.getRandomPhotosAsync(apiKey, count)
    override fun photoInCollectionAsync(idCollection: Int, page: Int) = unsplashServiceApi.getCollectionPhotoAsync(idCollection, apiKey, page)

    override fun collectionsAsync(page: Int) = unsplashServiceApi.getCollectionsAsync(apiKey, page)
    override fun searchPhotoAsync(query: String, page: Int) = unsplashServiceApi.searchPhotoAsync(apiKey, query, page)
    override fun searchCollectionsAsync(query: String, page: Int) = unsplashServiceApi.searchCollectionsAsync(apiKey, query, page)
    override fun searchPhotosInCollectionsAsync(query: String, collectionId: Int, page: Int) = unsplashServiceApi.searchPhotosInCollectionsAsync(apiKey, query, collectionId, page)
}