package com.example.myshop.ui.theme.screens.adminpannel

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.database.FirebaseDatabase

@Composable
fun SendAnnouncementScreen(navController: NavController) {
    val context = LocalContext.current
    var message by remember { mutableStateOf("") }
    val announcementsRef = FirebaseDatabase.getInstance().getReference("announcements")

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {
        Text("Send Announcement", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = message,
            onValueChange = { message = it },
            label = { Text("Message") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(16.dp))

        Button(onClick = {
            if (message.isNotBlank()) {
                val id = announcementsRef.push().key ?: return@Button
                announcementsRef.child(id).setValue(message).addOnSuccessListener {
                    Toast.makeText(context, "Announcement sent", Toast.LENGTH_SHORT).show()
                    message = ""
                }.addOnFailureListener {
                    Toast.makeText(context, "Failed to send", Toast.LENGTH_SHORT).show()
                }
            }
        }) {
            Text("Send")
        }
    }
}
