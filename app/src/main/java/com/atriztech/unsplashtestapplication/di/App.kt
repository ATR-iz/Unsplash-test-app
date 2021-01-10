package com.atriztech.unsplashtestapplication.di

import android.app.Application
import com.atriztech.unsplashtestapplication.di.AppComponent.Initializer.init

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        app = this
        buildComponentGraph()
    }

    companion object {
        private lateinit var appComponent: AppComponent
        private lateinit var app: App

        fun component(): AppComponent {
            return appComponent
        }

        fun buildComponentGraph() {
            appComponent = init(app)
        }
    }
}