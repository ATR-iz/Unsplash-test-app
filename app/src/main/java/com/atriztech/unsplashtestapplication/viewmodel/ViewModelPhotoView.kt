package com.atriztech.unsplashtestapplication.viewmodel

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.atriztech.future_unsplash.api.Photo
import javax.inject.Inject

class ViewModelPhotoView @Inject constructor() : ViewModel() {
    var photo = ObservableField<Photo>()
}