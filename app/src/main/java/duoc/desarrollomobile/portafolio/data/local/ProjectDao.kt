package duoc.desarrollomobile.portafolio.data.local

import androidx.room.*
import duoc.desarrollomobile.portafolio.data.model.Project
import kotlinx.coroutines.flow.Flow

/**
 * DAO para operaciones de la tabla Projects
 * Define todas las consultas SQL necesarias
 */
@Dao
interface ProjectDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(project: Project): Long

    @Update
    suspend fun update(project: Project)

    @Delete
    suspend fun delete(project: Project)

    @Query("SELECT * FROM projects ORDER BY createdAt DESC")
    fun getAllProjects(): Flow<List<Project>>

    @Query("SELECT * FROM projects WHERE id = :projectId")
    suspend fun getProjectById(projectId: Int): Project?

    @Query("SELECT * FROM projects WHERE category = :category ORDER BY createdAt DESC")
    fun getProjectsByCategory(category: String): Flow<List<Project>>

    @Query("SELECT * FROM projects WHERE isFavorite = 1 ORDER BY createdAt DESC")
    fun getFavoriteProjects(): Flow<List<Project>>

    @Query("UPDATE projects SET isFavorite = :isFavorite WHERE id = :projectId")
    suspend fun updateFavoriteStatus(projectId: Int, isFavorite: Boolean)

    @Query("DELETE FROM projects")
    suspend fun deleteAllProjects()

    @Query("SELECT COUNT(*) FROM projects")
    suspend fun getProjectCount(): Int
}