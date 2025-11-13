package com.example.cleanmvvmretrofit.home.data.di

import com.example.cleanmvvmretrofit.core.data.networking.HttpClientFactory
import com.example.cleanmvvmretrofit.home.data.HomeApiService
import com.example.cleanmvvmretrofit.home.data.TodoRepositoryImpl
import com.example.cleanmvvmretrofit.home.domain.TodoRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val homeDataModule = module {
    single<HomeApiService> {
        get<HttpClientFactory>().build().create(HomeApiService::class.java)
    }

    singleOf(::TodoRepositoryImpl).bind<TodoRepository>()
}