package com.example.cleanmvvmretrofit.home.presentation.matapelajaran

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.cleanmvvmretrofit.core.presentation.designsystem.component.StatCard
import com.example.cleanmvvmretrofit.home.domain.MataPelajaran
import com.example.cleanmvvmretrofit.home.domain.UjianWithStudents

@Composable
fun MataPelajaranScreen(
    mataPelajaranList: List<MataPelajaran>,
    ujianList: List<UjianWithStudents>,
    onAddMataPelajaran: (MataPelajaran) -> Unit,
    onUpdateMataPelajaran: (MataPelajaran) -> Unit,
    onDeleteMataPelajaran: (Int) -> Unit,
    getNextMatpelId: () -> Int
) {
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
            value = mataPelajaranList.size.toString(),
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
            items(mataPelajaranList) { matpel ->
                MataPelajaranCard(
                    mataPelajaran = matpel,
                    ujianCount = ujianList.count { it.ujian.idMatpel == matpel.idMatpel },
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
                    onUpdateMataPelajaran(MataPelajaran(matpelToEdit!!.idMatpel, namaMatpel))
                } else {
                    onAddMataPelajaran(MataPelajaran(getNextMatpelId(), namaMatpel))
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
                        onDeleteMataPelajaran(matpel.idMatpel)
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
