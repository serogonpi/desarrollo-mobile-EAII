package duoc.desarrollomobile.portafolio.utils

import java.text.SimpleDateFormat
import java.util.*

/**
 * Clase de utilidad para formateo de fechas
 * Formatea fechas para UI y base de datos
 */
object DateFormatter {

    private const val DISPLAY_FORMAT = "dd/MM/yyyy HH:mm"
    private const val ISO_8601_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'"

    /**
     * Formatea una fecha para mostrar en la UI
     * @param date Fecha a formatear
     * @return String en formato dd/MM/yyyy HH:mm
     */
    fun formatToDisplay(date: Date): String {
        val formatter = SimpleDateFormat(DISPLAY_FORMAT, Locale.getDefault())
        return formatter.format(date)
    }

    /**
     * Formatea un timestamp a formato ISO 8601 (para base de datos)
     * @param timestamp Timestamp en milisegundos
     * @return String en formato ISO 8601
     */
    fun formatToISO8601(timestamp: Long): String {
        val formatter = SimpleDateFormat(ISO_8601_FORMAT, Locale.getDefault())
        formatter.timeZone = TimeZone.getTimeZone("UTC")
        return formatter.format(Date(timestamp))
    }

    /**
     * Parsea un string de fecha en formato de visualización
     * @param dateString String a parsear
     * @return Date si el parseo es exitoso, null en caso contrario
     */
    fun parseFromDisplay(dateString: String): Date? {
        return try {
            val formatter = SimpleDateFormat(DISPLAY_FORMAT, Locale.getDefault())
            formatter.parse(dateString)
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Calcula la diferencia en días entre dos fechas
     * @param date1 Primera fecha
     * @param date2 Segunda fecha
     * @return Diferencia en días (puede ser negativa si date2 < date1)
     */
    fun getDaysBetween(date1: Date, date2: Date): Long {
        val diff = date2.time - date1.time
        return diff / (24 * 60 * 60 * 1000)
    }

    /**
     * Verifica si una fecha es hoy
     * @param date Fecha a verificar
     * @return true si la fecha es hoy, false en caso contrario
     */
    fun isToday(date: Date): Boolean {
        val today = Calendar.getInstance()
        val targetDate = Calendar.getInstance().apply { time = date }

        return today.get(Calendar.YEAR) == targetDate.get(Calendar.YEAR) &&
                today.get(Calendar.DAY_OF_YEAR) == targetDate.get(Calendar.DAY_OF_YEAR)
    }
}