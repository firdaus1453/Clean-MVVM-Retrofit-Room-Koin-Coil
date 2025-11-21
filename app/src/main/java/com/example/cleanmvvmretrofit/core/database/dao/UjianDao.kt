package com.example.cleanmvvmretrofit.core.database.dao

import androidx.room.*
import com.example.cleanmvvmretrofit.core.database.entity.UjianEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

@Dao
interface UjianDao {

    @Query("SELECT * FROM ujian ORDER BY tanggal DESC")
    fun getAllUjian(): Flow<List<UjianEntity>>

    @Query("SELECT * FROM ujian WHERE idUjian = :id")
    suspend fun getUjianById(id: Int): UjianEntity?

    @Query("SELECT * FROM ujian WHERE tanggal >= :startDate AND tanggal <= :endDate ORDER BY tanggal ASC")
    suspend fun getUjianByDateRange(startDate: LocalDateTime, endDate: LocalDateTime): List<UjianEntity>

    @Query("SELECT * FROM ujian WHERE idMatpel = :idMatpel ORDER BY tanggal DESC")
    suspend fun getUjianByMataPelajaran(idMatpel: Int): List<UjianEntity>

    @Query("SELECT * FROM ujian WHERE LOWER(namaUjian) LIKE LOWER(:query)")
    suspend fun searchUjian(query: String): List<UjianEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUjian(ujian: UjianEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUjianList(ujianList: List<UjianEntity>)

    @Update
    suspend fun updateUjian(ujian: UjianEntity)

    @Delete
    suspend fun deleteUjian(ujian: UjianEntity)

    @Query("DELETE FROM ujian WHERE idUjian = :id")
    suspend fun deleteUjianById(id: Int)

    @Query("DELETE FROM ujian")
    suspend fun deleteAllUjian()
}
