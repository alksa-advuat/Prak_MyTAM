package model

import com.google.gson.annotations.SerializedName

// Data class Mangan sekarang menggunakan URL gambar dari internet
// @SerializedName mencocokkan nama field Kotlin dengan key JSON dari API
data class Mangan(
    @SerializedName("nama")
    val nama: String,

    @SerializedName("deskripsi")
    val deskripsi: String,

    @SerializedName("harga")
    val harga: Int,

    @SerializedName("kalori")
    val kalori: Int,

    @SerializedName("protein")
    val protein: Int,

    @SerializedName("karbo")
    val karbo: Int,

    @SerializedName("lemak")
    val lemak: Int,

    // Modul 12: ganti ImageRes (Int lokal) → imageUrl (String dari internet)
    @SerializedName("image_url")
    val imageUrl: String
)