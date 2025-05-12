package com.example.myshop.ui.theme.screens.adminpannel

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.myshop.models.OrderModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

@Composable
fun OrdersScreen(navController: NavController) {
    val context = LocalContext.current
    val dbRef = FirebaseDatabase.getInstance().getReference("orders")
    var orders by remember { mutableStateOf<List<OrderModel>>(emptyList()) }

    LaunchedEffect(Unit) {
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val temp = mutableListOf<OrderModel>()
                for (child in snapshot.children) {
                    child.getValue(OrderModel::class.java)?.let {
                        temp.add(it)
                    }
                }
                orders = temp
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Failed to load orders", Toast.LENGTH_SHORT).show()
            }
        })
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Orders", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(16.dp))
        LazyColumn {
            items(orders) { order ->
                Card(modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("Order ID: ${order.orderId}")
                        Text("User ID: ${order.userId}")
                        Text("Total Price: \$${order.totalPrice}")
                        Spacer(Modifier.height(4.dp))
                        Text("Items:")
                        order.items.forEach { item ->
                            Text("- ${item.name} (\$${item.price})")
                        }
                    }
                }
            }
        }
    }
}