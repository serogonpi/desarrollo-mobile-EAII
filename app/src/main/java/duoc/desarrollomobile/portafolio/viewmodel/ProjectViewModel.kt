package duoc.desarrollomobile.portafolio.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import duoc.desarrollomobile.portafolio.data.local.PortfolioDatabase
import duoc.desarrollomobile.portafolio.data.model.Project
import duoc.desarrollomobile.portafolio.data.repository.ProjectRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel para gestionar el estado de Proyectos
 * Maneja la lógica de negocio y comunica la UI con el repositorio
 */
class ProjectViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ProjectRepository

    // StateFlow para observar la lista de proyectos
    val allProjects: StateFlow<List<Project>>
    val favoriteProjects: StateFlow<List<Project>>

    // Estado para categoría seleccionada
    private val _selectedCategory = MutableStateFlow("Todos")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    // Estado para proyecto seleccionado
    private val _selectedProject = MutableStateFlow<Project?>(null)
    val selectedProject: StateFlow<Project?> = _selectedProject.asStateFlow()

    // Estado de carga
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // Estado de mensaje
    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message.asStateFlow()

    init {
        val database = PortfolioDatabase.getDatabase(application)
        repository = ProjectRepository(database.projectDao())

        // Convertir Flow a StateFlow
        allProjects = MutableStateFlow(emptyList())
        favoriteProjects = MutableStateFlow(emptyList())

        // Observar cambios en los proyectos
        viewModelScope.launch {
            repository.allProjects.collect { projects ->
                (allProjects as MutableStateFlow).value = projects
            }
        }

        viewModelScope.launch {
            repository.favoriteProjects.collect { projects ->
                (favoriteProjects as MutableStateFlow).value = projects
            }
        }
    }

    /**
     * Inserta o actualiza un proyecto
     */
    fun saveProject(project: Project) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                if (project.id == 0) {
                    repository.insertProject(project)
                    _message.value = "Proyecto creado exitosamente"
                } else {
                    repository.updateProject(project)
                    _message.value = "Proyecto actualizado exitosamente"
                }
            } catch (e: Exception) {
                _message.value = "Error al guardar proyecto: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Elimina un proyecto
     */
    fun deleteProject(project: Project) {
        viewModelScope.launch {
            try {
                repository.deleteProject(project)
                _message.value = "Proyecto eliminado"
            } catch (e: Exception) {
                _message.value = "Error al eliminar proyecto: ${e.message}"
            }
        }
    }

    /**
     * Cambia el estado de favorito de un proyecto
     */
    fun toggleFavorite(projectId: Int, isFavorite: Boolean) {
        viewModelScope.launch {
            try {
                repository.toggleFavorite(projectId, isFavorite)
            } catch (e: Exception) {
                _message.value = "Error al actualizar favorito: ${e.message}"
            }
        }
    }

    /**
     * Selecciona un proyecto para ver detalles
     */
    fun selectProject(project: Project?) {
        _selectedProject.value = project
    }

    /**
     * Filtra proyectos por categoría
     */
    fun filterByCategory(category: String) {
        _selectedCategory.value = category
    }

    /**
     * Limpia el mensaje después de mostrarlo
     */
    fun clearMessage() {
        _message.value = null
    }

    /**
     * Obtiene el total de proyectos
     */
    fun getProjectCount(callback: (Int) -> Unit) {
        viewModelScope.launch {
            val count = repository.getProjectCount()
            callback(count)
        }
    }
}