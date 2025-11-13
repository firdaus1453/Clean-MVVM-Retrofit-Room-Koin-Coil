package com.example.cleanmvvmretrofit.home.data

import com.example.cleanmvvmretrofit.core.data.networking.safeCall
import com.example.cleanmvvmretrofit.core.domain.util.DataError
import com.example.cleanmvvmretrofit.core.domain.util.Result
import com.example.cleanmvvmretrofit.home.domain.TodoRepository
import com.example.cleanmvvmretrofit.home.domain.TodosResponse

class TodoRepositoryImpl(
    private val homeApiService: HomeApiService
) : TodoRepository {

    override suspend fun getTodos(): Result<List<TodosResponse>, DataError.Network> {
        return homeApiService.safeCall {
            getTodos()
        }
    }

    override suspend fun getTodoDetail(id: Int): Result<TodosResponse, DataError.Network> {
        return homeApiService.safeCall {
            getTodoDetail(id)
        }
    }
}