package com.example.cleanmvvmretrofit.core.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "peserta",
    primaryKeys = ["idUjian", "nis"],
    foreignKeys = [
        ForeignKey(
            entity = UjianEntity::class,
            parentColumns = ["idUjian"],
            childColumns = ["idUjian"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = SiswaEntity::class,
            parentColumns = ["nis"],
            childColumns = ["nis"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("idUjian"), Index("nis")]
)
data class PesertaEntity(
    val idUjian: Int,
    val nis: String,
    val nilai: Int,
    val isLulus: Boolean
)