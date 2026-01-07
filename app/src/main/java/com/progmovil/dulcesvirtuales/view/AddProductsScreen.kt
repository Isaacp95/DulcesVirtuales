package com.progmovil.dulcesvirtuales.view

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.progmovil.dulcesvirtuales.viewmodel.InventoryViewModel
import java.io.File
import java.io.FileOutputStream

@Composable
fun AddProductScreen(
    viewModel: InventoryViewModel,
    onProductAdded: () -> Unit
)
{
    var productId by remember { mutableStateOf("") }
    var productName by remember { mutableStateOf("") }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var productPrice by remember { mutableStateOf("") }
    var productStock by remember { mutableStateOf("") }

    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedImageUri = uri
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        //ID
        OutlinedTextField(
            value = productId,
            onValueChange = { productId = it },
            label = { Text("ID del producto") },
            modifier = Modifier.fillMaxWidth()
        )
        //Nombre
        OutlinedTextField(
            value = productName,
            onValueChange = { productName = it },
            label = { Text("Nombre del producto") },
            modifier = Modifier.fillMaxWidth()
        )
        //Precio
        OutlinedTextField(
            value = productPrice,
            onValueChange = { productPrice = it },
            label = { Text("Precio ($)") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
        )
        // Campo para Stock
        OutlinedTextField(
            value = productStock,
            onValueChange = { productStock = it },
            label = { Text("Cantidad en existencia") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        Box(
            modifier = Modifier
                .height(200.dp)
                .fillMaxWidth()
                .border(1.dp, Color.Gray)
                .clickable { launcher.launch("image/*") },
            contentAlignment = Alignment.Center
        ) {
            if (selectedImageUri != null) {
                Image(
                    painter = rememberAsyncImagePainter(selectedImageUri),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Text("Selecciona una imagen", color = Color.Gray)
            }
        }

        Button(
            onClick = {
                if (productId.isNotBlank() && productName.isNotBlank() && selectedImageUri != null) {
                    // Copiamos la imagen a almacenamiento interno
                    val fileName = "product_${productId}.jpg"
                    val localPath = copyImageToInternalStorage(context, selectedImageUri!!, fileName)

                    if (localPath != null) {
                        viewModel.saveProductToFirestore(
                            id = productId,
                            name = productName,
                            imageUri = localPath, // Guardamos la ruta interna
                            price = productPrice.toDoubleOrNull() ?: 0.00,
                            stock = productStock.toIntOrNull() ?: 0
                        )
                        Toast.makeText(context, "Producto guardado", Toast.LENGTH_SHORT).show()
                        onProductAdded()
                    } else {
                        Toast.makeText(context, "Error al guardar la imagen", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(context, "Completa todos los campos", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Guardar")
        }
    }

}

fun copyImageToInternalStorage(context: Context, imageUri: Uri, fileName: String): String? {
    return try {
        val inputStream = context.contentResolver.openInputStream(imageUri)
        val file = File(context.filesDir, fileName)
        val outputStream = FileOutputStream(file)
        inputStream?.copyTo(outputStream)
        inputStream?.close()
        outputStream.close()
        file.absolutePath
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

