package duoc.desarrollomobile.portafolio.ui.navigation

/**
 * Sealed class que define todas las pantallas de la aplicación
 * Cada pantalla tiene una ruta única para la navegación
 */
sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Gallery : Screen("gallery")
    object Posts : Screen("posts")
    object Contact : Screen("contact")
    object ProjectDetail : Screen("project_detail/{projectId}") {
        fun createRoute(projectId: Int) = "project_detail/$projectId"
    }
    object PostDetail : Screen("post_detail/{postId}") {
        fun createRoute(postId: Int) = "post_detail/$postId"
    }
    object AddProject : Screen("add_project")
    object AddPost : Screen("add_post")
}