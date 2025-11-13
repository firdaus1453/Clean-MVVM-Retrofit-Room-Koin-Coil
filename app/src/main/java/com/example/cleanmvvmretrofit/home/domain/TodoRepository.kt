package com.example.cleanmvvmretrofit.home.domain

import com.example.cleanmvvmretrofit.core.domain.util.DataError
import com.example.cleanmvvmretrofit.core.domain.util.Result

interface TodoRepository {
    suspend fun getTodos(): Result<List<TodosResponse>, DataError.Network>
    suspend fun getTodoDetail(id: Int): Result<TodosResponse, DataError.Network>
}