package com.example.cleanmvvmretrofit.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.cleanmvvmretrofit.core.database.dao.MataPelajaranDao
import com.example.cleanmvvmretrofit.core.database.dao.PesertaDao
import com.example.cleanmvvmretrofit.core.database.dao.SiswaDao
import com.example.cleanmvvmretrofit.core.database.dao.UjianDao
import com.example.cleanmvvmretrofit.core.database.entity.MataPelajaranEntity
import com.example.cleanmvvmretrofit.core.database.entity.PesertaEntity
import com.example.cleanmvvmretrofit.core.database.entity.SiswaEntity
import com.example.cleanmvvmretrofit.core.database.entity.UjianEntity

@Database(
    entities = [
        SiswaEntity::class,
        MataPelajaranEntity::class,
        UjianEntity::class,
        PesertaEntity::class
    ],
    version = 1
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract val siswaDao: SiswaDao
    abstract val mataPelajaranDao: MataPelajaranDao
    abstract val ujianDao: UjianDao
    abstract val pesertaDao: PesertaDao
}