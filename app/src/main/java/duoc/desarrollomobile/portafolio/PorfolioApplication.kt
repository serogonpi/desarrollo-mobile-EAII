package duoc.desarrollomobile.portafolio

import android.app.Application
import duoc.desarrollomobile.portafolio.data.local.DatabaseSeeder
import duoc.desarrollomobile.portafolio.data.local.PortfolioDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

/**
 * Clase Application personalizada
 * Se ejecuta cuando la app inicia y configura la base de datos con datos iniciales
 */
class PortfolioApplication : Application() {

    // Scope de la aplicación para operaciones en background
    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onCreate() {
        super.onCreate()

        // Inicializar la base de datos con datos de ejemplo
        applicationScope.launch {
            val database = PortfolioDatabase.getDatabase(applicationContext)
            val seeder = DatabaseSeeder(applicationContext)
            seeder.seedDatabase(database)
        }
    }
}