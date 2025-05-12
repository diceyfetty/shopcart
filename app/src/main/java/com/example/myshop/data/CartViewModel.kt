package com.example.myshop.data

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class CartViewModel : ViewModel() {
    private val _cartItems = mutableStateOf<List<CartItem>>(listOf())
    val cartItems: State<List<CartItem>> = _cartItems

    // For now, load static data (you can replace this with Firebase calls later)
    init {
        _cartItems.value = listOf(
            CartItem("Smartphone", 2, 399.99),
            CartItem("Laptop", 1, 799.99)
        )
    }

    fun removeItem(item: CartItem) {
        _cartItems.value = _cartItems.value.filter { it != item }
    }

    fun updateQuantity(item: CartItem, newQuantity: Int) {
        _cartItems.value = _cartItems.value.map {
            if (it == item) it.copy(quantity = newQuantity) else it
        }
    }
}

data class CartItem(val name: String, val quantity: Int, val price: Double)
