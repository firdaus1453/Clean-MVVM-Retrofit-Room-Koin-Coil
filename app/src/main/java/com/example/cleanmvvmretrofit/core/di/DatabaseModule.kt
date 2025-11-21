package com.example.cleanmvvmretrofit.core.di

import androidx.room.Room
import com.example.cleanmvvmretrofit.core.database.AppDatabase
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val databaseModule = module {

    single {
        Room.databaseBuilder(
            androidApplication(),
            AppDatabase::class.java,
            "app.db"
        ).build()
    }

    single { get<AppDatabase>().crudDao }
}