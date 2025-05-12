package com.example.myshop.ui.theme.screens.adminpannel

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

@Composable
fun MakeAdminScreen() {
    var secretCode by remember { mutableStateOf("") }
    val context = LocalContext.current
    val currentUser = FirebaseAuth.getInstance().currentUser

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Enter Admin Code", style = MaterialTheme.typography.headlineSmall)

        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = secretCode,
            onValueChange = { secretCode = it },
            label = { Text("Admin Code") }
        )

        Spacer(Modifier.height(16.dp))

        Button (onClick = {
            if (secretCode == "supersecret") {
                currentUser?.uid?.let { uid ->
                    FirebaseDatabase.getInstance().getReference("users")
                        .child(uid)
                        .child("role")
                        .setValue("admin")
                    Toast.makeText(context, "You are now an Admin!", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(context, "Invalid Code", Toast.LENGTH_SHORT).show()
            }
        }) {
            Text("Confirm")
        }
    }
}
