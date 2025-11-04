package duoc.desarrollomobile.portafolio.data.local

import androidx.room.*
import duoc.desarrollomobile.portafolio.data.model.ContactMessage
import kotlinx.coroutines.flow.Flow

/**
 * DAO para operaciones de la tabla Contact Messages
 * Define todas las consultas SQL necesarias
 */
@Dao
interface ContactMessageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(message: ContactMessage): Long

    @Update
    suspend fun update(message: ContactMessage)

    @Delete
    suspend fun delete(message: ContactMessage)

    @Query("SELECT * FROM contact_messages ORDER BY createdAt DESC")
    fun getAllMessages(): Flow<List<ContactMessage>>

    @Query("SELECT * FROM contact_messages WHERE id = :messageId")
    suspend fun getMessageById(messageId: Int): ContactMessage?

    @Query("SELECT * FROM contact_messages WHERE isRead = 0 ORDER BY createdAt DESC")
    fun getUnreadMessages(): Flow<List<ContactMessage>>

    @Query("SELECT * FROM contact_messages WHERE isRead = 1 ORDER BY createdAt DESC")
    fun getReadMessages(): Flow<List<ContactMessage>>

    @Query("UPDATE contact_messages SET isRead = :isRead WHERE id = :messageId")
    suspend fun updateReadStatus(messageId: Int, isRead: Boolean)

    @Query("DELETE FROM contact_messages")
    suspend fun deleteAllMessages()

    @Query("SELECT COUNT(*) FROM contact_messages WHERE isRead = 0")
    suspend fun getUnreadCount(): Int

    @Query("SELECT COUNT(*) FROM contact_messages")
    suspend fun getTotalMessageCount(): Int
}