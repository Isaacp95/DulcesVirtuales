package com.progmovil.dulcesvirtuales.model

data class Product(
        val id: String = "",
        val name: String = "",
        val imageUri: String? = null,
        val price: Double = 0.0,
        val stock: Int = 0
)