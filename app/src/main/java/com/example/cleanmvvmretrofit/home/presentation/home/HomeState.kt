package com.example.cleanmvvmretrofit.home.presentation.home

import com.example.cleanmvvmretrofit.home.domain.TodosResponse

data class HomeState(
    val isLoading: Boolean = false,
    val todos: List<TodosResponse> = emptyList(),
    val error: String? = null,
    val selectedTodoId: Int? = null
)