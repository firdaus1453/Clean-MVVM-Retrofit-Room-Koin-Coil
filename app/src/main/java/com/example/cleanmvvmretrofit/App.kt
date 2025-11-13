package com.example.cleanmvvmretrofit

import android.app.Application
import coil.Coil
import coil.ImageLoader
import com.example.cleanmvvmretrofit.core.di.imageModule
import com.example.cleanmvvmretrofit.home.data.di.homeDataModule
import com.example.cleanmvvmretrofit.home.presentation.di.homeViewModelModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber

class App : Application() {

    val applicationScope = CoroutineScope(SupervisorJob())
    private val imageLoader: ImageLoader by inject()

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(
                appModule,
                imageModule,
                homeDataModule,
                homeViewModelModule
            )
        }

        Coil.setImageLoader(imageLoader)
    }
}
