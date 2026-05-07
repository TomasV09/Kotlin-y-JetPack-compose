package com.example.compose4

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(viewModel: FinanzasViewModel = hiltViewModel()) {
    val transacciones by viewModel.transacciones.collectAsState()
    var mostrarDialogo by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Control de Finanzas") })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { mostrarDialogo = true }) {
                Icon(Icons.Default.Add, contentDescription = "Agregar")
            }
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize()) {
            // Resumen de saldo (opcional)
            Card(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            ) {
                val total = transacciones.sumOf { if (it.tipo == "INGRESO") it.monto else -it.monto }
                Text(
                    text = "Saldo Total: $${String.format("%.2f", total)}",
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.headlineSmall
                )
            }

            LazyColumn {
                items(transacciones) { item ->
                    TransactionItem(item, onDelete = { viewModel.eliminarTransaccion(item) })
                }
            }
        }

        if (mostrarDialogo) {
            AddTransactionDialog(
                onDismiss = { mostrarDialogo = false },
                onConfirm = { desc, monto, tipo, cat ->
                    viewModel.agregarTransaccion(desc, monto, tipo, cat)
                    mostrarDialogo = false
                }
            )
        }
    }
}

@Composable
fun TransactionItem(item: TransaccionEntity, onDelete: () -> Unit) {
    ListItem(
        headlineContent = { Text(item.descripcion) },
        supportingContent = { Text("${item.categoria} - ${if (item.tipo == "INGRESO") "Ingreso" else "Gasto"}") },
        trailingContent = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "${if (item.tipo == "INGRESO") "+" else "-"}$${item.monto}",
                    color = if (item.tipo == "INGRESO") Color(0xFF4CAF50) else Color.Red,
                    style = MaterialTheme.typography.bodyLarge
                )
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = Color.Gray)
                }
            }
        }
    )
}

@Composable
fun AddTransactionDialog(onDismiss: () -> Unit, onConfirm: (String, Double, String, String) -> Unit) {
    var desc by remember { mutableStateOf("") }
    var monto by remember { mutableStateOf("") }
    var tipo by remember { mutableStateOf("INGRESO") }
    var cat by remember { mutableStateOf("General") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Nueva Transacción") },
        text = {
            Column {
                TextField(value = desc, onValueChange = { desc = it }, label = { Text("Descripción") })
                TextField(value = monto, onValueChange = { monto = it }, label = { Text("Monto") })
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(selected = tipo == "INGRESO", onClick = { tipo = "INGRESO" })
                    Text("Ingreso")
                    Spacer(modifier = Modifier.width(8.dp))
                    RadioButton(selected = tipo == "GASTO", onClick = { tipo = "GASTO" })
                    Text("Gasto")
                }
                TextField(value = cat, onValueChange = { cat = it }, label = { Text("Categoría") })
            }
        },
        confirmButton = {
            Button(onClick = { onConfirm(desc, monto.toDoubleOrNull() ?: 0.0, tipo, cat) }) {
                Text("Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}
