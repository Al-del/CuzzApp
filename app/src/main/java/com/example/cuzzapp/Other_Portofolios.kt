package com.example.cuzzapp

import achivement_other
import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.example.cuzzapp.ui.theme.CuzzAppTheme
class Other_Portofolios : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

           DisplayAchievements(achivement_other)
        }
    }
}
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun DisplayAchievements(achievements: MutableList<achievementuriUSER?>) {
    //Make a box that aligns the content to the center
    Scaffold {
        LazyColumn(modifier = Modifier.fillMaxSize()){
            items(achievements) { achievement ->
                // Replace this with your actual composable that displays an achievement
                AchievementItem(achievement)
                Spacer(modifier = Modifier.padding(8.dp))
            }
        }
    }

}

@Composable
fun AchievementItem(achievement: achievementuriUSER?) {
    val painter = rememberImagePainter(data = achievement?.linkimagine)

    // Display your achievement here
    if (achievement != null) {
        Text(text = achievement.Premiu)
        Text(text =achievement.materie)
        Text(text =achievement.numeConcurs)
        Text(text =achievement.informatii)
        Image(
            painter = painter,
            contentDescription = null,
            modifier = Modifier

                .size(50.dp) // Set the size of the image
                .clip(CircleShape) // Clip the image to a circle
        )
    } // Replace this with actual fields of achievementuriUSER
}