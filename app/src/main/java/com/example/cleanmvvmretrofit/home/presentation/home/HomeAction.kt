package com.example.cleanmvvmretrofit.home.presentation.home

sealed interface HomeAction {
    data class OnTodoClick(val todoId: Int) : HomeAction
    data object OnRefresh : HomeAction
}