package duoc.desarrollomobile.portafolio.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import duoc.desarrollomobile.portafolio.data.local.PortfolioDatabase
import duoc.desarrollomobile.portafolio.data.model.ContactMessage
import duoc.desarrollomobile.portafolio.data.repository.ContactMessageRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel para gestionar el estado de Mensajes de Contacto
 * Maneja la lógica de negocio y comunica la UI con el repositorio
 */
class ContactViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ContactMessageRepository

    // StateFlow para observar la lista de mensajes
    val allMessages: StateFlow<List<ContactMessage>>
    val unreadMessages: StateFlow<List<ContactMessage>>

    // Estado para mensaje seleccionado
    private val _selectedMessage = MutableStateFlow<ContactMessage?>(null)
    val selectedMessage: StateFlow<ContactMessage?> = _selectedMessage.asStateFlow()

    // Estado para datos del formulario
    private val _formName = MutableStateFlow("")
    val formName: StateFlow<String> = _formName.asStateFlow()

    private val _formEmail = MutableStateFlow("")
    val formEmail: StateFlow<String> = _formEmail.asStateFlow()

    private val _formPhone = MutableStateFlow("")
    val formPhone: StateFlow<String> = _formPhone.asStateFlow()

    private val _formSubject = MutableStateFlow("")
    val formSubject: StateFlow<String> = _formSubject.asStateFlow()

    private val _formMessage = MutableStateFlow("")
    val formMessage: StateFlow<String> = _formMessage.asStateFlow()

    // Estado para ubicación GPS
    private val _latitude = MutableStateFlow<Double?>(null)
    val latitude: StateFlow<Double?> = _latitude.asStateFlow()

    private val _longitude = MutableStateFlow<Double?>(null)
    val longitude: StateFlow<Double?> = _longitude.asStateFlow()

    // Estado de validación
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

    // Estado de carga
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // Estado de mensaje de confirmación
    private val _statusMessage = MutableStateFlow<String?>(null)
    val statusMessage: StateFlow<String?> = _statusMessage.asStateFlow()

    // Estado para contador de mensajes no leídos
    private val _unreadCount = MutableStateFlow(0)
    val unreadCount: StateFlow<Int> = _unreadCount.asStateFlow()

    init {
        val database = PortfolioDatabase.getDatabase(application)
        repository = ContactMessageRepository(database.contactMessageDao())

        // Convertir Flow a StateFlow
        allMessages = MutableStateFlow(emptyList())
        unreadMessages = MutableStateFlow(emptyList())

        // Observar cambios en los mensajes
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

        // Actualizar contador de no leídos
        updateUnreadCount()
    }

    /**
     * Actualiza los campos del formulario
     */
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

    fun updateFormSubject(subject: String) {
        _formSubject.value = subject
        validateSubject(subject)
    }

    fun updateFormMessage(message: String) {
        _formMessage.value = message
        validateMessage(message)
    }

    /**
     * Actualiza la ubicación GPS
     */
    fun updateLocation(lat: Double?, lng: Double?) {
        _latitude.value = lat
        _longitude.value = lng
    }

    /**
     * Validaciones individuales por campo
     */
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

    /**
     * Valida todos los campos del formulario
     */
    private fun validateForm(): Boolean {
        validateName(_formName.value)
        validateEmail(_formEmail.value)
        validatePhone(_formPhone.value)
        validateSubject(_formSubject.value)
        validateMessage(_formMessage.value)

        return _nameError.value == null &&
                _emailError.value == null &&
                _phoneError.value == null &&
                _subjectError.value == null &&
                _messageError.value == null
    }

    /**
     * Envía el formulario de contacto
     */
    fun submitContactForm() {
        if (!validateForm()) {
            _statusMessage.value = "Por favor, corrige los errores en el formulario"
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            try {
                val contactMessage = ContactMessage(
                    name = _formName.value.trim(),
                    email = _formEmail.value.trim(),
                    phone = _formPhone.value.trim().ifBlank { null },
                    subject = _formSubject.value.trim(),
                    message = _formMessage.value.trim(),
                    latitude = _latitude.value,
                    longitude = _longitude.value
                )

                repository.insertMessage(contactMessage)
                _statusMessage.value = "¡Mensaje enviado exitosamente!"
                clearForm()
                updateUnreadCount()
            } catch (e: Exception) {
                _statusMessage.value = "Error al enviar mensaje: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Limpia el formulario
     */
    fun clearForm() {
        _formName.value = ""
        _formEmail.value = ""
        _formPhone.value = ""
        _formSubject.value = ""
        _formMessage.value = ""
        _nameError.value = null
        _emailError.value = null
        _phoneError.value = null
        _subjectError.value = null
        _messageError.value = null
    }

    /**
     * Marca un mensaje como leído
     */
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

    /**
     * Elimina un mensaje
     */
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

    /**
     * Actualiza el contador de mensajes no leídos
     */
    private fun updateUnreadCount() {
        viewModelScope.launch {
            _unreadCount.value = repository.getUnreadCount()
        }
    }

    /**
     * Selecciona un mensaje para ver detalles
     */
    fun selectMessage(message: ContactMessage?) {
        _selectedMessage.value = message
    }

    /**
     * Limpia el mensaje de estado
     */
    fun clearStatusMessage() {
        _statusMessage.value = null
    }
}