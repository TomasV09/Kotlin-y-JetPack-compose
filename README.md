# Suite de Aplicaciones Android con Kotlin y Jetpack Compose

## 📋 Descripción General

Este repositorio contiene una colección de aplicaciones Android desarrolladas con **Kotlin** y **Jetpack Compose**, un moderno framework declarativo para la construcción de interfaces de usuario nativas en Android. Cada aplicación demuestra las mejores prácticas, patrones de arquitectura y características avanzadas de la plataforma Android.

## 🎯 Objetivos del Proyecto

- Demostrar el uso profesional de **Jetpack Compose** en proyectos reales
- Implementar arquitecturas escalables y mantenibles
- Aplicar patrones de diseño reconocidos en la industria
- Proporcionar ejemplos funcionales de integración con servicios externos
- Facilitar el aprendizaje y referencias para desarrolladores Android

## 📱 Aplicaciones Incluidas

### 1. [Nombre de la App 1]
**Descripción breve:** Propósito principal de la aplicación.
- **Características principales:** 
  - Característica 1
  - Característica 2
  - Característica 3
- **Tecnologías:** Room Database, Retrofit, Coroutines
- **Ubicación:** `/app1`

### 2. [Nombre de la App 2]
**Descripción breve:** Propósito principal de la aplicación.
- **Características principales:** 
  - Característica 1
  - Característica 2
  - Característica 3
- **Tecnologías:** Firebase, Navigation, ViewModel
- **Ubicación:** `/app2`

### 3. [Nombre de la App 3]
**Descripción breve:** Propósito principal de la aplicación.
- **Características principales:** 
  - Característica 1
  - Característica 2
  - Característica 3
- **Tecnologías:** WorkManager, DataStore, Hilt
- **Ubicación:** `/app3`

## 🛠️ Tecnologías y Dependencias

### Stack Tecnológico Principal
- **Lenguaje:** Kotlin 1.9+
- **Framework UI:** Jetpack Compose 1.6+
- **IDE:** Android Studio Giraffe+
- **Gradle:** Kotlin DSL

### Dependencias Clave
```kotlin
// Jetpack Compose
implementation "androidx.compose.ui:ui:1.6.0"
implementation "androidx.compose.material3:material3:1.1.2"
implementation "androidx.compose.foundation:foundation:1.6.0"

// Architecture Components
implementation "androidx.lifecycle:lifecycle-viewmodel-compose:2.6.2"
implementation "androidx.navigation:navigation-compose:2.7.5"

// Data Persistence
implementation "androidx.room:room-runtime:2.6.1"
implementation "androidx.datastore:datastore-preferences:1.0.0"

// Networking
implementation "com.squareup.retrofit2:retrofit:2.10.0"
implementation "com.google.code.gson:gson:2.10.1"

// Concurrency
implementation "org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3"

// Dependency Injection
implementation "com.google.dagger:hilt-android:2.48"

// Testing
testImplementation "junit:junit:4.13.2"
androidTestImplementation "androidx.compose.ui:ui-test-junit4:1.6.0"
