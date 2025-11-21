package com.example.cleanmvvmretrofit.core.database.dao

import androidx.room.*
import com.example.cleanmvvmretrofit.core.database.entity.SiswaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SiswaDao {

    @Query("SELECT * FROM siswa ORDER BY nama ASC")
    fun getAllSiswa(): Flow<List<SiswaEntity>>

    @Query("SELECT * FROM siswa WHERE nis = :nis")
    suspend fun getSiswaByNis(nis: String): SiswaEntity?

    @Query("SELECT * FROM siswa WHERE LOWER(nama) LIKE LOWER(:query) OR LOWER(nis) LIKE LOWER(:query)")
    suspend fun searchSiswa(query: String): List<SiswaEntity>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertSiswa(siswa: SiswaEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSiswaList(siswaList: List<SiswaEntity>)

    @Update
    suspend fun updateSiswa(siswa: SiswaEntity)

    @Delete
    suspend fun deleteSiswa(siswa: SiswaEntity)

    @Query("DELETE FROM siswa WHERE nis = :nis")
    suspend fun deleteSiswaByNis(nis: String)

    @Query("DELETE FROM siswa")
    suspend fun deleteAllSiswa()
}
