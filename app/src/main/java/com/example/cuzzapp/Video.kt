package com.example.cuzzapp

import YouTubePlayer_ok
import android.annotation.SuppressLint
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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.MimeTypes
import androidx.media3.exoplayer.analytics.AnalyticsListener
import androidx.navigation.compose.rememberNavController
import com.example.cuzzapp.ui.theme.CuzzAppTheme
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import io.sanghun.compose.video.RepeatMode
import io.sanghun.compose.video.VideoPlayer
import io.sanghun.compose.video.controller.VideoPlayerControllerConfig
import io.sanghun.compose.video.uri.VideoPlayerMediaItem

import kotlinx.coroutines.delay
import video_l
import java.util.concurrent.TimeUnit

class Video : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val scaffoldState = rememberScaffoldState()
            Drawer(scaffoldState = scaffoldState) {

                val navController = rememberNavController() // Create a NavController

                Scaffold(
                    bottomBar = { BottomNavigationBar() }
                ) {
                    Box(modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black)) {

                        println(video_l.url)

                        YouTubePlayer_ok(video_l.id, lifecycleOwner = LocalLifecycleOwner.current)
                        Title_showustats()
                        ShowDescription()
                    }
                }
            }

            }

    }
}

@Composable
fun VideoPlayer_(url: String) {

    VideoPlayer(
        mediaItems = listOf(

            VideoPlayerMediaItem.NetworkMediaItem(
                url =url,
                mediaMetadata = MediaMetadata.Builder().setTitle("Widevine DASH cbcs: Tears").build(),
                mimeType = MimeTypes.APPLICATION_MPD,
                drmConfiguration = MediaItem.DrmConfiguration.Builder(C.WIDEVINE_UUID)
                    .setLicenseUri("https://proxy.uat.widevine.com/proxy?provider=widevine_test")
                    .build(),
            )
        ),
        handleLifecycle = true,
        autoPlay = true,
        usePlayerController = true,
        enablePip = true,
        handleAudioFocus = true,
        controllerConfig = VideoPlayerControllerConfig(
            showSpeedAndPitchOverlay = false,
            showSubtitleButton = false,
            showCurrentTimeAndTotalTime = true,
            showBufferingProgress = false,
            showForwardIncrementButton = true,
            showBackwardIncrementButton = true,
            showBackTrackButton = true,
            showNextTrackButton = true,
            showRepeatModeButton = true,
            controllerShowTimeMilliSeconds = 5_000,
            controllerAutoShow = true,
            showFullScreenButton = true,
        ),
        volume = 0.5f,  // volume 0.0f to 1.0f
        repeatMode = RepeatMode.NONE,       // or RepeatMode.ALL, RepeatMode.ONE
        onCurrentTimeChanged = { // long type, current player time (millisec)
            Log.e("CurrentTime", it.toString())
        },
        playerInstance = { // ExoPlayer instance (Experimental)
            addAnalyticsListener(
                object : AnalyticsListener {
                    // player logger
                }
            )
        },
        modifier = Modifier
            .fillMaxSize()
            .offset(x = 0.dp, y = 340.dp)
    )
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