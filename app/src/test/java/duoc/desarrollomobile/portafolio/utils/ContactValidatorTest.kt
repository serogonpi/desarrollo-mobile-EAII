package duoc.desarrollomobile.portafolio.utils

import org.junit.Test
import org.junit.Assert.*

/**
 * Pruebas unitarias para validación de datos de contacto
 *
 * Propósito: Asegurar que los datos de contacto cumplan con los formatos
 * y restricciones definidas antes de ser guardados en la base de datos.
 */
class ContactValidatorTest {

    @Test
    fun `validar email con formato correcto retorna true`() {
        // Arrange (Preparar)
        val emailValido = "usuario@ejemplo.com"

        // Act (Actuar)
        val resultado = ContactValidator.isValidEmail(emailValido)

        // Assert (Verificar)
        assertTrue("Email válido debería retornar true", resultado)
    }

    @Test
    fun `validar email sin arroba retorna false`() {
        // Arrange
        val emailInvalido = "usuarioejemplo.com"

        // Act
        val resultado = ContactValidator.isValidEmail(emailInvalido)

        // Assert
        assertFalse("Email sin @ debería retornar false", resultado)
    }

    @Test
    fun `validar email vacío retorna false`() {
        // Arrange
        val emailVacio = ""

        // Act
        val resultado = ContactValidator.isValidEmail(emailVacio)

        // Assert
        assertFalse("Email vacío debería retornar false", resultado)
    }

    @Test
    fun `validar teléfono chileno con formato correcto retorna true`() {
        // Arrange
        val telefonoValido = "+56912345678" // Formato internacional

        // Act
        val resultado = ContactValidator.isValidChileanPhone(telefonoValido)

        // Assert
        assertTrue("Teléfono chileno válido debería retornar true", resultado)
    }

    @Test
    fun `validar teléfono con longitud incorrecta retorna false`() {
        // Arrange
        val telefonoInvalido = "+5691234" // Muy corto

        // Act
        val resultado = ContactValidator.isValidChileanPhone(telefonoInvalido)

        // Assert
        assertFalse("Teléfono con longitud incorrecta debería retornar false", resultado)
    }

    @Test
    fun `validar nombre con longitud mínima válida retorna true`() {
        // Arrange
        val nombreValido = "Juan Pérez"

        // Act
        val resultado = ContactValidator.isValidName(nombreValido)

        // Assert
        assertTrue("Nombre con longitud válida debería retornar true", resultado)
    }

    @Test
    fun `validar nombre demasiado corto retorna false`() {
        // Arrange
        val nombreCorto = "J" // Menor a 2 caracteres

        // Act
        val resultado = ContactValidator.isValidName(nombreCorto)

        // Assert
        assertFalse("Nombre menor a 2 caracteres debería retornar false", resultado)
    }
}

/**
 * Clase de utilidad para validaciones de contacto
 * Esta clase debe ser implementada en tu proyecto
 */
object ContactValidator {

    fun isValidEmail(email: String): Boolean {
        if (email.isBlank()) return false
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$".toRegex()
        return emailRegex.matches(email)
    }

    fun isValidChileanPhone(phone: String): Boolean {
        // Formato chileno: +56 seguido de 9 dígitos
        val phoneRegex = "^\\+569\\d{8}$".toRegex()
        return phoneRegex.matches(phone)
    }

    fun isValidName(name: String): Boolean {
        return name.trim().length >= 2
    }
}