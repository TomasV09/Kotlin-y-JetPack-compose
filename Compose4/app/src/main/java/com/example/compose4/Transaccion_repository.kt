package com.example.compose4

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TransaccionRepository @Inject constructor(
    private val dao: TransaccionDao
) {
    fun getAll(): Flow<List<TransaccionEntity>> = dao.getAll()

    suspend fun insertar(t: TransaccionEntity) = dao.insertar(t)

    suspend fun eliminar(t: TransaccionEntity) = dao.eliminar(t)
}
