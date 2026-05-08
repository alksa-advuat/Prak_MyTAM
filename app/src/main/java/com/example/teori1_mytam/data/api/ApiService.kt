package com.example.teori1_mytam.data.api

import com.example.teori1_mytam.data.model.Mangan
import retrofit2.http.GET

interface ApiService {
    @GET("menu_makanan.json") // sesuaikan nama file JSON di Gist kamu
    suspend fun getMakanan(): List<Mangan>
}