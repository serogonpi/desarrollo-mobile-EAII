package duoc.desarrollomobile.portafolio.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import duoc.desarrollomobile.portafolio.ui.screens.contact.ContactScreen
import duoc.desarrollomobile.portafolio.ui.screens.gallery.GalleryScreen
import duoc.desarrollomobile.portafolio.ui.screens.home.HomeScreen
import duoc.desarrollomobile.portafolio.ui.screens.posts.PostsScreen

/**
 * Configuración del grafo de navegación de la aplicación
 * Define todas las rutas y transiciones entre pantallas
 */
@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        // Pantalla Home
        composable(route = Screen.Home.route) {
            HomeScreen(navController = navController)
        }

        // Pantalla Gallery
        composable(route = Screen.Gallery.route) {
            GalleryScreen(navController = navController)
        }

        // Pantalla Posts
        composable(route = Screen.Posts.route) {
            PostsScreen(navController = navController)
        }

        // Pantalla Contact
        composable(route = Screen.Contact.route) {
            ContactScreen(navController = navController)
        }
    }
}