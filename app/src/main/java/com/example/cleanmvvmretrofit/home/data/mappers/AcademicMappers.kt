package com.example.cleanmvvmretrofit.home.data.mappers

import com.example.cleanmvvmretrofit.core.database.entity.MataPelajaranEntity
import com.example.cleanmvvmretrofit.core.database.entity.PesertaEntity
import com.example.cleanmvvmretrofit.core.database.entity.SiswaEntity
import com.example.cleanmvvmretrofit.core.database.entity.UjianEntity
import com.example.cleanmvvmretrofit.home.domain.*

// Siswa Mappers
fun SiswaEntity.toDomain(): Siswa {
    return Siswa(
        nis = nis,
        nama = nama,
        alamat = alamat
    )
}

fun Siswa.toEntity(): SiswaEntity {
    return SiswaEntity(
        nis = nis,
        nama = nama,
        alamat = alamat
    )
}

// MataPelajaran Mappers
fun MataPelajaranEntity.toDomain(): MataPelajaran {
    return MataPelajaran(
        idMatpel = idMatpel,
        namaMatpel = namaMatpel
    )
}

fun MataPelajaran.toEntity(): MataPelajaranEntity {
    return MataPelajaranEntity(
        idMatpel = idMatpel,
        namaMatpel = namaMatpel
    )
}

// Ujian Mappers
fun UjianEntity.toDomain(namaMatpel: String): Ujian {
    return Ujian(
        idUjian = idUjian,
        namaUjian = namaUjian,
        idMatpel = idMatpel,
        namaMatpel = namaMatpel,
        tanggal = tanggal
    )
}

fun Ujian.toEntity(): UjianEntity {
    return UjianEntity(
        idUjian = idUjian,
        namaUjian = namaUjian,
        idMatpel = idMatpel,
        tanggal = tanggal
    )
}

// Peserta Mappers
fun PesertaEntity.toDomain(namaSiswa: String): Peserta {
    return Peserta(
        idUjian = idUjian,
        nis = nis,
        namaSiswa = namaSiswa,
        nilai = nilai,
        isLulus = isLulus
    )
}

fun Peserta.toEntity(): PesertaEntity {
    return PesertaEntity(
        idUjian = idUjian,
        nis = nis,
        nilai = nilai,
        isLulus = isLulus
    )
}
