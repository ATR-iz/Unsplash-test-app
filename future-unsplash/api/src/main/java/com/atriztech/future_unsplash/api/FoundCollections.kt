package com.atriztech.future_unsplash.api

data class FoundCollections(
    val results: List<PhotoCollection>,
    val total: Int,
    val total_pages: Int
)