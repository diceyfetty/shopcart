package com.example.myshop.data

import androidx.lifecycle.ViewModel
import com.example.myshop.models.AnnouncementModel
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AnnouncementViewModel : ViewModel() {
    private val dbRef: DatabaseReference = FirebaseDatabase.getInstance().getReference("announcements")

    fun sendAnnouncement(message: String, onComplete: (Boolean) -> Unit) {
        val announcement = AnnouncementModel(message)
        dbRef.push().setValue(announcement)
            .addOnCompleteListener { task ->
                onComplete(task.isSuccessful)
            }
    }
}
