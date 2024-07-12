package com.example.cuzzapp

import YouTubePlayer_ok
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.Card
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat.startActivity
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.MimeTypes
import androidx.media3.exoplayer.analytics.AnalyticsListener
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import com.example.cuzzapp.ui.theme.CuzzAppTheme
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import io.sanghun.compose.video.RepeatMode
import io.sanghun.compose.video.VideoPlayer
import io.sanghun.compose.video.controller.VideoPlayerControllerConfig
import io.sanghun.compose.video.uri.VideoPlayerMediaItem

import kotlinx.coroutines.delay
import seach_querr
import video_l
import java.util.concurrent.TimeUnit

class Video : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var searchQuery by remember { mutableStateOf(seach_querr) }
            val scaffoldState = rememberScaffoldState()

            Drawer(scaffoldState, searchQuery, onSearchQueryChange = { searchQuery = it }) {

            val navController = rememberNavController() // Create a NavController

                Scaffold(
                    bottomBar = { StatusHomeModeDarkPreview() }
                ) {
                    Box(modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                            colors = listOf(Color(0xFF345E2A), Color(0xFF403182))
                        ))) {

                        println(video_l.url)
                        Box(modifier = Modifier.fillMaxSize()) {
                            // Align YouTubePlayer_ok to the top center of the Box
                            YouTubePlayer_ok(video_l.id, lifecycleOwner = LocalLifecycleOwner.current, modifier = Modifier.align(Alignment.TopCenter))
                        }
                        Title_showustats()
                        ShowDescription()
                        DisplayVideos_for_Videos(seach_querr)
                    }
                }
            }

            }

    }
}

@Composable
fun DataAnalysisHowMuchSTATISTICSDoYouNeedToKnow(modifier: Modifier = Modifier) {
    Text(
        text = video_l.title,
        color = Color.White,
        textAlign = TextAlign.Center,

        style = TextStyle(
            fontSize = 17.sp),
        modifier = modifier
            .offset(x = 20.dp, y = 290.dp)
            .requiredWidth(width = 360.dp)
            .requiredHeight(height = 65.dp))
}

@Preview(widthDp = 360, heightDp = 65)
@Composable
private fun Title_showustats() {
    DataAnalysisHowMuchSTATISTICSDoYouNeedToKnow(Modifier)
}
@Composable
fun ShowDescription(modifier: Modifier = Modifier) {
    Text(
        text = video_l.description,
        color = Color.White,
        textAlign = TextAlign.Center,
        style = TextStyle(fontSize = 15.sp),
        modifier = modifier
            .offset(x = 20.dp, y = 340.dp)
            .requiredWidth(width = 360.dp)
            .requiredHeight(height = 65.dp)
    )
}
@Composable
fun DisplayVideos_for_Videos(searchQuery: String) {
    var videos by remember { mutableStateOf(emptyList<Map<String, String>>()) }
    val context = LocalContext.current
    LaunchedEffect(searchQuery) {
        videos = getEducationalVideos(searchQuery, 15)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyRow( // Changed from LazyColumn to LazyRow for horizontal scrolling
            modifier = Modifier
                .fillMaxWidth() // Ensure it fills the maximum width
                .height(200.dp) // Set a specific height for the row
                .align(Alignment.BottomStart) // Center the LazyRow within the Box
                .offset(y = -100.dp)
                .padding(horizontal = 16.dp) // Add some horizontal padding
        ) {
            items(videos.size) { index ->
                val video = videos[index]
                val painter = rememberImagePainter(data = video["thumbnail"])
                Log.d("Video",  video["thumbnail"].toString())
                androidx.compose.material.Card(
                    modifier = Modifier
                        .requiredWidth(300.dp)

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
                                val intent = Intent(context, Video::class.java)
                                startActivity(context, intent, null)
                            }
                    ) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(
                                    video["thumbnail"]?.replace(
                                        "/default.jpg",
                                        "/hqdefault.jpg"
                                    )
                                ) // Replace with a higher quality thumbnail URL if applicable
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