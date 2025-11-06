package duoc.desarrollomobile.portafolio.ui.screens.gallery

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import duoc.desarrollomobile.portafolio.data.model.Project
import duoc.desarrollomobile.portafolio.utils.CameraHelper
import duoc.desarrollomobile.portafolio.utils.PermissionHelper
import duoc.desarrollomobile.portafolio.viewmodel.ProjectViewModel
import java.io.File
import android.content.Intent
import android.net.Uri
import androidx.compose.ui.platform.LocalContext
import android.widget.Toast

/**
 * Pantalla de galería de proyectos
 * Permite ver, agregar y gestionar proyectos del portafolio
 * Incluye acceso a la cámara (recurso nativo)
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GalleryScreen(
    navController: NavController,
    viewModel: ProjectViewModel = viewModel()
) {
    val context = LocalContext.current
    val projects by viewModel.allProjects.collectAsState()
    val message by viewModel.message.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()

    // Estados para el diálogo de agregar proyecto
    var showAddDialog by remember { mutableStateOf(false) }
    var showFilterDialog by remember { mutableStateOf(false) }

    // Estado para permisos de cámara
    var hasCameraPermission by remember {
        mutableStateOf(PermissionHelper.hasCameraPermission(context))
    }

    // Launcher para permisos de cámara
    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasCameraPermission = isGranted
        if (!isGranted) {
            viewModel.clearMessage()
        }
    }

    // Mostrar mensajes
    LaunchedEffect(message) {
        message?.let {
            viewModel.clearMessage()
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if (hasCameraPermission) {
                        showAddDialog = true
                    } else {
                        cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                    }
                }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Agregar proyecto")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Filtros
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Categoría: $selectedCategory",
                    style = MaterialTheme.typography.titleMedium
                )

                FilterChip(
                    selected = false,
                    onClick = { showFilterDialog = true },
                    label = { Text("Filtrar") },
                    leadingIcon = {
                        Icon(Icons.Default.FilterList, contentDescription = "Filtrar")
                    }
                )
            }

            Divider()

            // Lista de proyectos
            if (projects.isEmpty()) {
                EmptyGalleryState()
            } else {
                val filteredProjects = if (selectedCategory == "Todos") {
                    projects
                } else {
                    projects.filter { it.category == selectedCategory }
                }

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(filteredProjects) { project ->
                        ProjectCard(
                            project = project,
                            onFavoriteClick = {
                                viewModel.toggleFavorite(project.id, !project.isFavorite)
                            },
                            onDeleteClick = {
                                viewModel.deleteProject(project)
                            }
                        )
                    }
                }
            }
        }

        // Diálogo para agregar proyecto
        if (showAddDialog) {
            AddProjectDialog(
                onDismiss = { showAddDialog = false },
                onProjectAdded = { project ->
                    viewModel.saveProject(project)
                    showAddDialog = false
                },
                hasCameraPermission = hasCameraPermission
            )
        }

        // Diálogo de filtros
        if (showFilterDialog) {
            FilterDialog(
                currentCategory = selectedCategory,
                onDismiss = { showFilterDialog = false },
                onCategorySelected = { category ->
                    viewModel.filterByCategory(category)
                    showFilterDialog = false
                }
            )
        }
    }
}

@Composable
fun EmptyGalleryState() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.PhotoLibrary,
            contentDescription = "Sin proyectos",
            modifier = Modifier.size(120.dp),
            tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "No hay proyectos aún",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Agrega tu primer proyecto usando el botón +",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectCard(
    project: Project,
    onFavoriteClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=dQw4w9WgXcQ&list=RDdQw4w9WgXcQ&start_radio=1"))
            context.startActivity(intent)
        }
    ) {
        Column {
            // Imagen del proyecto
            if (project.imageUri != null) {
                AsyncImage(
                    model = Uri.parse(project.imageUri),
                    contentDescription = project.title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentScale = ContentScale.Crop
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Image,
                        contentDescription = "Sin imagen",
                        modifier = Modifier.size(80.dp),
                        tint = MaterialTheme.colorScheme.surfaceVariant
                    )
                }
            }

            // Información del proyecto
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = project.title,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f)
                    )

                    Row {
                        IconButton(onClick = onFavoriteClick) {
                            Icon(
                                imageVector = if (project.isFavorite) {
                                    Icons.Default.Favorite
                                } else {
                                    Icons.Default.FavoriteBorder
                                },
                                contentDescription = "Favorito",
                                tint = if (project.isFavorite) {
                                    MaterialTheme.colorScheme.error
                                } else {
                                    MaterialTheme.colorScheme.onSurface
                                }
                            )
                        }

                        Box {
                            IconButton(onClick = { showMenu = true }) {
                                Icon(Icons.Default.MoreVert, contentDescription = "Opciones")
                            }

                            DropdownMenu(
                                expanded = showMenu,
                                onDismissRequest = { showMenu = false }
                            ) {
                                DropdownMenuItem(
                                    text = { Text("Eliminar") },
                                    onClick = {
                                        onDeleteClick()
                                        showMenu = false
                                    },
                                    leadingIcon = {
                                        Icon(Icons.Default.Delete, contentDescription = null)
                                    }
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                AssistChip(
                    onClick = { },
                    label = { Text(project.category) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Label,
                            contentDescription = "Categoría",
                            modifier = Modifier.size(18.dp)
                        )
                    }
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = project.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    maxLines = 3
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Tecnologías
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    project.getTechnologiesList().take(3).forEach { tech ->
                        SuggestionChip(
                            onClick = { },
                            label = { Text(tech, style = MaterialTheme.typography.bodySmall) }
                        )
                    }

                    if (project.getTechnologiesList().size > 3) {
                        SuggestionChip(
                            onClick = { },
                            label = {
                                Text(
                                    "+${project.getTechnologiesList().size - 3}",
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun FilterDialog(
    currentCategory: String,
    onDismiss: () -> Unit,
    onCategorySelected: (String) -> Unit
) {
    val categories = listOf("Todos", "Web", "Mobile", "Backend", "Desktop", "Otro")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Filtrar por categoría") },
        text = {
            Column {
                categories.forEach { category ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = category == currentCategory,
                            onClick = { onCategorySelected(category) }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(category)
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Cerrar")
            }
        }
    )
}