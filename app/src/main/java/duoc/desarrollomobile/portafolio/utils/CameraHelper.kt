package duoc.desarrollomobile.portafolio.utils

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

/**
 * Helper para gestionar operaciones de la cámara
 * Crea archivos temporales y URIs para captura de fotos
 */
object CameraHelper {

    /**
     * Crea un archivo temporal para guardar la foto
     */
    fun createImageFile(context: Context): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir = context.getExternalFilesDir(null)
        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        )
    }

    /**
     * Obtiene el URI del archivo para la cámara
     */
    fun getImageUri(context: Context, file: File): Uri {
        return FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file
        )
    }

    /**
     * Crea un URI listo para usar con la cámara
     */
    fun createImageUri(context: Context): Pair<Uri, File> {
        val file = createImageFile(context)
        val uri = getImageUri(context, file)
        return Pair(uri, file)
    }

    /**
     * Elimina un archivo de imagen
     */
    fun deleteImageFile(file: File): Boolean {
        return try {
            if (file.exists()) {
                file.delete()
            } else {
                false
            }
        } catch (e: Exception) {
            false
        }
    }

    /**
     * Verifica si un archivo de imagen existe
     */
    fun imageFileExists(file: File): Boolean {
        return file.exists() && file.length() > 0
    }
}