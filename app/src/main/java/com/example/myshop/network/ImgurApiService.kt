package com.example.myshop.network

import com.example.myshop.models.ImgurResponse
import okhttp3.MultipartBody

import retrofit2.Response
import retrofit2.http.Header
import retrofit2.http.Part
import retrofit2.http.POST
import retrofit2.http.Multipart

interface ImgurService{
    @Multipart
    @POST("3/image")
    suspend fun uploadImage(
        @Part image:MultipartBody.Part,
        @Header("Authorization") auth: String
    ): Response<ImgurResponse>
}