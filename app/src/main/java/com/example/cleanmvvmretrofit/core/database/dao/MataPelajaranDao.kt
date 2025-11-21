package com.example.cleanmvvmretrofit.core.database.dao

import androidx.room.*
import com.example.cleanmvvmretrofit.core.database.entity.MataPelajaranEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MataPelajaranDao {

    @Query("SELECT * FROM mata_pelajaran ORDER BY namaMatpel ASC")
    fun getAllMataPelajaran(): Flow<List<MataPelajaranEntity>>

    @Query("SELECT * FROM mata_pelajaran WHERE idMatpel = :id")
    suspend fun getMataPelajaranById(id: Int): MataPelajaranEntity?

    @Query("SELECT * FROM mata_pelajaran WHERE LOWER(namaMatpel) LIKE LOWER(:query)")
    suspend fun searchMataPelajaran(query: String): List<MataPelajaranEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMataPelajaran(mataPelajaran: MataPelajaranEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMataPelajaranList(mataPelajaranList: List<MataPelajaranEntity>)

    @Update
    suspend fun updateMataPelajaran(mataPelajaran: MataPelajaranEntity)

    @Delete
    suspend fun deleteMataPelajaran(mataPelajaran: MataPelajaranEntity)

    @Query("DELETE FROM mata_pelajaran WHERE idMatpel = :id")
    suspend fun deleteMataPelajaranById(id: Int)

    @Query("DELETE FROM mata_pelajaran")
    suspend fun deleteAllMataPelajaran()
}
