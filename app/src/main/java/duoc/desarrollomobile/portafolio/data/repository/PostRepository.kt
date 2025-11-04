package duoc.desarrollomobile.portafolio.data.repository

import duoc.desarrollomobile.portafolio.data.local.PostDao
import duoc.desarrollomobile.portafolio.data.model.Post
import kotlinx.coroutines.flow.Flow

/**
 * Repositorio para gestionar operaciones de Posts
 * Actúa como intermediario entre el DAO y el ViewModel
 * Centraliza la lógica de acceso a datos
 */
class PostRepository(private val postDao: PostDao) {

    // Flow para observar cambios en tiempo real
    val allPublishedPosts: Flow<List<Post>> = postDao.getAllPublishedPosts()
    val allPosts: Flow<List<Post>> = postDao.getAllPosts()

    /**
     * Inserta un nuevo post
     * @return ID del post insertado
     */
    suspend fun insertPost(post: Post): Long {
        return postDao.insert(post)
    }

    /**
     * Actualiza un post existente
     */
    suspend fun updatePost(post: Post) {
        postDao.update(post)
    }

    /**
     * Elimina un post
     */
    suspend fun deletePost(post: Post) {
        postDao.delete(post)
    }

    /**
     * Obtiene un post por ID
     */
    suspend fun getPostById(postId: Int): Post? {
        return postDao.getPostById(postId)
    }

    /**
     * Obtiene posts filtrados por tag
     */
    fun getPostsByTag(tag: String): Flow<List<Post>> {
        return postDao.getPostsByTag(tag)
    }

    /**
     * Incrementa el contador de vistas de un post
     */
    suspend fun incrementViewCount(postId: Int) {
        postDao.incrementViewCount(postId)
    }

    /**
     * Actualiza el estado de publicación de un post
     */
    suspend fun updatePublishStatus(postId: Int, isPublished: Boolean) {
        postDao.updatePublishStatus(postId, isPublished)
    }

    /**
     * Obtiene el total de posts publicados
     */
    suspend fun getPublishedPostCount(): Int {
        return postDao.getPublishedPostCount()
    }

    /**
     * Elimina todos los posts (usar con cuidado)
     */
    suspend fun deleteAllPosts() {
        postDao.deleteAllPosts()
    }
}