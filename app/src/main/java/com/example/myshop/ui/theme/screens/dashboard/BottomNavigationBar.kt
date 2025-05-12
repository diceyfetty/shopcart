package com.example.myshop.ui.theme.screens.dashboard
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*

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
