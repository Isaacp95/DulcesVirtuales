package com.progmovil.dulcesvirtuales.view

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.*
import com.progmovil.dulcesvirtuales.ui.theme.DulcesVirtualesTheme
import com.progmovil.dulcesvirtuales.viewmodel.AuthViewModel
import com.progmovil.dulcesvirtuales.viewmodel.InventoryViewModel

class MainActivity : ComponentActivity() {

    // ViewModels usando delegados (viewModels() se encarga del ciclo de vida)
    private val authViewModel: AuthViewModel by viewModels()
    private val inventoryViewModel: InventoryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val currentUser by authViewModel.currentUser.collectAsState()

            // 🔍 Log temporal
            Log.d("LOGIN_CHECK", "¿Usuario activo? ${currentUser != null}")

            if (currentUser != null) {
                InventoryNavigation(inventoryViewModel, authViewModel)
            } else {
                LoginScreen(authViewModel = authViewModel) {
                    Log.d("LOGIN", "Sesión iniciada correctamente.")
                }
            }
        }
    }
}
