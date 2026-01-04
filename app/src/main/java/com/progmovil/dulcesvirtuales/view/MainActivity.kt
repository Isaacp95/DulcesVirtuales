package com.progmovil.dulcesvirtuales.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.progmovil.dulcesvirtuales.viewmodel.AuthViewModel
import com.progmovil.dulcesvirtuales.viewmodel.InventoryViewModel

class MainActivity : ComponentActivity() {

    // ViewModels usando delegados (viewModels() se encarga del ciclo de vida)
    private val authViewModel: AuthViewModel by viewModels()
    private val inventoryViewModel: InventoryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()
            val currentUser by authViewModel.currentUser.collectAsState()

            // Observamos el cambio de usuario para redirigir al inventario automáticamente
            LaunchedEffect(currentUser) {
                if (currentUser != null) {
                    navController.navigate(Screen.Inventory.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            }
            NavHost(navController = navController, startDestination = Screen.Login.route) {
                composable(Screen.Login.route) {
                    LoginScreen(
                        authViewModel = authViewModel,
                        onNavigateToRegister = { navController.navigate(Screen.Register.route) },
                        onLoginSuccess = { /* El LaunchedEffect se encargará */ }
                    )
                }
                composable(Screen.Register.route) {
                    RegisterScreen(
                        authViewModel = authViewModel,
                        onRegisterSuccess = { /* El LaunchedEffect se encargará */ },
                        onBackToLogin = { navController.popBackStack() }
                    )
                }
                composable(Screen.Inventory.route) {
                    // Aquí llamas a tu función de inventario actual
                    InventoryNavigation(inventoryViewModel, authViewModel)
                }
            }
        }
    }
}
