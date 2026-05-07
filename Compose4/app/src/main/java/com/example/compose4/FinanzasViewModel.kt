package com.example.compose4

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class FinanzasViewModel @Inject constructor(
    private val repo: TransaccionRepository
) : ViewModel() {

    val transacciones = repo.getAll().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun agregarTransaccion(descripcion: String, monto: Double, tipo: String, categoria: String) {
        viewModelScope.launch {
            repo.insertar(
                TransaccionEntity(
                    descripcion = descripcion,
                    monto = monto,
                    tipo = tipo,
                    categoria = categoria
                )
            )
        }
    }

    fun eliminarTransaccion(transaccion: TransaccionEntity) {
        viewModelScope.launch {
            repo.eliminar(transaccion)
        }
    }
}
