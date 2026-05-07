package com.example.compose4

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TransaccionDao {
    @Query("SELECT * FROM transacciones ORDER BY fecha DESC")
    fun getAll(): Flow<List<TransaccionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(t: TransaccionEntity)

    @Delete
    suspend fun eliminar(t: TransaccionEntity)

    @Query("SELECT SUM(monto) FROM transacciones WHERE tipo = :tipo")
    suspend fun getTotalPorTipo(tipo: String): Double?
}