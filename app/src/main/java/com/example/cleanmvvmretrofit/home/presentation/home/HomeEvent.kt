package com.example.cleanmvvmretrofit.home.presentation.home

sealed interface HomeEvent {
    data class Error(val message: String) : HomeEvent
}