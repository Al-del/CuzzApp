package com.example.cuzzapp

import achivement
import achivement_other
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.collection.emptyLongSet
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.Button
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import coil.compose.rememberImagePainter
import com.example.cuzzapp.ui.theme.CuzzAppTheme
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment
import username_for_all
import viewedProfile

class Other_Portofolios : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            println("The length of the achievements list is: ${achivement.size}")
            val scaffoldState = rememberScaffoldState()
            Drawer(scaffoldState = scaffoldState) {
                DisplayAchievements(achivement,intent)
            }

        }
    }
}
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun DisplayAchievements(achievements: MutableList<achievementuriUSER?>, intent: Intent?){
    val context = LocalContext.current
    if(viewedProfile == username_for_all) {
        Scaffold(

            modifier = Modifier.fillMaxSize(),
            floatingActionButton = {
                FloatingActionButton(onClick = {
                    val intent = Intent(context, adaugare::class.java)
                    ContextCompat.startActivity(context, intent, null)
                }) {
                    Icon(Icons.Filled.Add, contentDescription = "Add")
                }
            },
            floatingActionButtonPosition = FabPosition.End,
        )

        {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(achievements) { achievement ->
                    // Replace this with your actual composable that displays an achievement
                    AchievementItem(achievement)
                    Spacer(modifier = Modifier.padding(8.dp))
                }
            }
        }
    }
    else
    {
        Scaffold{
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(achivement_other) { achievement ->
                    // Replace this with your actual composable that displays an achievement
                    AchievementItem(achievement)
                    Spacer(modifier = Modifier.padding(8.dp))
                }
            }
        }
    }

}

@Composable
fun AchievementItem(achievement: achievementuriUSER?) {
    val painter = rememberImagePainter(data = achievement?.linkimagine)

    // Display your achievement here
    if (achievement != null) {
        Spacer(modifier = Modifier.padding(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier.run {
                    background(Color.Black)
                        .width(250.dp)
                        .height(250.dp)
                        .fillMaxWidth(0.8f)// This will make the Box take 80% of the screen width

                },

                )
            {

                val imageId: Int = if (achievement.materie =="Informatica") {
                    R.drawable.informatica
                } else {
                    if (achievement.materie == "Matematica") {
                        R.drawable.fizica
                    } else {
                        if (achievement.materie == "Matematica") {
                            R.drawable.fizica
                        } else {
                            if (achievement.materie == "Matematica") {
                                R.drawable.fizica
                            } else {
                                R.drawable.informatica
                            }                            }                        }
                }
                Column(
                    modifier = Modifier.fillMaxHeight()
                )
                {
                    // for (a in achivement) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .background(Color.Red)
                    ) {
                        Image(
                            painter = painterResource(id = imageId),
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxSize()// Set the size of the image manually
                        )
                        Text(
                            text = "Your Text Here",
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    )
                    {

                        Column(modifier = Modifier.fillMaxSize()) {
                            Text(text = achievement.Premiu, color = Color.White)
                            Text(text = achievement.numeConcurs, color = Color.White)
                            Text(text = achievement.informatii, color = Color.White)
                        }

                        Image(
                            painter = painter,
                            contentDescription = null,
                            modifier = Modifier

                                .width(150.dp)
                                .height(150.dp)
                                .clip(CircleShape) // Clip the image to a circle
                        )
                    }
                }
                Spacer(modifier = Modifier.padding(50.dp))
            }
        }
    }
}// Replace this with actual fields of achievementuriUSER
//   }


