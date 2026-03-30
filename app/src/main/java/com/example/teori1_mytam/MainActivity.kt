package com.example.teori1_mytam

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.runtime.Composable
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
import com.example.teori1_mytam.ui.theme.Teori1_MyTAMTheme

// Import data model yang sudah kamu pisah
import model.Mangan
import model.ManganSources

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Teori1_MyTAMTheme {
                Scaffold(
                    bottomBar = { BottomNavigationBar() }
                ) { innerPadding ->
                    MainScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun MainScreen(modifier: Modifier = Modifier) {
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
        RecommendationSection()
        Spacer(modifier = Modifier.height(24.dp))
        FavoriteSection()
        Spacer(modifier = Modifier.height(24.dp))
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
            Text(
                text = "MealKost Kuu..",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Pastikan Kalorimu Terpenuhi! Sesuai Budgetmu",
                fontSize = 11.sp,
                color = Color.DarkGray
            )
        }
        Icon(
            imageVector = Icons.Rounded.AccountCircle,
            contentDescription = "Profile",
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape),
            tint = Color(0xFFA5C1A5)
        )
    }
}

@Composable
fun BudgetCardSection() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFEDF4ED))
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.AccountBox,
                    contentDescription = "Wallet",
                    tint = Color(0xFF6B9B6B)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Budget Hari Ini", fontWeight = FontWeight.SemiBold, color = Color.DarkGray)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Rp 25.000",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = Color.LightGray, thickness = 1.dp)
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Sisa Budget", color = Color.DarkGray)
                Button(
                    onClick = { /* TODO */ },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6B9B6B)),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                    modifier = Modifier.height(36.dp)
                ) {
                    Text(text = "Rp 10.000 >", fontSize = 14.sp)
                }
            }
        }
    }
}

@Composable
fun RecommendationSection() {
    // Ngambil data dari ManganSources.kt lu
    val rekomendasiList = ManganSources.dummyMangan

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Rekomendasi Untukmu", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Icon(imageVector = Icons.Default.MoreVert, contentDescription = "More", tint = Color.Gray)
        }
        Spacer(modifier = Modifier.height(12.dp))

        LazyRow(
            contentPadding = PaddingValues(horizontal = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(rekomendasiList) { food ->
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier.width(130.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Image(
                            painter = painterResource(id = food.ImageRes),
                            contentDescription = food.nama,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(90.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color.LightGray),
                            contentScale = ContentScale.Crop
                        )
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
fun FavoriteSection() {
    // Ngambil data dari ManganSources.kt lu
    val favoritList = ManganSources.dummyMangan

    Column(modifier = Modifier.padding(horizontal = 24.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
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
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .padding(12.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = food.ImageRes),
                            contentDescription = food.nama,
                            modifier = Modifier
                                .size(48.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color.LightGray),
                            contentScale = ContentScale.Crop
                        )
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
    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 8.dp
    ) {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            label = { Text("Home") },
            selected = true,
            onClick = { },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color(0xFF6B9B6B),
                selectedTextColor = Color(0xFF6B9B6B),
                indicatorColor = Color(0xFFE8F5E9)
            )
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Menu, contentDescription = "Menu") },
            label = { Text("Menu") },
            selected = false,
            onClick = { }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.FavoriteBorder, contentDescription = "Nutrisi") },
            label = { Text("Nutrisi") },
            selected = false,
            onClick = { }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
            label = { Text("Profile") },
            selected = false,
            onClick = { }
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GreetingPreview() {
    Teori1_MyTAMTheme {
        Scaffold(
            bottomBar = { BottomNavigationBar() }
        ) { innerPadding ->
            MainScreen(modifier = Modifier.padding(innerPadding))
        }
    }
}