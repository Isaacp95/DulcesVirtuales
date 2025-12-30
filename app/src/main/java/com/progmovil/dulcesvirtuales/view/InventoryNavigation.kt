package com.progmovil.dulcesvirtuales.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.progmovil.dulcesvirtuales.model.Product
import com.progmovil.dulcesvirtuales.viewmodel.AuthViewModel
import com.progmovil.dulcesvirtuales.viewmodel.InventoryViewModel

@Composable
fun InventoryNavigation(
    inventoryViewModel: InventoryViewModel, authViewModel: AuthViewModel) {

    var showAddScreen by remember { mutableStateOf(false) }
    var selectedProduct by remember { mutableStateOf<Product?>(null) }
    val context = LocalContext.current

    when {
        showAddScreen -> {
            AddProductScreen(
                viewModel = inventoryViewModel,
                onProductAdded = {
                    showAddScreen = false
                }
            )
        }

        selectedProduct != null -> {
            ProductDetailScreen(
                product = selectedProduct!!,
                viewModel = inventoryViewModel,
                onBack = { selectedProduct = null }
            )
        }

        else -> {
            MainScreen(
                viewModel = inventoryViewModel,
                onAddProductClick = { showAddScreen = true },
                onProductClick = { selectedProduct = it },
                onLogoutClick = {authViewModel.logout(context)}
            )
        }
    }
}