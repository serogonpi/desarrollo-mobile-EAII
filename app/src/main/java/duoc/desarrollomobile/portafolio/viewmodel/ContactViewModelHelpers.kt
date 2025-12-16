package duoc.desarrollomobile.portafolio.viewmodel

/**
 * Estados de la UI para el flujo de contacto
 */
sealed class UiState {
    object Idle : UiState()
    object Loading : UiState()
    data class Success(val message: String = "Operación exitosa") : UiState()
    data class Error(val message: String) : UiState()
}

/**
 * Data class para representar un contacto
 */
data class ContactData(
    val nombre: String,
    val email: String,
    val telefono: String,
    val mensaje: String
)

/**
 * Mock del ContactViewModel para pruebas unitarias
 * En producción, este sería tu ViewModel real con LiveData/StateFlow
 */
class MockContactViewModel {
    private var currentState: UiState = UiState.Idle

    fun getUiState(): UiState = currentState

    fun saveContact(contact: ContactData) {
        val errores = validateContact(contact)

        currentState = if (errores.isEmpty()) {
            // Simular guardado exitoso
            UiState.Success("Contacto guardado exitosamente")
        } else {
            UiState.Error(errores.first())
        }
    }

    fun validateContact(contact: ContactData): List<String> {
        val errores = mutableListOf<String>()

        if (contact.nombre.trim().length < 2) {
            errores.add("El nombre debe tener al menos 2 caracteres")
        }

        if (!contact.email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$".toRegex())) {
            errores.add("El email no tiene un formato válido")
        }

        if (!contact.telefono.matches("^\\+569\\d{8}$".toRegex())) {
            errores.add("El teléfono debe tener formato chileno (+56912345678)")
        }

        if (contact.mensaje.trim().length < 10) {
            errores.add("El mensaje debe tener al menos 10 caracteres")
        }

        return errores
    }

    fun clearForm() {
        currentState = UiState.Idle
    }
}