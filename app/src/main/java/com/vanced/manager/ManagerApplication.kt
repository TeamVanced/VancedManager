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
                apiModule,
                customTabsModule,
                datasourceModule,
                downloaderModule,
                installerModule,
                managerModule,
                networkModule,
                repositoryModule,
                serviceModule,
                viewModelModule,
            )
        }
    }

}