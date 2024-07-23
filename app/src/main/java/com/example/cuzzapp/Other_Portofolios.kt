package com.example.cuzzapp

import achivement
import achivement_other
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import coil.compose.rememberImagePainter
import com.example.cuzzapp.ui.theme.CuzzAppTheme
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment
import username_for_all
import username_true
import viewedProfile

class Other_Portofolios : ComponentActivity() {

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val prof_sel = intent.getStringExtra("userr")

        setContent {
            Toast.makeText(this, "The user is: $prof_sel", Toast.LENGTH_SHORT).show()
            Toast.makeText(this, "The user is: $username_true", Toast.LENGTH_SHORT).show()
    val gradientBrush = Brush.linearGradient(
        colors = listOf(
            Color(0xFF345E2A),
            Color(0xFF403182)
        ),
        start = Offset(0f, 0f),
        end = Offset.Infinite
    )


        println("The length of the achievements list is: ${achivement.size}")
        var searchQuery by remember { mutableStateOf("") }
        val scaffoldState = rememberScaffoldState()

        Drawer(scaffoldState, searchQuery, backgroundColor = gradientBrush,onSearchQueryChange = { searchQuery = it }) {
            Box(modifier = Modifier.fillMaxSize()) {
                if(username_true == prof_sel){
                    DisplayAchievements(achivement, intent, modifier =  Modifier.background(brush = gradientBrush))
                }else{
                    DisplayAchievements(achivement_other, intent, modifier =  Modifier.background(brush = gradientBrush))
                }
            }
        }

}
    }
}
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun DisplayAchievements(achievements: MutableList<achievementuriUSER?>, intent: Intent?,modifier: Modifier){
    val context = LocalContext.current
    if(viewedProfile == username_for_all) {

        Scaffold(

            modifier = modifier.fillMaxSize()
                ,
            floatingActionButton = {
                FloatingActionButton( modifier = modifier.offset(y = -50.dp), onClick = {
                    val intent = Intent(context, adaugare::class.java)
                    ContextCompat.startActivity(context, intent, null)
                }) {
                    Icon(Icons.Filled.Add, contentDescription = "Add")
                }
            },
            floatingActionButtonPosition = FabPosition.End,
        )

        {
            LazyColumn(modifier = modifier.fillMaxSize()) {
                itemsIndexed(achievements) { index, achievement ->
                    // Now you have access to both the index and the achievement
                    AchievementItem(achievement, index+1)
                    Spacer(modifier = modifier.padding(8.dp))
                }
            }
        }
    }
    else
    {
        Scaffold{
            LazyColumn(modifier = modifier.fillMaxSize()) {
                itemsIndexed(achievements) { index, achievement ->
                    // Now you have access to both the index and the achievement
                    AchievementItem(achievement, index+1)
                    Spacer(modifier = modifier.padding(8.dp))
                }
            }
        }
    }

}

@Composable
fun AchievementItem(achievement: achievementuriUSER?, indx: Int) {
    val painter = rememberImagePainter(data = achievement?.linkimagine)

    // Display your achievement here
    if (achievement != null) {
        Box(
            modifier = Modifier
                .requiredWidth(width = getScreenWidthDp().dp)
                .requiredHeight(height = 184.dp)
                .clip(shape = RoundedCornerShape(20.dp))
                .background(color = Color.White)
        ) {
            Text(
                textAlign = TextAlign.End,
                lineHeight = 1.sp,
                text = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            color = Color.Black,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    ) {}
                    withStyle(
                        style = SpanStyle(
                            color = Color.Black,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    ) { append(achievement.materie) }
                },
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(
                        x = 16.dp,
                        y = 94.dp
                    )
            )
            Text(
                text = achievement.numeConcurs,
                color = Color.Black,
                textAlign = TextAlign.End,
                lineHeight = 0.8.em,
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(
                        x = 16.dp,
                        y = 68.dp
                    )
            )
            Image(
                painter = painter,
                contentDescription = "image 219",
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(
                        x = 185.dp,
                        y = 20.dp
                    )
                    .requiredWidth(width = 167.dp)
                    .requiredHeight(height = 140.dp)
            )
            Box(
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(
                        x = 16.dp,
                        y = 16.dp
                    )
                    .requiredSize(size = 40.dp)
                    .clip(shape = RoundedCornerShape(50.dp))
                    .background(color = Color(0xffb0f41f))
            ) {
                Text(
                    text = indx.toString(),
                    color = Color.Black,
                    textAlign = TextAlign.End,
                    lineHeight = 1.em,
                    style = TextStyle(
                        fontSize = 16.sp
                    ),
                    modifier = Modifier
                        .align(alignment = Alignment.TopStart)
                        .offset(
                            x = 17.dp,
                            y = 12.dp
                        )
                        .requiredWidth(width = 6.dp)
                )
            }
            Text(
                lineHeight = 1.sp,
                text = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            color = Color.Black,
                            fontSize = 12.sp
                        )
                    ) {}
                    withStyle(
                        style = SpanStyle(
                            color = Color.Black,
                            fontSize = 12.sp
                        )
                    ) { append(achievement.informatii) }
                },
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(
                        x = 16.dp,
                        y = 120.dp
                    )
                    .requiredWidth(width = 174.dp)
            )
        }
    }
}


