package com.example.myshop.ui.theme.screens.product

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import androidx.compose.ui.layout.ContentScale
import com.example.myshop.data.ProductViewModel

@Composable
fun ViewProductScreen(
    navController: NavHostController,
    productId: String,
    viewModel: ProductViewModel = viewModel(),
    isAdmin: Boolean,
    onProductDeleted: () -> Unit,
    onNavigateToUpdateProduct: (String) -> Unit
) {
    val context = LocalContext.current
    val selectedProduct by viewModel.selectedProduct.collectAsState()

    LaunchedEffect(productId) {
        viewModel.getProductById(productId)
    }

    selectedProduct?.let { product ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text("Product Details", style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(16.dp))

            product.imageUrl?.let { imageUrl ->
                AsyncImage(
                    model = imageUrl,
                    contentDescription = "Product Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            Text("Name: ${product.name}", style = MaterialTheme.typography.titleMedium)
            Text("Category: ${product.category}", style = MaterialTheme.typography.bodyMedium)
            Text("Price: KSh ${product.price}", style = MaterialTheme.typography.bodyMedium)
            Text("Description: ${product.description}", style = MaterialTheme.typography.bodyMedium)

            if (isAdmin) {
                Spacer(modifier = Modifier.height(24.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Button(
                        onClick = { onNavigateToUpdateProduct(product.productId) },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Edit")
                    }
                    Button(
                        onClick = {
                            viewModel.deleteProduct(product.productId) { success ->
                                val message = if (success) {
                                    "Product deleted"
                                } else {
                                    "Delete failed"
                                }
                                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                                if (success) onProductDeleted()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Delete", color = Color.White)
                    }
                }
            }
        }
    } ?: Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}
