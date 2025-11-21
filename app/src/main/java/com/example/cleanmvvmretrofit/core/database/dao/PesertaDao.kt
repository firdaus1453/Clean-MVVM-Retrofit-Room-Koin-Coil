package com.example.cleanmvvmretrofit.core.database.dao

import androidx.room.*
import com.example.cleanmvvmretrofit.core.database.entity.PesertaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PesertaDao {

    @Query("SELECT * FROM peserta")
    fun getAllPeserta(): Flow<List<PesertaEntity>>

    @Query("SELECT * FROM peserta WHERE idUjian = :idUjian")
    suspend fun getPesertaByUjian(idUjian: Int): List<PesertaEntity>

    @Query("SELECT * FROM peserta WHERE idUjian = :idUjian")
    fun getPesertaByUjianFlow(idUjian: Int): Flow<List<PesertaEntity>>

    @Query("SELECT * FROM peserta WHERE nis = :nis")
    suspend fun getPesertaBySiswa(nis: String): List<PesertaEntity>

    @Query("SELECT * FROM peserta WHERE idUjian = :idUjian AND nis = :nis")
    suspend fun getPesertaByUjianAndNis(idUjian: Int, nis: String): PesertaEntity?

    @Query("SELECT * FROM peserta WHERE idUjian = :idUjian AND isLulus = 1")
    suspend fun getPesertaLulusByUjian(idUjian: Int): List<PesertaEntity>

    @Query("SELECT * FROM peserta WHERE idUjian = :idUjian AND isLulus = 0")
    suspend fun getPesertaTidakLulusByUjian(idUjian: Int): List<PesertaEntity>

    @Query("SELECT COUNT(*) FROM peserta WHERE idUjian = :idUjian")
    suspend fun getJumlahPesertaByUjian(idUjian: Int): Int

    @Query("SELECT COUNT(*) FROM peserta WHERE idUjian = :idUjian AND isLulus = 1")
    suspend fun getJumlahLulusByUjian(idUjian: Int): Int

    @Query("SELECT COUNT(*) FROM peserta WHERE idUjian = :idUjian AND isLulus = 0")
    suspend fun getJumlahTidakLulusByUjian(idUjian: Int): Int

    @Query("SELECT * FROM peserta WHERE nis = :nis AND isLulus = 0")
    suspend fun getMataPelajaranTidakLulusBySiswa(nis: String): List<PesertaEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPeserta(peserta: PesertaEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPesertaList(pesertaList: List<PesertaEntity>)

    @Update
    suspend fun updatePeserta(peserta: PesertaEntity)

    @Delete
    suspend fun deletePeserta(peserta: PesertaEntity)

    @Query("DELETE FROM peserta WHERE idUjian = :idUjian AND nis = :nis")
    suspend fun deletePesertaByUjianAndNis(idUjian: Int, nis: String)

    @Query("DELETE FROM peserta WHERE idUjian = :idUjian")
    suspend fun deletePesertaByUjian(idUjian: Int)

    @Query("DELETE FROM peserta")
    suspend fun deleteAllPeserta()
}
