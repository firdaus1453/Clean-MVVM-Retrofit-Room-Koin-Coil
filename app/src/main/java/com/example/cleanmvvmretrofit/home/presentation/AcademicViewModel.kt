package com.example.cleanmvvmretrofit.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cleanmvvmretrofit.home.domain.AcademicRepository
import com.example.cleanmvvmretrofit.home.domain.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime


class AcademicViewModel(
    private val repository: AcademicRepository
) : ViewModel() {

    // Siswa state
    private val _siswaList = MutableStateFlow<List<Siswa>>(emptyList())
    val siswaList = _siswaList.asStateFlow()

    // MataPelajaran state
    private val _mataPelajaranList = MutableStateFlow<List<MataPelajaran>>(emptyList())
    val mataPelajaranList = _mataPelajaranList.asStateFlow()

    // Ujian state
    private val _ujianList = MutableStateFlow<List<UjianWithStudents>>(emptyList())
    val ujianList = _ujianList.asStateFlow()

    init {
        loadAllData()
    }

    private fun loadAllData() {
        viewModelScope.launch {
            // Load Siswa
            repository.getAllSiswa().collect { siswaList ->
                _siswaList.value = siswaList
            }
        }

        viewModelScope.launch {
            // Load MataPelajaran
            repository.getAllMataPelajaran().collect { mataPelajaranList ->
                _mataPelajaranList.value = mataPelajaranList
            }
        }

        viewModelScope.launch {
            // Load Ujian with Students
            repository.getAllUjianWithStudents().collect { ujianList ->
                _ujianList.value = ujianList
            }
        }
    }

    // Siswa operations
    fun addSiswa(siswa: Siswa) {
        viewModelScope.launch {
            repository.insertSiswa(siswa)
        }
    }

    fun updateSiswa(oldNis: String, newSiswa: Siswa) {
        viewModelScope.launch {
            if (oldNis != newSiswa.nis) {
                // If NIS changed, delete old and insert new
                repository.deleteSiswa(oldNis)
                repository.insertSiswa(newSiswa)
            } else {
                repository.updateSiswa(newSiswa)
            }
        }
    }

    fun deleteSiswa(nis: String) {
        viewModelScope.launch {
            repository.deleteSiswa(nis)
        }
    }

    // MataPelajaran operations
    fun addMataPelajaran(mataPelajaran: MataPelajaran) {
        viewModelScope.launch {
            repository.insertMataPelajaran(mataPelajaran)
        }
    }

    fun updateMataPelajaran(mataPelajaran: MataPelajaran) {
        viewModelScope.launch {
            repository.updateMataPelajaran(mataPelajaran)
        }
    }

    fun deleteMataPelajaran(id: Int) {
        viewModelScope.launch {
            repository.deleteMataPelajaran(id)
        }
    }

    fun getNextMatpelId(): Int {
        return (_mataPelajaranList.value.maxOfOrNull { it.idMatpel } ?: 0) + 1
    }

    // Ujian operations
    fun addUjian(ujianWithStudents: UjianWithStudents) {
        viewModelScope.launch {
            repository.insertUjianWithPeserta(ujianWithStudents)
        }
    }

    fun updateUjian(ujianWithStudents: UjianWithStudents) {
        viewModelScope.launch {
            repository.updateUjianWithPeserta(ujianWithStudents)
        }
    }

    fun deleteUjian(id: Int) {
        viewModelScope.launch {
            repository.deleteUjian(id)
        }
    }

    fun getNextUjianId(): Int {
        return (_ujianList.value.maxOfOrNull { it.ujian.idUjian } ?: 0) + 1
    }

    // Query operations
    suspend fun getUjianByDateRange(startDate: LocalDateTime, endDate: LocalDateTime): List<UjianWithStudents> {
        return repository.getUjianByDateRange(startDate, endDate)
    }

    suspend fun getJumlahPesertaLulusTotal(): Int {
        return repository.getJumlahPesertaLulusTotal()
    }

    suspend fun getSiswaTidakLulusWithMataPelajaran(): Map<Siswa, List<String>> {
        return repository.getSiswaTidakLulusWithMataPelajaran()
    }
}
