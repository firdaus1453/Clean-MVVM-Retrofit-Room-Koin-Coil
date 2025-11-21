package com.example.cleanmvvmretrofit.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "mata_pelajaran")
data class MataPelajaranEntity(
    @PrimaryKey(autoGenerate = true) val idMatpel: Int = 0,
    val namaMatpel: String
)