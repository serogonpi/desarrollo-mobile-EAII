package duoc.desarrollomobile.portafolio.utils

import org.junit.Test
import org.junit.Assert.*

/**
 * Pruebas unitarias para validación de mensajes de contacto
 *
 * Propósito: Garantizar que los mensajes enviados a través del módulo
 * de contacto cumplan con restricciones de longitud y contenido,
 * evitando mensajes vacíos o excesivamente largos.
 */
class MessageValidatorTest {

    @Test
    fun `validar mensaje con longitud válida retorna true`() {
        // Arrange
        val mensajeValido = "Hola, me gustaría obtener más información sobre el proyecto."

        // Act
        val resultado = MessageValidator.isValidMessage(mensajeValido)

        // Assert
        assertTrue("Mensaje con longitud válida debería retornar true", resultado)
    }

    @Test
    fun `validar mensaje vacío retorna false`() {
        // Arrange
        val mensajeVacio = ""

        // Act
        val resultado = MessageValidator.isValidMessage(mensajeVacio)

        // Assert
        assertFalse("Mensaje vacío debería retornar false", resultado)
    }

    @Test
    fun `validar mensaje solo con espacios retorna false`() {
        // Arrange
        val mensajeSoloEspacios = "     "

        // Act
        val resultado = MessageValidator.isValidMessage(mensajeSoloEspacios)

        // Assert
        assertFalse("Mensaje solo con espacios debería retornar false", resultado)
    }

    @Test
    fun `validar mensaje demasiado corto retorna false`() {
        // Arrange
        val mensajeCorto = "Hola" // Menor a 10 caracteres

        // Act
        val resultado = MessageValidator.isValidMessage(mensajeCorto)

        // Assert
        assertFalse("Mensaje menor a 10 caracteres debería retornar false", resultado)
    }

    @Test
    fun `validar mensaje excesivamente largo retorna false`() {
        // Arrange
        val mensajeLargo = "A".repeat(501) // Mayor a 500 caracteres

        // Act
        val resultado = MessageValidator.isValidMessage(mensajeLargo)

        // Assert
        assertFalse("Mensaje mayor a 500 caracteres debería retornar false", resultado)
    }

    @Test
    fun `validar mensaje en el límite superior retorna true`() {
        // Arrange
        val mensajeLimite = "A".repeat(500) // Exactamente 500 caracteres

        // Act
        val resultado = MessageValidator.isValidMessage(mensajeLimite)

        // Assert
        assertTrue("Mensaje de exactamente 500 caracteres debería retornar true", resultado)
    }

    @Test
    fun `sanitizar mensaje elimina espacios extras correctamente`() {
        // Arrange
        val mensajeConEspacios = "  Hola    mundo   de   pruebas   "
        val esperado = "Hola mundo de pruebas"

        // Act
        val resultado = MessageValidator.sanitizeMessage(mensajeConEspacios)

        // Assert
        assertEquals("Mensaje sanitizado debería eliminar espacios extras", esperado, resultado)
    }
}

/**
 * Clase de utilidad para validaciones de mensajes
 * Esta clase debe ser implementada en tu proyecto
 */
object MessageValidator {

    private const val MIN_MESSAGE_LENGTH = 10
    private const val MAX_MESSAGE_LENGTH = 500

    fun isValidMessage(message: String): Boolean {
        val trimmed = message.trim()
        return trimmed.length in MIN_MESSAGE_LENGTH..MAX_MESSAGE_LENGTH
    }

    fun sanitizeMessage(message: String): String {
        return message.trim().replace("\\s+".toRegex(), " ")
    }
}