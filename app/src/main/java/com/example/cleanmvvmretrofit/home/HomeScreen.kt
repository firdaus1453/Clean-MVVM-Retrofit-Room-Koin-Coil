package com.example.cleanmvvmretrofit.home

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

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

// Navigation State
sealed class Screen {
    object UjianList : Screen()
    object SiswaList : Screen()
    object MataPelajaranList : Screen()
    data class UjianDetail(val ujianData: UjianWithStudents) : Screen()
}

// Dummy Data dengan ViewModel State
class AcademicDataState {
    var siswaList = mutableStateListOf<Siswa>()
    var mataPelajaranList = mutableStateListOf<MataPelajaran>()
    var ujianList = mutableStateListOf<UjianWithStudents>()

    init {
        // Initialize with dummy data
        siswaList.addAll(
            listOf(
                Siswa("2021001", "Ahmad Fadil", "Jl. Merdeka No. 12, Banjarmasin"),
                Siswa("2021002", "Siti Nurhaliza", "Jl. Ahmad Yani No. 45, Banjarmasin"),
                Siswa("2021003", "Budi Santoso", "Jl. Veteran No. 78, Banjarbaru"),
                Siswa("2021004", "Dewi Lestari", "Jl. Pangeran No. 23, Banjarmasin"),
                Siswa("2021005", "Rudi Hermawan", "Jl. Lambung Mangkurat No. 56, Banjarmasin"),
                Siswa("2021006", "Maya Anggraini", "Jl. Sudirman No. 89, Banjarbaru"),
                Siswa("2021007", "Andi Wijaya", "Jl. A. Yani KM 5, Banjarmasin"),
                Siswa("2021008", "Putri Rahmawati", "Jl. Sutoyo S No. 34, Banjarmasin")
            )
        )

        mataPelajaranList.addAll(
            listOf(
                MataPelajaran(1, "Matematika"),
                MataPelajaran(2, "Bahasa Indonesia"),
                MataPelajaran(3, "Bahasa Inggris"),
                MataPelajaran(4, "Fisika"),
                MataPelajaran(5, "Kimia"),
                MataPelajaran(6, "Biologi")
            )
        )

        ujianList.addAll(
            listOf(
                UjianWithStudents(
                    Ujian(1, "UTS Ganjil 2024", 1, "Matematika", LocalDateTime.of(2024, 9, 15, 8, 0)),
                    listOf(
                        Peserta(1, "2021001", "Ahmad Fadil", 85, true),
                        Peserta(1, "2021002", "Siti Nurhaliza", 92, true),
                        Peserta(1, "2021003", "Budi Santoso", 78, true),
                        Peserta(1, "2021004", "Dewi Lestari", 65, true),
                        Peserta(1, "2021005", "Rudi Hermawan", 88, true),
                        Peserta(1, "2021006", "Maya Anggraini", 55, false),
                        Peserta(1, "2021007", "Andi Wijaya", 45, false),
                        Peserta(1, "2021008", "Putri Rahmawati", 90, true)
                    )
                ),
                UjianWithStudents(
                    Ujian(2, "UAS Ganjil 2024", 1, "Matematika", LocalDateTime.of(2024, 12, 10, 8, 0)),
                    listOf(
                        Peserta(2, "2021001", "Ahmad Fadil", 88, true),
                        Peserta(2, "2021002", "Siti Nurhaliza", 95, true),
                        Peserta(2, "2021003", "Budi Santoso", 82, true),
                        Peserta(2, "2021004", "Dewi Lestari", 78, true),
                        Peserta(2, "2021005", "Rudi Hermawan", 91, true),
                        Peserta(2, "2021006", "Maya Anggraini", 68, true),
                        Peserta(2, "2021007", "Andi Wijaya", 58, false),
                        Peserta(2, "2021008", "Putri Rahmawati", 93, true)
                    )
                ),
                UjianWithStudents(
                    Ujian(3, "UTS Ganjil 2024", 2, "Bahasa Indonesia", LocalDateTime.of(2024, 9, 18, 10, 0)),
                    listOf(
                        Peserta(3, "2021001", "Ahmad Fadil", 80, true),
                        Peserta(3, "2021002", "Siti Nurhaliza", 87, true),
                        Peserta(3, "2021003", "Budi Santoso", 75, true),
                        Peserta(3, "2021004", "Dewi Lestari", 82, true),
                        Peserta(3, "2021005", "Rudi Hermawan", 78, true),
                        Peserta(3, "2021006", "Maya Anggraini", 85, true),
                        Peserta(3, "2021007", "Andi Wijaya", 72, true),
                        Peserta(3, "2021008", "Putri Rahmawati", 88, true)
                    )
                ),
                UjianWithStudents(
                    Ujian(4, "Ujian Harian 1", 3, "Bahasa Inggris", LocalDateTime.of(2024, 8, 20, 8, 0)),
                    listOf(
                        Peserta(4, "2021001", "Ahmad Fadil", 70, true),
                        Peserta(4, "2021002", "Siti Nurhaliza", 82, true),
                        Peserta(4, "2021003", "Budi Santoso", 65, true),
                        Peserta(4, "2021004", "Dewi Lestari", 75, true),
                        Peserta(4, "2021005", "Rudi Hermawan", 58, false),
                        Peserta(4, "2021006", "Maya Anggraini", 48, false),
                        Peserta(4, "2021007", "Andi Wijaya", 52, false),
                        Peserta(4, "2021008", "Putri Rahmawati", 78, true)
                    )
                ),
                UjianWithStudents(
                    Ujian(5, "UTS Ganjil 2024", 4, "Fisika", LocalDateTime.of(2024, 9, 22, 13, 0)),
                    listOf(
                        Peserta(5, "2021001", "Ahmad Fadil", 75, true),
                        Peserta(5, "2021002", "Siti Nurhaliza", 88, true),
                        Peserta(5, "2021003", "Budi Santoso", 72, true),
                        Peserta(5, "2021004", "Dewi Lestari", 65, true),
                        Peserta(5, "2021005", "Rudi Hermawan", 45, false),
                        Peserta(5, "2021006", "Maya Anggraini", 52, false)
                    )
                )
            )
        )
    }

    // CRUD for Siswa
    fun addSiswa(siswa: Siswa) {
        siswaList.add(siswa)
    }

    fun updateSiswa(oldNis: String, newSiswa: Siswa) {
        val index = siswaList.indexOfFirst { it.nis == oldNis }
        if (index != -1) {
            siswaList[index] = newSiswa
        }
    }

    fun deleteSiswa(nis: String) {
        siswaList.removeIf { it.nis == nis }
    }

    // CRUD for Mata Pelajaran
    fun addMataPelajaran(mataPelajaran: MataPelajaran) {
        mataPelajaranList.add(mataPelajaran)
    }

    fun updateMataPelajaran(id: Int, newMataPelajaran: MataPelajaran) {
        val index = mataPelajaranList.indexOfFirst { it.idMatpel == id }
        if (index != -1) {
            mataPelajaranList[index] = newMataPelajaran
        }
    }

    fun deleteMataPelajaran(id: Int) {
        mataPelajaranList.removeIf { it.idMatpel == id }
    }

    // CRUD for Ujian
    fun addUjian(ujianWithStudents: UjianWithStudents) {
        ujianList.add(ujianWithStudents)
    }

    fun updateUjian(id: Int, newUjianWithStudents: UjianWithStudents) {
        val index = ujianList.indexOfFirst { it.ujian.idUjian == id }
        if (index != -1) {
            ujianList[index] = newUjianWithStudents
        }
    }

    fun deleteUjian(id: Int) {
        ujianList.removeIf { it.ujian.idUjian == id }
    }

    fun getNextUjianId(): Int {
        return (ujianList.maxOfOrNull { it.ujian.idUjian } ?: 0) + 1
    }

    fun getNextMatpelId(): Int {
        return (mataPelajaranList.maxOfOrNull { it.idMatpel } ?: 0) + 1
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AcademicSystemApp(
    modifier: Modifier
) {
    val dataState = remember { AcademicDataState() }
    var currentScreen by remember { mutableStateOf<Screen>(Screen.UjianList) }
    var selectedTab by remember { mutableIntStateOf(0) }

    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = Color(0xFF1976D2),
            secondary = Color(0xFF42A5F5),
            tertiary = Color(0xFF90CAF9),
            background = Color(0xFFF5F5F5),
            surface = Color.White,
            onPrimary = Color.White,
            onSecondary = Color.White,
            onBackground = Color(0xFF212121),
            onSurface = Color(0xFF212121)
        )
    ) {
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
                                dataState = dataState,
                                onDetailClick = { ujianData ->
                                    currentScreen = Screen.UjianDetail(ujianData)
                                }
                            )
                            Screen.SiswaList -> SiswaScreen(dataState)
                            Screen.MataPelajaranList -> MataPelajaranScreen(dataState)
                            else -> {}
                        }
                    }
                }
            }
        }
    }
}

// ==================== DETAIL PESERTA SCREEN (NEW!) ====================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailPesertaScreen(
    ujianData: UjianWithStudents,
    onBackClick: () -> Unit
) {
    var filterStatus by remember { mutableStateOf<Boolean?>(null) }
    val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm")

    val filteredPeserta = remember(filterStatus) {
        when (filterStatus) {
            null -> ujianData.pesertaList
            true -> ujianData.pesertaList.filter { it.isLulus }
            false -> ujianData.pesertaList.filter { !it.isLulus }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            "Detail Peserta Ujian",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            ujianData.ujian.namaUjian,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White.copy(alpha = 0.9f)
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFF5F5F5))
        ) {
            // Info Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                ujianData.ujian.namaMatpel,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    Icons.Default.DateRange,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp),
                                    tint = Color.Gray
                                )
                                Text(
                                    ujianData.ujian.tanggal.format(formatter),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color.Gray
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        StatBox(
                            title = "Total",
                            value = ujianData.jumlahPeserta.toString(),
                            color = Color(0xFF2196F3),
                            modifier = Modifier.weight(1f)
                        )
                        StatBox(
                            title = "Lulus",
                            value = ujianData.jumlahSiswaLulus.toString(),
                            color = Color(0xFF4CAF50),
                            modifier = Modifier.weight(1f)
                        )
                        StatBox(
                            title = "Tidak Lulus",
                            value = ujianData.jumlahSiswaTidakLulus.toString(),
                            color = Color(0xFFF44336),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            // Filter Section
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Color.White,
                shadowElevation = 1.dp
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        "Filter Peserta",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FilterChip(
                            selected = filterStatus == null,
                            onClick = { filterStatus = null },
                            label = { Text("Semua (${ujianData.jumlahPeserta})") },
                            leadingIcon = {
                                Icon(
                                    Icons.Default.Person,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                            },
                            modifier = Modifier.weight(1f)
                        )
                        FilterChip(
                            selected = filterStatus == true,
                            onClick = { filterStatus = true },
                            label = { Text("Lulus (${ujianData.jumlahSiswaLulus})") },
                            leadingIcon = {
                                Icon(
                                    Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Color(0xFF4CAF50).copy(alpha = 0.2f),
                                selectedLabelColor = Color(0xFF4CAF50),
                                selectedLeadingIconColor = Color(0xFF4CAF50)
                            ),
                            modifier = Modifier.weight(1f)
                        )
                        FilterChip(
                            selected = filterStatus == false,
                            onClick = { filterStatus = false },
                            label = { Text("Tidak (${ujianData.jumlahSiswaTidakLulus})") },
                            leadingIcon = {
                                Icon(
                                    Icons.Default.Close,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Color(0xFFF44336).copy(alpha = 0.2f),
                                selectedLabelColor = Color(0xFFF44336),
                                selectedLeadingIconColor = Color(0xFFF44336)
                            ),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            // Peserta List
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                items(filteredPeserta.sortedByDescending { it.nilai }) { peserta ->
                    PesertaCard(peserta)
                }
            }
        }
    }
}

@Composable
fun StatBox(
    title: String,
    value: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = color.copy(alpha = 0.1f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                value,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = color
            )
            Text(
                title,
                style = MaterialTheme.typography.bodySmall,
                color = color,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun PesertaCard(peserta: Peserta) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Avatar & Info
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(
                            if (peserta.isLulus)
                                Brush.linearGradient(colors = listOf(Color(0xFF4CAF50), Color(0xFF8BC34A)))
                            else
                                Brush.linearGradient(colors = listOf(Color(0xFFF44336), Color(0xFFFF5722)))
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        peserta.namaSiswa.first().toString(),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                Column {
                    Text(
                        peserta.namaSiswa,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        "NIS: ${peserta.nis}",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
            }

            // Nilai & Status
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Nilai
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            when {
                                peserta.nilai >= 85 -> Color(0xFF4CAF50).copy(alpha = 0.1f)
                                peserta.nilai >= 70 -> Color(0xFF2196F3).copy(alpha = 0.1f)
                                peserta.nilai >= 60 -> Color(0xFFFF9800).copy(alpha = 0.1f)
                                else -> Color(0xFFF44336).copy(alpha = 0.1f)
                            }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        peserta.nilai.toString(),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = when {
                            peserta.nilai >= 85 -> Color(0xFF4CAF50)
                            peserta.nilai >= 70 -> Color(0xFF2196F3)
                            peserta.nilai >= 60 -> Color(0xFFFF9800)
                            else -> Color(0xFFF44336)
                        }
                    )
                }

                // Status Badge
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(
                            if (peserta.isLulus)
                                Color(0xFF4CAF50).copy(alpha = 0.1f)
                            else
                                Color(0xFFF44336).copy(alpha = 0.1f)
                        )
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            if (peserta.isLulus) Icons.Default.CheckCircle else Icons.Default.Close,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = if (peserta.isLulus) Color(0xFF4CAF50) else Color(0xFFF44336)
                        )
                        Text(
                            if (peserta.isLulus) "Lulus" else "Tidak",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = if (peserta.isLulus) Color(0xFF4CAF50) else Color(0xFFF44336)
                        )
                    }
                }
            }
        }
    }
}

// ==================== UJIAN SCREEN ====================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UjianScreen(
    dataState: AcademicDataState,
    onDetailClick: (UjianWithStudents) -> Unit
) {
    var selectedUjian by remember { mutableStateOf<UjianWithStudents?>(null) }
    var showAddDialog by remember { mutableStateOf(false) }
    var ujianToDelete by remember { mutableStateOf<UjianWithStudents?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Header Stats
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            StatCard(
                title = "Total Ujian",
                value = dataState.ujianList.size.toString(),
                icon = Icons.Default.Menu,
                color = Color(0xFF1976D2),
                modifier = Modifier.weight(1f)
            )
            StatCard(
                title = "Mendatang",
                value = dataState.ujianList.count { it.ujian.tanggal.isAfter(LocalDateTime.now()) }.toString(),
                icon = Icons.Default.DateRange,
                color = Color(0xFFFF9800),
                modifier = Modifier.weight(1f)
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Daftar Ujian",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(48.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Tambah Ujian", tint = Color.White)
            }
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(dataState.ujianList.sortedByDescending { it.ujian.tanggal }) { ujianData ->
                UjianCard(
                    ujianData = ujianData,
                    isExpanded = selectedUjian == ujianData,
                    onClick = {
                        selectedUjian = if (selectedUjian == ujianData) null else ujianData
                    },
                    onDetailClick = { onDetailClick(ujianData) },
                    onDeleteClick = { ujianToDelete = ujianData }
                )
            }
        }
    }

    // Add Ujian Dialog
    if (showAddDialog) {
        AddUjianDialog(
            dataState = dataState,
            onDismiss = { showAddDialog = false },
            onConfirm = { ujian ->
                dataState.addUjian(ujian)
                showAddDialog = false
            }
        )
    }

    // Delete Confirmation Dialog
    ujianToDelete?.let { ujian ->
        AlertDialog(
            onDismissRequest = { ujianToDelete = null },
            icon = { Icon(Icons.Default.Warning, contentDescription = null, tint = Color(0xFFF44336)) },
            title = { Text("Hapus Ujian?") },
            text = { Text("Apakah Anda yakin ingin menghapus ujian '${ujian.ujian.namaUjian}'?") },
            confirmButton = {
                Button(
                    onClick = {
                        dataState.deleteUjian(ujian.ujian.idUjian)
                        ujianToDelete = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF44336))
                ) {
                    Text("Hapus")
                }
            },
            dismissButton = {
                TextButton(onClick = { ujianToDelete = null }) {
                    Text("Batal")
                }
            }
        )
    }
}

@Composable
fun UjianCard(
    ujianData: UjianWithStudents,
    isExpanded: Boolean,
    onClick: () -> Unit,
    onDetailClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm")
    val passPercentage = if (ujianData.jumlahPeserta > 0) {
        (ujianData.jumlahSiswaLulus.toFloat() / ujianData.jumlahPeserta * 100).toInt()
    } else 0

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        ujianData.ujian.namaUjian,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            Icons.Default.Menu,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            ujianData.ujian.namaMatpel,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(
                            when {
                                passPercentage >= 80 -> Color(0xFF4CAF50)
                                passPercentage >= 60 -> Color(0xFFFF9800)
                                else -> Color(0xFFF44336)
                            }.copy(alpha = 0.1f)
                        )
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        "$passPercentage% Lulus",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold,
                        color = when {
                            passPercentage >= 80 -> Color(0xFF4CAF50)
                            passPercentage >= 60 -> Color(0xFFFF9800)
                            else -> Color(0xFFF44336)
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                InfoChip(
                    icon = Icons.Default.DateRange,
                    text = ujianData.ujian.tanggal.format(formatter)
                )
                InfoChip(
                    icon = Icons.Default.Person,
                    text = "${ujianData.jumlahPeserta} Siswa"
                )
            }

            AnimatedVisibility(
                visible = isExpanded,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Column(
                    modifier = Modifier.padding(top = 16.dp)
                ) {
                    HorizontalDivider(modifier = Modifier.padding(bottom = 16.dp))

                    Text(
                        "Detail Statistik",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        DetailStatItem(
                            title = "Total Peserta",
                            value = ujianData.jumlahPeserta.toString(),
                            color = Color(0xFF2196F3),
                            modifier = Modifier.weight(1f)
                        )
                        DetailStatItem(
                            title = "Lulus",
                            value = ujianData.jumlahSiswaLulus.toString(),
                            color = Color(0xFF4CAF50),
                            modifier = Modifier.weight(1f)
                        )
                        DetailStatItem(
                            title = "Tidak Lulus",
                            value = ujianData.jumlahSiswaTidakLulus.toString(),
                            color = Color(0xFFF44336),
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = onDetailClick,
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3))
                        ) {
                            Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Lihat Detail Peserta")
                        }

                        IconButton(
                            onClick = onDeleteClick,
                            modifier = Modifier
                                .size(40.dp)
                                .background(Color(0xFFF44336).copy(alpha = 0.1f), RoundedCornerShape(8.dp))
                        ) {
                            Icon(Icons.Default.Delete, contentDescription = "Hapus", tint = Color(0xFFF44336))
                        }
                    }
                }
            }
        }
    }
}

// ==================== ADD UJIAN DIALOG ====================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddUjianDialog(
    dataState: AcademicDataState,
    onDismiss: () -> Unit,
    onConfirm: (UjianWithStudents) -> Unit
) {
    var namaUjian by remember { mutableStateOf("") }
    var selectedMatpel by remember { mutableStateOf<MataPelajaran?>(null) }
    var tanggalUjian by remember { mutableStateOf(LocalDateTime.now()) }
    var selectedSiswaList by remember { mutableStateOf(listOf<Siswa>()) }
    var showMatpelMenu by remember { mutableStateOf(false) }
    var showSiswaSelection by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.9f),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Text(
                    "Tambah Ujian Baru",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(16.dp))

                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        OutlinedTextField(
                            value = namaUjian,
                            onValueChange = { namaUjian = it },
                            label = { Text("Nama Ujian") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )
                    }

                    item {
                        ExposedDropdownMenuBox(
                            expanded = showMatpelMenu,
                            onExpandedChange = { showMatpelMenu = it }
                        ) {
                            OutlinedTextField(
                                value = selectedMatpel?.namaMatpel ?: "",
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Mata Pelajaran") },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = showMatpelMenu) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .menuAnchor()
                            )
                            ExposedDropdownMenu(
                                expanded = showMatpelMenu,
                                onDismissRequest = { showMatpelMenu = false }
                            ) {
                                dataState.mataPelajaranList.forEach { matpel ->
                                    DropdownMenuItem(
                                        text = { Text(matpel.namaMatpel) },
                                        onClick = {
                                            selectedMatpel = matpel
                                            showMatpelMenu = false
                                        }
                                    )
                                }
                            }
                        }
                    }

                    item {
                        OutlinedButton(
                            onClick = { showSiswaSelection = true },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(Icons.Default.Person, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Pilih Peserta (${selectedSiswaList.size} siswa)")
                        }

                        if (selectedSiswaList.isNotEmpty()) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 8.dp)
                            ) {
                                selectedSiswaList.forEach { siswa ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 4.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            siswa.nama,
                                            style = MaterialTheme.typography.bodySmall
                                        )
                                        IconButton(
                                            onClick = { selectedSiswaList = selectedSiswaList - siswa },
                                            modifier = Modifier.size(24.dp)
                                        ) {
                                            Icon(
                                                Icons.Default.Close,
                                                contentDescription = "Hapus",
                                                modifier = Modifier.size(16.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Batal")
                    }
                    Button(
                        onClick = {
                            if (namaUjian.isNotBlank() && selectedMatpel != null && selectedSiswaList.isNotEmpty()) {
                                val pesertaList = selectedSiswaList.map { siswa ->
                                    val nilai = (60..100).random()
                                    Peserta(
                                        idUjian = dataState.getNextUjianId(),
                                        nis = siswa.nis,
                                        namaSiswa = siswa.nama,
                                        nilai = nilai,
                                        isLulus = nilai >= 60
                                    )
                                }

                                val ujianBaru = UjianWithStudents(
                                    ujian = Ujian(
                                        idUjian = dataState.getNextUjianId(),
                                        namaUjian = namaUjian,
                                        idMatpel = selectedMatpel!!.idMatpel,
                                        namaMatpel = selectedMatpel!!.namaMatpel,
                                        tanggal = tanggalUjian
                                    ),
                                    pesertaList = pesertaList
                                )
                                onConfirm(ujianBaru)
                            }
                        },
                        modifier = Modifier.weight(1f),
                        enabled = namaUjian.isNotBlank() && selectedMatpel != null && selectedSiswaList.isNotEmpty()
                    ) {
                        Text("Simpan")
                    }
                }
            }
        }
    }

    // Siswa Selection Dialog
    if (showSiswaSelection) {
        Dialog(onDismissRequest = { showSiswaSelection = false }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.7f),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        "Pilih Peserta Ujian",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(dataState.siswaList) { siswa ->
                            val isSelected = selectedSiswaList.contains(siswa)
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        selectedSiswaList = if (isSelected) {
                                            selectedSiswaList - siswa
                                        } else {
                                            selectedSiswaList + siswa
                                        }
                                    },
                                colors = CardDefaults.cardColors(
                                    containerColor = if (isSelected) Color(0xFF1976D2).copy(alpha = 0.1f) else Color.White
                                )
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Checkbox(
                                        checked = isSelected,
                                        onCheckedChange = null
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column {
                                        Text(
                                            siswa.nama,
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontWeight = FontWeight.Medium
                                        )
                                        Text(
                                            "NIS: ${siswa.nis}",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = Color.Gray
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { showSiswaSelection = false },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Selesai (${selectedSiswaList.size} siswa dipilih)")
                    }
                }
            }
        }
    }
}

// ==================== SISWA SCREEN ====================
@Composable
fun SiswaScreen(dataState: AcademicDataState) {
    var searchQuery by remember { mutableStateOf("") }
    var showAddDialog by remember { mutableStateOf(false) }
    var siswaToEdit by remember { mutableStateOf<Siswa?>(null) }
    var siswaToDelete by remember { mutableStateOf<Siswa?>(null) }

    val filteredSiswa = remember(searchQuery, dataState.siswaList.size) {
        if (searchQuery.isEmpty()) {
            dataState.siswaList
        } else {
            dataState.siswaList.filter {
                it.nama.contains(searchQuery, ignoreCase = true) ||
                        it.nis.contains(searchQuery, ignoreCase = true)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Search Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            placeholder = { Text("Cari siswa...") },
            leadingIcon = {
                Icon(Icons.Default.Search, contentDescription = null)
            },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { searchQuery = "" }) {
                        Icon(Icons.Default.Clear, contentDescription = "Clear")
                    }
                }
            },
            singleLine = true,
            shape = RoundedCornerShape(12.dp)
        )

        // Stats
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            StatCard(
                title = "Total Siswa",
                value = dataState.siswaList.size.toString(),
                icon = Icons.Default.Person,
                color = Color(0xFF9C27B0),
                modifier = Modifier.weight(1f)
            )
            StatCard(
                title = "Hasil Pencarian",
                value = filteredSiswa.size.toString(),
                icon = Icons.Default.Search,
                color = Color(0xFF00BCD4),
                modifier = Modifier.weight(1f)
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Daftar Siswa",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(48.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Tambah Siswa", tint = Color.White)
            }
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(filteredSiswa) { siswa ->
                SiswaCard(
                    siswa = siswa,
                    onEditClick = { siswaToEdit = siswa },
                    onDeleteClick = { siswaToDelete = siswa }
                )
            }
        }
    }

    // Add/Edit Dialog
    if (showAddDialog || siswaToEdit != null) {
        AddEditSiswaDialog(
            siswa = siswaToEdit,
            onDismiss = {
                showAddDialog = false
                siswaToEdit = null
            },
            onConfirm = { nis, nama, alamat ->
                if (siswaToEdit != null) {
                    dataState.updateSiswa(siswaToEdit!!.nis, Siswa(nis, nama, alamat))
                } else {
                    dataState.addSiswa(Siswa(nis, nama, alamat))
                }
                showAddDialog = false
                siswaToEdit = null
            }
        )
    }

    // Delete Confirmation
    siswaToDelete?.let { siswa ->
        AlertDialog(
            onDismissRequest = { siswaToDelete = null },
            icon = { Icon(Icons.Default.Warning, contentDescription = null, tint = Color(0xFFF44336)) },
            title = { Text("Hapus Siswa?") },
            text = { Text("Apakah Anda yakin ingin menghapus siswa '${siswa.nama}'?") },
            confirmButton = {
                Button(
                    onClick = {
                        dataState.deleteSiswa(siswa.nis)
                        siswaToDelete = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF44336))
                ) {
                    Text("Hapus")
                }
            },
            dismissButton = {
                TextButton(onClick = { siswaToDelete = null }) {
                    Text("Batal")
                }
            }
        )
    }
}

@Composable
fun SiswaCard(
    siswa: Siswa,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.linearGradient(
                            colors = listOf(
                                Color(0xFF1976D2),
                                Color(0xFF42A5F5)
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    siswa.nama.first().toString(),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    siswa.nama,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        Icons.Default.AccountCircle,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        "NIS: ${siswa.nis}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        Icons.Default.LocationOn,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = Color.Gray
                    )
                    Text(
                        siswa.alamat,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
            }

            IconButton(onClick = onEditClick) {
                Icon(Icons.Default.Edit, contentDescription = "Edit", tint = Color(0xFF2196F3))
            }
            IconButton(onClick = onDeleteClick) {
                Icon(Icons.Default.Delete, contentDescription = "Hapus", tint = Color(0xFFF44336))
            }
        }
    }
}

@Composable
fun AddEditSiswaDialog(
    siswa: Siswa?,
    onDismiss: () -> Unit,
    onConfirm: (String, String, String) -> Unit
) {
    var nis by remember { mutableStateOf(siswa?.nis ?: "") }
    var nama by remember { mutableStateOf(siswa?.nama ?: "") }
    var alamat by remember { mutableStateOf(siswa?.alamat ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (siswa == null) "Tambah Siswa" else "Edit Siswa") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = nis,
                    onValueChange = { nis = it },
                    label = { Text("NIS") },
                    singleLine = true,
                    enabled = siswa == null,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = nama,
                    onValueChange = { nama = it },
                    label = { Text("Nama Lengkap") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = alamat,
                    onValueChange = { alamat = it },
                    label = { Text("Alamat") },
                    minLines = 2,
                    maxLines = 3,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(nis, nama, alamat) },
                enabled = nis.isNotBlank() && nama.isNotBlank() && alamat.isNotBlank()
            ) {
                Text("Simpan")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Batal")
            }
        }
    )
}

// ==================== MATA PELAJARAN SCREEN ====================
@Composable
fun MataPelajaranScreen(dataState: AcademicDataState) {
    var showAddDialog by remember { mutableStateOf(false) }
    var matpelToEdit by remember { mutableStateOf<MataPelajaran?>(null) }
    var matpelToDelete by remember { mutableStateOf<MataPelajaran?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        StatCard(
            title = "Total Mata Pelajaran",
            value = dataState.mataPelajaranList.size.toString(),
            icon = Icons.Default.Menu,
            color = Color(0xFF4CAF50),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Daftar Mata Pelajaran",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(48.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Tambah Mata Pelajaran", tint = Color.White)
            }
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(dataState.mataPelajaranList) { matpel ->
                MataPelajaranCard(
                    mataPelajaran = matpel,
                    ujianCount = dataState.ujianList.count { it.ujian.idMatpel == matpel.idMatpel },
                    onEditClick = { matpelToEdit = matpel },
                    onDeleteClick = { matpelToDelete = matpel }
                )
            }
        }
    }

    // Add/Edit Dialog
    if (showAddDialog || matpelToEdit != null) {
        AddEditMataPelajaranDialog(
            mataPelajaran = matpelToEdit,
            onDismiss = {
                showAddDialog = false
                matpelToEdit = null
            },
            onConfirm = { namaMatpel ->
                if (matpelToEdit != null) {
                    dataState.updateMataPelajaran(matpelToEdit!!.idMatpel, MataPelajaran(matpelToEdit!!.idMatpel, namaMatpel))
                } else {
                    dataState.addMataPelajaran(MataPelajaran(dataState.getNextMatpelId(), namaMatpel))
                }
                showAddDialog = false
                matpelToEdit = null
            }
        )
    }

    // Delete Confirmation
    matpelToDelete?.let { matpel ->
        AlertDialog(
            onDismissRequest = { matpelToDelete = null },
            icon = { Icon(Icons.Default.Warning, contentDescription = null, tint = Color(0xFFF44336)) },
            title = { Text("Hapus Mata Pelajaran?") },
            text = { Text("Apakah Anda yakin ingin menghapus mata pelajaran '${matpel.namaMatpel}'?") },
            confirmButton = {
                Button(
                    onClick = {
                        dataState.deleteMataPelajaran(matpel.idMatpel)
                        matpelToDelete = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF44336))
                ) {
                    Text("Hapus")
                }
            },
            dismissButton = {
                TextButton(onClick = { matpelToDelete = null }) {
                    Text("Batal")
                }
            }
        )
    }
}

@Composable
fun MataPelajaranCard(
    mataPelajaran: MataPelajaran,
    ujianCount: Int,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        Brush.linearGradient(
                            colors = listOf(
                                Color(0xFF4CAF50),
                                Color(0xFF8BC34A)
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Menu,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(32.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    mataPelajaran.namaMatpel,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    "Kode: MATPEL${mataPelajaran.idMatpel.toString().padStart(3, '0')}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFF4CAF50).copy(alpha = 0.1f))
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(
                    "$ujianCount Ujian",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF4CAF50)
                )
            }

            IconButton(onClick = onEditClick) {
                Icon(Icons.Default.Edit, contentDescription = "Edit", tint = Color(0xFF2196F3))
            }
            IconButton(onClick = onDeleteClick) {
                Icon(Icons.Default.Delete, contentDescription = "Hapus", tint = Color(0xFFF44336))
            }
        }
    }
}

@Composable
fun AddEditMataPelajaranDialog(
    mataPelajaran: MataPelajaran?,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var namaMatpel by remember { mutableStateOf(mataPelajaran?.namaMatpel ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (mataPelajaran == null) "Tambah Mata Pelajaran" else "Edit Mata Pelajaran") },
        text = {
            OutlinedTextField(
                value = namaMatpel,
                onValueChange = { namaMatpel = it },
                label = { Text("Nama Mata Pelajaran") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(namaMatpel) },
                enabled = namaMatpel.isNotBlank()
            ) {
                Text("Simpan")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Batal")
            }
        }
    )
}

// ==================== SHARED COMPONENTS ====================
@Composable
fun StatCard(
    title: String,
    value: String,
    icon: ImageVector,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(color.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    icon,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    value,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = color
                )
                Text(
                    title,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
        }
    }
}

@Composable
fun InfoChip(icon: ImageVector, text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(
            icon,
            contentDescription = null,
            modifier = Modifier.size(16.dp),
            tint = Color.Gray
        )
        Text(
            text,
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray
        )
    }
}

@Composable
fun DetailStatItem(
    title: String,
    value: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.1f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                value,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = color
            )
            Text(
                title,
                style = MaterialTheme.typography.bodySmall,
                color = color,
                textAlign = TextAlign.Center
            )
        }
    }
}