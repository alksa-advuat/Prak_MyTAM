package com.example.teori1_mytam.network

import model.Mangan
import retrofit2.http.GET

interface ApiService {

    @GET("menu_makanan.json")
    suspend fun getMakanan(): List<Mangan>
}