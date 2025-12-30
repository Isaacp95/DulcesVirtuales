package com.progmovil.dulcesvirtuales.view

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.progmovil.dulcesvirtuales.model.Product
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import com.progmovil.dulcesvirtuales.viewmodel.InventoryViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    product: Product,
    viewModel: InventoryViewModel,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle del producto") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Image(
                painter = rememberAsyncImagePainter(Uri.parse(product.imageUri)),
                contentDescription = product.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp),
                contentScale = ContentScale.Crop
            )

            Text("ID: ${product.id}", style = MaterialTheme.typography.titleMedium)
            Text("Nombre: ${product.name}", style = MaterialTheme.typography.titleMedium)

            Spacer(Modifier.height(24.dp))

            // Botón de eliminar
            Button(
                onClick = {
                    viewModel.deleteProduct(product.id)
                    onBack()
                },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            ) {
                Text("Eliminar producto", color = MaterialTheme.colorScheme.onError)
            }

            // Botón adicional para regresar (opcional si ya tienes el ícono arriba)
            OutlinedButton(onClick = onBack) {
                Text("Volver")
            }
        }
    }
}

