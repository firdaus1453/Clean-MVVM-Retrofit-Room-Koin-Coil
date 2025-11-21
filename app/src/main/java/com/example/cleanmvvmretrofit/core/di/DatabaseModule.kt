package com.example.cleanmvvmretrofit.core.di

import androidx.room.Room
import com.example.cleanmvvmretrofit.core.database.AppDatabase
import com.example.cleanmvvmretrofit.home.data.AcademicRepositoryImpl
import com.example.cleanmvvmretrofit.home.domain.AcademicRepository
import com.example.cleanmvvmretrofit.home.presentation.AcademicViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val databaseModule = module {

    single {
        Room.databaseBuilder(
            androidApplication(),
            AppDatabase::class.java,
            "app.db"
        ).build()
    }

    // DAOs
    single { get<AppDatabase>().siswaDao }
    single { get<AppDatabase>().mataPelajaranDao }
    single { get<AppDatabase>().ujianDao }
    single { get<AppDatabase>().pesertaDao }


    // Repository
    single<AcademicRepository> {
        AcademicRepositoryImpl(
            siswaDao = get(),
            mataPelajaranDao = get(),
            ujianDao = get(),
            pesertaDao = get()
        )
    }

    // ViewModel
    viewModel { AcademicViewModel(repository = get()) }
}