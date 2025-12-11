package duoc.desarrollomobile.portafolio.data.model

import com.google.gson.annotations.SerializedName

/**
 * Modelo para Tipo de Proyecto desde la API
 */
data class TipoProyecto(
    @SerializedName("id")
    val id: Long,

    @SerializedName("codigo")
    val codigo: String,

    @SerializedName("nombre")
    val nombre: String
)

/**
 * Modelo para Presupuesto desde la API
 */
data class Presupuesto(
    @SerializedName("id")
    val id: Long,

    @SerializedName("codigo")
    val codigo: String,

    @SerializedName("nombre")
    val nombre: String
)

/**
 * Modelo para enviar contacto a la API
 */
data class ContactoRequest(
    @SerializedName("nombre")
    val nombre: String,

    @SerializedName("email")
    val email: String,

    @SerializedName("telefono")
    val telefono: String? = null,

    @SerializedName("empresa")
    val empresa: String? = null,

    @SerializedName("tipoProyecto")
    val tipoProyecto: String,

    @SerializedName("presupuesto")
    val presupuesto: String? = null,

    @SerializedName("asunto")
    val asunto: String,

    @SerializedName("mensaje")
    val mensaje: String
)

/**
 * Respuesta de la API
 */
data class ApiResponse<T>(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("message")
    val message: String,

    @SerializedName("data")
    val data: T?,

    @SerializedName("timestamp")
    val timestamp: String?
)

/**
 * Modelo para Contacto guardado (respuesta)
 */
data class ContactoResponse(
    @SerializedName("id")
    val id: Long,

    @SerializedName("nombre")
    val nombre: String,

    @SerializedName("email")
    val email: String,

    @SerializedName("telefono")
    val telefono: String?,

    @SerializedName("empresa")
    val empresa: String?,

    @SerializedName("tipoProyecto")
    val tipoProyecto: String,

    @SerializedName("presupuesto")
    val presupuesto: String?,

    @SerializedName("asunto")
    val asunto: String,

    @SerializedName("mensaje")
    val mensaje: String,

    @SerializedName("fechaCreacion")
    val fechaCreacion: String
)