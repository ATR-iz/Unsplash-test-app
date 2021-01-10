package com.atriztech.core_utils

import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ContextModule(var context: Context) {

    @Provides
    @Singleton
    fun providesContext(): Context {
        return context
    }
}