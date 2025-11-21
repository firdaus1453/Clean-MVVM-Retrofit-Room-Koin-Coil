package com.example.cleanmvvmretrofit.home.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.example.cleanmvvmretrofit.core.presentation.designsystem.Theme
import com.example.cleanmvvmretrofit.home.domain.UjianWithStudents
import com.example.cleanmvvmretrofit.home.presentation.home_detail.DetailPesertaScreen
import com.example.cleanmvvmretrofit.home.presentation.matapelajaran.MataPelajaranScreen
import com.example.cleanmvvmretrofit.home.presentation.siswa.SiswaScreen
import com.example.cleanmvvmretrofit.home.presentation.ujian.UjianScreen
import org.koin.androidx.compose.koinViewModel

// Navigation State
sealed class Screen {
    object UjianList : Screen()
    object SiswaList : Screen()
    object MataPelajaranList : Screen()
    data class UjianDetail(val ujianData: UjianWithStudents) : Screen()
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AcademicSystemApp(
    modifier: Modifier,
    viewModel: AcademicViewModel = koinViewModel()
) {
    val siswaList by viewModel.siswaList.collectAsState()
    val mataPelajaranList by viewModel.mataPelajaranList.collectAsState()
    val ujianList by viewModel.ujianList.collectAsState()

    var currentScreen by remember { mutableStateOf<Screen>(Screen.UjianList) }
    var selectedTab by remember { mutableIntStateOf(0) }

    Theme {
        when (currentScreen) {
            is Screen.UjianDetail -> {
                DetailPesertaScreen(
                    ujianData = (currentScreen as Screen.UjianDetail).ujianData,
                    onBackClick = { currentScreen = Screen.UjianList }
                )
            }
            else -> {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = {
                                Column {
                                    Text(
                                        "Sistem Informasi Akademik",
                                        style = MaterialTheme.typography.titleLarge,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        "SMA Negeri 1 Banjarmasin",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
                                    )
                                }
                            },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                titleContentColor = MaterialTheme.colorScheme.onPrimary
                            )
                        )
                    }
                ) { paddingValues ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                            .background(MaterialTheme.colorScheme.background)
                    ) {
                        TabRow(
                            selectedTabIndex = selectedTab,
                            containerColor = MaterialTheme.colorScheme.surface,
                            contentColor = MaterialTheme.colorScheme.primary
                        ) {
                            listOf("Ujian", "Siswa", "Mata Pelajaran").forEachIndexed { index, title ->
                                Tab(
                                    selected = selectedTab == index,
                                    onClick = {
                                        selectedTab = index
                                        currentScreen = when (index) {
                                            0 -> Screen.UjianList
                                            1 -> Screen.SiswaList
                                            else -> Screen.MataPelajaranList
                                        }
                                    },
                                    text = {
                                        Text(
                                            title,
                                            fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal
                                        )
                                    }
                                )
                            }
                        }

                        when (currentScreen) {
                            Screen.UjianList -> UjianScreen(
                                ujianList = ujianList,
                                mataPelajaranList = mataPelajaranList,
                                siswaList = siswaList,
                                onAddUjian = { viewModel.addUjian(it) },
                                onDeleteUjian = { viewModel.deleteUjian(it) },
                                getNextUjianId = { viewModel.getNextUjianId() },
                                onDetailClick = { ujianData ->
                                    currentScreen = Screen.UjianDetail(ujianData)
                                }
                            )
                            Screen.SiswaList -> SiswaScreen(
                                siswaList = siswaList,
                                onAddSiswa = { viewModel.addSiswa(it) },
                                onUpdateSiswa = { oldNis, newSiswa -> viewModel.updateSiswa(oldNis, newSiswa) },
                                onDeleteSiswa = { viewModel.deleteSiswa(it) }
                            )
                            Screen.MataPelajaranList -> MataPelajaranScreen(
                                mataPelajaranList = mataPelajaranList,
                                ujianList = ujianList,
                                onAddMataPelajaran = { viewModel.addMataPelajaran(it) },
                                onUpdateMataPelajaran = { viewModel.updateMataPelajaran(it) },
                                onDeleteMataPelajaran = { viewModel.deleteMataPelajaran(it) },
                                getNextMatpelId = { viewModel.getNextMatpelId() }
                            )
                            else -> {}
                        }
                    }
                }
            }
        }
    }
}