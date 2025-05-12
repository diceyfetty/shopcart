package com.example.myshop.data

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.myshop.models.CartItem

class CartViewModel : ViewModel() {

    private val _cartItems = mutableStateOf<List<CartItem>>(emptyList())
    val cartItems: State<List<CartItem>> = _cartItems

    fun addItem(item: CartItem) {
        val current = _cartItems.value.toMutableList()
        val index = current.indexOfFirst { it.id == item.id }

        if (index != -1) {
            // If item exists, update quantity
            val existing = current[index]
            current[index] = existing.copy(quantity = existing.quantity + item.quantity)
        } else {
            // Add new item
            current.add(item)
        }

        _cartItems.value = current
    }

    fun removeItem(item: CartItem) {
        _cartItems.value = _cartItems.value.filter { it.id != item.id }
    }

    fun updateQuantity(item: CartItem, newQuantity: Int) {
        _cartItems.value = _cartItems.value.map {
            if (it.id == item.id) it.copy(quantity = newQuantity) else it
        }
    }

    fun clearCart() {
        _cartItems.value = emptyList()
    }
}