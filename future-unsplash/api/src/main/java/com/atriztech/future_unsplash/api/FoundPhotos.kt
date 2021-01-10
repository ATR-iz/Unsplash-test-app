package com.atriztech.future_unsplash.api

data class FoundPhotos(
    val results: List<Photo>,
    val total: Int,
    val total_pages: Int
)