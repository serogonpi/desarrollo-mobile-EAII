package duoc.desarrollomobile.portafolio.ui.screens.contact

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import duoc.desarrollomobile.portafolio.utils.LocationHelper
import duoc.desarrollomobile.portafolio.utils.PermissionHelper
import duoc.desarrollomobile.portafolio.viewmodel.ContactViewModel

/**
 * Pantalla de contacto
 * Formulario completo con validaciones, acceso a GPS y conexión a API REST
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactScreen(
    navController: NavController,
    viewModel: ContactViewModel = viewModel()
) {
    val context = LocalContext.current
    val locationHelper = remember { LocationHelper(context) }

    // Estados del ViewModel
    val formName by viewModel.formName.collectAsState()
    val formEmail by viewModel.formEmail.collectAsState()
    val formPhone by viewModel.formPhone.collectAsState()
    val formCompany by viewModel.formCompany.collectAsState() // ✅ NUEVO
    val formSubject by viewModel.formSubject.collectAsState()
    val formMessage by viewModel.formMessage.collectAsState()

    // ✅ NUEVO: Estados de la API
    val tiposProyecto by viewModel.tiposProyecto.collectAsState()
    val presupuestos by viewModel.presupuestos.collectAsState()
    val selectedTipoProyecto by viewModel.selectedTipoProyecto.collectAsState()
    val selectedPresupuesto by viewModel.selectedPresupuesto.collectAsState()

    // Estados de error
    val nameError by viewModel.nameError.collectAsState()
    val emailError by viewModel.emailError.collectAsState()
    val phoneError by viewModel.phoneError.collectAsState()
    val subjectError by viewModel.subjectError.collectAsState()
    val messageError by viewModel.messageError.collectAsState()

    // Estados de ubicación
    val latitude by viewModel.latitude.collectAsState()
    val longitude by viewModel.longitude.collectAsState()

    // Estados generales
    val isLoading by viewModel.isLoading.collectAsState()
    val statusMessage by viewModel.statusMessage.collectAsState()

    // Estados locales
    var hasLocationPermission by remember {
        mutableStateOf(PermissionHelper.hasLocationPermission(context))
    }
    var isLoadingLocation by remember { mutableStateOf(false) }
    var locationError by remember { mutableStateOf<String?>(null) }
    var showSuccessDialog by remember { mutableStateOf(false) }

    // ✅ NUEVO: Estados para los dropdowns
    var expandedTipoProyecto by remember { mutableStateOf(false) }
    var expandedPresupuesto by remember { mutableStateOf(false) }

    // Launcher para permisos de ubicación
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        hasLocationPermission = permissions.values.all { it }
        if (hasLocationPermission) {
            isLoadingLocation = true
            locationHelper.getLastLocation(
                onSuccess = { lat, lng ->
                    viewModel.updateLocation(lat, lng)
                    isLoadingLocation = false
                    locationError = null
                },
                onFailure = { error ->
                    locationError = error
                    isLoadingLocation = false
                }
            )
        }
    }

    // Mostrar diálogo de éxito
    LaunchedEffect(statusMessage) {
        if (statusMessage?.contains("exitosamente") == true) {
            showSuccessDialog = true
        }
    }

    Scaffold(
        snackbarHost = {
            if (statusMessage != null && !statusMessage!!.contains("exitosamente")) {
                Snackbar(
                    modifier = Modifier.padding(16.dp),
                    action = {
                        TextButton(onClick = { viewModel.clearStatusMessage() }) {
                            Text("OK")
                        }
                    }
                ) {
                    Text(statusMessage!!)
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Encabezado
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.ContactMail,
                        contentDescription = "Contacto",
                        modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "¡Contáctame!",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Envíame un mensaje y me pondré en contacto contigo lo antes posible",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Sección de ubicación GPS
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Ubicación GPS",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )

                            if (latitude != null && longitude != null) {
                                Text(
                                    text = "Lat: %.6f, Lng: %.6f".format(latitude, longitude),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.7f)
                                )
                            } else {
                                Text(
                                    text = "No capturada",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.7f)
                                )
                            }
                        }

                        if (isLoadingLocation) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                strokeWidth = 2.dp
                            )
                        } else {
                            IconButton(
                                onClick = {
                                    if (hasLocationPermission) {
                                        isLoadingLocation = true
                                        locationHelper.getCurrentLocation(
                                            onSuccess = { lat, lng ->
                                                viewModel.updateLocation(lat, lng)
                                                isLoadingLocation = false
                                                locationError = null
                                            },
                                            onFailure = { error ->
                                                locationError = error
                                                isLoadingLocation = false
                                            }
                                        )
                                    } else {
                                        locationPermissionLauncher.launch(
                                            PermissionHelper.LOCATION_PERMISSIONS
                                        )
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.MyLocation,
                                    contentDescription = "Obtener ubicación"
                                )
                            }
                        }
                    }

                    if (locationError != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = locationError!!,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.error
                        )
                    }

                    if (!hasLocationPermission) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Permiso de ubicación no concedido",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Formulario de Contacto",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Campo: Nombre
            OutlinedTextField(
                value = formName,
                onValueChange = { viewModel.updateFormName(it) },
                label = { Text("Nombre completo *") },
                placeholder = { Text("Juan Pérez") },
                modifier = Modifier.fillMaxWidth(),
                isError = nameError != null,
                supportingText = {
                    if (nameError != null) {
                        Text(
                            text = nameError!!,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                },
                leadingIcon = {
                    Icon(Icons.Default.Person, contentDescription = null)
                },
                singleLine = true,
                enabled = !isLoading
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Campo: Email
            OutlinedTextField(
                value = formEmail,
                onValueChange = { viewModel.updateFormEmail(it) },
                label = { Text("Email *") },
                placeholder = { Text("correo@ejemplo.com") },
                modifier = Modifier.fillMaxWidth(),
                isError = emailError != null,
                supportingText = {
                    if (emailError != null) {
                        Text(
                            text = emailError!!,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                },
                leadingIcon = {
                    Icon(Icons.Default.Email, contentDescription = null)
                },
                singleLine = true,
                enabled = !isLoading
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Campo: Teléfono
            OutlinedTextField(
                value = formPhone,
                onValueChange = { viewModel.updateFormPhone(it) },
                label = { Text("Teléfono (opcional)") },
                placeholder = { Text("+56912345678") },
                modifier = Modifier.fillMaxWidth(),
                isError = phoneError != null,
                supportingText = {
                    if (phoneError != null) {
                        Text(
                            text = phoneError!!,
                            color = MaterialTheme.colorScheme.error
                        )
                    } else {
                        Text(
                            text = "8-15 dígitos, opcional",
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    }
                },
                leadingIcon = {
                    Icon(Icons.Default.Phone, contentDescription = null)
                },
                singleLine = true,
                enabled = !isLoading
            )

            Spacer(modifier = Modifier.height(12.dp))

            // ✅ NUEVO: Campo Empresa
            OutlinedTextField(
                value = formCompany,
                onValueChange = { viewModel.updateFormCompany(it) },
                label = { Text("Empresa (opcional)") },
                placeholder = { Text("Mi Empresa S.A.") },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = {
                    Icon(Icons.Default.Business, contentDescription = null)
                },
                singleLine = true,
                enabled = !isLoading
            )

            Spacer(modifier = Modifier.height(12.dp))

            // ✅ NUEVO: Dropdown Tipo de Proyecto
            ExposedDropdownMenuBox(
                expanded = expandedTipoProyecto,
                onExpandedChange = { expandedTipoProyecto = !expandedTipoProyecto && !isLoading }
            ) {
                OutlinedTextField(
                    value = tiposProyecto.find { it.codigo == selectedTipoProyecto }?.nombre ?: "",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Tipo de Proyecto *") },
                    placeholder = { Text("Selecciona un tipo") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedTipoProyecto)
                    },
                    leadingIcon = {
                        Icon(Icons.Default.WorkOutline, contentDescription = null)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    enabled = !isLoading,
                    colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
                )

                ExposedDropdownMenu(
                    expanded = expandedTipoProyecto,
                    onDismissRequest = { expandedTipoProyecto = false }
                ) {
                    if (tiposProyecto.isEmpty()) {
                        DropdownMenuItem(
                            text = { Text("Cargando...") },
                            onClick = { }
                        )
                    } else {
                        tiposProyecto.forEach { tipo ->
                            DropdownMenuItem(
                                text = { Text(tipo.nombre) },
                                onClick = {
                                    viewModel.updateTipoProyecto(tipo.codigo)
                                    expandedTipoProyecto = false
                                },
                                leadingIcon = {
                                    if (tipo.codigo == selectedTipoProyecto) {
                                        Icon(Icons.Default.Check, contentDescription = null)
                                    }
                                }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // ✅ NUEVO: Dropdown Presupuesto
            ExposedDropdownMenuBox(
                expanded = expandedPresupuesto,
                onExpandedChange = { expandedPresupuesto = !expandedPresupuesto && !isLoading }
            ) {
                OutlinedTextField(
                    value = presupuestos.find { it.codigo == selectedPresupuesto }?.nombre ?: "",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Presupuesto (opcional)") },
                    placeholder = { Text("Selecciona un rango") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedPresupuesto)
                    },
                    leadingIcon = {
                        Icon(Icons.Default.AttachMoney, contentDescription = null)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    enabled = !isLoading,
                    colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
                )

                ExposedDropdownMenu(
                    expanded = expandedPresupuesto,
                    onDismissRequest = { expandedPresupuesto = false }
                ) {
                    if (presupuestos.isEmpty()) {
                        DropdownMenuItem(
                            text = { Text("Cargando...") },
                            onClick = { }
                        )
                    } else {
                        // Opción para limpiar selección
                        DropdownMenuItem(
                            text = { Text("Ninguno") },
                            onClick = {
                                viewModel.updatePresupuesto("")
                                expandedPresupuesto = false
                            }
                        )

                        presupuestos.forEach { presupuesto ->
                            DropdownMenuItem(
                                text = { Text(presupuesto.nombre) },
                                onClick = {
                                    viewModel.updatePresupuesto(presupuesto.codigo)
                                    expandedPresupuesto = false
                                },
                                leadingIcon = {
                                    if (presupuesto.codigo == selectedPresupuesto) {
                                        Icon(Icons.Default.Check, contentDescription = null)
                                    }
                                }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Campo: Asunto
            OutlinedTextField(
                value = formSubject,
                onValueChange = { viewModel.updateFormSubject(it) },
                label = { Text("Asunto *") },
                placeholder = { Text("Consulta sobre proyecto") },
                modifier = Modifier.fillMaxWidth(),
                isError = subjectError != null,
                supportingText = {
                    if (subjectError != null) {
                        Text(
                            text = subjectError!!,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                },
                leadingIcon = {
                    Icon(Icons.Default.Subject, contentDescription = null)
                },
                singleLine = true,
                enabled = !isLoading
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Campo: Mensaje
            OutlinedTextField(
                value = formMessage,
                onValueChange = { viewModel.updateFormMessage(it) },
                label = { Text("Mensaje *") },
                placeholder = { Text("Escribe tu mensaje aquí...") },
                modifier = Modifier.fillMaxWidth(),
                isError = messageError != null,
                supportingText = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = messageError ?: "",
                            color = if (messageError != null) {
                                MaterialTheme.colorScheme.error
                            } else {
                                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            }
                        )
                        Text(
                            text = "${formMessage.length} caracteres",
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    }
                },
                leadingIcon = {
                    Icon(Icons.Default.Message, contentDescription = null)
                },
                minLines = 5,
                maxLines = 8,
                enabled = !isLoading
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Botón de envío
            Button(
                onClick = { viewModel.submitContactForm() },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading && selectedTipoProyecto != null
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Enviando...")
                } else {
                    Icon(Icons.Default.Send, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Enviar Mensaje")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ✅ NUEVO: Indicador de estado de conexión
            if (tiposProyecto.isEmpty() || presupuestos.isEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "No se pudieron cargar los datos del servidor. Verifica tu conexión.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }

    // Diálogo de éxito
    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = {
                showSuccessDialog = false
                viewModel.clearStatusMessage()
            },
            icon = {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(64.dp)
                )
            },
            title = {
                Text(
                    text = "¡Mensaje Enviado!",
                    style = MaterialTheme.typography.headlineSmall
                )
            },
            text = {
                Text("Tu mensaje ha sido enviado exitosamente. Me pondré en contacto contigo pronto.")
            },
            confirmButton = {
                Button(
                    onClick = {
                        showSuccessDialog = false
                        viewModel.clearStatusMessage()
                    }
                ) {
                    Text("Aceptar")
                }
            }
        )
    }
}