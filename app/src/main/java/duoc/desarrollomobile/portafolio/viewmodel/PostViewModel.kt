package duoc.desarrollomobile.portafolio.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import duoc.desarrollomobile.portafolio.data.local.PortfolioDatabase
import duoc.desarrollomobile.portafolio.data.model.Post
import duoc.desarrollomobile.portafolio.data.repository.PostRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel para gestionar el estado de Posts
 * Maneja la lógica de negocio y comunica la UI con el repositorio
 */
class PostViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: PostRepository

    // StateFlow para observar la lista de posts
    val allPosts: StateFlow<List<Post>>
    val publishedPosts: StateFlow<List<Post>>

    // Estado para post seleccionado
    private val _selectedPost = MutableStateFlow<Post?>(null)
    val selectedPost: StateFlow<Post?> = _selectedPost.asStateFlow()

    // Estado para tag seleccionado
    private val _selectedTag = MutableStateFlow<String?>(null)
    val selectedTag: StateFlow<String?> = _selectedTag.asStateFlow()

    // Estado de carga
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // Estado de mensaje
    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message.asStateFlow()

    init {
        val database = PortfolioDatabase.getDatabase(application)
        repository = PostRepository(database.postDao())

        // Convertir Flow a StateFlow
        allPosts = MutableStateFlow(emptyList())
        publishedPosts = MutableStateFlow(emptyList())

        // Observar cambios en los posts
        viewModelScope.launch {
            repository.allPosts.collect { posts ->
                (allPosts as MutableStateFlow).value = posts
            }
        }

        viewModelScope.launch {
            repository.allPublishedPosts.collect { posts ->
                (publishedPosts as MutableStateFlow).value = posts
            }
        }
    }

    /**
     * Inserta o actualiza un post
     */
    fun savePost(post: Post) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                if (post.id == 0) {
                    repository.insertPost(post)
                    _message.value = "Post creado exitosamente"
                } else {
                    repository.updatePost(post)
                    _message.value = "Post actualizado exitosamente"
                }
            } catch (e: Exception) {
                _message.value = "Error al guardar post: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Elimina un post
     */
    fun deletePost(post: Post) {
        viewModelScope.launch {
            try {
                repository.deletePost(post)
                _message.value = "Post eliminado"
            } catch (e: Exception) {
                _message.value = "Error al eliminar post: ${e.message}"
            }
        }
    }

    /**
     * Selecciona un post para ver detalles
     */
    fun selectPost(post: Post?) {
        _selectedPost.value = post
        // Incrementar contador de vistas si el post existe
        post?.let {
            viewModelScope.launch {
                repository.incrementViewCount(it.id)
            }
        }
    }

    /**
     * Filtra posts por tag
     */
    fun filterByTag(tag: String?) {
        _selectedTag.value = tag
    }

    /**
     * Cambia el estado de publicación de un post
     */
    fun togglePublishStatus(postId: Int, isPublished: Boolean) {
        viewModelScope.launch {
            try {
                repository.updatePublishStatus(postId, isPublished)
                _message.value = if (isPublished) "Post publicado" else "Post ocultado"
            } catch (e: Exception) {
                _message.value = "Error al cambiar estado: ${e.message}"
            }
        }
    }

    /**
     * Limpia el mensaje después de mostrarlo
     */
    fun clearMessage() {
        _message.value = null
    }

    /**
     * Obtiene el total de posts publicados
     */
    fun getPublishedPostCount(callback: (Int) -> Unit) {
        viewModelScope.launch {
            val count = repository.getPublishedPostCount()
            callback(count)
        }
    }
}