package com.example.myshop.ui.theme

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.myshop.R
import com.example.myshop.data.AuthViewModel
import com.example.myshop.navigation.ROUTE_ADMIN_PANEL
import com.example.myshop.navigation.ROUTE_HOME
import com.example.myshop.navigation.ROUTE_REGISTER
import com.example.myshop.navigation.ROUTE_SPLASH
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    navController: NavHostController,
    authViewModel: AuthViewModel
) {
    val splashScreenDuration = 2400L
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        delay(splashScreenDuration)

        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            val userId = currentUser.uid
            FirebaseDatabase.getInstance().getReference("Users")
                .child(userId)
                .child("role")
                .get()
                .addOnSuccessListener { snapshot ->
                    val role = snapshot.getValue(String::class.java)
                    if (role == "admin") {
                        navController.navigate(ROUTE_ADMIN_PANEL) {
                            popUpTo(ROUTE_SPLASH) { inclusive = true }
                        }
                    } else {
                        navController.navigate(ROUTE_HOME) {
                            popUpTo(ROUTE_SPLASH) { inclusive = true }
                        }
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(context, "Failed to fetch role", Toast.LENGTH_SHORT).show()
                    navController.navigate(ROUTE_HOME) {
                        popUpTo(ROUTE_SPLASH) { inclusive = true }
                    }
                }
        } else {
            navController.navigate(ROUTE_REGISTER) {
                popUpTo(ROUTE_SPLASH) { inclusive = true }
            }
        }
    }

    // UI
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(70.dp))

            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "App Logo",
                modifier = Modifier.size(250.dp)
            )

            Spacer(modifier = Modifier.height(70.dp))

            Text(
                text = "QuickCart",
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Bold,
                fontSize = 30.sp,
                color = Color.Red
            )

            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = "Shop smart, Live Easy!",
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = Color.White
            )
        }
    }
}
