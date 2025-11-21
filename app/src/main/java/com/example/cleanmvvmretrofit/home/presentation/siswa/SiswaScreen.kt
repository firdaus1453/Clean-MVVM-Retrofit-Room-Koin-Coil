package com.example.cleanmvvmretrofit.home.presentation.siswa
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
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
import com.example.cleanmvvmretrofit.home.domain.Siswa

@Composable
fun SiswaScreen(
    siswaList: List<Siswa>,
    onAddSiswa: (Siswa) -> Unit,
    onUpdateSiswa: (String, Siswa) -> Unit,
    onDeleteSiswa: (String) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var showAddDialog by remember { mutableStateOf(false) }
    var siswaToEdit by remember { mutableStateOf<Siswa?>(null) }
    var siswaToDelete by remember { mutableStateOf<Siswa?>(null) }

    val filteredSiswa = remember(searchQuery, siswaList.size) {
        if (searchQuery.isEmpty()) {
            siswaList
        } else {
            siswaList.filter {
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
                value = siswaList.size.toString(),
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
                    onUpdateSiswa(siswaToEdit!!.nis, Siswa(nis, nama, alamat))
                } else {
                    onAddSiswa(Siswa(nis, nama, alamat))
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
                        onDeleteSiswa(siswa.nis)
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