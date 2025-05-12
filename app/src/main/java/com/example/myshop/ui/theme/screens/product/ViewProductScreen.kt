package com.example.myshop.ui.theme.screens.product

import android.content.Intent
import android.net.Uri
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
import com.example.myshop.data.CartViewModel
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
    val cartViewModel: CartViewModel = viewModel()

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
            Text("Price: KSh ${product.price}", style = MaterialTheme.typography.bodyMedium)
            Text("Description: ${product.description}", style = MaterialTheme.typography.bodyMedium)

            Spacer(modifier = Modifier.height(24.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                // Add to Cart button
                Button(
                    onClick = {
                        val cartItem = com.example.myshop.models.CartItem(
                            id = product.productId,
                            name = product.name,
                            price = product.price,
                            quantity = 1
                        )
                        cartViewModel.addItem(cartItem)
                        Toast.makeText(context, "Added to cart", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Add to Cart")
                }

                // Buy Now button (Initiates M-Pesa payment)
                Button(
                    onClick = {
                        // Initiate the M-Pesa payment using the Safaricom payment link
                        val amount = product.price
                        val phoneNumber = "+254790709595"  // Seller's phone number
                        val url = "https://mpesa.com/send-money?amount=$amount&phone=$phoneNumber"
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                        context.startActivity(intent)
                    },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Buy Now")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Message Seller button (opens WhatsApp)
            Button(
                onClick = {
                    val phoneNumber = "+254719411231" // Seller's phone number in international format
                    val message = "Hello, I'm interested in the ${product.name}"
                    val url = "https://wa.me/$phoneNumber?text=${Uri.encode(message)}"
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    context.startActivity(intent)
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Message Seller")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // View Cart button
            Button(
                onClick = {
                    // Navigate to the Cart screen
                    navController.navigate("cartScreen")
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("View Cart")
            }
        }
    } ?: Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}