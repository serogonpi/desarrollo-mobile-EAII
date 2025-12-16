package duoc.desarrollomobile.portafolio.utils

/**
 * Clase de utilidad para validaciones GPS
 * Valida coordenadas de latitud y longitud
 */
object GPSValidator {

    /**
     * Valida que una latitud esté en el rango válido (-90 a 90 grados)
     * @param latitude Double a validar
     * @return true si está en rango, false en caso contrario
     */
    fun isValidLatitude(latitude: Double): Boolean {
        return latitude >= -90.0 && latitude <= 90.0
    }

    /**
     * Valida que una longitud esté en el rango válido (-180 a 180 grados)
     * @param longitude Double a validar
     * @return true si está en rango, false en caso contrario
     */
    fun isValidLongitude(longitude: Double): Boolean {
        return longitude >= -180.0 && longitude <= 180.0
    }

    /**
     * Valida que ambas coordenadas sean válidas
     * @param latitude Latitud a validar
     * @param longitude Longitud a validar
     * @return true si ambas coordenadas son válidas, false en caso contrario
     */
    fun areValidCoordinates(latitude: Double, longitude: Double): Boolean {
        return isValidLatitude(latitude) && isValidLongitude(longitude)
    }
}