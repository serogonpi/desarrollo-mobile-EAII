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
}