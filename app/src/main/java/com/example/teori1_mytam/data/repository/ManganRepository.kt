package com.example.teori1_mytam.data.repository

import com.example.teori1_mytam.data.api.RetrofitClient
import com.example.teori1_mytam.data.model.Mangan

class ManganRepository {
    suspend fun getMakanan(): List<Mangan> {
        return try {
            RetrofitClient.instance.getMakanan()
        } catch (e: Exception) {
            emptyList()
        }
    }
}