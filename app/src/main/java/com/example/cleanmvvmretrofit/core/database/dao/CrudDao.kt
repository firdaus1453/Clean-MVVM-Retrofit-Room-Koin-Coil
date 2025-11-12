package com.example.cleanmvvmretrofit.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.cleanmvvmretrofit.core.database.entity.CrudEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CrudDao {

    @Query("SELECT * FROM crud ORDER BY id ASC")
    fun getAll(): Flow<List<CrudEntity>>

    @Query("SELECT * FROM crud WHERE id = :id")
    suspend fun getCrudById(id: Int): CrudEntity?

    @Query("SELECT * FROM crud WHERE LOWER(name) LIKE LOWER(:query)")
    suspend fun searchCrudByName(query: String): List<CrudEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCruds(pokemons: List<CrudEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCrud(pokemon: CrudEntity)

    @Query("DELETE FROM crud")
    suspend fun deleteAll()
}