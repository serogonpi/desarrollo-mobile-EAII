package duoc.desarrollomobile.portafolio.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Modelo de datos para Proyectos del portafolio
 * Representa un proyecto individual en la galería
 */
@Entity(tableName = "projects")
data class Project(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val title: String,
    val description: String,
    val imageUri: String? = null, // URI de la imagen capturada
    val technologies: String, // Tecnologías separadas por coma
    val githubUrl: String? = null,
    val category: String, // Web, Mobile, Backend, etc.
    val isFavorite: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
) {
    /**
     * Obtiene la lista de tecnologías como array
     */
    fun getTechnologiesList(): List<String> {
        return technologies.split(",").map { it.trim() }.filter { it.isNotEmpty() }
    }

    /**
     * Valida que el proyecto tenga los datos mínimos requeridos
     */
    fun isValid(): Boolean {
        return title.isNotBlank() &&
                description.isNotBlank() &&
                category.isNotBlank() &&
                technologies.isNotBlank()
    }
}
