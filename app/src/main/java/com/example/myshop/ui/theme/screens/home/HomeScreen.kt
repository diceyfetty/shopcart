package com.example.myshop.ui.theme.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import com.example.findables.models.ProductModel
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.example.myshop.R
import com.example.myshop.data.AuthViewModel
import com.example.myshop.data.ProductViewModel
import coil.compose.rememberImagePainter

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun HomeScreen(
    navController: NavController,
    authViewModel: AuthViewModel = viewModel(),
    productViewModel: ProductViewModel = viewModel(),
    onNavigateToCart: () -> Unit,
    onNavigateToAccount: () -> Unit,
    onNavigateToCategories: () -> Unit,
    onNavigateToOffers: () -> Unit,
    onProductClick: (String) -> Unit
) {
    val products by productViewModel.productList.collectAsState()
    val currentUser by authViewModel.currentUser.collectAsState() // Collect current user data
    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("QuickCart") },
                navigationIcon = {
                    Row(
                        modifier = Modifier
                            .clickable(onClick = onNavigateToAccount)
                            .padding(start = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.AccountCircle, contentDescription = "Account")
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = currentUser?.firstname ?: "Guest", // Display the first name of the user or "Guest"
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                },
                actions = { /* Removed cart icon */ }
            )
        },
        bottomBar = {
            BottomNavigationBar(
                onHomeClick = {},
                onCartClick = onNavigateToCart,
                onAccountClick = onNavigateToAccount,
                onCategoryClick = onNavigateToCategories,
                onOffersClick = onNavigateToOffers
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Welcome Banner
            Text(
                text = "Welcome, ${currentUser?.firstname ?: "Guest"} 👋",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Promo Banner Image (use your own drawable or URL image)
            Image(
                painter = painterResource(id = R.drawable.promo_banner), // Replace with actual resource
                contentDescription = "Promo Banner",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .clip(MaterialTheme.shapes.medium)
                    .padding(bottom = 16.dp)
            )

            // Search Field
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
                placeholder = { Text("Search products...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )

            // Filtered Product Grid
            val filteredProducts = products.filter {
                it.name.contains(searchQuery.text, ignoreCase = true) ||
                        it.category.contains(searchQuery.text, ignoreCase = true)
            }

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(filteredProducts) { product ->
                    ProductCard(product = product) {
                        onProductClick(product.productId)
                    }
                }
            }
        }
    }
}


@Composable
fun BottomNavigationBar(
    onHomeClick: () -> Unit,
    onCartClick: () -> Unit,
    onAccountClick: () -> Unit,
    onCategoryClick: () -> Unit,
    onOffersClick: () -> Unit
) {
    NavigationBar {
        NavigationBarItem(
            selected = true,
            onClick = onHomeClick,
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            label = { Text("Home") }
        )
        NavigationBarItem(
            selected = false,
            onClick = onCartClick,
            icon = { Icon(Icons.Default.ShoppingCart, contentDescription = "Cart") },
            label = { Text("Cart") }
        )
        NavigationBarItem(
            selected = false,
            onClick = onAccountClick,
            icon = { Icon(Icons.Default.AccountCircle, contentDescription = "Account") },
            label = { Text("Account") }
        )
        NavigationBarItem(
            selected = false,
            onClick = onCategoryClick,
            icon = { Icon(Icons.Default.Category, contentDescription = "Categories") },
            label = { Text("Categories") }
        )
        NavigationBarItem(
            selected = false,
            onClick = onOffersClick,
            icon = { Icon(Icons.Default.LocalOffer, contentDescription = "Offers") },
            label = { Text("Offers") }
        )
    }
}


@Composable
fun ProductCard(product: ProductModel, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(8.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.Start
        ) {
            AsyncImage(
                model = product.imageUrl,
                contentDescription = product.name,
                contentScale = ContentScale.Fit, // Ensures full image is visible
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 180.dp, max = 250.dp) // Adjust as needed
                    .clip(RoundedCornerShape(12.dp))
                    .align(Alignment.CenterHorizontally)
            )
            Text(
                text = product.name,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "Category: ${product.category}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "$${product.price}",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}