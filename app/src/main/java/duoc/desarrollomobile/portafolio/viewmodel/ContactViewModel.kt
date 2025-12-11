package duoc.desarrollomobile.portafolio.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import duoc.desarrollomobile.portafolio.data.local.PortfolioDatabase
import duoc.desarrollomobile.portafolio.data.model.*
import duoc.desarrollomobile.portafolio.data.repository.ContactMessageRepository
import duoc.desarrollomobile.portafolio.data.repository.ContactApiRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ContactViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ContactMessageRepository
    private val apiRepository = ContactApiRepository() // ✅ NUEVO

    // ✅ NUEVO: Estados para datos de la API
    private val _tiposProyecto = MutableStateFlow<List<TipoProyecto>>(emptyList())
    val tiposProyecto: StateFlow<List<TipoProyecto>> = _tiposProyecto.asStateFlow()

    private val _presupuestos = MutableStateFlow<List<Presupuesto>>(emptyList())
    val presupuestos: StateFlow<List<Presupuesto>> = _presupuestos.asStateFlow()

    // ✅ NUEVO: Estados para tipo de proyecto y presupuesto seleccionados
    private val _selectedTipoProyecto = MutableStateFlow<String?>(null)
    val selectedTipoProyecto: StateFlow<String?> = _selectedTipoProyecto.asStateFlow()

    private val _selectedPresupuesto = MutableStateFlow<String?>(null)
    val selectedPresupuesto: StateFlow<String?> = _selectedPresupuesto.asStateFlow()

    // Tus estados existentes...
    val allMessages: StateFlow<List<ContactMessage>>
    val unreadMessages: StateFlow<List<ContactMessage>>

    private val _selectedMessage = MutableStateFlow<ContactMessage?>(null)
    val selectedMessage: StateFlow<ContactMessage?> = _selectedMessage.asStateFlow()

    private val _formName = MutableStateFlow("")
    val formName: StateFlow<String> = _formName.asStateFlow()

    private val _formEmail = MutableStateFlow("")
    val formEmail: StateFlow<String> = _formEmail.asStateFlow()

    private val _formPhone = MutableStateFlow("")
    val formPhone: StateFlow<String> = _formPhone.asStateFlow()

    private val _formCompany = MutableStateFlow("") // ✅ NUEVO: para empresa
    val formCompany: StateFlow<String> = _formCompany.asStateFlow()

    private val _formSubject = MutableStateFlow("")
    val formSubject: StateFlow<String> = _formSubject.asStateFlow()

    private val _formMessage = MutableStateFlow("")
    val formMessage: StateFlow<String> = _formMessage.asStateFlow()

    private val _latitude = MutableStateFlow<Double?>(null)
    val latitude: StateFlow<Double?> = _latitude.asStateFlow()

    private val _longitude = MutableStateFlow<Double?>(null)
    val longitude: StateFlow<Double?> = _longitude.asStateFlow()

    private val _nameError = MutableStateFlow<String?>(null)
    val nameError: StateFlow<String?> = _nameError.asStateFlow()

    private val _emailError = MutableStateFlow<String?>(null)
    val emailError: StateFlow<String?> = _emailError.asStateFlow()

    private val _phoneError = MutableStateFlow<String?>(null)
    val phoneError: StateFlow<String?> = _phoneError.asStateFlow()

    private val _subjectError = MutableStateFlow<String?>(null)
    val subjectError: StateFlow<String?> = _subjectError.asStateFlow()

    private val _messageError = MutableStateFlow<String?>(null)
    val messageError: StateFlow<String?> = _messageError.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _statusMessage = MutableStateFlow<String?>(null)
    val statusMessage: StateFlow<String?> = _statusMessage.asStateFlow()

    private val _unreadCount = MutableStateFlow(0)
    val unreadCount: StateFlow<Int> = _unreadCount.asStateFlow()

    init {
        val database = PortfolioDatabase.getDatabase(application)
        repository = ContactMessageRepository(database.contactMessageDao())

        allMessages = MutableStateFlow(emptyList())
        unreadMessages = MutableStateFlow(emptyList())

        viewModelScope.launch {
            repository.allMessages.collect { messages ->
                (allMessages as MutableStateFlow).value = messages
            }
        }

        viewModelScope.launch {
            repository.unreadMessages.collect { messages ->
                (unreadMessages as MutableStateFlow).value = messages
            }
        }

        updateUnreadCount()

        // ✅ NUEVO: Cargar datos de la API al iniciar
        loadTiposProyecto()
        loadPresupuestos()
    }

    // ✅ NUEVO: Cargar tipos de proyecto desde la API
    fun loadTiposProyecto() {
        viewModelScope.launch {
            _isLoading.value = true
            apiRepository.getTiposProyecto()
                .onSuccess { tipos ->
                    _tiposProyecto.value = tipos
                }
                .onFailure { error ->
                    _statusMessage.value = "Error al cargar tipos de proyecto: ${error.message}"
                }
            _isLoading.value = false
        }
    }

    // ✅ NUEVO: Cargar presupuestos desde la API
    fun loadPresupuestos() {
        viewModelScope.launch {
            _isLoading.value = true
            apiRepository.getPresupuestos()
                .onSuccess { presups ->
                    _presupuestos.value = presups
                }
                .onFailure { error ->
                    _statusMessage.value = "Error al cargar presupuestos: ${error.message}"
                }
            _isLoading.value = false
        }
    }

    // ✅ NUEVO: Actualizar tipo de proyecto seleccionado
    fun updateTipoProyecto(codigo: String) {
        _selectedTipoProyecto.value = codigo
    }

    // ✅ NUEVO: Actualizar presupuesto seleccionado
    fun updatePresupuesto(codigo: String) {
        _selectedPresupuesto.value = codigo
    }

    // Tus funciones existentes...
    fun updateFormName(name: String) {
        _formName.value = name
        validateName(name)
    }

    fun updateFormEmail(email: String) {
        _formEmail.value = email
        validateEmail(email)
    }

    fun updateFormPhone(phone: String) {
        _formPhone.value = phone
        validatePhone(phone)
    }

    // ✅ NUEVO: Actualizar empresa
    fun updateFormCompany(company: String) {
        _formCompany.value = company
    }

    fun updateFormSubject(subject: String) {
        _formSubject.value = subject
        validateSubject(subject)
    }

    fun updateFormMessage(message: String) {
        _formMessage.value = message
        validateMessage(message)
    }

    fun updateLocation(lat: Double?, lng: Double?) {
        _latitude.value = lat
        _longitude.value = lng
    }

    // Tus validaciones existentes...
    private fun validateName(name: String) {
        _nameError.value = when {
            name.isBlank() -> "El nombre es obligatorio"
            name.length < 3 -> "El nombre debe tener al menos 3 caracteres"
            !name.matches(Regex("^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$")) -> "El nombre solo puede contener letras"
            else -> null
        }
    }

    private fun validateEmail(email: String) {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        _emailError.value = when {
            email.isBlank() -> "El email es obligatorio"
            !email.matches(emailPattern.toRegex()) -> "Email inválido"
            else -> null
        }
    }

    private fun validatePhone(phone: String) {
        if (phone.isNotBlank()) {
            val phonePattern = "^[+]?[0-9]{8,15}$"
            _phoneError.value = if (!phone.matches(phonePattern.toRegex())) {
                "Teléfono inválido (8-15 dígitos)"
            } else null
        } else {
            _phoneError.value = null
        }
    }

    private fun validateSubject(subject: String) {
        _subjectError.value = when {
            subject.isBlank() -> "El asunto es obligatorio"
            subject.length < 5 -> "El asunto debe tener al menos 5 caracteres"
            else -> null
        }
    }

    private fun validateMessage(message: String) {
        _messageError.value = when {
            message.isBlank() -> "El mensaje es obligatorio"
            message.length < 10 -> "El mensaje debe tener al menos 10 caracteres"
            else -> null
        }
    }

    private fun validateForm(): Boolean {
        validateName(_formName.value)
        validateEmail(_formEmail.value)
        validatePhone(_formPhone.value)
        validateSubject(_formSubject.value)
        validateMessage(_formMessage.value)

        // ✅ NUEVO: Validar que se haya seleccionado un tipo de proyecto
        if (_selectedTipoProyecto.value.isNullOrBlank()) {
            _statusMessage.value = "Por favor, selecciona un tipo de proyecto"
            return false
        }

        return _nameError.value == null &&
                _emailError.value == null &&
                _phoneError.value == null &&
                _subjectError.value == null &&
                _messageError.value == null
    }

    // ✅ ACTUALIZADO: Enviar a la API REST
    fun submitContactForm() {
        if (!validateForm()) {
            _statusMessage.value = "Por favor, corrige los errores en el formulario"
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            try {
                // Crear el objeto para enviar a la API
                val contactoRequest = ContactoRequest(
                    nombre = _formName.value.trim(),
                    email = _formEmail.value.trim(),
                    telefono = _formPhone.value.trim().ifBlank { null },
                    empresa = _formCompany.value.trim().ifBlank { null },
                    tipoProyecto = _selectedTipoProyecto.value!!,
                    presupuesto = _selectedPresupuesto.value,
                    asunto = _formSubject.value.trim(),
                    mensaje = _formMessage.value.trim()
                )

                // Enviar a la API
                apiRepository.enviarContacto(contactoRequest)
                    .onSuccess { response ->
                        _statusMessage.value = "¡Mensaje enviado exitosamente! ID: ${response.id}"

                        // Opcionalmente, también guardar localmente
                        val localMessage = ContactMessage(
                            name = response.nombre,
                            email = response.email,
                            phone = response.telefono,
                            subject = response.asunto,
                            message = response.mensaje,
                            latitude = _latitude.value,
                            longitude = _longitude.value
                        )
                        repository.insertMessage(localMessage)

                        clearForm()
                        updateUnreadCount()
                    }
                    .onFailure { error ->
                        _statusMessage.value = "Error al enviar mensaje: ${error.message}"
                    }
            } catch (e: Exception) {
                _statusMessage.value = "Error inesperado: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearForm() {
        _formName.value = ""
        _formEmail.value = ""
        _formPhone.value = ""
        _formCompany.value = ""
        _formSubject.value = ""
        _formMessage.value = ""
        _selectedTipoProyecto.value = null
        _selectedPresupuesto.value = null
        _nameError.value = null
        _emailError.value = null
        _phoneError.value = null
        _subjectError.value = null
        _messageError.value = null
    }

    fun markAsRead(messageId: Int) {
        viewModelScope.launch {
            try {
                repository.markAsRead(messageId, true)
                updateUnreadCount()
            } catch (e: Exception) {
                _statusMessage.value = "Error al marcar mensaje: ${e.message}"
            }
        }
    }

    fun deleteMessage(message: ContactMessage) {
        viewModelScope.launch {
            try {
                repository.deleteMessage(message)
                _statusMessage.value = "Mensaje eliminado"
                updateUnreadCount()
            } catch (e: Exception) {
                _statusMessage.value = "Error al eliminar mensaje: ${e.message}"
            }
        }
    }

    private fun updateUnreadCount() {
        viewModelScope.launch {
            _unreadCount.value = repository.getUnreadCount()
        }
    }

    fun selectMessage(message: ContactMessage?) {
        _selectedMessage.value = message
    }

    fun clearStatusMessage() {
        _statusMessage.value = null
    }
}