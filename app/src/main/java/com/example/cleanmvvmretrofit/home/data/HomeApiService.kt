package com.example.cleanmvvmretrofit.home.data

import com.example.cleanmvvmretrofit.home.domain.TodosResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface HomeApiService {
    @GET("todos")
    suspend fun getTodos(): Response<List<TodosResponse>>

    @GET("todos/{id}")
    suspend fun getTodoDetail(
        @Path("id") id: Int
    ): Response<TodosResponse>
}