package com.example.cleanmvvmretrofit

import android.content.SharedPreferences
import com.example.cleanmvvmretrofit.core.data.networking.HttpClientFactory
import kotlinx.coroutines.CoroutineScope
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val appModule = module {

    single<CoroutineScope> {
        (androidApplication() as App).applicationScope
    }

    singleOf(::HttpClientFactory)

    // Provide shared OkHttpClient
    single {
        get<HttpClientFactory>().buildOkHttpClient()
    }

//    viewModelOf(::MainViewModel)
}
