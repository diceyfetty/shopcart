package com.example.myshop.models

data class UserModel(
    val firstname: String = "",
    val lastname: String = "",
    val email: String = "",
    val password: String = "",
    val userId: String = "",
    val role: String = "user"
)