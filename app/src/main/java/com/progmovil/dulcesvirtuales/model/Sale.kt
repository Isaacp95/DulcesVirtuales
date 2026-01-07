package com.progmovil.dulcesvirtuales.model

data class Sale(
    val id: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val items: List<CartItem> = emptyList(),
    val total: Double = 0.0
)
data class CartItem(
    val productId: String = "",
    val productName: String = "",
    val quantity: Int = 0,
    val priceAtSale: Double = 0.0
)