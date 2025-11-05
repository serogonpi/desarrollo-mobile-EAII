package duoc.desarrollomobile.portafolio.ui.screens.gallery

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import duoc.desarrollomobile.portafolio.data.model.Project
import duoc.desarrollomobile.portafolio.utils.CameraHelper
import java.io.File

/**
 * Diálogo para agregar un nuevo proyecto
 * Incluye captura de imagen con cámara (recurso nativo)
 * Formulario con validaciones visuales
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProjectDialog(
    onDismiss: () -> Unit,
    onProjectAdded: (Project) -> Unit,
    hasCameraPermission: Boolean
) {
    val context = LocalContext.current

    // Estados del formulario
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var technologies by remember { mutableStateOf("") }
    var githubUrl by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Mobile") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var tempImageFile by remember { mutableStateOf<File?>(null) }

    // Estados de validación
    var titleError by remember { mutableStateOf<String?>(null) }
    var descriptionError by remember { mutableStateOf<String?>(null) }
    var technologiesError by remember { mutableStateOf<String?>(null) }

    // Mostrar menú de categorías
    var showCategoryMenu by remember { mutableStateOf(false) }

    // Launcher para la cámara
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success && tempImageFile != null) {
            imageUri = Uri.fromFile(tempImageFile)
        }
    }

    // Validaciones
    fun validateTitle(value: String) {
        titleError = when {
            value.isBlank() -> "El título es obligatorio"
            value.length < 3 -> "El título debe tener al menos 3 caracteres"
            else -> null
        }
    }

    fun validateDescription(value: String) {
        descriptionError = when {
            value.isBlank() -> "La descripción es obligatoria"
            value.length < 10 -> "La descripción debe tener al menos 10 caracteres"
            else -> null
        }
    }

    fun validateTechnologies(value: String) {
        technologiesError = when {
            value.isBlank() -> "Debes agregar al menos una tecnología"
            else -> null
        }
    }

    fun validateForm(): Boolean {
        validateTitle(title)
        validateDescription(description)
        validateTechnologies(technologies)

        return titleError == null &&
                descriptionError == null &&
                technologiesError == null
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        modifier = Modifier.fillMaxWidth()
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            shape = MaterialTheme.shapes.large,
            tonalElevation = 6.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp)
            ) {
                // Título del diálogo
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Nuevo Proyecto",
                        style = MaterialTheme.typography.headlineSmall
                    )

                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Cerrar")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Sección de imagen
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        if (imageUri != null) {
                            AsyncImage(
                                model = imageUri,
                                contentDescription = "Imagen del proyecto",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AddPhotoAlternate,
                                    contentDescription = "Agregar foto",
                                    modifier = Modifier.size(64.dp),
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    text = "Sin imagen",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Botón para tomar foto
                Button(
                    onClick = {
                        if (hasCameraPermission) {
                            val (uri, file) = CameraHelper.createImageUri(context)
                            tempImageFile = file
                            cameraLauncher.launch(uri)
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = hasCameraPermission
                ) {
                    Icon(Icons.Default.CameraAlt, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (hasCameraPermission) {
                            "Tomar Foto"
                        } else {
                            "Permiso de cámara requerido"
                        }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Campo: Título
                OutlinedTextField(
                    value = title,
                    onValueChange = {
                        title = it
                        validateTitle(it)
                    },
                    label = { Text("Título *") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = titleError != null,
                    supportingText = {
                        if (titleError != null) {
                            Text(
                                text = titleError!!,
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    },
                    leadingIcon = {
                        Icon(Icons.Default.Title, contentDescription = null)
                    },
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Campo: Descripción
                OutlinedTextField(
                    value = description,
                    onValueChange = {
                        description = it
                        validateDescription(it)
                    },
                    label = { Text("Descripción *") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = descriptionError != null,
                    supportingText = {
                        if (descriptionError != null) {
                            Text(
                                text = descriptionError!!,
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    },
                    leadingIcon = {
                        Icon(Icons.Default.Description, contentDescription = null)
                    },
                    minLines = 3,
                    maxLines = 5
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Campo: Tecnologías
                OutlinedTextField(
                    value = technologies,
                    onValueChange = {
                        technologies = it
                        validateTechnologies(it)
                    },
                    label = { Text("Tecnologías (separadas por coma) *") },
                    placeholder = { Text("Kotlin, Android, Compose") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = technologiesError != null,
                    supportingText = {
                        if (technologiesError != null) {
                            Text(
                                text = technologiesError!!,
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    },
                    leadingIcon = {
                        Icon(Icons.Default.Code, contentDescription = null)
                    }
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Campo: Categoría (Dropdown)
                ExposedDropdownMenuBox(
                    expanded = showCategoryMenu,
                    onExpandedChange = { showCategoryMenu = it }
                ) {
                    OutlinedTextField(
                        value = category,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Categoría") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = showCategoryMenu)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(),
                        leadingIcon = {
                            Icon(Icons.Default.Label, contentDescription = null)
                        }
                    )

                    ExposedDropdownMenu(
                        expanded = showCategoryMenu,
                        onDismissRequest = { showCategoryMenu = false }
                    ) {
                        listOf("Web", "Mobile", "Backend", "Desktop", "Otro").forEach { cat ->
                            DropdownMenuItem(
                                text = { Text(cat) },
                                onClick = {
                                    category = cat
                                    showCategoryMenu = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Campo: GitHub URL (opcional)
                OutlinedTextField(
                    value = githubUrl,
                    onValueChange = { githubUrl = it },
                    label = { Text("URL de GitHub (opcional)") },
                    placeholder = { Text("https://github.com/...") },
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = {
                        Icon(Icons.Default.Link, contentDescription = null)
                    },
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Botones de acción
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Cancelar")
                    }

                    Button(
                        onClick = {
                            if (validateForm()) {
                                val project = Project(
                                    title = title.trim(),
                                    description = description.trim(),
                                    imageUri = imageUri?.toString(),
                                    technologies = technologies.trim(),
                                    githubUrl = githubUrl.trim().ifBlank { null },
                                    category = category
                                )
                                onProjectAdded(project)
                            }
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Default.Save, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Guardar")
                    }
                }
            }
        }
    }
}