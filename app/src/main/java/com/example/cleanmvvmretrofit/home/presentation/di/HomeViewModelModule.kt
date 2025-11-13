package com.example.cleanmvvmretrofit.home.presentation.di

import com.example.cleanmvvmretrofit.home.presentation.home.HomeViewModel
import com.example.cleanmvvmretrofit.home.presentation.home_detail.HomeDetailViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val homeViewModelModule = module {
    viewModelOf(::HomeViewModel)

    viewModel { parameters ->
        HomeDetailViewModel(
            todoRepository = get(),
            todoId = parameters.get()
        )
    }
}