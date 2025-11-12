package com.example.cleanmvvmretrofit.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.cleanmvvmretrofit.core.database.dao.CrudDao
import com.example.cleanmvvmretrofit.core.database.entity.CrudEntity

@Database(
    entities = [
        CrudEntity::class
    ],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract val crudDao: CrudDao
}