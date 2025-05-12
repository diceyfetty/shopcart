package com.example.myshop.ui.theme.screens.product

import androidx.compose.foundation.clickable
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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.myshop.data.ProductViewModel
import com.example.myshop.navigation.ROUTE_UPDATE_PRODUCT


@Composable
fun ManageProductsScreen(
    navController: NavHostController,
    viewModel: ProductViewModel,
    onProductClick: (String) -> Unit
) {
    val productList by viewModel.productList.collectAsState()

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(productList) { product ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .clickable { onProductClick(product.productId) }
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(product.name, style = MaterialTheme.typography.titleMedium)
                    Text("Category: ${product.category}")
                    Text("Price: \$${product.price}")

                    Spacer(modifier = Modifier.height(8.dp))

                    // Row containing update and delete buttons
                    Row (
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Update Button
                        Button(
                            onClick = {
                                navController.navigate("${ROUTE_UPDATE_PRODUCT}/${product.productId}")
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Update")
                        }

                        // Delete Button
                        Button(
                            onClick = {
                                viewModel.deleteProduct(product.productId) { success ->
                                    if (success) {
                                        // Optional: Show snackbar, toast, etc.
                                    }
                                }
                            },

                                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Delete")
                        }
                    }
                }
            }
        }
    }
}

