package com.example.teori1_mytam.data.model

import com.google.gson.annotations.SerializedName

data class Mangan(
    val nama: String = "",
    val deskripsi: String = "",
    val harga: Int = 0,
    @SerializedName("image_url")
    val imageUrl: String = "",
    val kalori: Int = 0,
    val protein: Int = 0,
    val karbo: Int = 0,
    val lemak: Int = 0
)