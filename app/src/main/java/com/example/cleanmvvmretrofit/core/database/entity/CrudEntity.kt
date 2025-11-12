package com.example.cleanmvvmretrofit.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "crud")
data class CrudEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val types: String,
    val imageUrl: String,
    val category: String
)