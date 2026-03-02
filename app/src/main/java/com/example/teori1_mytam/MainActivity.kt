package com.example.teori1_mytam
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.Image
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.teori1_mytam.ui.theme.Teori1_MyTAMTheme
import model.ManganSources
import model.Mangan
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            setContent {
                Teori1_MyTAMTheme {
                    FoodList()
                }
            }
            }
        }
    }


@Composable
fun FoodList() {
    val foodList = ManganSources.dummyMangan

    LazyColumn {
        items(foodList) { food ->
            FoodItemCard(food)
        }
    }
}
@Composable
fun FoodItemCard(food: Mangan) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Image(
            painter = painterResource(id = food.ImageRes),
            contentDescription = food.nama,
            modifier = Modifier.size(150.dp),
            contentScale = ContentScale.Crop
        )
        Text(text = "Nama: ${food.nama}")
        Text(text = "Harga: Rp ${food.harga}")
        Text(text = "Kalori: ${food.kalori}")
        Text(text = "Protein: ${food.protein}g")
    }
}
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Teori1_MyTAMTheme {
        FoodList()
    }
}