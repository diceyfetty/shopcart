package com.example.myshop.network

import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ImgurApi {
    @Multipart
    @POST("image")
    suspend fun uploadImage(
        @Part image: MultipartBody.Part,
        @Header("Authorization") authHeader: String
    ): Response<UploadImageResponse>
}
