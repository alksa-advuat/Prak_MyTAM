package com.example.teori1_mytam

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.example.teori1_mytam.ui.theme.Teori1_MyTAMTheme

// Import data model lu
import model.Mangan
import model.ManganSources

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Teori1_MyTAMTheme {
                // State untuk mengatur perpindahan halaman (Navigasi)
                var selectedFood by remember { mutableStateOf<Mangan?>(null) }

                Scaffold(
                    bottomBar = {
                        // Sembunyikan BottomBar saat berada di halaman detail
                        if (selectedFood == null) {
                            BottomNavigationBar()
                        }
                    }
                ) { innerPadding ->
                    // Logika Navigasi: Jika tidak ada makanan dipilih, tampilkan MainScreen
                    if (selectedFood == null) {
                        MainScreen(
                            modifier = Modifier.padding(innerPadding),
                            onFoodClick = { food ->
                                selectedFood = food // Pindah ke halaman detail
                            }
                        )
                    } else {
                        // Jika ada makanan diklik, tampilkan DetailScreen
                        DetailScreen(
                            food = selectedFood!!,
                            modifier = Modifier.padding(innerPadding),
                            onBackClick = {
                                selectedFood = null // Kembali ke MainScreen
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MainScreen(modifier: Modifier = Modifier, onFoodClick: (Mangan) -> Unit) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFFAFAFA))
            .verticalScroll(rememberScrollState())
    ) {
        HeaderSection()
        Spacer(modifier = Modifier.height(16.dp))
        BudgetCardSection()
        Spacer(modifier = Modifier.height(24.dp))
        RecommendationSection(onFoodClick)
        Spacer(modifier = Modifier.height(24.dp))
        FavoriteSection(onFoodClick)
        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun DetailScreen(food: Mangan, modifier: Modifier = Modifier, onBackClick: () -> Unit) {
    // State untuk Modul 9
    var isLoading by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    Box(modifier = modifier.fillMaxSize().background(Color.White)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            // Gambar Makanan
            Image(
                painter = painterResource(id = food.ImageRes),
                contentDescription = food.nama,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Info Makanan
            Text(text = food.nama, fontSize = 26.sp, fontWeight = FontWeight.Bold, color = Color.Black)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = food.deskripsi, fontSize = 16.sp, color = Color.DarkGray)

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = Color.LightGray, thickness = 1.dp)
            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "Harga: Rp ${food.harga}", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFF6B9B6B))
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Kandungan: ${food.kalori} Kcal | Protein ${food.protein}g", fontSize = 14.sp, color = Color.Gray)

            Spacer(modifier = Modifier.weight(1f)) // Dorong tombol ke paling bawah

            // Tombol TUGAS MODUL 9 (Coroutine & Loading)
            Button(
                onClick = {
                    coroutineScope.launch {
                        isLoading = true
                        delay(2000) // Delay 2 detik tanpa nge-freeze UI
                        isLoading = false
                        snackbarHostState.showSnackbar("Pesanan ${food.nama} berhasil diproses!")
                    }
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                enabled = !isLoading, // Matikan fungsi klik saat loading
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6B9B6B)),
                shape = RoundedCornerShape(12.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Memproses...")
                } else {
                    Text("Pesan Sekarang", fontSize = 16.sp)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Tombol Kembali
            OutlinedButton(
                onClick = onBackClick,
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF6B9B6B)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Kembali", fontSize = 16.sp)
            }
        }

        // Snackbar Modul 9
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 16.dp)
        )
    }
}


@Composable
fun HeaderSection() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 48.dp, start = 24.dp, end = 24.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(text = "MealKost Kuu..", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color.Black)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Pastikan Kalorimu Terpenuhi! Sesuai Budgetmu", fontSize = 11.sp, color = Color.DarkGray)
        }
        Icon(
            imageVector = Icons.Rounded.AccountCircle, contentDescription = "Profile",
            modifier = Modifier.size(56.dp).clip(CircleShape), tint = Color(0xFFA5C1A5)
        )
    }
}

@Composable
fun BudgetCardSection() {
    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFEDF4ED))
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Default.AccountBox, contentDescription = "Wallet", tint = Color(0xFF6B9B6B))
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Budget Hari Ini", fontWeight = FontWeight.SemiBold, color = Color.DarkGray)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Rp 25.000", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.Black)
            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = Color.LightGray, thickness = 1.dp)
            Spacer(modifier = Modifier.height(16.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Sisa Budget", color = Color.DarkGray)
                Button(
                    onClick = { },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6B9B6B)),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                    modifier = Modifier.height(36.dp)
                ) { Text(text = "Rp 10.000 >", fontSize = 14.sp) }
            }
        }
    }
}

@Composable
fun RecommendationSection(onFoodClick: (Mangan) -> Unit) {
    val rekomendasiList = ManganSources.dummyMangan
    Column {
        Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text(text = "Rekomendasi Untukmu", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Icon(imageVector = Icons.Default.MoreVert, contentDescription = "More", tint = Color.Gray)
        }
        Spacer(modifier = Modifier.height(12.dp))
        LazyRow(contentPadding = PaddingValues(horizontal = 24.dp), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            items(rekomendasiList) { food ->
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier
                        .width(130.dp)
                        .clickable { onFoodClick(food) } // Makanan bisa diklik
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Image(painter = painterResource(id = food.ImageRes), contentDescription = food.nama, modifier = Modifier.fillMaxWidth().height(90.dp).clip(RoundedCornerShape(8.dp)).background(Color.LightGray), contentScale = ContentScale.Crop)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(text = food.nama, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Text(text = "Rp ${food.harga}", color = Color.LightGray, fontSize = 12.sp)
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 4.dp)) {
                            Icon(imageVector = Icons.Default.Info, contentDescription = "Cal", modifier = Modifier.size(12.dp), tint = Color(0xFF6B9B6B))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = "${food.kalori} kcal", color = Color.Gray, fontSize = 11.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FavoriteSection(onFoodClick: (Mangan) -> Unit) {
    val favoritList = ManganSources.dummyMangan
    Column(modifier = Modifier.padding(horizontal = 24.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text(text = "Menu Favorit Anak Kost", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Icon(imageVector = Icons.Default.MoreVert, contentDescription = "More", tint = Color.Gray)
        }
        Spacer(modifier = Modifier.height(12.dp))
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            favoritList.forEach { food ->
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onFoodClick(food) } // Makanan bisa diklik
                ) {
                    Row(modifier = Modifier.padding(12.dp).fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        Image(painter = painterResource(id = food.ImageRes), contentDescription = food.nama, modifier = Modifier.size(48.dp).clip(RoundedCornerShape(8.dp)).background(Color.LightGray), contentScale = ContentScale.Crop)
                        Spacer(modifier = Modifier.width(16.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = food.nama, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Text(text = food.deskripsi, color = Color.Gray, fontSize = 12.sp)
                        }
                        Text(text = "Rp ${food.harga}", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun BottomNavigationBar() {
    NavigationBar(containerColor = Color.White, tonalElevation = 8.dp) {
        NavigationBarItem(icon = { Icon(Icons.Default.Home, contentDescription = "Home") }, label = { Text("Home") }, selected = true, onClick = { }, colors = NavigationBarItemDefaults.colors(selectedIconColor = Color(0xFF6B9B6B), selectedTextColor = Color(0xFF6B9B6B), indicatorColor = Color(0xFFE8F5E9)))
        NavigationBarItem(icon = { Icon(Icons.Default.Menu, contentDescription = "Menu") }, label = { Text("Menu") }, selected = false, onClick = { })
        NavigationBarItem(icon = { Icon(Icons.Default.FavoriteBorder, contentDescription = "Nutrisi") }, label = { Text("Nutrisi") }, selected = false, onClick = { })
        NavigationBarItem(icon = { Icon(Icons.Default.Person, contentDescription = "Profile") }, label = { Text("Profile") }, selected = false, onClick = { })
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GreetingPreview() {
    Teori1_MyTAMTheme {
        Scaffold(bottomBar = { BottomNavigationBar() }) { innerPadding ->
            MainScreen(modifier = Modifier.padding(innerPadding), onFoodClick = {})
        }
    }
}