package duoc.desarrollomobile.portafolio.utils

/**
 * Clase de utilidad para validaciones de mensajes
 * Valida longitud y contenido de mensajes
 */
object MessageValidator {

    private const val MIN_MESSAGE_LENGTH = 10
    private const val MAX_MESSAGE_LENGTH = 500

    /**
     * Valida que un mensaje tenga longitud válida (10-500 caracteres)
     * @param message String a validar
     * @return true si la longitud es válida, false en caso contrario
     */
    fun isValidMessage(message: String): Boolean {
        val trimmed = message.trim()
        return trimmed.length in MIN_MESSAGE_LENGTH..MAX_MESSAGE_LENGTH
    }

    /**
     * Sanitiza un mensaje eliminando espacios extras
     * @param message String a sanitizar
     * @return String con espacios normalizados
     */
    fun sanitizeMessage(message: String): String {
        return message.trim().replace("\\s+".toRegex(), " ")
    }
}