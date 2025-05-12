package com.example.myshop.ui.theme.bscreens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OffersScreen(
    navController: NavHostController,
    onNavigateToHome: () -> Unit,
    onNavigateToCart: () -> Unit,
    onNavigateToAccount: () -> Unit,
    onNavigateToCategories: () -> Unit
) {
    var selectedTabIndex by remember { mutableStateOf(4) }  // Selected index for Offers

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Today's Offers") },
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
                onCategoryClick = { onNavigateToCategories(); selectedTabIndex = 3 },
                onOffersClick = { selectedTabIndex = 4 } // Already on Offers
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            Text("Flash Deals", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(12.dp))

            LazyRow {
                items(5) {
                    Card(
                        modifier = Modifier
                            .width(180.dp)
                            .padding(end = 8.dp)
                    ) {
                        Column(
                            Modifier.padding(12.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(Icons.Default.LocalOffer, contentDescription = null, tint = Color.Red)
                            Spacer(Modifier.height(8.dp))
                            Text("50% OFF", style = MaterialTheme.typography.bodyLarge)
                        }
                    }
                }
            }
        }
    }
}
