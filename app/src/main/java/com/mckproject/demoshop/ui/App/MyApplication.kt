package com.mckproject.demoshop.ui.App

import android.app.Application
import com.mckproject.demoshop.ui.di.AppModule
import org.koin.core.context.startKoin

class MyApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            modules(AppModule)
        }
    }

}