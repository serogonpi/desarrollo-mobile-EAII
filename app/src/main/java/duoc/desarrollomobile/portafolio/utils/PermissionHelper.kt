package duoc.desarrollomobile.portafolio.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

/**
 * Helper para gestionar permisos de manera centralizada
 * Simplifica la verificación de permisos en toda la app
 */
object PermissionHelper {

    // Permisos de cámara
    val CAMERA_PERMISSIONS = arrayOf(
        Manifest.permission.CAMERA
    )

    // Permisos de ubicación
    val LOCATION_PERMISSIONS = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    // Permisos de almacenamiento (para imágenes)
    val STORAGE_PERMISSIONS = arrayOf(
        Manifest.permission.READ_MEDIA_IMAGES
    )

    /**
     * Verifica si un permiso específico está concedido
     */
    fun hasPermission(context: Context, permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    /**
     * Verifica si todos los permisos de un array están concedidos
     */
    fun hasPermissions(context: Context, permissions: Array<String>): Boolean {
        return permissions.all { hasPermission(context, it) }
    }

    /**
     * Verifica permisos de cámara
     */
    fun hasCameraPermission(context: Context): Boolean {
        return hasPermissions(context, CAMERA_PERMISSIONS)
    }

    /**
     * Verifica permisos de ubicación
     */
    fun hasLocationPermission(context: Context): Boolean {
        return hasPermissions(context, LOCATION_PERMISSIONS)
    }
}