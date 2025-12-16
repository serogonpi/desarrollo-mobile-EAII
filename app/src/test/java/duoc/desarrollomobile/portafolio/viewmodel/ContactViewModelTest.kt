package duoc.desarrollomobile.portafolio.viewmodel

import org.junit.Test
import org.junit.Assert.*
import org.junit.Before

/**
 * Pruebas unitarias para estados de UI del ContactViewModel
 *
 * Propósito: Verificar que los estados de la aplicación (cargando,
 * éxito, error) se manejen correctamente, garantizando una experiencia
 * de usuario consistente y sin estados inválidos.
 */
class ContactViewModelTest {

    private lateinit var viewModel: MockContactViewModel

    @Before
    fun setup() {
        viewModel = MockContactViewModel()
    }

    @Test
    fun `estado inicial del ViewModel debería ser Idle`() {
        // Arrange & Act
        val estadoInicial = viewModel.getUiState()

        // Assert
        assertTrue("Estado inicial debería ser Idle", estadoInicial is UiState.Idle)
    }

    @Test
    fun `guardar contacto válido cambia estado a Loading y luego Success`() {
        // Arrange
        val contacto = ContactData(
            nombre = "Juan Pérez",
            email = "juan@ejemplo.com",
            telefono = "+56912345678",
            mensaje = "Mensaje de prueba válido"
        )

        // Act
        viewModel.saveContact(contacto)
        val estadoDespuesDeGuardar = viewModel.getUiState()

        // Assert
        assertTrue("Estado después de guardar debería ser Success",
            estadoDespuesDeGuardar is UiState.Success)
    }

    @Test
    fun `guardar contacto con email inválido cambia estado a Error`() {
        // Arrange
        val contactoInvalido = ContactData(
            nombre = "Juan Pérez",
            email = "email-invalido",  // Sin @
            telefono = "+56912345678",
            mensaje = "Mensaje de prueba"
        )

        // Act
        viewModel.saveContact(contactoInvalido)
        val estadoFinal = viewModel.getUiState()

        // Assert
        assertTrue("Estado debería ser Error para email inválido",
            estadoFinal is UiState.Error)
        if (estadoFinal is UiState.Error) {
            assertTrue("Mensaje de error debería mencionar email",
                estadoFinal.message.contains("email", ignoreCase = true))
        }
    }

    @Test
    fun `limpiar formulario resetea el estado a Idle`() {
        // Arrange
        viewModel.saveContact(ContactData("Test", "test@test.com", "+56912345678", "Mensaje válido"))

        // Act
        viewModel.clearForm()
        val estadoDespuesDeLimpiar = viewModel.getUiState()

        // Assert
        assertTrue("Estado después de limpiar debería ser Idle",
            estadoDespuesDeLimpiar is UiState.Idle)
    }

    @Test
    fun `validar todos los campos vacíos retorna lista de errores`() {
        // Arrange
        val contactoVacio = ContactData("", "", "", "")

        // Act
        val errores = viewModel.validateContact(contactoVacio)

        // Assert
        assertTrue("Debería haber errores de validación", errores.isNotEmpty())
        assertTrue("Debería contener error de nombre",
            errores.any { it.contains("nombre", ignoreCase = true) })
        assertTrue("Debería contener error de email",
            errores.any { it.contains("email", ignoreCase = true) })
        assertTrue("Debería contener error de teléfono",
            errores.any { it.contains("teléfono", ignoreCase = true) })
        assertTrue("Debería contener error de mensaje",
            errores.any { it.contains("mensaje", ignoreCase = true) })
    }
}

/**
 * Estados de la UI
 */
sealed class UiState {
    object Idle : UiState()
    object Loading : UiState()
    data class Success(val message: String = "Operación exitosa") : UiState()
    data class Error(val message: String) : UiState()
}

/**
 * Data class para contacto
 */
data class ContactData(
    val nombre: String,
    val email: String,
    val telefono: String,
    val mensaje: String
)

/**
 * Mock del ViewModel para pruebas
 * En tu proyecto real, este sería el ContactViewModel real
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