package duoc.desarrollomobile.portafolio.data.local

import androidx.room.*
import duoc.desarrollomobile.portafolio.data.model.Post
import kotlinx.coroutines.flow.Flow

/**
 * DAO para operaciones de la tabla Posts
 * Define todas las consultas SQL necesarias
 */
@Dao
interface PostDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(post: Post): Long

    @Update
    suspend fun update(post: Post)

    @Delete
    suspend fun delete(post: Post)

    @Query("SELECT * FROM posts WHERE isPublished = 1 ORDER BY createdAt DESC")
    fun getAllPublishedPosts(): Flow<List<Post>>

    @Query("SELECT * FROM posts ORDER BY createdAt DESC")
    fun getAllPosts(): Flow<List<Post>>

    @Query("SELECT * FROM posts WHERE id = :postId")
    suspend fun getPostById(postId: Int): Post?

    @Query("SELECT * FROM posts WHERE tags LIKE '%' || :tag || '%' AND isPublished = 1 ORDER BY createdAt DESC")
    fun getPostsByTag(tag: String): Flow<List<Post>>

    @Query("UPDATE posts SET viewCount = viewCount + 1 WHERE id = :postId")
    suspend fun incrementViewCount(postId: Int)

    @Query("UPDATE posts SET isPublished = :isPublished WHERE id = :postId")
    suspend fun updatePublishStatus(postId: Int, isPublished: Boolean)

    @Query("DELETE FROM posts")
    suspend fun deleteAllPosts()

    @Query("SELECT COUNT(*) FROM posts WHERE isPublished = 1")
    suspend fun getPublishedPostCount(): Int
}