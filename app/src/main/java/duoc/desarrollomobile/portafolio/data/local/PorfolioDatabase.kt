package duoc.desarrollomobile.portafolio.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import duoc.desarrollomobile.portafolio.data.model.ContactMessage
import duoc.desarrollomobile.portafolio.data.model.Post
import duoc.desarrollomobile.portafolio.data.model.Project

/**
 * Base de datos Room del portafolio
 * Implementa el patrón Singleton para asegurar una única instancia
 * Versión 1 - Primera implementación
 */
@Database(
    entities = [
        Project::class,
        Post::class,
        ContactMessage::class
    ],
    version = 1,
    exportSchema = false
)
abstract class PortfolioDatabase : RoomDatabase() {

    // DAOs abstractos
    abstract fun projectDao(): ProjectDao
    abstract fun postDao(): PostDao
    abstract fun contactMessageDao(): ContactMessageDao

    companion object {
        @Volatile
        private var INSTANCE: PortfolioDatabase? = null

        /**
         * Obtiene la instancia única de la base de datos
         * Patrón Singleton thread-safe
         */
        fun getDatabase(context: Context): PortfolioDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PortfolioDatabase::class.java,
                    "portfolio_database"
                )
                    .fallbackToDestructiveMigration() // En producción usar migraciones
                    .build()
                INSTANCE = instance
                instance
            }
        }

        /**
         * Método para testing - permite resetear la instancia
         */
        fun destroyInstance() {
            INSTANCE = null
        }
    }
}