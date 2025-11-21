package com.example.cleanmvvmretrofit.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "siswa")
data class SiswaEntity(
    @PrimaryKey val nis: String,
    val nama: String,
    val alamat: String
)