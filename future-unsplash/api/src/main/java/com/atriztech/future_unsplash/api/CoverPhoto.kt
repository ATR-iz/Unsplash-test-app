package com.atriztech.future_unsplash.api

import android.os.Parcelable
import com.atriztech.future_unsplash.api.Urls
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CoverPhoto(
    val blur_hash: String,
    val color: String,
    val description: String,
    val height: Int,
    val id: String,
    val liked_by_user: Boolean,
    val likes: Int,
    val urls: Urls,
    val width: Int
): Parcelable