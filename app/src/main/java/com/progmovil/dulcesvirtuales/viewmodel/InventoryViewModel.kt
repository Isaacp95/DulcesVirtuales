package com.progmovil.dulcesvirtuales.viewmodel

import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.progmovil.dulcesvirtuales.model.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


class InventoryViewModel : ViewModel() {

    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products

    private val firestore = FirebaseFirestore.getInstance()
    private val collectionRef = firestore.collection("productos")


    //private val databaseRef: DatabaseReference = FirebaseDatabase.getInstance().getReference("productos")

    init {
        loadProductsFromFirestore()
    }

    // Guardar o actualizar producto
    fun saveProductToFirestore(id: String, name: String, imageUri: String) {
        val product = Product(id = id, name = name, imageUri = imageUri)
        collectionRef.document(id).set(product)
    }

    // Recuperar productos desde Firebase Realtime Database
    // Leer todos los productos
    private fun loadProductsFromFirestore() {
        collectionRef.addSnapshotListener { snapshot, error ->
            if (error != null) {
                // Manejo de errores si quieres loggear o mostrar algo
                return@addSnapshotListener
            }

            val productList = snapshot?.documents?.mapNotNull { doc ->
                doc.toObject(Product::class.java)
            } ?: emptyList()

            _products.value = productList
        }
    }
    fun deleteProduct(id: String) {
        collectionRef.document(id).delete()
    }
}