package com.example.myshop.network

data class UploadImageResponse(
    val data: ImageData?,
    val success: Boolean,
    val status: Int
)

data class ImageData(
    val id: String,
    val title: String?,
    val description: String?,
    val type: String,
    val animated: Boolean,
    val width: Int,
    val height: Int,
    val size: Int,
    val views: Int,
    val bandwidth: Long,
    val deletehash: String?,
    val name: String?,
    val section: String?,
    val link: String
)
