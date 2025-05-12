package com.example.myshop.data

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class ProfileViewModel : ViewModel() {
    private val _userProfile = mutableStateOf<UserProfile?>(null)
    val userProfile: State<UserProfile?> = _userProfile

    init {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            FirebaseDatabase.getInstance().getReference("Users")
                .child(userId)
                .get()
                .addOnSuccessListener { snapshot ->
                    val profile = snapshot.getValue(UserProfile::class.java)
                    _userProfile.value = profile
                }
        }
    }
}

data class UserProfile(val name: String, val email: String)
