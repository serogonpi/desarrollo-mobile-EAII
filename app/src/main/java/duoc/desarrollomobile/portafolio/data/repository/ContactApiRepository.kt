package duoc.desarrollomobile.portafolio.data.repository

import duoc.desarrollomobile.portafolio.data.model.*
import duoc.desarrollomobile.portafolio.data.remote.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Repositorio para manejar las peticiones a la API
 */
class ContactApiRepository {

    private val apiService = RetrofitClient.apiService

    /**
     * Obtener tipos de proyecto desde la API
     */
    suspend fun getTiposProyecto(): Result<List<TipoProyecto>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getTiposProyecto()
                if (response.isSuccessful && response.body() != null) {
                    Result.success(response.body()!!)
                } else {
                    Result.failure(Exception("Error: ${response.code()} - ${response.message()}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    /**
     * Obtener presupuestos desde la API
     */
    suspend fun getPresupuestos(): Result<List<Presupuesto>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getPresupuestos()
                if (response.isSuccessful && response.body() != null) {
                    Result.success(response.body()!!)
                } else {
                    Result.failure(Exception("Error: ${response.code()} - ${response.message()}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    /**
     * Enviar contacto a la API
     */
    suspend fun enviarContacto(contacto: ContactoRequest): Result<ContactoResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.enviarContacto(contacto)
                if (response.isSuccessful && response.body() != null) {
                    val apiResponse = response.body()!!
                    if (apiResponse.success && apiResponse.data != null) {
                        Result.success(apiResponse.data)
                    } else {
                        Result.failure(Exception(apiResponse.message))
                    }
                } else {
                    Result.failure(Exception("Error: ${response.code()} - ${response.message()}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    /**
     * Listar todos los contactos
     */
    suspend fun listarContactos(): Result<List<ContactoResponse>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.listarContactos()
                if (response.isSuccessful && response.body() != null) {
                    Result.success(response.body()!!)
                } else {
                    Result.failure(Exception("Error: ${response.code()} - ${response.message()}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}