package duoc.desarrollomobile.portafolio.data.local

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import duoc.desarrollomobile.portafolio.data.model.Post
import duoc.desarrollomobile.portafolio.data.model.Project
import kotlinx.coroutines.flow.first

/**
 * Clase para poblar la base de datos con datos iniciales
 * Solo se ejecuta la primera vez que se abre la app
 */
class DatabaseSeeder(private val context: Context) {

    private val Context.dataStore by preferencesDataStore(name = "app_preferences")
    private val IS_FIRST_LAUNCH = booleanPreferencesKey("is_first_launch")

    /**
     * Verifica si es el primer lanzamiento
     */
    suspend fun isFirstLaunch(): Boolean {
        val preferences = context.dataStore.data.first()
        return preferences[IS_FIRST_LAUNCH] ?: true
    }

    /**
     * Marca que ya no es el primer lanzamiento
     */
    suspend fun markFirstLaunchComplete() {
        context.dataStore.edit { preferences ->
            preferences[IS_FIRST_LAUNCH] = false
        }
    }

    /**
     * Puebla la base de datos con datos iniciales
     */
    suspend fun seedDatabase(database: PortfolioDatabase) {
        if (isFirstLaunch()) {
            // Insertar proyectos de ejemplo
            seedProjects(database.projectDao())

            // Insertar posts de ejemplo
            seedPosts(database.postDao())

            // Marcar como completado
            markFirstLaunchComplete()
        }
    }

    /**
     * Crea proyectos de ejemplo basados en el portafolio
     */
    private suspend fun seedProjects(projectDao: ProjectDao) {
        val projects = listOf(
            Project(
                title = "Portafolio Web React",
                description = "Portafolio personal desarrollado con React, React Router y Bootstrap. Incluye secciones de inicio, galería de proyectos, blog y formulario de contacto con diseño responsive.",
                imageUri = null,
                technologies = "React, JavaScript, React Router, Bootstrap, CSS",
                githubUrl = "https://github.com/tu-usuario/portfolio-react",
                category = "Web",
                isFavorite = true
            ),
            Project(
                title = "App Móvil de Portafolio",
                description = "Aplicación móvil nativa de portafolio desarrollada en Kotlin con Jetpack Compose. Implementa arquitectura MVVM, persistencia local con Room Database y acceso a recursos nativos como cámara y GPS.",
                imageUri = null,
                technologies = "Kotlin, Jetpack Compose, Room, MVVM, CameraX, Location Services",
                githubUrl = "https://github.com/tu-usuario/portfolio-android",
                category = "Mobile",
                isFavorite = true
            ),
            Project(
                title = "Sistema de Gestión de Tareas",
                description = "Aplicación web para gestión de tareas y proyectos. Permite crear, editar y organizar tareas con prioridades, fechas límite y categorías. Incluye sistema de notificaciones.",
                imageUri = null,
                technologies = "React, Node.js, Express, MongoDB, Material-UI",
                githubUrl = null,
                category = "Web",
                isFavorite = false
            ),
            Project(
                title = "API REST E-commerce",
                description = "API RESTful para plataforma de comercio electrónico. Implementa autenticación JWT, gestión de productos, carrito de compras y procesamiento de pagos con integración de Stripe.",
                imageUri = null,
                technologies = "Node.js, Express, MongoDB, JWT, Stripe API",
                githubUrl = null,
                category = "Backend",
                isFavorite = false
            ),
            Project(
                title = "Dashboard Analytics",
                description = "Panel de control administrativo con visualización de datos en tiempo real. Incluye gráficos interactivos, filtros avanzados y exportación de reportes en PDF y Excel.",
                imageUri = null,
                technologies = "React, Chart.js, Material-UI, Redux, Axios",
                githubUrl = null,
                category = "Web",
                isFavorite = false
            ),
            Project(
                title = "App de Clima",
                description = "Aplicación móvil que muestra el clima actual y pronóstico extendido usando la API de OpenWeather. Incluye búsqueda de ciudades, favoritos y notificaciones de alertas meteorológicas.",
                imageUri = null,
                technologies = "Kotlin, Retrofit, Coroutines, Material Design",
                githubUrl = null,
                category = "Mobile",
                isFavorite = false
            )
        )

        projects.forEach { project ->
            projectDao.insert(project)
        }
    }

    /**
     * Crea posts de ejemplo
     */
    private suspend fun seedPosts(postDao: PostDao) {
        val posts = listOf(
            Post(
                title = "Introducción a Jetpack Compose",
                content = """
                    Jetpack Compose es el moderno kit de herramientas de Android para crear interfaces de usuario nativas. Simplifica y acelera el desarrollo de UI en Android con menos código, herramientas potentes y APIs intuitivas de Kotlin.
                    
                    ¿Por qué usar Compose?
                    
                    1. Menos código: Escribe menos código con funciones componibles que describen la UI.
                    
                    2. Intuitivo: Solo describe tu UI y Compose se encarga del resto. A medida que cambia el estado de la app, tu UI se actualiza automáticamente.
                    
                    3. Acelera el desarrollo: Compatible con todo tu código existente para que puedas adoptarlo cuando quieras.
                    
                    4. Potente: Crea apps hermosas con acceso directo a las APIs de Android y soporte integrado para Material Design.
                    
                    Compose usa un paradigma declarativo, lo que significa que describes cómo debe verse tu UI en función del estado actual, en lugar de describir cómo transformar la UI de un estado a otro.
                """.trimIndent(),
                summary = "Descubre las ventajas de usar Jetpack Compose para el desarrollo de interfaces modernas en Android.",
                author = "Desarrollador Mobile",
                imageUri = null,
                tags = "android, kotlin, jetpack-compose, ui, mobile",
                isPublished = true,
                viewCount = 125
            ),
            Post(
                title = "Arquitectura MVVM en Android",
                content = """
                    La arquitectura MVVM (Model-View-ViewModel) es un patrón de diseño que separa la lógica de presentación de la lógica de negocio de tu aplicación Android.
                    
                    Componentes principales:
                    
                    Model: Representa los datos y la lógica de negocio. Incluye las entidades de base de datos, repositorios y fuentes de datos.
                    
                    View: La interfaz de usuario. En Android puede ser una Activity, Fragment o composables de Jetpack Compose. Solo se encarga de mostrar datos y capturar eventos del usuario.
                    
                    ViewModel: Actúa como puente entre la View y el Model. Mantiene el estado de la UI y sobrevive a cambios de configuración.
                    
                    Ventajas de MVVM:
                    
                    - Separación de responsabilidades clara
                    - Facilita las pruebas unitarias
                    - El código es más mantenible y escalable
                    - Reduce el acoplamiento entre componentes
                    
                    En Android, usamos componentes como LiveData, StateFlow y Room Database para implementar este patrón de forma eficiente.
                """.trimIndent(),
                summary = "Aprende a implementar la arquitectura MVVM en tus aplicaciones Android para un código más limpio y mantenible.",
                author = "Desarrollador Mobile",
                imageUri = null,
                tags = "android, mvvm, arquitectura, kotlin, best-practices",
                isPublished = true,
                viewCount = 89
            ),
            Post(
                title = "Room Database: Persistencia Local en Android",
                content = """
                    Room es una librería de persistencia que proporciona una capa de abstracción sobre SQLite, facilitando el acceso a la base de datos y reduciendo el código repetitivo.
                    
                    Componentes principales de Room:
                    
                    1. Entity: Representa una tabla en la base de datos. Cada instancia de la entidad representa una fila.
                    
                    2. DAO (Data Access Object): Define los métodos para acceder a la base de datos con anotaciones como @Query, @Insert, @Update y @Delete.
                    
                    3. Database: Clase abstracta que extiende RoomDatabase y sirve como punto de acceso principal a los datos persistentes.
                    
                    Ventajas de usar Room:
                    
                    - Verificación en tiempo de compilación de consultas SQL
                    - Menos código repetitivo
                    - Fácil migración de bases de datos
                    - Integración con LiveData y Flow para observar cambios
                    - Compatible con coroutines de Kotlin
                    
                    Room simplifica significativamente el trabajo con bases de datos locales en Android, haciéndolo más seguro y eficiente.
                """.trimIndent(),
                summary = "Guía completa sobre cómo usar Room Database para almacenar datos localmente en Android.",
                author = "Desarrollador Mobile",
                imageUri = null,
                tags = "android, room, database, sqlite, persistence",
                isPublished = true,
                viewCount = 156
            ),
            Post(
                title = "Validación de Formularios en Kotlin",
                content = """
                    La validación de formularios es crucial para garantizar la integridad de los datos y mejorar la experiencia del usuario en nuestras aplicaciones.
                    
                    Mejores prácticas para validación:
                    
                    1. Validación en tiempo real: Proporciona retroalimentación inmediata mientras el usuario escribe.
                    
                    2. Mensajes claros: Los mensajes de error deben ser específicos y ayudar al usuario a corregir el problema.
                    
                    3. Validación visual: Usa colores, iconos y estilos para indicar campos válidos e inválidos.
                    
                    4. Centralizar la lógica: Mantén las reglas de validación en una capa separada (como ViewModel) para facilitar las pruebas.
                    
                    Tipos de validación comunes:
                    
                    - Campos obligatorios
                    - Longitud mínima y máxima
                    - Formato de email
                    - Formato de teléfono
                    - Validaciones personalizadas con regex
                    
                    Con Jetpack Compose y StateFlow, podemos implementar validaciones reactivas que actualicen la UI automáticamente.
                """.trimIndent(),
                summary = "Aprende las mejores prácticas para implementar validación de formularios robusta en tus apps Android.",
                author = "Desarrollador Mobile",
                imageUri = null,
                tags = "kotlin, validation, forms, ux, best-practices",
                isPublished = true,
                viewCount = 73
            ),
            Post(
                title = "Acceso a Recursos Nativos: Cámara y GPS",
                content = """
                    El acceso a recursos nativos como la cámara y GPS permite crear experiencias más ricas e interactivas en aplicaciones móviles.
                    
                    Cámara en Android:
                    
                    Usamos CameraX, una librería de Jetpack que simplifica el desarrollo de aplicaciones con cámara. CameraX maneja la compatibilidad entre dispositivos y proporciona una API simple y consistente.
                    
                    Pasos clave:
                    1. Solicitar permisos de cámara
                    2. Configurar CameraX con casos de uso (Preview, ImageCapture)
                    3. Manejar el ciclo de vida correctamente
                    4. Guardar las fotos capturadas
                    
                    GPS y Ubicación:
                    
                    Para acceder a la ubicación usamos Fused Location Provider de Google Play Services, que combina señales de GPS, Wi-Fi y redes móviles para determinar la ubicación más precisa.
                    
                    Consideraciones importantes:
                    - Siempre solicitar permisos en runtime
                    - Verificar que los servicios de ubicación estén habilitados
                    - Manejar casos cuando el usuario deniegue permisos
                    - Ser transparente sobre cómo se usa la ubicación
                    
                    El acceso responsable a estos recursos mejora significativamente la funcionalidad de nuestras apps.
                """.trimIndent(),
                summary = "Guía práctica para implementar acceso a cámara y GPS en aplicaciones Android de forma segura.",
                author = "Desarrollador Mobile",
                imageUri = null,
                tags = "android, camera, gps, permissions, native-resources",
                isPublished = true,
                viewCount = 201
            ),
            Post(
                title = "Material Design 3 en Jetpack Compose",
                content = """
                    Material Design 3 (Material You) es el sistema de diseño más reciente de Google, que trae personalización dinámica de colores y componentes renovados.
                    
                    Principales características:
                    
                    1. Theming dinámico: Los colores se adaptan automáticamente al fondo de pantalla del usuario (Android 12+).
                    
                    2. Componentes actualizados: Botones, cards, navigation bars con nuevo diseño.
                    
                    3. Tipografía mejorada: Nueva escala tipográfica más flexible.
                    
                    4. Tokens de diseño: Sistema consistente de espaciado, elevación y formas.
                    
                    Implementación en Compose:
                    
                    - MaterialTheme proporciona colores, tipografía y formas
                    - Componentes M3 como Button, Card, NavigationBar
                    - Soporte para tema claro y oscuro
                    - Personalización completa del tema
                    
                    Ventajas de M3:
                    
                    - Experiencia más personal para los usuarios
                    - Mayor accesibilidad
                    - Diseño moderno y coherente
                    - Fácil adaptación entre plataformas
                    
                    Material 3 hace que nuestras apps se vean modernas y se integren perfectamente con el sistema Android.
                """.trimIndent(),
                summary = "Descubre cómo implementar Material Design 3 en tus aplicaciones con Jetpack Compose.",
                author = "Desarrollador Mobile",
                imageUri = null,
                tags = "material-design, ui, jetpack-compose, android, design",
                isPublished = true,
                viewCount = 94
            )
        )

        posts.forEach { post ->
            postDao.insert(post)
        }
    }
}