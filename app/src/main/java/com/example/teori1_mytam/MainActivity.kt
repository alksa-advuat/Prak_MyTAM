package com.example.teori1_mytam

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.teori1_mytam.data.model.Mangan
import com.example.teori1_mytam.data.repository.ManganRepository
import com.example.teori1_mytam.ui.theme.Teori1_MyTAMTheme
import kotlinx.coroutines.launch

private val Green      = Color(0xFF6B9B6B)
private val GreenLight = Color(0xFFEDF4ED)
private val GreenSoft  = Color(0xFFE8F5E9)
private val BgGray     = Color(0xFFFAFAFA)

enum class KategoriMenu { SEMUA, NASI, LAUK, MINUM, SNACK }


data class AppState(
    val selectedFood: Mangan?      = null,
    val activeTab: Int             = 0,
    val favoriteItems: Set<String> = emptySet(),
    val logMakanan: List<Mangan>   = emptyList(),
    val allMakanan: List<Mangan>   = emptyList(),
    val isLoading: Boolean         = true,
    val isError: Boolean           = false
)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Teori1_MyTAMTheme {
                var state by remember { mutableStateOf(AppState()) }
                val repository = remember { ManganRepository() }

                LaunchedEffect(Unit) {
                    state = state.copy(isLoading = true, isError = false)
                    try {
                        val data = repository.getMakanan()
                        state = if (data.isEmpty()) {
                            state.copy(isLoading = false, isError = true)
                        } else {
                            state.copy(isLoading = false, isError = false, allMakanan = data)
                        }
                    } catch (_: Exception) {
                        state = state.copy(isLoading = false, isError = true)
                    }
                }

                val toggleFav: (String) -> Unit = { name ->
                    state = state.copy(
                        favoriteItems = if (state.favoriteItems.contains(name))
                            state.favoriteItems - name else state.favoriteItems + name
                    )
                }
                val toggleLog: (Mangan) -> Unit = { food ->
                    state = state.copy(
                        logMakanan = if (state.logMakanan.contains(food))
                            state.logMakanan - food else state.logMakanan + food
                    )
                }

                Scaffold(
                    bottomBar = {
                        if (state.selectedFood == null)
                            AppBottomBar(state.activeTab) { state = state.copy(activeTab = it) }
                    }
                ) { pad ->
                    val mod = Modifier.padding(pad)
                    when {
                        state.isLoading -> LoadingScreen(mod)
                        state.isError   -> ErrorScreen(mod)
                        else -> when {
                            state.selectedFood != null -> DetailScreen(
                                food        = state.selectedFood!!,
                                isFavorite  = state.favoriteItems.contains(state.selectedFood!!.nama),
                                isLogged    = state.logMakanan.contains(state.selectedFood!!),
                                onFavToggle = toggleFav,
                                onLogToggle = toggleLog,
                                onBackClick = { state = state.copy(selectedFood = null) },
                                modifier    = mod
                            )
                            state.activeTab == 0 -> HomeScreen(
                                modifier         = mod,
                                allMakanan       = state.allMakanan,
                                favoriteItems    = state.favoriteItems,
                                onFavoriteToggle = toggleFav,
                                onFoodClick      = { state = state.copy(selectedFood = it) }
                            )
                            state.activeTab == 1 -> MenuScreen(
                                modifier         = mod,
                                allMakanan       = state.allMakanan,
                                favoriteItems    = state.favoriteItems,
                                onFavoriteToggle = toggleFav,
                                onFoodClick      = { state = state.copy(selectedFood = it) }
                            )
                            state.activeTab == 2 -> NutrisiScreen(
                                modifier    = mod,
                                logMakanan  = state.logMakanan,
                                allMakanan  = state.allMakanan,
                                onLogToggle = toggleLog,
                                onFoodClick = { state = state.copy(selectedFood = it) }
                            )
                            else -> ProfilePlaceholder(mod)
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    Box(modifier.fillMaxSize(), Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator(color = Green)
            Spacer(Modifier.height(12.dp))
            Text("Memuat menu...", color = Color.Gray, fontSize = 14.sp)
        }
    }
}

@Composable
fun ErrorScreen(modifier: Modifier = Modifier) {
    Box(modifier.fillMaxSize().padding(32.dp), Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("😵", fontSize = 48.sp)
            Spacer(Modifier.height(12.dp))
            Text("Gagal Memuat Data", fontWeight = FontWeight.Bold,
                fontSize = 18.sp, color = Color(0xFFE57373))
            Spacer(Modifier.height(8.dp))
            Text("Pastikan koneksi internet kamu menyala",
                fontSize = 13.sp, color = Color.Gray, textAlign = TextAlign.Center)
        }
    }
}

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    allMakanan: List<Mangan>,
    favoriteItems: Set<String>,
    onFavoriteToggle: (String) -> Unit,
    onFoodClick: (Mangan) -> Unit
) {
    Column(modifier = modifier.fillMaxSize().background(BgGray)
        .verticalScroll(rememberScrollState())) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 48.dp, start = 24.dp, end = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text("MealKost Kuu..", fontSize = 28.sp, fontWeight = FontWeight.Bold)
                Text("Pastikan Kalorimu Terpenuhi! Sesuai Budgetmu", fontSize = 11.sp, color = Color.DarkGray)
            }
            Icon(Icons.Rounded.AccountCircle, null,
                modifier = Modifier.size(56.dp).clip(CircleShape), tint = Color(0xFFA5C1A5))
        }

        Spacer(Modifier.height(16.dp))

        Card(modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = GreenLight)
        ) {
            Column(Modifier.padding(20.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.AccountBox, null, tint = Green)
                    Spacer(Modifier.width(8.dp))
                    Text("Budget Hari Ini", fontWeight = FontWeight.SemiBold, color = Color.DarkGray)
                }
                Spacer(Modifier.height(8.dp))
                Text("Rp 25.000", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(16.dp))
                HorizontalDivider(color = Color.LightGray)
                Spacer(Modifier.height(16.dp))
                Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween, Alignment.CenterVertically) {
                    Text("Sisa Budget", color = Color.DarkGray)
                    Button(onClick = {},
                        colors = ButtonDefaults.buttonColors(containerColor = Green),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                        modifier = Modifier.height(36.dp)
                    ) { Text("Rp 10.000 >", fontSize = 14.sp) }
                }
            }
        }

        Spacer(Modifier.height(24.dp))
        SectionHeader("Rekomendasi Untukmu")
        Spacer(Modifier.height(12.dp))
        LazyRow(contentPadding = PaddingValues(horizontal = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            items(allMakanan) { food ->
                RekomCard(food, favoriteItems.contains(food.nama), onFavoriteToggle, onFoodClick)
            }
        }

        Spacer(Modifier.height(24.dp))
        SectionHeader("Menu Favorit Anak Kost")
        Spacer(Modifier.height(12.dp))
        Column(Modifier.padding(horizontal = 24.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            allMakanan.forEach { food ->
                FavListCard(food, favoriteItems.contains(food.nama), onFavoriteToggle, onFoodClick)
            }
        }
        Spacer(Modifier.height(24.dp))
    }
}

@Composable
fun MenuScreen(
    modifier: Modifier = Modifier,
    allMakanan: List<Mangan>,
    favoriteItems: Set<String>,
    onFavoriteToggle: (String) -> Unit,
    onFoodClick: (Mangan) -> Unit
) {
    var query    by remember { mutableStateOf("") }
    var kategori by remember { mutableStateOf(KategoriMenu.SEMUA) }

    val filtered = allMakanan.filter { food ->
        (query.isBlank() || food.nama.contains(query, true) || food.deskripsi.contains(query, true)) &&
                when (kategori) {
                    KategoriMenu.SEMUA -> true
                    KategoriMenu.NASI  -> food.nama.contains("Nasi", true)
                    KategoriMenu.LAUK  -> listOf("Ayam","Ikan","Tempe","Tahu","Gado").any { food.nama.contains(it, true) }
                    KategoriMenu.MINUM -> listOf("Es","Jus","Teh").any { food.nama.contains(it, true) }
                    KategoriMenu.SNACK -> food.kalori < 200
                }
    }

    Column(modifier = modifier.fillMaxSize().background(BgGray)) {
        Column(Modifier.fillMaxWidth().background(Color.White)
            .padding(top = 24.dp, start = 24.dp, end = 24.dp, bottom = 16.dp)) {
            Text("Menu Lengkap", fontSize = 22.sp, fontWeight = FontWeight.Bold)
            Text("${allMakanan.size} menu tersedia", fontSize = 12.sp, color = Color.Gray)
            Spacer(Modifier.height(14.dp))
            OutlinedTextField(
                value = query, onValueChange = { query = it },
                placeholder = { Text("Cari menu...", fontSize = 14.sp) },
                leadingIcon  = { Icon(Icons.Default.Search, null, tint = Green) },
                trailingIcon = { if (query.isNotEmpty())
                    Icon(Icons.Default.Close, null, Modifier.clickable { query = "" }, tint = Color.Gray) },
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Green, unfocusedBorderColor = Color(0xFFE0E0E0)),
                singleLine = true
            )
            Spacer(Modifier.height(12.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(KategoriMenu.entries.toList()) { k ->
                    FilterChip(
                        selected = kategori == k, onClick = { kategori = k },
                        label = { Text(when (k) {
                            KategoriMenu.SEMUA->"🍽 Semua"; KategoriMenu.NASI->"🍚 Nasi"
                            KategoriMenu.LAUK->"🍗 Lauk"; KategoriMenu.MINUM->"🥤 Minum"; KategoriMenu.SNACK->"🍿 Snack"
                        }, fontSize = 13.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Green, selectedLabelColor = Color.White),
                        border = FilterChipDefaults.filterChipBorder(enabled = true, selected = kategori == k,
                            borderColor = Color.Transparent, selectedBorderColor = Color.Transparent)
                    )
                }
            }
        }

        if (filtered.isEmpty()) {
            Box(Modifier.fillMaxSize(), Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("😅", fontSize = 48.sp); Spacer(Modifier.height(8.dp))
                    Text("Menu tidak ditemukan", fontWeight = FontWeight.Bold)
                    Text("Coba kata kunci lain", fontSize = 13.sp, color = Color.Gray)
                }
            }
        } else {
            Text("${filtered.size} menu ditemukan", fontSize = 12.sp, color = Color.Gray,
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 10.dp))
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement   = Arrangement.spacedBy(12.dp)
            ) {
                items(filtered) { food ->
                    MenuGridCard(food, favoriteItems.contains(food.nama), onFavoriteToggle, onFoodClick)
                }
            }
        }
    }
}

@Composable
fun NutrisiScreen(
    modifier: Modifier = Modifier,
    logMakanan: List<Mangan>,
    allMakanan: List<Mangan>,
    onLogToggle: (Mangan) -> Unit,
    onFoodClick: (Mangan) -> Unit
) {
    val tKal = 2000; val tProt = 60; val tKarbo = 250; val tLemak = 65
    val totKal   = logMakanan.sumOf { it.kalori }.coerceAtMost(tKal)
    val totProt  = logMakanan.sumOf { it.protein }.coerceAtMost(tProt)
    val totKarbo = logMakanan.sumOf { it.karbo }.coerceAtMost(tKarbo)
    val totLemak = logMakanan.sumOf { it.lemak }.coerceAtMost(tLemak)
    val kalProg  = (totKal / tKal.toFloat()).coerceIn(0f, 1f)

    Column(modifier = modifier.fillMaxSize().background(BgGray).verticalScroll(rememberScrollState())) {
        Column(Modifier.fillMaxWidth().background(Color.White)
            .padding(top = 24.dp, start = 24.dp, end = 24.dp, bottom = 20.dp)) {
            Text("Nutrisi Harian", fontSize = 22.sp, fontWeight = FontWeight.Bold)
            Text("Catat makananmu, pantau gizimu", fontSize = 12.sp, color = Color.Gray)
        }

        Spacer(Modifier.height(16.dp))

        Card(Modifier.fillMaxWidth().padding(horizontal = 24.dp), shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Green), elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Kalori Hari Ini", fontSize = 14.sp, color = Color.White.copy(.85f))
                Spacer(Modifier.height(16.dp))
                Box(Modifier.size(140.dp), Alignment.Center) {
                    androidx.compose.foundation.Canvas(Modifier.size(140.dp)) {
                        drawArc(Color.White.copy(.25f), -90f, 360f, false,
                            style = Stroke(14.dp.toPx(), cap = StrokeCap.Round))
                        drawArc(Color.White, -90f, 360f * kalProg, false,
                            style = Stroke(14.dp.toPx(), cap = StrokeCap.Round))
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("$totKal", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        Text("/ $tKal kcal", fontSize = 11.sp, color = Color.White.copy(.75f))
                    }
                }
                Spacer(Modifier.height(16.dp))
                Row(Modifier.fillMaxWidth(), Arrangement.SpaceEvenly) {
                    MiniInfo("Tercatat", "$totKal kcal")
                    Box(Modifier.width(1.dp).height(36.dp).background(Color.White.copy(.3f)))
                    MiniInfo("Sisa", "${(tKal - totKal).coerceAtLeast(0)} kcal")
                    Box(Modifier.width(1.dp).height(36.dp).background(Color.White.copy(.3f)))
                    MiniInfo("Status", when {
                        kalProg < 0.4f -> "Kurang 😟"; kalProg < 0.8f -> "Oke 👍"
                        kalProg < 1f   -> "Hampir ✅"; else           -> "Cukup 🎉"
                    })
                }
            }
        }

        Spacer(Modifier.height(20.dp))

        Card(Modifier.fillMaxWidth().padding(horizontal = 24.dp), shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(Color.White), elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Column(Modifier.padding(20.dp)) {
                Text("Makro Nutrisi", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                Text("Target harian mahasiswa aktif", fontSize = 11.sp, color = Color.Gray)
                Spacer(Modifier.height(16.dp))
                MacroBar("🥩 Protein",     totProt,  tProt,  "g", Color(0xFFE57373))
                Spacer(Modifier.height(14.dp))
                MacroBar("🍚 Karbohidrat", totKarbo, tKarbo, "g", Color(0xFFFFB74D))
                Spacer(Modifier.height(14.dp))
                MacroBar("🧈 Lemak",       totLemak, tLemak, "g", Color(0xFF64B5F6))
            }
        }

        Spacer(Modifier.height(20.dp))

        Column(Modifier.padding(horizontal = 24.dp)) {
            Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween, Alignment.CenterVertically) {
                Column {
                    Text("Makanan Hari Ini", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Text(if (logMakanan.isEmpty()) "Belum ada catatan" else "${logMakanan.size} menu dicatat",
                        fontSize = 12.sp, color = Color.Gray)
                }
                if (logMakanan.isNotEmpty()) {
                    TextButton(onClick = { logMakanan.toList().forEach { onLogToggle(it) } }) {
                        Text("Reset", color = Color.Gray, fontSize = 12.sp)
                    }
                }
            }
            Spacer(Modifier.height(12.dp))

            if (logMakanan.isEmpty()) {
                Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(Color.White), elevation = CardDefaults.cardElevation(1.dp)
                ) {
                    Column(Modifier.fillMaxWidth().padding(32.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("🍽️", fontSize = 36.sp); Spacer(Modifier.height(8.dp))
                        Text("Belum ada makanan dicatat", fontWeight = FontWeight.Bold, color = Color.DarkGray)
                        Text("Buka detail menu → tap \"Catat Makanan Ini\"", fontSize = 12.sp, color = Color.Gray)
                    }
                }
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    logMakanan.forEach { food ->
                        Card(Modifier.fillMaxWidth().clickable { onFoodClick(food) },
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(Color.White), elevation = CardDefaults.cardElevation(1.dp)
                        ) {
                            Row(Modifier.padding(12.dp).fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                                AsyncImage(model = food.imageUrl, contentDescription = food.nama,
                                    modifier = Modifier.size(44.dp).clip(RoundedCornerShape(8.dp)),
                                    contentScale = ContentScale.Crop)
                                Spacer(Modifier.width(12.dp))
                                Column(Modifier.weight(1f)) {
                                    Text(food.nama, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                    Text("${food.kalori} kcal | P:${food.protein}g K:${food.karbo}g L:${food.lemak}g",
                                        fontSize = 11.sp, color = Color.Gray)
                                }
                                IconButton(onClick = { onLogToggle(food) }, modifier = Modifier.size(28.dp)) {
                                    Icon(Icons.Default.Close, null, tint = Color.LightGray, modifier = Modifier.size(16.dp))
                                }
                            }
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(20.dp))

        Column(Modifier.padding(horizontal = 24.dp)) {
            Text("Tambah dari Menu", fontWeight = FontWeight.Bold, fontSize = 15.sp)
            Text("Ketuk + untuk mencatat apa yang kamu makan", fontSize = 12.sp, color = Color.Gray)
            Spacer(Modifier.height(12.dp))
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                allMakanan.forEach { food ->
                    val logged = logMakanan.contains(food)
                    Card(Modifier.fillMaxWidth().clickable { onFoodClick(food) },
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(if (logged) GreenLight else Color.White),
                        elevation = CardDefaults.cardElevation(1.dp)
                    ) {
                        Row(Modifier.padding(12.dp).fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                            AsyncImage(model = food.imageUrl, contentDescription = food.nama,
                                modifier = Modifier.size(44.dp).clip(RoundedCornerShape(8.dp)),
                                contentScale = ContentScale.Crop)
                            Spacer(Modifier.width(12.dp))
                            Column(Modifier.weight(1f)) {
                                Text(food.nama, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                Text("${food.kalori} kcal | Rp ${food.harga}", fontSize = 11.sp, color = Color.Gray)
                            }
                            IconButton(onClick = { onLogToggle(food) }) {
                                Icon(if (logged) Icons.Default.CheckCircle else Icons.Default.AddCircle,
                                    null, tint = if (logged) Green else Color.LightGray)
                            }
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(20.dp))

        Column(Modifier.padding(horizontal = 24.dp)) {
            Text("Tips Nutrisi Anak Kost", fontWeight = FontWeight.Bold, fontSize = 15.sp)
            Spacer(Modifier.height(12.dp))
            listOf(
                Triple("💧","Minum Air Cukup","Minimal 8 gelas/hari untuk metabolisme optimal."),
                Triple("🥚","Protein Murah Meriah","Telur & tempe = sumber protein terbaik untuk budget kost."),
                Triple("⏰","Jangan Skip Sarapan","Sarapan meningkatkan konsentrasi kuliah hingga 30%."),
                Triple("🌙","Hindari Makan >9PM","Metabolisme melambat malam hari.")
            ).forEach { (e, t, d) ->
                Card(Modifier.fillMaxWidth().padding(bottom = 10.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(Color.White), elevation = CardDefaults.cardElevation(1.dp)
                ) {
                    Row(Modifier.padding(14.dp), verticalAlignment = Alignment.Top) {
                        Text(e, fontSize = 24.sp); Spacer(Modifier.width(12.dp))
                        Column {
                            Text(t, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            Text(d, fontSize = 12.sp, color = Color.Gray, lineHeight = 17.sp)
                        }
                    }
                }
            }
        }
        Spacer(Modifier.height(24.dp))
    }
}

@Composable
fun DetailScreen(
    food: Mangan, isFavorite: Boolean, isLogged: Boolean,
    onFavToggle: (String) -> Unit, onLogToggle: (Mangan) -> Unit,
    onBackClick: () -> Unit, modifier: Modifier = Modifier
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Box(modifier = modifier.fillMaxSize().background(Color.White)) {
        Column(Modifier.fillMaxSize().padding(24.dp)) {
            // Modul 12: AsyncImage + overlay favorit
            Box(Modifier.fillMaxWidth().height(250.dp)) {
                AsyncImage(
                    model = food.imageUrl, contentDescription = food.nama,
                    modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(16.dp)),
                    contentScale = ContentScale.Crop
                )
                Box(
                    modifier = Modifier.align(Alignment.TopEnd).padding(12.dp).size(36.dp)
                        .clip(CircleShape).background(Color.White.copy(.65f))
                        .clickable { onFavToggle(food.nama) },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        null, tint = if (isFavorite) Color.Red else Color.Gray,
                        modifier = Modifier.size(20.dp))
                }
            }

            Spacer(Modifier.height(20.dp))
            Text(food.nama, fontSize = 26.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(4.dp))
            Text(food.deskripsi, fontSize = 15.sp, color = Color.DarkGray)
            Spacer(Modifier.height(16.dp))
            HorizontalDivider(color = Color.LightGray)
            Spacer(Modifier.height(16.dp))
            Text("Harga: Rp ${food.harga}", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Green)
            Spacer(Modifier.height(12.dp))

            Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(GreenLight)) {
                Row(Modifier.fillMaxWidth().padding(16.dp), Arrangement.SpaceEvenly) {
                    NutriBadge("🔥 Kalori","${food.kalori}","kcal")
                    NutriBadge("🥩 Protein","${food.protein}","gram")
                    NutriBadge("🍚 Karbo","${food.karbo}","gram")
                    NutriBadge("🧈 Lemak","${food.lemak}","gram")
                }
            }

            Spacer(Modifier.weight(1f))

            Button(
                onClick = {
                    onLogToggle(food)
                    scope.launch {
                        snackbarHostState.showSnackbar(
                            if (!isLogged) "${food.nama} dicatat ke tracker nutrisi! 🥗"
                            else "${food.nama} dihapus dari tracker"
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = if (isLogged) Color(0xFFE57373) else Green),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(if (isLogged) Icons.Default.Close else Icons.Default.AddCircle, null, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
                Text(if (isLogged) "Hapus dari Catatan" else "Catat Makanan Ini", fontSize = 16.sp)
            }

            Spacer(Modifier.height(12.dp))
            OutlinedButton(onClick = onBackClick, modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Green),
                shape = RoundedCornerShape(12.dp)
            ) { Text("Kembali", fontSize = 16.sp) }
        }
        SnackbarHost(snackbarHostState, Modifier.align(Alignment.BottomCenter).padding(bottom = 16.dp))
    }
}

@Composable
fun ProfilePlaceholder(modifier: Modifier = Modifier) {
    Box(modifier.fillMaxSize(), Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("👤", fontSize = 48.sp); Spacer(Modifier.height(8.dp))
            Text("Profile", fontWeight = FontWeight.Bold, color = Color.DarkGray)
            Text("Coming soon!", fontSize = 13.sp, color = Color.Gray)
        }
    }
}

@Composable
fun SectionHeader(title: String) {
    Row(Modifier.fillMaxWidth().padding(horizontal = 24.dp), Arrangement.SpaceBetween, Alignment.CenterVertically) {
        Text(title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        Icon(Icons.Default.MoreVert, null, tint = Color.Gray)
    }
}

@Composable
fun RekomCard(food: Mangan, isFav: Boolean, onFavToggle: (String) -> Unit, onClick: (Mangan) -> Unit) {
    Card(shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(Color.White),
        elevation = CardDefaults.cardElevation(2.dp), modifier = Modifier.width(130.dp).clickable { onClick(food) }
    ) {
        Column(Modifier.padding(12.dp)) {
            Box(Modifier.fillMaxWidth().height(90.dp)) {
                AsyncImage(model = food.imageUrl, contentDescription = food.nama,
                    modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop)
                Box(modifier = Modifier.align(Alignment.TopEnd).padding(4.dp).size(24.dp)
                    .clip(CircleShape).background(Color.White.copy(.7f)).clickable { onFavToggle(food.nama) },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(if (isFav) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        null, tint = if (isFav) Color.Red else Color.Gray, modifier = Modifier.size(13.dp))
                }
            }
            Spacer(Modifier.height(10.dp))
            Text(food.nama, fontWeight = FontWeight.Bold, fontSize = 13.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Text("Rp ${food.harga}", color = Color.LightGray, fontSize = 12.sp)
            Row(Modifier.padding(top = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Info, null, Modifier.size(11.dp), tint = Green)
                Spacer(Modifier.width(3.dp))
                Text("${food.kalori} kcal", color = Color.Gray, fontSize = 11.sp)
            }
        }
    }
}

@Composable
fun FavListCard(food: Mangan, isFav: Boolean, onFavToggle: (String) -> Unit, onClick: (Mangan) -> Unit) {
    Card(shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(Color.White),
        elevation = CardDefaults.cardElevation(1.dp), modifier = Modifier.fillMaxWidth().clickable { onClick(food) }
    ) {
        Row(Modifier.padding(12.dp).fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(model = food.imageUrl, contentDescription = food.nama,
                modifier = Modifier.size(48.dp).clip(RoundedCornerShape(8.dp)), contentScale = ContentScale.Crop)
            Spacer(Modifier.width(16.dp))
            Column(Modifier.weight(1f)) {
                Text(food.nama, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Text(food.deskripsi, color = Color.Gray, fontSize = 12.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
            }
            Text("Rp ${food.harga}", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Spacer(Modifier.width(8.dp))
            Icon(if (isFav) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                null, tint = if (isFav) Color.Red else Color.LightGray,
                modifier = Modifier.size(20.dp).clickable { onFavToggle(food.nama) })
        }
    }
}

@Composable
fun MenuGridCard(food: Mangan, isFav: Boolean, onFavToggle: (String) -> Unit, onClick: (Mangan) -> Unit) {
    Card(shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(Color.White),
        elevation = CardDefaults.cardElevation(2.dp), modifier = Modifier.fillMaxWidth().clickable { onClick(food) }
    ) {
        Column {
            Box(Modifier.fillMaxWidth().height(120.dp)) {
                AsyncImage(model = food.imageUrl, contentDescription = food.nama,
                    modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
                    contentScale = ContentScale.Crop)
                Box(modifier = Modifier.align(Alignment.TopEnd).padding(8.dp).size(28.dp)
                    .clip(CircleShape).background(Color.White.copy(.75f)).clickable { onFavToggle(food.nama) },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(if (isFav) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        null, tint = if (isFav) Color.Red else Color.Gray, modifier = Modifier.size(15.dp))
                }
                Box(modifier = Modifier.align(Alignment.BottomStart).padding(8.dp)
                    .clip(RoundedCornerShape(6.dp)).background(Color.Black.copy(.55f))
                    .padding(horizontal = 6.dp, vertical = 2.dp)
                ) { Text("${food.kalori} kcal", fontSize = 10.sp, color = Color.White) }
            }
            Column(Modifier.padding(10.dp)) {
                Text(food.nama, fontWeight = FontWeight.Bold, fontSize = 13.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Text(food.deskripsi, fontSize = 11.sp, color = Color.Gray, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Spacer(Modifier.height(6.dp))
                Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween, Alignment.CenterVertically) {
                    Text("Rp ${food.harga}", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Green)
                    Box(Modifier.clip(RoundedCornerShape(4.dp)).background(GreenLight).padding(horizontal = 5.dp, vertical = 2.dp)) {
                        Text("P:${food.protein}g", fontSize = 10.sp, color = Green, fontWeight = FontWeight.Medium)
                    }
                }
            }
        }
    }
}

@Composable
fun NutriBadge(label: String, value: String, unit: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label, fontSize = 11.sp, color = Color.Gray)
        Text(value, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Text(unit,  fontSize = 10.sp, color = Color.Gray)
    }
}

@Composable
fun MacroBar(label: String, current: Int, target: Int, unit: String, color: Color) {
    val progress = (current / target.toFloat()).coerceIn(0f, 1f)
    Column {
        Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
            Text(label, fontSize = 13.sp, fontWeight = FontWeight.Medium)
            Text("$current / $target $unit", fontSize = 12.sp, color = Color.Gray)
        }
        Spacer(Modifier.height(6.dp))
        Box(Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(4.dp)).background(Color(0xFFF0F0F0))) {
            Box(Modifier.fillMaxWidth(progress).fillMaxHeight().clip(RoundedCornerShape(4.dp)).background(color))
        }
    }
}

@Composable
fun MiniInfo(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
        Text(label, fontSize = 11.sp, color = Color.White.copy(.75f))
    }
}

@Composable
fun AppBottomBar(activeTab: Int, onTabChange: (Int) -> Unit) {
    NavigationBar(containerColor = Color.White, tonalElevation = 8.dp) {
        listOf(
            Triple(Icons.Default.Home,           "Home",    0),
            Triple(Icons.Default.Menu,           "Menu",    1),
            Triple(Icons.Default.FavoriteBorder, "Nutrisi", 2),
            Triple(Icons.Default.Person,         "Profile", 3)
        ).forEach { (icon, label, idx) ->
            NavigationBarItem(
                icon = { Icon(icon, null) }, label = { Text(label) },
                selected = activeTab == idx, onClick = { onTabChange(idx) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Green, selectedTextColor = Green, indicatorColor = GreenSoft)
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun Preview() {
    Teori1_MyTAMTheme {
        Scaffold(bottomBar = { AppBottomBar(0) {} }) { pad ->
            LoadingScreen(Modifier.padding(pad))
        }
    }
}