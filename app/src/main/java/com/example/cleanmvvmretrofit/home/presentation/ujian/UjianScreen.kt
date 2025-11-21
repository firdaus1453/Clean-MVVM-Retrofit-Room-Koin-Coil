package com.example.cleanmvvmretrofit.home.presentation.ujian

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.cleanmvvmretrofit.core.presentation.designsystem.component.DetailStatItem
import com.example.cleanmvvmretrofit.core.presentation.designsystem.component.InfoChip
import com.example.cleanmvvmretrofit.core.presentation.designsystem.component.StatCard
import com.example.cleanmvvmretrofit.home.domain.MataPelajaran
import com.example.cleanmvvmretrofit.home.domain.Peserta
import com.example.cleanmvvmretrofit.home.domain.Siswa
import com.example.cleanmvvmretrofit.home.domain.Ujian
import com.example.cleanmvvmretrofit.home.domain.UjianWithStudents
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UjianScreen(
    ujianList: List<UjianWithStudents>,
    mataPelajaranList: List<MataPelajaran>,
    siswaList: List<Siswa>,
    onAddUjian: (UjianWithStudents) -> Unit,
    onDeleteUjian: (Int) -> Unit,
    getNextUjianId: () -> Int,
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
                value = ujianList.size.toString(),
                icon = Icons.Default.Menu,
                color = Color(0xFF1976D2),
                modifier = Modifier.weight(1f)
            )
            StatCard(
                title = "Mendatang",
                value = ujianList.count { it.ujian.tanggal.isAfter(LocalDateTime.now()) }.toString(),
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
            items(ujianList.sortedByDescending { it.ujian.tanggal }) { ujianData ->
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
            mataPelajaranList = mataPelajaranList,
            siswaList = siswaList,
            getNextUjianId = getNextUjianId,
            onDismiss = { showAddDialog = false },
            onConfirm = { ujian ->
                onAddUjian(ujian)
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
                        onDeleteUjian(ujian.ujian.idUjian)
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
    mataPelajaranList: List<MataPelajaran>,
    siswaList: List<Siswa>,
    getNextUjianId: () -> Int,
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
                                mataPelajaranList.forEach { matpel ->
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
                                val nextId = getNextUjianId()
                                val pesertaList = selectedSiswaList.map { siswa ->
                                    val nilai = (60..100).random()
                                    Peserta(
                                        idUjian = nextId,
                                        nis = siswa.nis,
                                        namaSiswa = siswa.nama,
                                        nilai = nilai,
                                        isLulus = nilai >= 60
                                    )
                                }

                                val ujianBaru = UjianWithStudents(
                                    ujian = Ujian(
                                        idUjian = nextId,
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
                        items(siswaList) { siswa ->
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