package com.example.myshop.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.myshop.data.AuthViewModel
import com.example.myshop.data.ProductViewModel
import com.example.myshop.ui.theme.SplashScreen

import com.example.myshop.ui.theme.bscreens.AccountScreen
import com.example.myshop.ui.theme.bscreens.CartScreen
import com.example.myshop.ui.theme.bscreens.CategoriesScreen
import com.example.myshop.ui.theme.bscreens.OffersScreen
import com.example.myshop.ui.theme.screens.adminpannel.AdminPanelScreen
import com.example.myshop.ui.theme.screens.adminpannel.ManageUsersScreen
import com.example.myshop.ui.theme.screens.adminpannel.OrdersScreen
import com.example.myshop.ui.theme.screens.adminpannel.SendAnnouncementScreen
import com.example.myshop.ui.theme.screens.home.HomeScreen
import com.example.myshop.ui.theme.screens.login.LoginScreen
import com.example.myshop.ui.theme.screens.product.AddProductScreen
import com.example.myshop.ui.theme.screens.product.ManageProductsScreen
import com.example.myshop.ui.theme.screens.product.UpdateProductScreen
import com.example.myshop.ui.theme.screens.product.ViewProductScreen
import com.example.myshop.ui.theme.screens.register.RegisterScreen
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavHost(
    navController: NavHostController = rememberNavController(),
    startDestination: String = ROUTE_SPLASH
) {
    val viewModel: ProductViewModel = viewModel()
    val authViewModel: AuthViewModel = viewModel()
    var isAdmin by rememberSaveable { mutableStateOf(false) }
    val currentUserId = FirebaseAuth.getInstance().currentUser?.uid

    // Fetch user role
    LaunchedEffect(currentUserId) {
        currentUserId?.let {
            FirebaseDatabase.getInstance().getReference("Users")
                .child(it)
                .child("role")
                .get()
                .addOnSuccessListener { snapshot ->
                    isAdmin = snapshot.getValue(String::class.java) == "admin"
                }
        }
    }

    NavHost(navController = navController, startDestination = startDestination) {

        composable(ROUTE_SPLASH) {
            SplashScreen(navController = navController, authViewModel = authViewModel)
        }

        composable(ROUTE_LOGIN) {
            LoginScreen(navController = navController, authViewModel = authViewModel)
        }

        composable(ROUTE_REGISTER) {
            RegisterScreen(navController = navController, authViewModel = authViewModel)
        }

        composable(ROUTE_HOME) {
            HomeScreen(
                navController = navController,
                authViewModel = authViewModel,
                onNavigateToCart = { navController.navigate(ROUTE_CART) },
                onNavigateToAccount = { navController.navigate(ROUTE_PROFILE) },
                onNavigateToCategories = { navController.navigate(ROUTE_CATEGORIES) },
                onNavigateToOffers = { navController.navigate(ROUTE_OFFERS) },
                onProductClick = { productId ->
                    navController.navigate("$ROUTE_VIEW_PRODUCT/$productId")
                }
            )
        }

        composable(ROUTE_CART) {
            CartScreen(navController = navController)
        }

        composable(ROUTE_PROFILE) {
            AccountScreen(navController = navController, authViewModel = authViewModel)
        }

        composable(ROUTE_CATEGORIES) {
            CategoriesScreen(
                navController = navController,
                onNavigateToHome = { navController.navigate(ROUTE_HOME) },
                onNavigateToCart = { navController.navigate(ROUTE_CART) },
                onNavigateToAccount = { navController.navigate(ROUTE_PROFILE) },
                onNavigateToOffers = { navController.navigate(ROUTE_OFFERS) }
            )
        }

        composable(ROUTE_OFFERS) {
            OffersScreen(
                navController = navController,
                onNavigateToHome = { navController.navigate(ROUTE_HOME) },
                onNavigateToCart = { navController.navigate(ROUTE_CART) },
                onNavigateToAccount = { navController.navigate(ROUTE_PROFILE) },
                onNavigateToCategories = { navController.navigate(ROUTE_CATEGORIES) }
            )
        }

        composable(ROUTE_ADD_PRODUCT) {
            AddProductScreen(
                navController = navController,
                viewModel = viewModel,
                onProductAdded = { navController.popBackStack() }
            )
        }

        composable(ROUTE_MANAGE_PRODUCTS) {
            ManageProductsScreen(
                navController = navController,
                viewModel = viewModel,
                onProductClick = { productId ->
                    navController.navigate("$ROUTE_VIEW_PRODUCT/$productId")
                }
            )
        }

        composable(
            route = "$ROUTE_UPDATE_PRODUCT/{productId}",
            arguments = listOf(navArgument("productId") { type = NavType.StringType })
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId") ?: ""
            UpdateProductScreen(
                navController = navController,
                productId = productId,
                viewModel = viewModel,
                onProductUpdated = { navController.popBackStack() }
            )
        }

        composable(
            route = "$ROUTE_VIEW_PRODUCT/{productId}",
            arguments = listOf(navArgument("productId") { type = NavType.StringType })
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId") ?: ""
            ViewProductScreen(
                navController = navController,
                productId = productId,
                viewModel = viewModel,
                isAdmin = isAdmin,
                onProductDeleted = { navController.popBackStack() },
                onNavigateToUpdateProduct = { pid ->
                    navController.navigate("$ROUTE_UPDATE_PRODUCT/$pid")
                }
            )
        }

        composable(ROUTE_ADMIN_PANEL) {
            AdminPanelScreen(
                navController = navController,
                onAddProduct = { navController.navigate(ROUTE_ADD_PRODUCT) },
                onManageProducts = { navController.navigate(ROUTE_MANAGE_PRODUCTS) },
                onViewOrders = { navController.navigate(ROUTE_ORDERS) },
                onManageUsers = { navController.navigate(ROUTE_MANAGE_USERS) },
                onSendNotification = { navController.navigate(ROUTE_SEND_ANNOUNCEMENT) }
            )
        }

        // Admin-only routes
        composable(ROUTE_MANAGE_USERS) {
            if (isAdmin) {
                ManageUsersScreen(navController = navController)
            } else {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Access Denied", style = MaterialTheme.typography.bodyLarge)
                }
            }
        }

        composable(ROUTE_ORDERS) {
            if (isAdmin) {
                OrdersScreen(navController = navController)
            } else {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Access Denied", style = MaterialTheme.typography.bodyLarge)
                }
            }
        }


        composable(ROUTE_SEND_ANNOUNCEMENT) {
            if (isAdmin) {
                SendAnnouncementScreen(navController = navController)
            } else {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Access Denied", style = MaterialTheme.typography.bodyLarge)
                }
            }
        }

    }
}
