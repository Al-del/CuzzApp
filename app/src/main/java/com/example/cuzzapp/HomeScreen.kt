package com.example.cuzzapp

import API_KEY
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
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
import androidx.compose.material.Text
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
import androidx.compose.ui.draw.paint
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntSize
import androidx.core.content.ContextCompat.startActivity
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.util.copy
import backcolor
import coil.compose.AsyncImage
import coil.request.ImageRequest
import seach_querr
import state
import username_for_all
import username_true
import video_l

var videos = listOf<Map<String, String>>()
class HomeScreen : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
       setContent {
        HOmescreen(this) // Pass your specific composable content here

}
    }
}
@Composable
fun HOmescreen(homeScreen: HomeScreen) {
    var searchQuery by remember { mutableStateOf(seach_querr) }
    var showGuidelines by remember { mutableStateOf(true) } // Initialize showGuidelines state
    val scaffoldState = rememberScaffoldState()
    Drawer(scaffoldState, searchQuery, onSearchQueryChange = { searchQuery = it }) {

        Box(
            modifier = Modifier.fillMaxSize()
                .background(
                    color = Color(0xffb379df)
                )
        ) {
            // Conditionally display the GuidelinesOverlay based on showGuidelines state
            if (showGuidelines) {
                Box(
                    modifier = Modifier.fillMaxSize().offset(),
                    contentAlignment = Alignment.TopCenter
                ) {
                    GuidelinesOverlay()
                }
            }
        }
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
            ResizableDisplayVideosBox(
                homeScreen,
                searchQuery,
                showGuidelines
            ) // Pass showGuidelines to ResizableDisplayVideosBox
        }
    }
}

@Composable
fun ResizableDisplayVideosBox(homeScreen: HomeScreen, searchQuery: String, showGuidelines: Boolean) {
    var boxSize by remember { mutableStateOf(IntSize(300, 520)) } // Initial size

    Box(
        contentAlignment = Alignment.BottomCenter,
        modifier = Modifier
            .size(height = boxSize.height.dp, width = getScreenWidthDp().dp)
            .clip(RoundedCornerShape(12.dp))
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF8B93FF),
                        Color(0xFFEDD1FA),
                        Color(0xFF4120A9)
                    ),
                    start = Offset(0f, 0f),
                    end = Offset.Infinite
                )
            )
            .pointerInput(Unit) {
                detectDragGestures { _, dragAmount ->
                    val heightChange = dragAmount.y
                    boxSize = IntSize(
                        width = boxSize.width,
                        height = (boxSize.height - heightChange).coerceAtLeast(100f).toInt()
                    )
                    // Update showGuidelines state here if needed
                }
            }
    ) {
        DisplayVideos(homeScreen, searchQuery)
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





suspend fun getEducationalVideos(query: String, maxResults: Int = 15): List<Map<String, String>> = withContext(Dispatchers.IO) {
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
@Composable
fun DisplayVideos(homeScreen: HomeScreen, searchQuery: String) {
    var videos by remember { mutableStateOf(emptyList<Map<String, String>>()) }

    LaunchedEffect(searchQuery) {
        videos = getEducationalVideos(searchQuery, 15)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxHeight(0.8f)
                .align(Alignment.BottomCenter)
                .offset(y = -LocalConfiguration.current.screenHeightDp.dp * 0.1f)
        ) {
            items(videos.size) { index ->
                val video = videos[index]
                val painter = rememberImagePainter(data = video["thumbnail"])
                Log.d("Video",  video["thumbnail"].toString())
                Card(
                    modifier = Modifier
                        .fillMaxWidth()

                        .padding(8.dp)

                        .clip(RoundedCornerShape(12.dp)), // Set rounded corners for the Card
                    elevation = 8.dp,
                    backgroundColor = Color.Transparent
                ) {

                    Box(
                        modifier = Modifier
                            .fillMaxSize()

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
                    ) {
AsyncImage(
    model = ImageRequest.Builder(LocalContext.current)
        .data(video["thumbnail"]?.replace("/default.jpg", "/hqdefault.jpg")) // Replace with a higher quality thumbnail URL if applicable
        .crossfade(true)
        .build(),
    contentDescription = "Video Thumbnail",
    modifier = Modifier.fillMaxSize(),
    contentScale = ContentScale.Crop // Ensures the image covers the Card
)
                        Text(
                            text = video["title"].toString(),
                            style = MaterialTheme.typography.h6,
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .padding(8.dp),
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun getScreenWidthDp(): Int {
    val configuration = LocalConfiguration.current
    val screenWidthPx = configuration.screenWidthDp // Screen width in dp
    return screenWidthPx
}

@Composable
fun GuidelinesOverlay() {
    // Box with rounded corners and semi-transparent background
    Box(
        modifier = Modifier
            .padding(16.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xAA000000)) // Semi-transparent black
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Guidelines",
                color = Color.White,
                style = MaterialTheme.typography.h6
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Pentru accesarea tuturor optiunilor trebuie sa dai slide. Aicea poti observa videoclipurile educationale.",
                color = Color.White,
                style = MaterialTheme.typography.body2
            )
            Spacer(modifier = Modifier.height(16.dp))
            // Custom canvas for drawing the circle
            Canvas(modifier = Modifier.size(50.dp)) {
                val strokeWidth = 8f
                val radius = size.minDimension / 2 - strokeWidth / 2
                drawArc(
                    color = Color.Gray,
                    startAngle = -90f,
                    sweepAngle = 360f,
                    useCenter = false,
                    style = Stroke(width = strokeWidth)
                )
                drawArc(
                    color = Color.Green,
                    startAngle = -90f,
                    sweepAngle = 360f * 0.86f, // 86% filled
                    useCenter = false,
                    style = Stroke(width = strokeWidth)
                )
            }
        }
    }
}

