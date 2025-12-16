package duoc.desarrollomobile.portafolio.utils

/**
 * Clase de utilidad para validaciones de contacto
 * Valida emails, teléfonos chilenos y nombres
 */
object ContactValidator {

    /**
     * Valida que un email tenga formato correcto
     * @param email String a validar
     * @return true si el email es válido, false en caso contrario
     */
    fun isValidEmail(email: String): Boolean {
        if (email.isBlank()) return false
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$".toRegex()
        return emailRegex.matches(email)
    }

    /**
     * Valida que un teléfono tenga formato chileno
     * Formato esperado: +56912345678 (11 dígitos totales)
     * @param phone String a validar
     * @return true si el teléfono es válido, false en caso contrario
     */
    fun isValidChileanPhone(phone: String): Boolean {
        // Formato chileno: +56 seguido de 9 dígitos
        val phoneRegex = "^\\+569\\d{8}$".toRegex()
        return phoneRegex.matches(phone)
    }

    /**
     * Valida que un nombre tenga longitud mínima
     * @param name String a validar
     * @return true si el nombre tiene 2+ caracteres, false en caso contrario
     */
    fun isValidName(name: String): Boolean {
        return name.trim().length >= 2
    }
}