package com.example.myshop.models

import com.example.findables.models.ProductModel

data class OrderModel(
    var orderId: String = "",
    val userId: String = "",
    val items: List<ProductModel> = emptyList(),
    val totalPrice: Double = 0.0
)

