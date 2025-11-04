package duoc.desarrollomobile.portafolio.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Modelo de datos para mensajes de contacto
 * Almacena los mensajes enviados a través del formulario de contacto
 * Incluye ubicación GPS del usuario (recurso nativo)
 */
@Entity(tableName = "contact_messages")
data class ContactMessage(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val name: String,
    val email: String,
    val phone: String? = null,
    val subject: String,
    val message: String,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val isRead: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
) {
    /**
     * Valida el formato del email
     */
    fun isEmailValid(): Boolean {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        return email.matches(emailPattern.toRegex())
    }

    /**
     * Valida el formato del teléfono (opcional pero si existe debe ser válido)
     */
    fun isPhoneValid(): Boolean {
        if (phone.isNullOrBlank()) return true // Es opcional
        val phonePattern = "^[+]?[0-9]{8,15}$"
        return phone.matches(phonePattern.toRegex())
    }

    /**
     * Valida que el mensaje tenga todos los datos requeridos
     */
    fun isValid(): Boolean {
        return name.isNotBlank() &&
                name.length >= 3 &&
                email.isNotBlank() &&
                isEmailValid() &&
                subject.isNotBlank() &&
                subject.length >= 5 &&
                message.isNotBlank() &&
                message.length >= 10 &&
                isPhoneValid()
    }

    /**
     * Obtiene la ubicación como string formateado
     */
    fun getFormattedLocation(): String {
        return if (latitude != null && longitude != null) {
            "Lat: %.6f, Lng: %.6f".format(latitude, longitude)
        } else {
            "Sin ubicación"
        }
    }
}