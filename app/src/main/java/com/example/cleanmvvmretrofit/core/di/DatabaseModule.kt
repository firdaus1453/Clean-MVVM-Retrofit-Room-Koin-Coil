package com.example.cleanmvvmretrofit.core.di

import androidx.room.Room
import com.example.cleanmvvmretrofit.core.database.AppDatabase
import com.example.cleanmvvmretrofit.core.domain.SessionStorage
import com.example.cleanmvvmretrofit.core.data.auth.EncryptedSessionStorage
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
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

    singleOf(::EncryptedSessionStorage).bind<SessionStorage>()
}