package duoc.desarrollomobile.portafolio.data.repository

import duoc.desarrollomobile.portafolio.data.local.ContactMessageDao
import duoc.desarrollomobile.portafolio.data.model.ContactMessage
import kotlinx.coroutines.flow.Flow

/**
 * Repositorio para gestionar operaciones de Mensajes de Contacto
 * Actúa como intermediario entre el DAO y el ViewModel
 * Centraliza la lógica de acceso a datos
 */
class ContactMessageRepository(private val contactMessageDao: ContactMessageDao) {

    // Flow para observar cambios en tiempo real
    val allMessages: Flow<List<ContactMessage>> = contactMessageDao.getAllMessages()
    val unreadMessages: Flow<List<ContactMessage>> = contactMessageDao.getUnreadMessages()
    val readMessages: Flow<List<ContactMessage>> = contactMessageDao.getReadMessages()

    /**
     * Inserta un nuevo mensaje de contacto
     * @return ID del mensaje insertado
     */
    suspend fun insertMessage(message: ContactMessage): Long {
        return contactMessageDao.insert(message)
    }

    /**
     * Actualiza un mensaje existente
     */
    suspend fun updateMessage(message: ContactMessage) {
        contactMessageDao.update(message)
    }

    /**
     * Elimina un mensaje
     */
    suspend fun deleteMessage(message: ContactMessage) {
        contactMessageDao.delete(message)
    }

    /**
     * Obtiene un mensaje por ID
     */
    suspend fun getMessageById(messageId: Int): ContactMessage? {
        return contactMessageDao.getMessageById(messageId)
    }

    /**
     * Marca un mensaje como leído o no leído
     */
    suspend fun markAsRead(messageId: Int, isRead: Boolean) {
        contactMessageDao.updateReadStatus(messageId, isRead)
    }

    /**
     * Obtiene el total de mensajes no leídos
     */
    suspend fun getUnreadCount(): Int {
        return contactMessageDao.getUnreadCount()
    }

    /**
     * Obtiene el total de mensajes
     */
    suspend fun getTotalMessageCount(): Int {
        return contactMessageDao.getTotalMessageCount()
    }

    /**
     * Elimina todos los mensajes (usar con cuidado)
     */
    suspend fun deleteAllMessages() {
        contactMessageDao.deleteAllMessages()
    }
}