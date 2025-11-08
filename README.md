# Portafolio Mobile - Android App

Aplicación móvil nativa de portafolio personal desarrollada en **Kotlin** con **Jetpack Compose** y arquitectura **MVVM**.

---

## Características Principales

- **Diseño Material 3**: Interfaz moderna siguiendo las guías de Material Design 3
- **Arquitectura MVVM**: Separación clara de responsabilidades y código mantenible
- **Persistencia Local**: Base de datos Room para almacenamiento de proyectos, posts y mensajes
- **Recursos Nativos**:
  - **Cámara**: Captura de imágenes para proyectos usando CameraX
  - **GPS**: Obtención de ubicación en formulario de contacto
- **Formularios Validados**: Validación en tiempo real con retroalimentación visual
- **Navegación Fluida**: Navigation Component con Bottom Navigation Bar
- **Animaciones**: Transiciones suaves y animaciones de entrada
- **Gestión de Estado**: StateFlow para manejo reactivo del estado de la UI
- **Datos de Ejemplo**: Carga automática de proyectos y posts de muestra al instalar

---

## Tecnologías Utilizadas

### Core
- **Kotlin** - Lenguaje de programación
- **Jetpack Compose** - UI moderna y declarativa
- **Material 3** - Sistema de diseño

### Arquitectura y Estructura
- **MVVM** - Patrón de arquitectura
- **Room Database** - Persistencia local
- **Coroutines** - Programación asíncrona
- **StateFlow/Flow** - Gestión reactiva de estado

### Jetpack Components
- **Navigation Compose** - Navegación entre pantallas
- **ViewModel** - Gestión de estado y lógica de negocio
- **Lifecycle** - Manejo del ciclo de vida
- **DataStore** - Almacenamiento de preferencias

### Recursos Nativos
- **CameraX** - Acceso a la cámara del dispositivo
- **Location Services** - Acceso al GPS
- **Permissions** - Gestión de permisos en runtime

### Librerías Adicionales
- **Coil** - Carga de imágenes
- **Material Icons Extended** - Iconografía extendida

---

## Estructura del Proyecto
```
app/src/main/java/duoc/desarrollomobile/portafolio/
├── data/
│   ├── local/
│   │   ├── PortfolioDatabase.kt      # Base de datos Room
│   │   ├── DatabaseSeeder.kt         # Datos iniciales
│   │   ├── ProjectDao.kt             # DAO de proyectos
│   │   ├── PostDao.kt                # DAO de posts
│   │   └── ContactMessageDao.kt      # DAO de mensajes
│   ├── model/
│   │   ├── Project.kt                # Entidad Proyecto
│   │   ├── Post.kt                   # Entidad Post
│   │   └── ContactMessage.kt         # Entidad Mensaje
│   └── repository/
│       ├── ProjectRepository.kt      # Repositorio de proyectos
│       ├── PostRepository.kt         # Repositorio de posts
│       └── ContactMessageRepository.kt
├── viewmodel/
│   ├── ProjectViewModel.kt           # ViewModel de proyectos
│   ├── PostViewModel.kt              # ViewModel de posts
│   └── ContactViewModel.kt           # ViewModel de contacto
├── ui/
│   ├── screens/
│   │   ├── home/
│   │   │   └── HomeScreen.kt         # Pantalla de inicio
│   │   ├── gallery/
│   │   │   ├── GalleryScreen.kt      # Galería de proyectos
│   │   │   └── AddProjectDialog.kt   # Diálogo agregar proyecto
│   │   ├── posts/
│   │   │   ├── PostsScreen.kt        # Lista de posts
│   │   │   └── AddPostDialog.kt      # Diálogo agregar post
│   │   └── contact/
│   │       └── ContactScreen.kt      # Formulario de contacto
│   ├── navigation/
│   │   ├── Screen.kt                 # Definición de rutas
│   │   └── NavGraph.kt               # Configuración de navegación
│   └── theme/
│       └── Theme.kt                  # Tema Material 3
├── utils/
│   ├── PermissionHelper.kt           # Helper de permisos
│   ├── CameraHelper.kt               # Helper de cámara
│   └── LocationHelper.kt             # Helper de GPS
├── MainActivity.kt                    # Actividad principal
└── PortfolioApplication.kt           # Clase Application
```

---

## Funcionalidades

### 1. Home (Inicio)
- Presentación personal con información del desarrollador
- Tarjetas de estadísticas (proyectos, posts, tecnologías)
- Accesos rápidos a otras secciones
- Visualización de habilidades técnicas con progreso
- Animaciones de entrada suaves

### 2. Gallery (Galería de Proyectos)
- Lista de proyectos con imágenes
- **Captura de fotos** con la cámara del dispositivo
- Filtrado por categorías (Web, Mobile, Backend, etc.)
- Marcar proyectos como favoritos
- Agregar/Editar/Eliminar proyectos
- Validación de formularios en tiempo real

### 3. Posts (Blog)
- Lista de publicaciones con resúmenes
- Búsqueda de posts por título o contenido
- Filtrado por tags
- Contador de vistas
- Agregar/Editar/Eliminar posts
- Formulario completo con validaciones:
  - Título (5-100 caracteres)
  - Resumen (10-200 caracteres)
  - Contenido (mínimo 20 caracteres)
  - Autor (solo letras)
  - Tags (separados por coma)

### 4. Contact (Contacto)
- Formulario de contacto profesional
- **Captura de ubicación GPS** del usuario
- Validaciones en tiempo real:
  - Nombre (mínimo 3 caracteres, solo letras)
  - Email (formato válido)
  - Teléfono (8-15 dígitos, opcional)
  - Asunto (mínimo 5 caracteres)
  - Mensaje (mínimo 10 caracteres)
- Visualización de coordenadas GPS
- Almacenamiento local de mensajes

---

## Permisos

La aplicación solicita los siguientes permisos:

- **CAMERA**: Para capturar fotos de proyectos
- **READ_MEDIA_IMAGES**: Para acceder a imágenes guardadas
- **ACCESS_FINE_LOCATION**: Para obtener ubicación precisa
- **ACCESS_COARSE_LOCATION**: Para obtener ubicación aproximada

Todos los permisos se solicitan en runtime y son manejados de forma segura.

---

## Arquitectura MVVM

El proyecto sigue el patrón **Model-View-ViewModel** para una arquitectura limpia y escalable:

### Model (Modelo)
- **Entities**: Clases de datos (`Project`, `Post`, `ContactMessage`)
- **DAOs**: Interfaces de acceso a datos con consultas SQL
- **Database**: Gestor de Room Database
- **Repositories**: Capa de abstracción para acceso a datos

### View (Vista)
- **Screens**: Composables de Jetpack Compose
- **Components**: Componentes UI reutilizables
- **Navigation**: Configuración de navegación

### ViewModel
- Gestiona el estado de la UI con `StateFlow`
- Ejecuta operaciones en background con `Coroutines`
- Centraliza la lógica de validación
- Comunica la View con el Repository

**Flujo de datos:**
```
View (Compose) ←→ ViewModel ←→ Repository ←→ DAO ←→ Room Database
```

---

## Diseño

### Material Design 3
- Tema dinámico con soporte para modo claro/oscuro
- Paleta de colores coherente
- Componentes M3: Cards, Buttons, TextFields, NavigationBar
- Elevación y sombras sutiles
- Tipografía escalable y accesible

### Principios de Usabilidad
- Navegación intuitiva con bottom navigation
- Feedback visual inmediato en formularios
- Mensajes de error claros y específicos
- Loading states durante operaciones asíncronas
- Iconografía consistente

---

## Base de Datos

### Tablas Room

**projects**
```kotlin
id (PK), title, description, imageUri, technologies, 
githubUrl, category, isFavorite, createdAt
```

**posts**
```kotlin
id (PK), title, content, summary, author, imageUri, 
tags, isPublished, viewCount, createdAt
```

**contact_messages**
```kotlin
id (PK), name, email, phone, subject, message, 
latitude, longitude, isRead, createdAt
```
