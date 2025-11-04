package duoc.desarrollomobile.portafolio.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Modelo de datos para Posts del blog
 * Representa una publicación individual
 */
@Entity(tableName = "posts")
data class Post(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val title: String,
    val content: String,
    val summary: String,
    val author: String,
    val imageUri: String? = null,
    val tags: String, // Tags separados por coma
    val isPublished: Boolean = true,
    val viewCount: Int = 0,
    val createdAt: Long = System.currentTimeMillis()
) {
    /**
     * Obtiene la lista de tags como array
     */
    fun getTagsList(): List<String> {
        return tags.split(",").map { it.trim() }.filter { it.isNotEmpty() }
    }

    /**
     * Valida que el post tenga los datos mínimos requeridos
     */
    fun isValid(): Boolean {
        return title.isNotBlank() &&
                content.isNotBlank() &&
                author.isNotBlank() &&
                title.length >= 5 &&
                content.length >= 20
    }

    /**
     * Obtiene un resumen corto del contenido
     */
    fun getShortSummary(maxLength: Int = 150): String {
        return if (summary.isNotBlank()) {
            if (summary.length > maxLength) {
                summary.substring(0, maxLength) + "..."
            } else {
                summary
            }
        } else {
            if (content.length > maxLength) {
                content.substring(0, maxLength) + "..."
            } else {
                content
            }
        }
    }
}
