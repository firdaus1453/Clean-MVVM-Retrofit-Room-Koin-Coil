package com.example.cleanmvvmretrofit.home.domain

import java.time.LocalDateTime

// Data Classes
data class Siswa(
    val nis: String,
    val nama: String,
    val alamat: String
)

data class MataPelajaran(
    val idMatpel: Int,
    val namaMatpel: String
)

data class Ujian(
    val idUjian: Int,
    val namaUjian: String,
    val idMatpel: Int,
    val namaMatpel: String,
    val tanggal: LocalDateTime
)

data class Peserta(
    val idUjian: Int,
    val nis: String,
    val namaSiswa: String,
    val nilai: Int,
    val isLulus: Boolean
)

data class UjianWithStudents(
    val ujian: Ujian,
    val pesertaList: List<Peserta>
) {
    val jumlahPeserta: Int get() = pesertaList.size
    val jumlahSiswaLulus: Int get() = pesertaList.count { it.isLulus }
    val jumlahSiswaTidakLulus: Int get() = pesertaList.count { !it.isLulus }
}