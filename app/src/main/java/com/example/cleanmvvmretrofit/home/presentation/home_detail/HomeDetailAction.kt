package com.example.cleanmvvmretrofit.home.presentation.home_detail

sealed interface HomeDetailAction {
    data object OnBackClick : HomeDetailAction
}