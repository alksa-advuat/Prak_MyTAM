package model

// Sesuaikan dengan nama package utama project lu supaya R.drawable terbaca
import com.example.teori1_mytam.R

object ManganSources {
    val dummyMangan = listOf(
        Mangan(
            nama = "Nasi + Telur",
            deskripsi = "Menu hemat anak kos",
            harga = 10000,
            kalori = 400,
            protein = 12,
            ImageRes = R.drawable.nastel
        ),
        Mangan(
            nama = "Tempe + Nasi",
            deskripsi = "Sumber protein murah",
            harga = 8000,
            kalori = 400,
            protein = 15,
            ImageRes = R.drawable.temnas
        ),
        Mangan(
            nama = "Ayam Goreng",
            deskripsi = "Tinggi Protein",
            harga = 15000,
            kalori = 350,
            protein = 25,
            ImageRes = R.drawable.nasaym
        ),
        Mangan(
            nama = "Ikan+Sayur",
            deskripsi = "Tinggi Protein dan Gizi",
            harga = 16000,
            kalori = 300,
            protein = 25,
            ImageRes = R.drawable.tam
        ),
        Mangan(
            nama = "Ayam Bakar",
            deskripsi = "Tinggi Protein",
            harga = 16000,
            kalori = 350,
            protein = 25,
            ImageRes = R.drawable.img
        )
    )
}