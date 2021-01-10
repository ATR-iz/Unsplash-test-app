package com.atriztech.future_unsplash.api

import kotlinx.coroutines.Deferred

interface UnsplashService {
    fun randomPhotoAsync(count: Int): Deferred<List<Photo>>
    fun photoInCollectionAsync(idCollection: Int, page: Int): Deferred<List<Photo>>
    fun collectionsAsync(page: Int): Deferred<List<PhotoCollection>>
    fun searchPhotoAsync(query: String, page: Int): Deferred<FoundPhotos>
    fun searchCollectionsAsync(query: String, page: Int): Deferred<FoundCollections>
    fun searchPhotosInCollectionsAsync(query: String, collectionId: Int, page: Int): Deferred<FoundPhotos>
}