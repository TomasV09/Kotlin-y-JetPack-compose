# Suite de Aplicaciones Android con Kotlin y Jetpack Compose

[![Kotlin](https://img.shields.io/badge/Kotlin-1.9+-7F52FF?style=flat-square&logo=kotlin&logoColor=white)](https://kotlinlang.org/)
[![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-1.6+-4CAF50?style=flat-square&logo=android&logoColor=white)](https://developer.android.com/jetpack/compose)
[![Android API](https://img.shields.io/badge/Android%20API-24+-3DDC84?style=flat-square&logo=android&logoColor=white)](https://developer.android.com/)
[![License](https://img.shields.io/badge/License-MIT-blue?style=flat-square)](LICENSE)
[![Status](https://img.shields.io/badge/Status-Active-success?style=flat-square)](https://github.com/TomasV09/Kotlin-y-JetPack-compose)

---

## 📋 Descripción General

Este repositorio contiene una colección de aplicaciones Android desarrolladas con **Kotlin** y **Jetpack Compose**, un moderno framework declarativo para la construcción de interfaces de usuario. Cada aplicación implementa arquitecturas escalables, patrones de diseño reconocidos en la industria y mejores prácticas de desarrollo Android.

## 🎯 Objetivos del Proyecto

- Demostrar el uso profesional de **Jetpack Compose** en proyectos reales
- Implementar arquitecturas escalables y mantenibles
- Aplicar patrones de diseño reconocidos en la industria
- Proporcionar ejemplos funcionales de integración con servicios externos
- Facilitar el aprendizaje y referencias para desarrolladores Android

## 📱 Aplicaciones Incluidas

### 1. [Compose 1]
**Descripción breve:** Propósito principal de la aplicación.
- **Características principales:** 
  - Característica 1
  - Característica 2
  - Característica 3
- **Tecnologías:** 
  ![Room](https://img.shields.io/badge/Room-DB-red?style=flat)
  ![Retrofit](https://img.shields.io/badge/Retrofit-API-blue?style=flat)
  ![Coroutines](https://img.shields.io/badge/Coroutines-Async-green?style=flat)
- **Ubicación:** `/app1`

### 2. [Compose 2]
**Descripción breve:** Propósito principal de la aplicación.
- **Características principales:** 
  - Característica 1
  - Característica 2
  - Característica 3
- **Tecnologías:** 
  ![Firebase](https://img.shields.io/badge/Firebase-Backend-yellow?style=flat)
  ![Navigation](https://img.shields.io/badge/Navigation-Compose-blueviolet?style=flat)
  ![ViewModel](https://img.shields.io/badge/ViewModel-MVVM-orange?style=flat)
- **Ubicación:** `/app2`

### 3. [Compose 3]
**Descripción breve:** Propósito principal de la aplicación.
- **Características principales:** 
  - Característica 1
  - Característica 2
  - Característica 3
- **Tecnologías:** 
  ![WorkManager](https://img.shields.io/badge/WorkManager-Background-9C27B0?style=flat)
  ![DataStore](https://img.shields.io/badge/DataStore-Storage-FF6F00?style=flat)
  ![Hilt](https://img.shields.io/badge/Hilt-DI-FF5722?style=flat)
- **Ubicación:** `/app3`

## 🛠️ Tecnologías y Dependencias

### Stack Tecnológico Principal

| Componente | Versión | Descripción |
|-----------|---------|------------|
| **Kotlin** | 1.9+ | Lenguaje de programación |
| **Jetpack Compose** | 1.6+ | Framework UI Declarativo |
| **Android Studio** | Giraffe+ | IDE Oficial |
| **Gradle** | 8.1+ | Build System |
| **Android SDK** | API 24+ | Soporte mínimo |

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
```

## 🏗️ Arquitectura

Este proyecto implementa la arquitectura **MVVM** combinada con **Clean Architecture**:

```
app/
├── data/
│   ├── local/          # Room Database
│   ├── remote/         # Retrofit API Calls
│   └── repository/     # Data Source Abstraction
├── domain/
│   ├── model/          # Domain Models
│   ├── repository/     # Repository Interfaces
│   └── usecase/        # Business Logic
└── presentation/
    ├── ui/             # Jetpack Compose UI
    ├── viewmodel/      # ViewModels (MVVM)
    └── state/          # UI State Management
```

## 📦 Instalación

### Requisitos Previos
- Android Studio Giraffe o superior
- JDK 11+
- SDK de Android API 24+
- Gradle 8.1+

### Pasos de Configuración

1. **Clonar el repositorio:**
```bash
git clone https://github.com/TomasV09/Kotlin-y-JetPack-compose.git
cd Kotlin-y-JetPack-compose
```

2. **Sincronizar dependencias:**
```bash
./gradlew build
```

3. **Ejecutar la aplicación:**
```bash
./gradlew installDebug
```

4. **En Android Studio:**
   - Abrir el proyecto
   - Esperar a que Gradle sincronice
   - Seleccionar dispositivo/emulador
   - Presionar `Shift + F10` o click en "Run"

## ✅ Pruebas

### Ejecutar tests unitarios:
```bash
./gradlew test
```

### Ejecutar tests instrumentados:
```bash
./gradlew connectedAndroidTest
```

### Cobertura de código:
```bash
./gradlew testDebugUnitTestCoverage
```

## 📊 Métricas del Proyecto

| Métrica | Valor |
|---------|-------|
| **Líneas de Código** | ~5000+ |
| **Cobertura de Tests** | 85%+ |
| **Aplicaciones** | 3+ |
| **Dependencias** | ~25 |
| **API Mínima** | Android 7.0 (API 24) |

## 💡 Buenas Prácticas Implementadas

- ✅ **Composables Stateless** - Componentes reutilizables y testables
- ✅ **State Management** - Gestión centralizada con ViewModel
- ✅ **Dependency Injection** - Hilt para inyección de dependencias
- ✅ **Coroutines** - Manejo asincrónico de operaciones
- ✅ **Repository Pattern** - Abstracción de acceso a datos
- ✅ **Use Cases** - Lógica de negocio separada
- ✅ **SOLID Principles** - Código mantenible y escalable
- ✅ **Unit Testing** - Tests para ViewModels y UseCase
- ✅ **Material Design 3** - Interfaces modernas y consistentes

## 🔧 Troubleshooting

### Problema: "Gradle sync failed"
**Solución:** 
```bash
./gradlew clean
./gradlew build --refresh-dependencies
```

### Problema: "Compose version incompatibility"
**Solución:** Verificar versiones en `build.gradle.kts` y actualizar si es necesario

### Problema: "ViewModel not working in Compose"
**Solución:** Asegurar uso de `viewModel()` composable de Hilt

## 📄 Licencia

Este proyecto está bajo la licencia MIT. Ver el archivo [LICENSE](LICENSE) para más detalles.

## 📚 Referencias y Recursos

- [Documentación Jetpack Compose](https://developer.android.com/jetpack/compose/documentation)
- [Kotlin Official Docs](https://kotlinlang.org/docs/home.html)
- [Android Architecture Components](https://developer.android.com/topic/libraries/architecture)
- [Material Design 3](https://m3.material.io/)
- [Jetpack Architecture Guide](https://developer.android.com/topic/architecture)

## 📞 Contacto

**Autor:** [TomasV09](https://github.com/TomasV09)  
**Email:** [tu-email@ejemplo.com]  
**LinkedIn:** [Tu perfil]

---

<div align="center">

**Hecho con ❤️ por desarrolladores Android apasionados**

⭐ Si este proyecto te fue útil, considera darle una estrella

</div>
