package com.atriztech.unsplashtestapplication.di

import com.atriztech.unsplashtestapplication.view.FragmentSearch
import com.atriztech.unsplashtestapplication.MainActivity
import com.atriztech.unsplashtestapplication.view.*
import com.atriztech.unsplashtestapplication.view.collectionlist.FragmentCollectionList
import com.atriztech.unsplashtestapplication.view.photolist.FragmentPhotoList
import dagger.Component
import dagger.android.AndroidInjector
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent : AndroidInjector<App> {
    fun inject(mainActivity: MainActivity)
    fun inject(fragmentPhotoList: FragmentPhotoList)
    fun inject(fragmentCollectionList: FragmentCollectionList)
    fun inject(fragmentPhotoView: FragmentPhotoView)
    fun inject(fragmentRandomPhoto: FragmentRandomPhoto)
    fun inject(fragmentSearch: FragmentSearch)

    object Initializer {
        fun init(app: App): AppComponent {
            return DaggerAppComponent.builder()
                .appModule(AppModule(app))
                .build()
        }
    }
}