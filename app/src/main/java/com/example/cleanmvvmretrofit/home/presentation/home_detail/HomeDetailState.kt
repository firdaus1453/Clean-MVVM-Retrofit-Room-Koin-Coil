package com.example.cleanmvvmretrofit.home.presentation.home_detail

import com.example.cleanmvvmretrofit.home.domain.TodosResponse

data class HomeDetailState(
    val isLoading: Boolean = true,
    val todo: TodosResponse? = null,
    val error: String? = null
)