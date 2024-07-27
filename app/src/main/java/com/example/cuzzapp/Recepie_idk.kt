package com.example.cuzzapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.ViewGroup
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import coil.compose.rememberImagePainter
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.viewinterop.AndroidView
class Recepie_idk : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val title = intent.getStringExtra("title")
        val image = intent.getStringExtra("image")
        val ingredients = intent.getStringExtra("ingredients")
        val video = intent.getStringExtra("video")
        val instruction = intent.getStringExtra("instruction")
        val urlus = intent.getStringExtra("urlus")
        setContent {
            if (urlus != null) {
                var searchQuery by remember { mutableStateOf("") }
                val scaffoldState = rememberScaffoldState()

                Drawer(scaffoldState, searchQuery, backgroundColor = SolidColor(Color(0xFFffffff)) ,onSearchQueryChange = { searchQuery = it }) {

                    WebViewScreen(urlus)
                }
            }
        }
    }


    @Composable
    fun WebViewScreen(url: String) {
        AndroidView(
            factory = { context ->
                WebView(context).apply {
                    settings.javaScriptEnabled = true
                    webViewClient = WebViewClient()

                    settings.loadWithOverviewMode = true
                    settings.useWideViewPort = true
                    settings.setSupportZoom(true)
                }
            },
            update = { webView ->
                webView.loadUrl(url)
            }
        )
    }
}