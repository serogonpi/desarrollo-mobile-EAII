package duoc.desarrollomobile.portafolio.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource

/**
 * Helper para gestionar operaciones de ubicación GPS
 * Proporciona métodos para obtener la ubicación actual del dispositivo
 */
class LocationHelper(private val context: Context) {

    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    /**
     * Verifica si los servicios de ubicación están habilitados
     */
    fun isLocationEnabled(): Boolean {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    /**
     * Verifica si tenemos permisos de ubicación
     */
    fun hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
    }

    /**
     * Obtiene la última ubicación conocida
     */
    fun getLastLocation(
        onSuccess: (Double, Double) -> Unit,
        onFailure: (String) -> Unit
    ) {
        if (!hasLocationPermission()) {
            onFailure("No se tienen permisos de ubicación")
            return
        }

        if (!isLocationEnabled()) {
            onFailure("Los servicios de ubicación están deshabilitados")
            return
        }

        try {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    if (location != null) {
                        onSuccess(location.latitude, location.longitude)
                    } else {
                        // Si no hay última ubicación, intentar obtener ubicación actual
                        getCurrentLocation(onSuccess, onFailure)
                    }
                }
                .addOnFailureListener { exception ->
                    onFailure("Error al obtener ubicación: ${exception.message}")
                }
        } catch (e: SecurityException) {
            onFailure("Error de seguridad al acceder a la ubicación")
        }
    }

    /**
     * Obtiene la ubicación actual (más preciso pero tarda más)
     */
    fun getCurrentLocation(
        onSuccess: (Double, Double) -> Unit,
        onFailure: (String) -> Unit
    ) {
        if (!hasLocationPermission()) {
            onFailure("No se tienen permisos de ubicación")
            return
        }

        if (!isLocationEnabled()) {
            onFailure("Los servicios de ubicación están deshabilitados")
            return
        }

        try {
            val cancellationTokenSource = CancellationTokenSource()

            fusedLocationClient.getCurrentLocation(
                Priority.PRIORITY_HIGH_ACCURACY,
                cancellationTokenSource.token
            ).addOnSuccessListener { location: Location? ->
                if (location != null) {
                    onSuccess(location.latitude, location.longitude)
                } else {
                    onFailure("No se pudo obtener la ubicación actual")
                }
            }.addOnFailureListener { exception ->
                onFailure("Error al obtener ubicación: ${exception.message}")
            }
        } catch (e: SecurityException) {
            onFailure("Error de seguridad al acceder a la ubicación")
        }
    }
}