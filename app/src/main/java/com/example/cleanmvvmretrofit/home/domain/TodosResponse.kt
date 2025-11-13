package com.example.cleanmvvmretrofit.home.domain

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TodosResponse(
    @SerialName("completed")
    val completed: Boolean = false,
    @SerialName("id")
    val id: Int = 0,
    @SerialName("title")
    val title: String = "",
    @SerialName("userId")
    val userId: Int = 0
)



