package com.example.cuzzapp

import API_KEY
import AppNavigator
import BottomNavigationBar
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import coil.compose.rememberImagePainter
import points
import url_photo
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.Divider
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat.startActivity
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import video_l

var videos = listOf<Map<String, String>>()
class HomeScreen : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            println("url $url_photo")
            MyDrawer(this,rememberScaffoldState())

        }
    }
}
@Composable
fun HOmescreen(homeScreen: HomeScreen) {
    var searchQuery by remember { mutableStateOf("calculus") }

    println("Current searchQuery: $searchQuery") // Add this line

    Box(modifier = Modifier.fillMaxSize()
        .background(Color(0xFF262323)) // Set background color to 262323
    ) {
        LoadImageFromUrl(url = url_photo, points = points)
        Rectangle2(searchQuery, { newQuery -> searchQuery = newQuery })
        DisplayVideos(homeScreen,searchQuery)

    }
}
@Composable
fun LoadImageFromUrl(url: String, points: Int) {
    val painter = rememberImagePainter(data = url)

    Box(
        modifier = Modifier
            .fillMaxSize() // Fill the parent
            .offset(y = 30.dp, x = 10.dp), // Move the box down
        contentAlignment = Alignment.TopStart // Align the content to the top start
    ) {
        Image(
            painter = painter,
            contentDescription = null,
            modifier = Modifier
                .clickable {

                }
                .size(50.dp) // Set the size of the image
                .clip(CircleShape) // Clip the image to a circle
        )

        Text(
            text = "Points: $points",
            color = Color.White, // Set the color of the text to white

            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(x = (-20).dp, y = (10).dp) // Move the text up and to the left
        )
    }
}
@Composable
fun Rectangle2(searchQuery: String, onQueryChange: (String) -> Unit, modifier: Modifier = Modifier) {
    TextField(
        value = searchQuery,
        onValueChange = {

            newText ->
            println("onValueChange called with: $newText")
            onQueryChange(newText)
        },
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color(0xff9d9d9d),
            textColor = Color(0xff1c1b1f),
        ),
        modifier = modifier
            .offset(x = 55.dp, y = 30.dp)
            .requiredWidth(width = 220.dp)
            .requiredHeight(height = 50.dp)
            .clip(shape = RoundedCornerShape(100.dp)
            ))
    Image(
        painter = painterResource(id = R.drawable.lupa),
        contentDescription = "search",
        colorFilter = ColorFilter.tint(Color(0xff1c1b1f)),
        modifier = modifier
            .offset(x = 220.dp, y = 35.dp)
            .requiredSize(size = 35.dp))
}





suspend fun getEducationalVideos(query: String, maxResults: Int = 5): List<Map<String, String>> = withContext(Dispatchers.IO) {
    val client = OkHttpClient()
    val url = "https://www.googleapis.com/youtube/v3/search?q=$query&part=id,snippet&maxResults=$maxResults&type=video&videoCategoryId=27&key=$API_KEY"
    val request = Request.Builder().url(url).build()
    val response = client.newCall(request).execute()
    val jsonString = response.body?.string()
    val jsonObject = JSONObject(jsonString)
    println(jsonString)
    val items = jsonObject.getJSONArray("items")
    val videos = mutableListOf<Map<String, String>>()
    for (i in 0 until items.length()) {
        val item = items.getJSONObject(i)
        val video = mapOf(
            "title" to item.getJSONObject("snippet").getString("title"),
            "description" to item.getJSONObject("snippet").getString("description"),
            "url" to "https://www.youtube.com/watch?v=${item.getJSONObject("id").getString("videoId")}",
            "thumbnail" to item.getJSONObject("snippet").getJSONObject("thumbnails").getJSONObject("default").getString("url"), // Add this line
            "id" to item.getJSONObject("id").getString("videoId") // Add this line

        )
        videos.add(video)
    }
    videos
}
fun printVideos(videos: List<Map<String, String>>) {
    for (video in videos) {
        println("Title: ${video["title"]}")
        println("Description: ${video["description"]}")
        println("URL: ${video["url"]}")
        println("-----")
    }
}

fun searchVideosByText(query: String, maxResults: Int = 5): List<Map<String, String>> {
    val client = OkHttpClient()
    val url = "https://www.googleapis.com/youtube/v3/search?q=$query&part=id,snippet&maxResults=$maxResults&type=video&key=$API_KEY"
    val request = Request.Builder().url(url).build()
    val response = client.newCall(request).execute()
    val jsonString = response.body?.string()
    val jsonObject = JSONObject(jsonString)
    val items = jsonObject.getJSONArray("items")
    val videos = mutableListOf<Map<String, String>>()
    for (i in 0 until items.length()) {
        val item = items.getJSONObject(i)
        val video = mapOf(
            "title" to item.getJSONObject("snippet").getString("title"),
            "description" to item.getJSONObject("snippet").getString("description"),
            "url" to "https://www.youtube.com/watch?v=${item.getJSONObject("id").getString("videoId")}",
            "thumbnail" to item.getJSONObject("snippet").getJSONObject("thumbnails").getJSONObject("default").getString("url")
        )
        videos.add(video)
    }
    return videos
}
@Composable
fun DisplayVideos(homeScreen: HomeScreen, searchQuery: String) {
    var videos by remember { mutableStateOf(emptyList<Map<String, String>>()) }

    LaunchedEffect(searchQuery) {
        videos = getEducationalVideos(searchQuery, 5)
    }

    val gradientBrush = Brush.verticalGradient(
        colors = listOf(Color(0xFFCF0A0A), Color(0xFF262323)),
        startY = 0f,
        endY = 500f
    )

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxHeight(0.8f)
                .align(Alignment.BottomCenter)
                .offset(y = -LocalConfiguration.current.screenHeightDp.dp * 0.1f)
        ) {
            items(videos.size) { index ->
                val video = videos[index]
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    elevation = 8.dp,
                    backgroundColor = Color.Transparent, // Set background color to Transparent
                    contentColor = Color.White // Set content color to White
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(brush = gradientBrush) // Set background to gradient
                    ) {
                        Row(
                            modifier = Modifier.padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            val painter = rememberImagePainter(data = video["thumbnail"])
                            Image(
                                painter = painter,
                                contentDescription = "Video Thumbnail",
                                modifier = Modifier
                                    .clickable {
                                        video_l.title = video["title"].toString()
                                        video_l.description = video["description"].toString()
                                        video_l.url = video["url"].toString()
                                        video_l.thumbnail = video["thumbnail"].toString()
                                        video_l.id = video["id"].toString()
                                        println(videos[index])
                                        val intent = Intent(homeScreen, Video::class.java)
                                        startActivity(homeScreen, intent, null)
                                    }
                                    .size(120.dp)
                                    .clip(RoundedCornerShape(8.dp))

                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = video["title"].toString(),
                                style = MaterialTheme.typography.h6,
                                modifier = Modifier.padding(8.dp),
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MyDrawer(homeScreen: HomeScreen,scaffoldState: ScaffoldState) {
    val coroutineScope = rememberCoroutineScope()
    val navController = rememberNavController() // Create a NavController
val context = LocalContext.current // Get the local context to use startActivity
    Scaffold(
        scaffoldState = scaffoldState,
        drawerContent = {
            Column(
                modifier = Modifier
                    .fillMaxSize() // Fill the parent
                    .background(Color(0xFFD9D9D9)), // Set background color
                verticalArrangement = Arrangement.Center, // Center vertically
                horizontalAlignment = Alignment.CenterHorizontally // Center horizontally
            ) {
                Box(
                ) {
                    val painter = rememberImagePainter(data = url_photo)
                    Image(
                        painter = painter,
                        contentDescription = null,
                        modifier = Modifier
                            .clickable {

                            }
                            .size(200.dp) // Set the size of the image
                            .clip(CircleShape) // Clip the image to a circle
                    )
                }
                    Spacer(modifier = Modifier.height(24.dp)) // Add bigger space

                Button(
                    onClick = {
                        val intent = Intent(context, asis::class.java)
                        startActivity(context, intent, null)

                    },
                    shape = RoundedCornerShape(80.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xff5d5d5d)),
                    modifier = Modifier
                        .requiredWidth(width = 170.dp)
                        .requiredHeight(height = 40.dp)
                        .clickable {
                            val intent = Intent(context, asis::class.java)
                            startActivity(context, intent, null)
                        }
                ) {
                    Text("Asistenta")
                }
                Spacer(modifier = Modifier.height(24.dp)) // Add bigger space
                Button(
                    onClick = {
                        Log.d("Drawer", "Ranking button clicked")
                        val intent = Intent(context, Image__to_book::class.java)
                        startActivity(context, intent, null)
                    },
                    shape = RoundedCornerShape(80.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xff5d5d5d)),
                    modifier = Modifier
                        .requiredWidth(width = 170.dp)
                        .requiredHeight(height = 40.dp)
                        .clickable {

                        }
                ) {
                    Text("Image to bookd")
                }
                Spacer(modifier = Modifier.height(24.dp)) // Add bigger space
                Button(
                    onClick = {
                        val intent = Intent(context, HomeScreen::class.java)
                        startActivity(context, intent, null)
                    },
                    shape = RoundedCornerShape(80.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xff5d5d5d)),
                    modifier = Modifier
                        .requiredWidth(width = 170.dp)
                        .requiredHeight(height = 40.dp)
                ) {
                    Text("Home")
                }
                Spacer(modifier = Modifier.height(24.dp)) // Add bigger space
                Button(
                    onClick = {
                        val intent = Intent(context, Profile::class.java)
                        startActivity(context, intent, null)

                    },
                    shape = RoundedCornerShape(80.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xff5d5d5d)),
                    modifier = Modifier
                        .requiredWidth(width = 170.dp)
                        .requiredHeight(height = 40.dp)
                ) {
                    Text("Profile")
                }
                Spacer(modifier = Modifier.height(24.dp)) // Add bigger space

                Button(
                    onClick = {
                        val intent = Intent(context, Shop::class.java)
                        startActivity(context, intent, null)

                    },
                    shape = RoundedCornerShape(80.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xff5d5d5d)),
                    modifier = Modifier
                        .requiredWidth(width = 170.dp)
                        .requiredHeight(height = 40.dp)
                        .clickable {
                            val intent = Intent(context, asis::class.java)
                            startActivity(context, intent, null)
                        }
                ) {
                    Text("Shop")
                }

            }
        },
        content = {
            // Your content
            HOmescreen(homeScreen)
        },
        bottomBar = { AppNavigator(navController) } // Pass the NavController to AppNavigator

    )
}

@Composable
fun AppNavigator(navController: NavHostController) { // Add NavController as a parameter
    NavHost(navController, startDestination = Screen.Home.route) {
        composable(Screen.Home.route) { HomeScreen() }
        composable(Screen.Ranking.route) { RankingScreen() }
        composable(Screen.Profile.route) { Profile() }
    }
    BottomNavigationBar()
}