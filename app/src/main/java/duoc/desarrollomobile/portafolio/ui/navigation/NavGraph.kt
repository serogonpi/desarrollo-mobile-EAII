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

        // Pantalla Project Detail (con parámetro)
        composable(
            route = Screen.ProjectDetail.route,
            arguments = listOf(
                navArgument("projectId") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->
            val projectId = backStackEntry.arguments?.getInt("projectId") ?: 0
            // ProjectDetailScreen(navController = navController, projectId = projectId)
            // Por ahora comentado, lo crearemos después
        }

        // Pantalla Post Detail (con parámetro)
        composable(
            route = Screen.PostDetail.route,
            arguments = listOf(
                navArgument("postId") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->
            val postId = backStackEntry.arguments?.getInt("postId") ?: 0
            // PostDetailScreen(navController = navController, postId = postId)
            // Por ahora comentado, lo crearemos después
        }

        // Pantalla Add Project
        composable(route = Screen.AddProject.route) {
            // AddProjectScreen(navController = navController)
            // Por ahora comentado, lo crearemos después
        }

        // Pantalla Add Post
        composable(route = Screen.AddPost.route) {
            // AddPostScreen(navController = navController)
            // Por ahora comentado, lo crearemos después
        }
    }
}