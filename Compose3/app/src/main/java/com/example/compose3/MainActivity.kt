package com.example.compose3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.compose3.ui.theme.Compose3Theme
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Compose3Theme {
                val context = LocalContext.current
                val db = WeatherDatabase.getDatabase(context)
                val repository = WeatherRepository()
                val factory = WeatherViewModelFactory(repository, db.searchHistoryDao())
                val viewModel: WeatherViewModel = viewModel(factory = factory)
                
                WeatherScreen(viewModel)
            }
        }
    }
}

class WeatherViewModelFactory(
    private val repository: WeatherRepository,
    private val dao: SearchHistoryDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WeatherViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return WeatherViewModel(repository, dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class WeatherRepository {
    suspend fun getWeather(city: String): WeatherData {
        delay(1000)
        // Simulamos condiciones basadas en la entrada para variar la UI
        val condition = when {
            city.contains("noche", true) -> WeatherCondition.NIGHT
            city.contains("lluvia", true) -> WeatherCondition.RAINY
            else -> WeatherCondition.SUNNY
        }
        return WeatherData(
            city = city.replaceFirstChar { it.uppercase() },
            temperature = (20..35).random(),
            description = when(condition) {
                WeatherCondition.SUNNY -> "Soleado"
                WeatherCondition.RAINY -> "Lluvia ligera"
                WeatherCondition.NIGHT -> "Despejado"
            },
            humidity = "${(40..90).random()}%",
            wind = "${(5..25).random()} km/h",
            condition = condition
        )
    }
}

sealed class WeatherUiState {
    object Idle : WeatherUiState()
    object Loading : WeatherUiState()
    data class Success(val data: WeatherData) : WeatherUiState()
    data class Error(val message: String) : WeatherUiState()
}

class WeatherViewModel(
    private val repository: WeatherRepository,
    private val dao: SearchHistoryDao
) : ViewModel() {
    
    private val _uiState = MutableStateFlow<WeatherUiState>(WeatherUiState.Idle)
    val uiState: StateFlow<WeatherUiState> = _uiState.asStateFlow()

    val history: StateFlow<List<String>> = dao.getRecentHistory()
        .map { list -> list.map { it.city } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun loadWeather(city: String) {
        if (city.isBlank()) return
        viewModelScope.launch {
            _uiState.value = WeatherUiState.Loading
            try {
                val data = repository.getWeather(city)
                _uiState.value = WeatherUiState.Success(data)
                // Persistimos en Room la ciudad buscada exitosamente
                dao.insertSearch(SearchHistoryEntity(city = data.city))
            } catch (e: Exception) {
                _uiState.value = WeatherUiState.Error(e.message ?: "Error al obtener clima")
            }
        }
    }
}

@Composable
fun WeatherScreen(viewModel: WeatherViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val history by viewModel.history.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    val data = (uiState as? WeatherUiState.Success)?.data
    
    val topColor by animateColorAsState(
        targetValue = when (data?.condition) {
            WeatherCondition.SUNNY -> Color(0xFFFF9800)
            WeatherCondition.RAINY -> Color(0xFF2196F3)
            WeatherCondition.NIGHT -> Color(0xFF673AB7)
            null -> MaterialTheme.colorScheme.surfaceVariant
        },
        animationSpec = tween(1000), label = "top"
    )

    val bottomColor by animateColorAsState(
        targetValue = when (data?.condition) {
            WeatherCondition.SUNNY -> Color(0xFFFFEB3B)
            WeatherCondition.RAINY -> Color(0xFF90CAF9)
            WeatherCondition.NIGHT -> Color(0xFF311B92)
            null -> MaterialTheme.colorScheme.surface
        },
        animationSpec = tween(1000), label = "bottom"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(topColor, bottomColor)))
            .systemBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Buscador Material 3
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Buscar ciudad...", color = Color.White.copy(alpha = 0.7f)) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color.White) },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(Icons.Default.Clear, contentDescription = null, tint = Color.White)
                        }
                    }
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = Color.White.copy(alpha = 0.5f),
                    cursorColor = Color.White
                ),
                shape = RoundedCornerShape(16.dp),
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = {
                    viewModel.loadWeather(searchQuery)
                    focusManager.clearFocus()
                })
            )

            // Historial de chips (LazyRow)
            if (history.isNotEmpty()) {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(history) { city ->
                        SuggestionChip(
                            onClick = { 
                                searchQuery = city
                                viewModel.loadWeather(city)
                                focusManager.clearFocus()
                            },
                            label = { Text(city, color = Color.White) },
                            colors = SuggestionChipDefaults.suggestionChipColors(
                                containerColor = Color.White.copy(alpha = 0.2f)
                            ),
                            border = SuggestionChipDefaults.suggestionChipBorder(
                                enabled = true,
                                borderColor = Color.White.copy(alpha = 0.3f),
                                borderWidth = 1.dp
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Estado de la UI
            when (val state = uiState) {
                is WeatherUiState.Loading -> {
                    Box(Modifier.weight(1f), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = Color.White)
                    }
                }
                is WeatherUiState.Success -> {
                    Box(Modifier.weight(1f)) {
                        WeatherContent(state.data)
                    }
                }
                is WeatherUiState.Error -> {
                    Box(Modifier.weight(1f), contentAlignment = Alignment.Center) {
                        Text(text = state.message, color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
                is WeatherUiState.Idle -> {
                    Box(Modifier.weight(1f), contentAlignment = Alignment.Center) {
                        Text(
                            "Busca una ciudad para comenzar",
                            color = Color.White.copy(alpha = 0.7f),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun WeatherContent(data: WeatherData) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = data.city,
                style = MaterialTheme.typography.headlineLarge,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = data.description,
                style = MaterialTheme.typography.titleMedium,
                color = Color.White.copy(alpha = 0.8f)
            )
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            AnimatedWeatherIcon(condition = data.condition)
            Text(
                text = "${data.temperature}°",
                style = MaterialTheme.typography.displayLarge.copy(
                    fontSize = 100.sp,
                    fontWeight = FontWeight.Black
                ),
                color = Color.White
            )
        }

        Row(
            modifier = Modifier.padding(bottom = 32.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            WeatherChip(icon = Icons.Default.WaterDrop, label = data.humidity)
            WeatherChip(icon = Icons.Default.Air, label = data.wind)
        }
    }
}

@Composable
fun AnimatedWeatherIcon(condition: WeatherCondition) {
    val infiniteTransition = rememberInfiniteTransition(label = "weather")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = if (condition == WeatherCondition.SUNNY) 360f else 0f,
        animationSpec = infiniteRepeatable(tween(15000, easing = LinearEasing)),
        label = "rotation"
    )
    val scale by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(tween(2000), RepeatMode.Reverse),
        label = "scale"
    )

    val icon = when (condition) {
        WeatherCondition.SUNNY -> Icons.Default.WbSunny
        WeatherCondition.RAINY -> Icons.Default.Cloud
        WeatherCondition.NIGHT -> Icons.Default.NightsStay
    }

    Icon(
        imageVector = icon,
        contentDescription = null,
        modifier = Modifier
            .size(140.dp)
            .rotate(rotation)
            .graphicsLayer(scaleX = scale, scaleY = scale)
            .padding(16.dp),
        tint = Color.White
    )
}

@Composable
fun WeatherChip(icon: ImageVector, label: String) {
    Surface(
        color = Color.White.copy(alpha = 0.2f),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(imageVector = icon, contentDescription = null, modifier = Modifier.size(18.dp), tint = Color.White)
            Text(text = label, color = Color.White, style = MaterialTheme.typography.labelLarge)
        }
    }
}
