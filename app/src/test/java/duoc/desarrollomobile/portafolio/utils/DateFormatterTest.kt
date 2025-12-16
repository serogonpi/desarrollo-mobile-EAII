package duoc.desarrollomobile.portafolio.utils

import org.junit.Test
import org.junit.Assert.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * Pruebas unitarias para formateo de fechas y timestamps
 *
 * Propósito: Verificar que las fechas se formateen correctamente
 * para mostrar en la UI y para guardar en la base de datos,
 * garantizando consistencia en toda la aplicación.
 */
class DateFormatterTest {

    @Test
    fun `formatear fecha actual a formato legible retorna string correcto`() {
        // Arrange
        val fecha = Date(1700000000000L) // 14 nov 2023, 22:13:20 UTC
        val formatoEsperado = "dd/MM/yyyy HH:mm"

        // Act
        val resultado = DateFormatter.formatToDisplay(fecha)

        // Assert
        assertNotNull("Fecha formateada no debería ser null", resultado)
        assertTrue("Fecha debería contener barras /", resultado.contains("/"))
        assertTrue("Fecha debería contener dos puntos :", resultado.contains(":"))
    }

    @Test
    fun `formatear timestamp a ISO 8601 retorna formato correcto`() {
        // Arrange
        val timestamp = 1700000000000L

        // Act
        val resultado = DateFormatter.formatToISO8601(timestamp)

        // Assert
        assertTrue("ISO 8601 debería contener T", resultado.contains("T"))
        assertTrue("ISO 8601 debería contener Z", resultado.endsWith("Z"))
    }

    @Test
    fun `parsear string de fecha válida retorna Date`() {
        // Arrange
        val fechaString = "15/12/2025 14:30"

        // Act
        val resultado = DateFormatter.parseFromDisplay(fechaString)

        // Assert
        assertNotNull("Parseo de fecha válida debería retornar Date", resultado)
    }

    @Test
    fun `parsear string de fecha inválida retorna null`() {
        // Arrange
        val fechaInvalida = "fecha-invalida"

        // Act
        val resultado = DateFormatter.parseFromDisplay(fechaInvalida)

        // Assert
        assertNull("Parseo de fecha inválida debería retornar null", resultado)
    }

    @Test
    fun `calcular diferencia en días entre fechas retorna valor correcto`() {
        // Arrange
        val fecha1 = Date(1700000000000L) // 14 nov 2023
        val fecha2 = Date(1700086400000L) // 15 nov 2023

        // Act
        val dias = DateFormatter.getDaysBetween(fecha1, fecha2)

        // Assert
        assertEquals("Diferencia debería ser 1 día", 1L, dias)
    }

    @Test
    fun `validar si fecha es hoy retorna true para fecha actual`() {
        // Arrange
        val hoy = Date()

        // Act
        val resultado = DateFormatter.isToday(hoy)

        // Assert
        assertTrue("Fecha actual debería ser reconocida como hoy", resultado)
    }

    @Test
    fun `validar si fecha es hoy retorna false para fecha pasada`() {
        // Arrange
        val ayer = Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000)

        // Act
        val resultado = DateFormatter.isToday(ayer)

        // Assert
        assertFalse("Fecha de ayer no debería ser reconocida como hoy", resultado)
    }
}

/**
 * Clase de utilidad para formateo de fechas
 * Esta clase debe ser implementada en tu proyecto
 */
object DateFormatter {

    private const val DISPLAY_FORMAT = "dd/MM/yyyy HH:mm"
    private const val ISO_8601_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'"

    fun formatToDisplay(date: Date): String {
        val formatter = SimpleDateFormat(DISPLAY_FORMAT, Locale.getDefault())
        return formatter.format(date)
    }

    fun formatToISO8601(timestamp: Long): String {
        val formatter = SimpleDateFormat(ISO_8601_FORMAT, Locale.getDefault())
        formatter.timeZone = TimeZone.getTimeZone("UTC")
        return formatter.format(Date(timestamp))
    }

    fun parseFromDisplay(dateString: String): Date? {
        return try {
            val formatter = SimpleDateFormat(DISPLAY_FORMAT, Locale.getDefault())
            formatter.parse(dateString)
        } catch (e: Exception) {
            null
        }
    }

    fun getDaysBetween(date1: Date, date2: Date): Long {
        val diff = date2.time - date1.time
        return diff / (24 * 60 * 60 * 1000)
    }

    fun isToday(date: Date): Boolean {
        val today = Calendar.getInstance()
        val targetDate = Calendar.getInstance().apply { time = date }

        return today.get(Calendar.YEAR) == targetDate.get(Calendar.YEAR) &&
                today.get(Calendar.DAY_OF_YEAR) == targetDate.get(Calendar.DAY_OF_YEAR)
    }
}