package com.example.cleanmvvmretrofit.home.data

import com.example.cleanmvvmretrofit.core.database.dao.MataPelajaranDao
import com.example.cleanmvvmretrofit.core.database.dao.PesertaDao
import com.example.cleanmvvmretrofit.core.database.dao.SiswaDao
import com.example.cleanmvvmretrofit.core.database.dao.UjianDao
import com.example.cleanmvvmretrofit.home.data.mappers.toDomain
import com.example.cleanmvvmretrofit.home.data.mappers.toEntity
import com.example.cleanmvvmretrofit.home.domain.AcademicRepository
import com.example.cleanmvvmretrofit.home.domain.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDateTime

class AcademicRepositoryImpl(
    private val siswaDao: SiswaDao,
    private val mataPelajaranDao: MataPelajaranDao,
    private val ujianDao: UjianDao,
    private val pesertaDao: PesertaDao
) : AcademicRepository {

    // Siswa operations
    override fun getAllSiswa(): Flow<List<Siswa>> {
        return siswaDao.getAllSiswa().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getSiswaByNis(nis: String): Siswa? {
        return siswaDao.getSiswaByNis(nis)?.toDomain()
    }

    override suspend fun searchSiswa(query: String): List<Siswa> {
        return siswaDao.searchSiswa("%$query%").map { it.toDomain() }
    }

    override suspend fun insertSiswa(siswa: Siswa) {
        siswaDao.insertSiswa(siswa.toEntity())
    }

    override suspend fun updateSiswa(siswa: Siswa) {
        siswaDao.updateSiswa(siswa.toEntity())
    }

    override suspend fun deleteSiswa(nis: String) {
        siswaDao.deleteSiswaByNis(nis)
    }

    // MataPelajaran operations
    override fun getAllMataPelajaran(): Flow<List<MataPelajaran>> {
        return mataPelajaranDao.getAllMataPelajaran().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getMataPelajaranById(id: Int): MataPelajaran? {
        return mataPelajaranDao.getMataPelajaranById(id)?.toDomain()
    }

    override suspend fun insertMataPelajaran(mataPelajaran: MataPelajaran) {
        mataPelajaranDao.insertMataPelajaran(mataPelajaran.toEntity())
    }

    override suspend fun updateMataPelajaran(mataPelajaran: MataPelajaran) {
        mataPelajaranDao.updateMataPelajaran(mataPelajaran.toEntity())
    }

    override suspend fun deleteMataPelajaran(id: Int) {
        mataPelajaranDao.deleteMataPelajaranById(id)
    }

    // Ujian operations
    override fun getAllUjianWithStudents(): Flow<List<UjianWithStudents>> {
        return ujianDao.getAllUjian().map { ujianEntities ->
            ujianEntities.map { ujianEntity ->
                val mataPelajaran = mataPelajaranDao.getMataPelajaranById(ujianEntity.idMatpel)
                val pesertaEntities = pesertaDao.getPesertaByUjian(ujianEntity.idUjian)

                val pesertaList = pesertaEntities.map { pesertaEntity ->
                    val siswa = siswaDao.getSiswaByNis(pesertaEntity.nis)
                    pesertaEntity.toDomain(siswa?.nama ?: "Unknown")
                }

                UjianWithStudents(
                    ujian = ujianEntity.toDomain(mataPelajaran?.namaMatpel ?: "Unknown"),
                    pesertaList = pesertaList
                )
            }
        }
    }

    override suspend fun getUjianWithStudentsById(id: Int): UjianWithStudents? {
        val ujianEntity = ujianDao.getUjianById(id) ?: return null
        val mataPelajaran = mataPelajaranDao.getMataPelajaranById(ujianEntity.idMatpel)
        val pesertaEntities = pesertaDao.getPesertaByUjian(ujianEntity.idUjian)

        val pesertaList = pesertaEntities.map { pesertaEntity ->
            val siswa = siswaDao.getSiswaByNis(pesertaEntity.nis)
            pesertaEntity.toDomain(siswa?.nama ?: "Unknown")
        }

        return UjianWithStudents(
            ujian = ujianEntity.toDomain(mataPelajaran?.namaMatpel ?: "Unknown"),
            pesertaList = pesertaList
        )
    }

    override suspend fun getUjianByDateRange(
        startDate: LocalDateTime,
        endDate: LocalDateTime
    ): List<UjianWithStudents> {
        val ujianEntities = ujianDao.getUjianByDateRange(startDate, endDate)

        return ujianEntities.map { ujianEntity ->
            val mataPelajaran = mataPelajaranDao.getMataPelajaranById(ujianEntity.idMatpel)
            val pesertaEntities = pesertaDao.getPesertaByUjian(ujianEntity.idUjian)

            val pesertaList = pesertaEntities.map { pesertaEntity ->
                val siswa = siswaDao.getSiswaByNis(pesertaEntity.nis)
                pesertaEntity.toDomain(siswa?.nama ?: "Unknown")
            }

            UjianWithStudents(
                ujian = ujianEntity.toDomain(mataPelajaran?.namaMatpel ?: "Unknown"),
                pesertaList = pesertaList
            )
        }
    }

    override suspend fun insertUjianWithPeserta(ujianWithStudents: UjianWithStudents) {
        val ujianId = ujianDao.insertUjian(ujianWithStudents.ujian.toEntity())

        val pesertaEntities = ujianWithStudents.pesertaList.map { peserta ->
            peserta.copy(idUjian = ujianId.toInt()).toEntity()
        }
        pesertaDao.insertPesertaList(pesertaEntities)
    }

    override suspend fun updateUjianWithPeserta(ujianWithStudents: UjianWithStudents) {
        ujianDao.updateUjian(ujianWithStudents.ujian.toEntity())

        // Delete existing peserta and insert new ones
        pesertaDao.deletePesertaByUjian(ujianWithStudents.ujian.idUjian)

        val pesertaEntities = ujianWithStudents.pesertaList.map { it.toEntity() }
        pesertaDao.insertPesertaList(pesertaEntities)
    }

    override suspend fun deleteUjian(id: Int) {
        ujianDao.deleteUjianById(id)
    }

    // Peserta operations
    override suspend fun getPesertaByUjian(idUjian: Int): List<Peserta> {
        val pesertaEntities = pesertaDao.getPesertaByUjian(idUjian)

        return pesertaEntities.map { pesertaEntity ->
            val siswa = siswaDao.getSiswaByNis(pesertaEntity.nis)
            pesertaEntity.toDomain(siswa?.nama ?: "Unknown")
        }
    }

    override suspend fun updatePeserta(peserta: Peserta) {
        pesertaDao.updatePeserta(peserta.toEntity())
    }

    override suspend fun insertPeserta(peserta: Peserta) {
        pesertaDao.insertPeserta(peserta.toEntity())
    }

    override suspend fun deletePeserta(idUjian: Int, nis: String) {
        pesertaDao.deletePesertaByUjianAndNis(idUjian, nis)
    }

    // Statistics
    override suspend fun getJumlahPesertaLulusTotal(): Int {
        val allUjian = ujianDao.getAllUjian()
        var totalLulus = 0

        allUjian.collect { ujianList ->
            ujianList.forEach { ujian ->
                totalLulus += pesertaDao.getJumlahLulusByUjian(ujian.idUjian)
            }
        }

        return totalLulus
    }

    override suspend fun getSiswaTidakLulusWithMataPelajaran(): Map<Siswa, List<String>> {
        val result = mutableMapOf<Siswa, MutableList<String>>()
        val allSiswa = siswaDao.getAllSiswa()

        allSiswa.collect { siswaList ->
            siswaList.forEach { siswaEntity ->
                val pesertaTidakLulus = pesertaDao.getMataPelajaranTidakLulusBySiswa(siswaEntity.nis)

                if (pesertaTidakLulus.isNotEmpty()) {
                    val mataPelajaranNames = mutableListOf<String>()

                    pesertaTidakLulus.forEach { peserta ->
                        val ujian = ujianDao.getUjianById(peserta.idUjian)
                        ujian?.let {
                            val mataPelajaran = mataPelajaranDao.getMataPelajaranById(it.idMatpel)
                            mataPelajaran?.let { mp ->
                                mataPelajaranNames.add(mp.namaMatpel)
                            }
                        }
                    }

                    if (mataPelajaranNames.isNotEmpty()) {
                        result[siswaEntity.toDomain()] = mataPelajaranNames
                    }
                }
            }
        }

        return result
    }
}
