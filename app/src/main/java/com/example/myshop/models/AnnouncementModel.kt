package com.example.myshop.models

data class AnnouncementModel(
    val message: String = "",
    val timestamp: Long = System.currentTimeMillis()
)

