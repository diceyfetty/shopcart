package com.example.myshop.ui.theme.bscreens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.myshop.ui.theme.screens.home.HomeScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoriesScreen(
    navController: NavHostController,
    onNavigateToHome: () -> Unit,
    onNavigateToCart: () -> Unit,
    onNavigateToAccount: () -> Unit,
    onNavigateToOffers: () -> Unit
) {
    var selectedTabIndex by remember { mutableStateOf(3) }  // Selected index for BottomNavigationBar

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Categories") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        bottomBar = {
            BottomNavigationBar(
                selectedTabIndex = selectedTabIndex,
                onHomeClick = { onNavigateToHome(); selectedTabIndex = 0 },
                onCartClick = { onNavigateToCart(); selectedTabIndex = 1 },
                onAccountClick = { onNavigateToAccount(); selectedTabIndex = 2 },
                onCategoryClick = { selectedTabIndex = 3 }, // Already on Categories
                onOffersClick = { onNavigateToOffers(); selectedTabIndex = 4 }
            )
        }
    ) { padding ->
        val categories = listOf("Electronics", "Fashion", "Books", "Home", "Toys", "Beauty", "Sports")

        LazyColumn(
            contentPadding = padding,
            modifier = Modifier.fillMaxSize()
        ) {
            items(categories) { category ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .clickable {
                            // Handle category click (future filtering logic)
                        },
                    elevation = CardDefaults.cardElevation()
                ) {
                    Text(
                        text = category,
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
    }
}

@Composable
fun BottomNavigationBar(
    selectedTabIndex: Int,
    onHomeClick: () -> Unit,
    onCartClick: () -> Unit,
    onAccountClick: () -> Unit,
    onCategoryClick: () -> Unit,
    onOffersClick: () -> Unit
) {
    NavigationBar {
        NavigationBarItem(
            selected = selectedTabIndex == 0,
            onClick = onHomeClick,
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            label = { Text("Home") }
        )
        NavigationBarItem(
            selected = selectedTabIndex == 1,
            onClick = onCartClick,
            icon = { Icon(Icons.Default.ShoppingCart, contentDescription = "Cart") },
            label = { Text("Cart") }
        )
        NavigationBarItem(
            selected = selectedTabIndex == 2,
            onClick = onAccountClick,
            icon = { Icon(Icons.Default.AccountCircle, contentDescription = "Account") },
            label = { Text("Account") }
        )
        NavigationBarItem(
            selected = selectedTabIndex == 3,
            onClick = onCategoryClick,
            icon = { Icon(Icons.Default.Category, contentDescription = "Categories") },
            label = { Text("Categories") }
        )
        NavigationBarItem(
            selected = selectedTabIndex == 4,
            onClick = onOffersClick,
            icon = { Icon(Icons.Default.LocalOffer, contentDescription = "Offers") },
            label = { Text("Offers") }
        )
    }
}
