package com.progmovil.dulcesvirtuales.viewmodel

import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.progmovil.dulcesvirtuales.model.CartItem
import com.progmovil.dulcesvirtuales.model.Product
import com.progmovil.dulcesvirtuales.model.Sale
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


class InventoryViewModel : ViewModel() {

    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products

    private val firestore = FirebaseFirestore.getInstance()
    private val collectionRef = firestore.collection("productos")

    private val _cart = MutableStateFlow<List<CartItem>>(emptyList())
    val cart: StateFlow<List<CartItem>> = _cart
    //private val databaseRef: DatabaseReference = FirebaseDatabase.getInstance().getReference("productos")

    init {
        loadProductsFromFirestore()
    }

    // Guardar o actualizar producto
    fun saveProductToFirestore(id: String, name: String, imageUri: String, price: Double, stock: Int) {
        val product = Product(
            id = id,
            name = name,
            imageUri = imageUri,
            price = price,
            stock = stock
        )
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

    fun addToCart(product: Product, quantity: Int) {
        val newItem = CartItem(
            productId = product.id,
            productName = product.name,
            quantity = quantity,
            priceAtSale = product.price
        )
        _cart.value = _cart.value + newItem
    }

    fun checkout() {
        // 1. Calcular total
        val total = _cart.value.sumOf { it.quantity * it.priceAtSale }

        // 2. Crear objeto Sale y guardarlo en una nueva colección "ventas" en Firestore
        val sale = Sale(items = _cart.value, total = total)
        firestore.collection("ventas").add(sale)

        // 3. RESTAR EL STOCK de cada producto en la colección "productos"
        _cart.value.forEach { item ->
            firestore.collection("productos").document(item.productId)
                .update("stock", com.google.firebase.firestore.FieldValue.increment(-item.quantity.toLong()))
        }

        // 4. Limpiar bolsa
        _cart.value = emptyList()
    }
}