package com.example.cleanmvvmretrofit.core.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(
    tableName = "ujian",
    foreignKeys = [
        ForeignKey(
            entity = MataPelajaranEntity::class,
            parentColumns = ["idMatpel"],
            childColumns = ["idMatpel"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("idMatpel")]
)
data class UjianEntity(
    @PrimaryKey(autoGenerate = true) val idUjian: Int = 0,
    val namaUjian: String,
    val idMatpel: Int,
    val tanggal: LocalDateTime
)