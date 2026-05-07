package com.example.compose2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.compose2.ui.theme.Compose2Theme

//Data class Tarea(id, titulo, completada)
data class Tarea(
    val id: Int,
    val titulo: String,
    val completada: Boolean = false
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Compose2Theme {
                AppNavigation()
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    // Estado de las tareas en un nivel superior para persistencia simple durante la sesión
    var tareas by remember { mutableStateOf(listOf<Tarea>()) }

    // Configurar NavHost con dos rutas: lista y detalle
    NavHost(navController = navController, startDestination = "lista") {
        composable("lista") {
            ListaTareasScreen(
                tareas = tareas,
                onTareasUpdate = { tareas = it },
                onNavigateToDetail = { id ->
                    // 12. Pasar el id de tarea como argumento de navegación
                    navController.navigate("detalle/$id")
                }
            )
        }
        composable(
            route = "detalle/{tareaId}",
            arguments = listOf(navArgument("tareaId") { type = NavType.IntType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("tareaId") ?: 0
            val tarea = tareas.find { it.id == id }
            DetalleTareaScreen(tarea = tarea, onBack = { navController.popBackStack() })
        }
    }
}

//Pantalla ListaTareasScreen con LazyColumn
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaTareasScreen(
    tareas: List<Tarea>,
    onTareasUpdate: (List<Tarea>) -> Unit,
    onNavigateToDetail: (Int) -> Unit
) {
    var mostrarDialogo by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Mis Tareas") })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { mostrarDialogo = true }) {
                Icon(Icons.Default.Add, contentDescription = "Agregar tarea")
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            items(tareas, key = { it.id }) { tarea ->
                TareaItem(
                    tarea = tarea,
                    onCheckedChange = { checked ->
                        onTareasUpdate(tareas.map {
                            if (it.id == tarea.id) it.copy(completada = checked) else it
                        })
                    },
                    onDelete = {
                        onTareasUpdate(tareas.filter { it.id != tarea.id })
                    },
                    onClick = { onNavigateToDetail(tarea.id) }
                )
            }
        }

        // Tarea con AlertDialog y TextField
        if (mostrarDialogo) {
            NuevaTareaDialog(
                onDismiss = { mostrarDialogo = false },
                onConfirm = { titulo ->
                    val nuevoId = (tareas.maxOfOrNull { it.id } ?: 0) + 1
                    onTareasUpdate(tareas + Tarea(nuevoId, titulo))
                    mostrarDialogo = false
                }
            )
        }
    }
}

@Composable
fun TareaItem(
    tarea: Tarea,
    onCheckedChange: (Boolean) -> Unit,
    onDelete: () -> Unit,
    onClick: () -> Unit
) {
    // Animación de opacidad (fade) cuando la tarea está completada
    val alpha by animateFloatAsState(
        targetValue = if (tarea.completada) 0.5f else 1f,
        label = "fadeAnimation"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .alpha(alpha)
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = tarea.completada,
                onCheckedChange = onCheckedChange
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = tarea.titulo,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodyLarge.copy(
                    textDecoration = if (tarea.completada) TextDecoration.LineThrough else TextDecoration.None
                )
            )
            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Eliminar tarea"
                )
            }
        }
    }
}

@Composable
fun NuevaTareaDialog(onDismiss: () -> Unit, onConfirm: (String) -> Unit) {
    var texto by remember { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Nueva Tarea") },
        text = {
            TextField(
                value = texto,
                onValueChange = { texto = it },
                label = { Text("Título de la tarea") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
        },
        confirmButton = {
            Button(
                onClick = { if (texto.isNotBlank()) onConfirm(texto) },
                enabled = texto.isNotBlank()
            ) {
                Text("Agregar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleTareaScreen(tarea: Tarea?, onBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle de Tarea") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            if (tarea != null) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "ID: ${tarea.id}", style = MaterialTheme.typography.labelSmall)
                    Text(text = tarea.titulo, style = MaterialTheme.typography.headlineMedium)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = if (tarea.completada) "Estado: Completada" else "Estado: Pendiente",
                        color = if (tarea.completada) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                    )
                }
            } else {
                Text("Tarea no encontrada")
            }
        }
    }
}
