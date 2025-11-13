package com.example.cleanmvvmretrofit.home.presentation.home_detail

sealed interface HomeDetailEvent {
    data object NavigateBack : HomeDetailEvent
    data class Error(val message: String) : HomeDetailEvent
}