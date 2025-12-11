package duoc.desarrollomobile.portafolio.data.remote

import duoc.desarrollomobile.portafolio.data.model.*
import retrofit2.Response
import retrofit2.http.*

/**
 * Interface para definir los endpoints de la API
 */
interface ContactApiService {

    /**
     * Obtener tipos de proyecto
     * GET /api/tipos-proyecto
     */
    @GET("tipos-proyecto")
    suspend fun getTiposProyecto(): Response<List<TipoProyecto>>

    /**
     * Obtener presupuestos
     * GET /api/presupuestos
     */
    @GET("presupuestos")
    suspend fun getPresupuestos(): Response<List<Presupuesto>>

    /**
     * Enviar contacto
     * POST /api/contact
     */
    @POST("contact")
    suspend fun enviarContacto(
        @Body contacto: ContactoRequest
    ): Response<ApiResponse<ContactoResponse>>

    /**
     * Listar todos los contactos guardados
     * GET /api/contactos
     */
    @GET("contactos")
    suspend fun listarContactos(): Response<List<ContactoResponse>>

    /**
     * Obtener un contacto por ID
     * GET /api/contactos/{id}
     */
    @GET("contactos/{id}")
    suspend fun obtenerContacto(
        @Path("id") id: Long
    ): Response<ContactoResponse>
}