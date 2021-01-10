package com.atriztech.future_unsplash.api

import android.os.Parcelable
import com.atriztech.future_unsplash.api.CoverPhoto
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PhotoCollection(
    val cover_photo: CoverPhoto,
    val description: String,
    val id: Int,
    val last_collected_at: String,
    val `private`: Boolean,
    val published_at: String,
    val share_key: String,
    val title: String,
    val total_photos: Int,
    val updated_at: String
): Parcelable