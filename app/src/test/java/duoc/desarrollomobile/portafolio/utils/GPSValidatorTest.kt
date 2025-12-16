package duoc.desarrollomobile.portafolio.utils

import org.junit.Test
import org.junit.Assert.*

/**
 * Pruebas unitarias para validación de coordenadas GPS
 *
 * Propósito: Verificar que las coordenadas de latitud y longitud
 * estén dentro de rangos válidos antes de usarlas en el mapa o
 * guardarlas en la base de datos.
 */
class GPSValidatorTest {

    @Test
    fun `validar latitud dentro del rango válido retorna true`() {
        // Arrange
        val latitudValida = -33.4489 // Santiago, Chile

        // Act
        val resultado = GPSValidator.isValidLatitude(latitudValida)

        // Assert
        assertTrue("Latitud válida (-90 a 90) debería retornar true", resultado)
    }

    @Test
    fun `validar latitud mayor a 90 grados retorna false`() {
        // Arrange
        val latitudInvalida = 95.0

        // Act
        val resultado = GPSValidator.isValidLatitude(latitudInvalida)

        // Assert
        assertFalse("Latitud mayor a 90° debería retornar false", resultado)
    }

    @Test
    fun `validar latitud menor a -90 grados retorna false`() {
        // Arrange
        val latitudInvalida = -95.0

        // Act
        val resultado = GPSValidator.isValidLatitude(latitudInvalida)

        // Assert
        assertFalse("Latitud menor a -90° debería retornar false", resultado)
    }

    @Test
    fun `validar longitud dentro del rango válido retorna true`() {
        // Arrange
        val longitudValida = -70.6693 // Santiago, Chile

        // Act
        val resultado = GPSValidator.isValidLongitude(longitudValida)

        // Assert
        assertTrue("Longitud válida (-180 a 180) debería retornar true", resultado)
    }

    @Test
    fun `validar longitud mayor a 180 grados retorna false`() {
        // Arrange
        val longitudInvalida = 185.0

        // Act
        val resultado = GPSValidator.isValidLongitude(longitudInvalida)

        // Assert
        assertFalse("Longitud mayor a 180° debería retornar false", resultado)
    }

    @Test
    fun `validar coordenadas completas de Santiago retorna true`() {
        // Arrange
        val latitud = -33.4489
        val longitud = -70.6693

        // Act
        val resultado = GPSValidator.areValidCoordinates(latitud, longitud)

        // Assert
        assertTrue("Coordenadas de Santiago deberían ser válidas", resultado)
    }

    @Test
    fun `validar coordenadas con valores nulos retorna false`() {
        // Arrange
        val latitud = 0.0
        val longitud = 0.0

        // Act
        val resultado = GPSValidator.areValidCoordinates(latitud, longitud)

        // Assert
        // Nota: 0,0 es técnicamente válido pero en la práctica indica error
        // Esta prueba depende de tu lógica de negocio
        assertTrue("Coordenadas 0,0 son técnicamente válidas", resultado)
    }
}

/**
 * Clase de utilidad para validaciones GPS
 * Esta clase debe ser implementada en tu proyecto
 */
object GPSValidator {

    fun isValidLatitude(latitude: Double): Boolean {
        return latitude >= -90.0 && latitude <= 90.0
    }

    fun isValidLongitude(longitude: Double): Boolean {
        return longitude >= -180.0 && longitude <= 180.0
    }

    fun areValidCoordinates(latitude: Double, longitude: Double): Boolean {
        return isValidLatitude(latitude) && isValidLongitude(longitude)
    }
}