package com.example.cleanmvvmretrofit.home.domain

import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

interface AcademicRepository {

    // Siswa operations
    fun getAllSiswa(): Flow<List<Siswa>>
    suspend fun getSiswaByNis(nis: String): Siswa?
    suspend fun searchSiswa(query: String): List<Siswa>
    suspend fun insertSiswa(siswa: Siswa)
    suspend fun updateSiswa(siswa: Siswa)
    suspend fun deleteSiswa(nis: String)

    // MataPelajaran operations
    fun getAllMataPelajaran(): Flow<List<MataPelajaran>>
    suspend fun getMataPelajaranById(id: Int): MataPelajaran?
    suspend fun insertMataPelajaran(mataPelajaran: MataPelajaran)
    suspend fun updateMataPelajaran(mataPelajaran: MataPelajaran)
    suspend fun deleteMataPelajaran(id: Int)

    // Ujian operations
    fun getAllUjianWithStudents(): Flow<List<UjianWithStudents>>
    suspend fun getUjianWithStudentsById(id: Int): UjianWithStudents?
    suspend fun getUjianByDateRange(startDate: LocalDateTime, endDate: LocalDateTime): List<UjianWithStudents>
    suspend fun insertUjianWithPeserta(ujianWithStudents: UjianWithStudents)
    suspend fun updateUjianWithPeserta(ujianWithStudents: UjianWithStudents)
    suspend fun deleteUjian(id: Int)

    // Peserta operations
    suspend fun getPesertaByUjian(idUjian: Int): List<Peserta>
    suspend fun updatePeserta(peserta: Peserta)
    suspend fun insertPeserta(peserta: Peserta)
    suspend fun deletePeserta(idUjian: Int, nis: String)

    // Statistics
    suspend fun getJumlahPesertaLulusTotal(): Int
    suspend fun getSiswaTidakLulusWithMataPelajaran(): Map<Siswa, List<String>>
}
