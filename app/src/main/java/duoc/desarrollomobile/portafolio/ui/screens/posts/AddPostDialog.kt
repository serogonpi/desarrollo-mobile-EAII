package duoc.desarrollomobile.portafolio.ui.screens.posts

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import duoc.desarrollomobile.portafolio.data.model.Post

/**
 * Diálogo para agregar un nuevo post
 * Formulario completo con validaciones visuales
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPostDialog(
    onDismiss: () -> Unit,
    onPostAdded: (Post) -> Unit
) {
    // Estados del formulario
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var summary by remember { mutableStateOf("") }
    var author by remember { mutableStateOf("") }
    var tags by remember { mutableStateOf("") }
    var isPublished by remember { mutableStateOf(true) }

    // Estados de validación
    var titleError by remember { mutableStateOf<String?>(null) }
    var contentError by remember { mutableStateOf<String?>(null) }
    var summaryError by remember { mutableStateOf<String?>(null) }
    var authorError by remember { mutableStateOf<String?>(null) }
    var tagsError by remember { mutableStateOf<String?>(null) }

    // Validaciones
    fun validateTitle(value: String) {
        titleError = when {
            value.isBlank() -> "El título es obligatorio"
            value.length < 5 -> "El título debe tener al menos 5 caracteres"
            value.length > 100 -> "El título no puede exceder 100 caracteres"
            else -> null
        }
    }

    fun validateContent(value: String) {
        contentError = when {
            value.isBlank() -> "El contenido es obligatorio"
            value.length < 20 -> "El contenido debe tener al menos 20 caracteres"
            else -> null
        }
    }

    fun validateSummary(value: String) {
        summaryError = when {
            value.isBlank() -> "El resumen es obligatorio"
            value.length < 10 -> "El resumen debe tener al menos 10 caracteres"
            value.length > 200 -> "El resumen no puede exceder 200 caracteres"
            else -> null
        }
    }

    fun validateAuthor(value: String) {
        authorError = when {
            value.isBlank() -> "El autor es obligatorio"
            value.length < 3 -> "El nombre del autor debe tener al menos 3 caracteres"
            !value.matches(Regex("^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$")) -> "El nombre solo puede contener letras"
            else -> null
        }
    }

    fun validateTags(value: String) {
        tagsError = when {
            value.isBlank() -> "Debes agregar al menos un tag"
            value.split(",").any { it.trim().isEmpty() } -> "Los tags no pueden estar vacíos"
            else -> null
        }
    }

    fun validateForm(): Boolean {
        validateTitle(title)
        validateContent(content)
        validateSummary(summary)
        validateAuthor(author)
        validateTags(tags)

        return titleError == null &&
                contentError == null &&
                summaryError == null &&
                authorError == null &&
                tagsError == null
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
                        text = "Nueva Publicación",
                        style = MaterialTheme.typography.headlineSmall
                    )

                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Cerrar")
                    }
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
                    placeholder = { Text("Título del post") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = titleError != null,
                    supportingText = {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = titleError ?: "",
                                color = if (titleError != null) {
                                    MaterialTheme.colorScheme.error
                                } else {
                                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                }
                            )
                            Text(
                                text = "${title.length}/100",
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                        }
                    },
                    leadingIcon = {
                        Icon(Icons.Default.Title, contentDescription = null)
                    },
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Campo: Autor
                OutlinedTextField(
                    value = author,
                    onValueChange = {
                        author = it
                        validateAuthor(it)
                    },
                    label = { Text("Autor *") },
                    placeholder = { Text("Tu nombre") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = authorError != null,
                    supportingText = {
                        if (authorError != null) {
                            Text(
                                text = authorError!!,
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    },
                    leadingIcon = {
                        Icon(Icons.Default.Person, contentDescription = null)
                    },
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Campo: Resumen
                OutlinedTextField(
                    value = summary,
                    onValueChange = {
                        summary = it
                        validateSummary(it)
                    },
                    label = { Text("Resumen *") },
                    placeholder = { Text("Breve descripción del post") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = summaryError != null,
                    supportingText = {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = summaryError ?: "",
                                color = if (summaryError != null) {
                                    MaterialTheme.colorScheme.error
                                } else {
                                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                }
                            )
                            Text(
                                text = "${summary.length}/200",
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                        }
                    },
                    leadingIcon = {
                        Icon(Icons.Default.ShortText, contentDescription = null)
                    },
                    minLines = 2,
                    maxLines = 3
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Campo: Contenido
                OutlinedTextField(
                    value = content,
                    onValueChange = {
                        content = it
                        validateContent(it)
                    },
                    label = { Text("Contenido *") },
                    placeholder = { Text("Escribe el contenido completo del post...") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = contentError != null,
                    supportingText = {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = contentError ?: "",
                                color = if (contentError != null) {
                                    MaterialTheme.colorScheme.error
                                } else {
                                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                }
                            )
                            Text(
                                text = "${content.length} caracteres",
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                        }
                    },
                    leadingIcon = {
                        Icon(Icons.Default.Article, contentDescription = null)
                    },
                    minLines = 5,
                    maxLines = 8
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Campo: Tags
                OutlinedTextField(
                    value = tags,
                    onValueChange = {
                        tags = it
                        validateTags(it)
                    },
                    label = { Text("Tags (separados por coma) *") },
                    placeholder = { Text("kotlin, android, tutorial") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = tagsError != null,
                    supportingText = {
                        if (tagsError != null) {
                            Text(
                                text = tagsError!!,
                                color = MaterialTheme.colorScheme.error
                            )
                        } else {
                            Text(
                                text = "Separa los tags con comas",
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                        }
                    },
                    leadingIcon = {
                        Icon(Icons.Default.LocalOffer, contentDescription = null)
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Switch: Publicar inmediatamente
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Publicar inmediatamente",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = androidx.compose.ui.text.font.FontWeight.Medium
                        )
                        Text(
                            text = if (isPublished) "El post será visible" else "El post estará oculto",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    }

                    Switch(
                        checked = isPublished,
                        onCheckedChange = { isPublished = it }
                    )
                }

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
                                val post = Post(
                                    title = title.trim(),
                                    content = content.trim(),
                                    summary = summary.trim(),
                                    author = author.trim(),
                                    tags = tags.trim(),
                                    isPublished = isPublished
                                )
                                onPostAdded(post)
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