package com.atriztech.future_unsplash.api

import android.os.Parcelable
import com.atriztech.future_unsplash.api.Urls
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Photo(
    val alt_description: String,
    val blur_hash: String,
    val color: String,
    val created_at: String,
    val description: String,
    val downloads: Int,
    val height: Int,
    val id: String,
    val liked_by_user: Boolean,
    val likes: Int,
    val promoted_at: String,
    val updated_at: String,
    val urls: Urls,
    val views: Int,
    val width: Int
): Parcelable