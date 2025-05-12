package com.example.myshop.data

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.myshop.models.OrderModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class OrderViewModel : ViewModel() {
    private val dbRef: DatabaseReference = FirebaseDatabase.getInstance().getReference("orders")
    private val _orders = MutableStateFlow<List<OrderModel>>(emptyList())
    val orders: StateFlow<List<OrderModel>> = _orders

    init {
        fetchOrders()
    }

    private fun fetchOrders() {
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val orderList = mutableListOf<OrderModel>()
                for (orderSnap in snapshot.children) {
                    val order = orderSnap.getValue(OrderModel::class.java)
                    order?.orderId = orderSnap.key ?: ""
                    order?.let { orderList.add(it) }
                }
                _orders.value = orderList
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("OrderViewModel", "Firebase Error: ${error.message}")
            }
        })
    }
}
