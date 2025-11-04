package duoc.desarrollomobile.portafolio.data.repository

import duoc.desarrollomobile.portafolio.data.local.ProjectDao
import duoc.desarrollomobile.portafolio.data.model.Project
import kotlinx.coroutines.flow.Flow

/**
 * Repositorio para gestionar operaciones de Proyectos
 * Actúa como intermediario entre el DAO y el ViewModel
 * Centraliza la lógica de acceso a datos
 */
class ProjectRepository(private val projectDao: ProjectDao) {

    // Flow para observar cambios en tiempo real
    val allProjects: Flow<List<Project>> = projectDao.getAllProjects()
    val favoriteProjects: Flow<List<Project>> = projectDao.getFavoriteProjects()

    /**
     * Inserta un nuevo proyecto
     * @return ID del proyecto insertado
     */
    suspend fun insertProject(project: Project): Long {
        return projectDao.insert(project)
    }

    /**
     * Actualiza un proyecto existente
     */
    suspend fun updateProject(project: Project) {
        projectDao.update(project)
    }

    /**
     * Elimina un proyecto
     */
    suspend fun deleteProject(project: Project) {
        projectDao.delete(project)
    }

    /**
     * Obtiene un proyecto por ID
     */
    suspend fun getProjectById(projectId: Int): Project? {
        return projectDao.getProjectById(projectId)
    }

    /**
     * Obtiene proyectos filtrados por categoría
     */
    fun getProjectsByCategory(category: String): Flow<List<Project>> {
        return projectDao.getProjectsByCategory(category)
    }

    /**
     * Actualiza el estado de favorito de un proyecto
     */
    suspend fun toggleFavorite(projectId: Int, isFavorite: Boolean) {
        projectDao.updateFavoriteStatus(projectId, isFavorite)
    }

    /**
     * Obtiene el total de proyectos
     */
    suspend fun getProjectCount(): Int {
        return projectDao.getProjectCount()
    }

    /**
     * Elimina todos los proyectos (usar con cuidado)
     */
    suspend fun deleteAllProjects() {
        projectDao.deleteAllProjects()
    }
}