package com.atriztech.unsplashtestapplication.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.atriztech.unsplashtestapplication.viewmodel.ViewModelPhotoView
import com.atriztech.unsplashtestapplication.R
import com.atriztech.unsplashtestapplication.databinding.FragmentPhotoViewBinding
import com.atriztech.future_unsplash.api.Photo
import com.atriztech.unsplashtestapplication.CustomFragment
import com.squareup.picasso.Picasso

class FragmentPhotoView  : CustomFragment() {

    companion object{
        const val KEY_PHOTO = "photo"

        fun bundleFor(photo: Photo): Bundle {
            val bundle = Bundle()
            bundle.putParcelable(KEY_PHOTO, photo)
            return bundle
        }
    }

    private lateinit var binding: FragmentPhotoViewBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setupActionBar(isHideActionBar = true)

        val photo = requireArguments().getParcelable<Photo>(KEY_PHOTO) as Photo

        val viewModelPhotoView = ViewModelPhotoView()
        viewModelPhotoView.photo.set(photo)

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_photo_view, container, false)
        binding.viewModel = viewModelPhotoView

        Picasso.get()
            .load(photo.urls.regular)
            .into(binding.photo)

        return binding.root
    }
}