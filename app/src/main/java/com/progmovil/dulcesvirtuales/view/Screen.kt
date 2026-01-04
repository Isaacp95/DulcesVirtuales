package com.progmovil.dulcesvirtuales.view

// Una Sealed Class es como un listado de opciones "cerrado" y seguro
sealed class Screen(val route: String) {

    // Definimos cada pantalla como un objeto único
    object Login : Screen("login")
    object Register : Screen("register")
    object Inventory : Screen("inventory")
    /* En el futuro podrías agregar:
       object ProductDetail : Screen("detail_screen")
    */
}