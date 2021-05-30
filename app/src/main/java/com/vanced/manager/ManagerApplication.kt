package com.vanced.manager

import android.app.Application
import com.vanced.manager.di.*
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class ManagerApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@ManagerApplication)

            modules(
                mapperModule,
                viewModelModule,
                serviceModule,
                repositoryModule,
                packageManagerModule,
                networkModule,
                downloaderModule
            )
        }
    }

}