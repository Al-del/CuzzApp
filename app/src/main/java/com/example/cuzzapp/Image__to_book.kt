package com.example.cuzzapp

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.core.content.FileProvider
import coil.compose.rememberImagePainter
import com.example.cuzzapp.components.MyImageArea

import com.example.cuzzapp.ui.theme.CuzzAppTheme
import com.example.cuzzapp.ui.theme.Pink
import org.osmdroid.library.BuildConfig
import java.io.File
import java.util.Date
import java.util.Locale
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.jerry.jetpack_take_select_photo_image_2.viewmodel.MainViewModel
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Call
import okhttp3.Callback
import okhttp3.HttpUrl
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.Response
import okio.IOException
import username_true
import java.io.ByteArrayOutputStream
import okhttp3.RequestBody.Companion.toRequestBody

import okhttp3.*
import seach_querr
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class book_class{
    var url:String = ""
    var user:String = ""
}
data class Book(
    val Title: String,
    val Mirror_1: String,
    val Extension: String,
    val Pages: String
)

class Image__to_book : ComponentActivity() {
    private lateinit var takePictureLauncher: ActivityResultLauncher<Intent>
    private val imageBitmap = mutableStateOf<Bitmap?>(null)
    val userr = book_class()
    val mdl = MainViewModel()
    var mesaj: String? = null
    var books :List<Book> = emptyList()
    val mesajState = mutableStateOf<String?>(null)
    //Create a new variable as a mutable stae bool isGot
    var isGot = mutableStateOf(false)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        takePictureLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    imageBitmap.value = result.data?.extras?.get("data") as? Bitmap
                }
            }

        setContent {
            val scaffoldState = rememberScaffoldState()
            var searchQuery by remember { mutableStateOf(seach_querr) }
            var isvisible by remember { mutableStateOf(true) }

            Drawer(scaffoldState, searchQuery, backgroundColor =  SolidColor(Color(0xFFac5288)),onSearchQueryChange = { searchQuery = it }) {
            val context = LocalContext.current
                Scaffold(modifier = Modifier.offset(y = -35.dp)){ paddingValues ->
                    Box (
                        modifier = Modifier
                            .padding(paddingValues)
                            .fillMaxSize()

                            .background(
                                brush = Brush.verticalGradient(
                                    colors = listOf(Color(0xFFac5288), Color(0xFF3c1053)), // Replace with your desired colors
                                    startY = 0f, // Gradient start
                                    endY = 1000f // Gradient end
                                )
                            )


                    ){

                        // Top left box
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .fillMaxWidth(0.6f)
                                .fillMaxHeight(0.4f)
                                .clip(RoundedCornerShape(topStart = 0.dp, bottomEnd = 0.dp, topEnd = 150.dp, bottomStart = 0.dp))
                                .background(
                                    brush = Brush.verticalGradient(
                                        colors = listOf(Color(0xFF8b3f79), Color(0xFF8b3f79), Color(0xFF3c1053)), // Replace with your desired colors
                                        startY = 0f, // Gradient start
                                        endY = 1000f // Gradient end
                                    )
                                )

                        )
                        {
                            Image(
                                painter = painterResource(id = R.drawable.om3),
                                contentDescription = "human",
                                modifier = Modifier.align(Alignment.Center).offset(y=-25.dp).scale(0.8f)
                            )
                            Box(
                                modifier = Modifier
                                    .align(Alignment.BottomEnd)
                                    .fillMaxWidth(1f)
                                    .fillMaxHeight(0.35f)
                                    .clip(RoundedCornerShape(topStart = 200.dp, topEnd = 0.dp, bottomEnd = 0.dp, bottomStart = 0.dp))
                                    .background(Color(0xFFCB3066))
                            )
                        }

                        // Bottom right box
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .fillMaxWidth(0.7f)
                                .fillMaxHeight(0.6f)
                                .clip(RoundedCornerShape(bottomStart = 250.dp, topEnd = 0.dp, bottomEnd = 0.dp, topStart = 0.dp))


                                .background(
                                    brush = Brush.verticalGradient(
                                        colors = listOf(Color(0xFFac5288), Color(0xFF7c5c8c),Color(0xFF7c5c8c)), // Replace with your desired colors
                                        startY = 0f, // Gradient start
                                        endY = 1000f // Gradient end
                                    )
                                )
                        )

                        {
                            Column(
                                modifier = Modifier.fillMaxSize().offset(x=52.dp)//.padding(end = 20.dp)
                            ) {
                                val uri = remember { mutableStateOf<Uri?>(null) }

                                //image to show bottom sheet
                                if(isvisible==true) {
                                    MyImageArea(
                                        directory = File(cacheDir, "images"),
                                        uri = uri.value,
                                        onSetUri = {
                                            uri.value = it
                                        },
                                        upload = { selectedUri ->
                                            mdl.setContext(context) // Initialize context
                                            CoroutineScope(Dispatchers.IO).launch {
                                                val response = mdl.upload(selectedUri)
                                                mesaj = response
                                                mesajState.value = response
                                                Log.d("img", "onCreate: $response")
                                            }
                                        }

                                    )
                                }
                                // TextField to display the value of mesaj
                                if (mesajState.value != null) {
                                    isvisible = false
                                    Spacer(modifier = Modifier.height(25.dp))
                                    TextField(
                                        value = mesajState.value ?: "",
                                        onValueChange = {mesajState.value = it},
                                        label = { Text("Response") },
                                        modifier = Modifier.align(Alignment.CenterHorizontally)
                                            .clip(RoundedCornerShape(16.dp))
                                            .alpha(0.25f).padding(end=20.dp)

                                    )
                                    // Button click handler
                                    Button(
                                        modifier = Modifier
                                            .align(Alignment.CenterHorizontally)
                                            ,
                                        onClick = {
                                            CoroutineScope(Dispatchers.IO).launch {
                                                books = searchBooks(mesajState.value ?: "")
                                                // Update the UI to display the list of books
                                                // Replace with the actual code to update the UI
                                                Log.d("img", "onCreate: $books")
                                                isGot.value = true
                                            }
                                        }
                                    ) {
                                        Text(text = "Search for book")
                                    }
                                    if(isGot.value){
                                        BookList(books)
                                    }
                                }
                            }
                        }





                    }
                }

            }

        }
    }
suspend fun searchBooks(query: String): List<Book> {
    val client = OkHttpClient()

    // Replace with your server's URL
    val url = HttpUrl.Builder()
        .scheme("https")
        .host("reliably-expert-mammoth.ngrok-free.app")
        .addPathSegment("search")
        .addQueryParameter("q", query)
        .build()

    val request = Request.Builder()
        .url(url)
        .build()

    return suspendCoroutine { continuation ->
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                continuation.resumeWithException(e)
            }

            override fun onResponse(call: Call, response: Response) {
                if (!response.isSuccessful) {
                    continuation.resumeWithException(IOException("Unexpected code $response"))
                } else {
                    // Get the JSON response
                    val json = response.body?.string()

                    // Initialize Moshi
                    val moshi = Moshi.Builder()
                        .add(KotlinJsonAdapterFactory())
                        .build()

                    // Define the list type
                    val listType = Types.newParameterizedType(List::class.java, Book::class.java)

                    // Initialize the adapter
                    val adapter: JsonAdapter<List<Book>> = moshi.adapter(listType)

                    // Parse the JSON response
                    var books = adapter.fromJson(json)

                    // Filter out books that do not have a Mirror link
                    books = books?.filter { it.Mirror_1.isNotEmpty() } ?: emptyList()

                    // Return the list of books
                    continuation.resume(books)
                }
            }
        })
    }
}
@Composable
fun BookList(books: List<Book>) {
    val context = LocalContext.current
    LazyColumn(modifier =Modifier.offset(x=-50.dp)) {
        items(books) { book ->
            BookItem(book = book) {
                // Handle book click
                val openURL = Intent(Intent.ACTION_VIEW)
                openURL.data = Uri.parse(it.Mirror_1)
                context.startActivity(openURL)
            }
        }
    }
}

@Composable
fun BookItem(book: Book, onBookClick: (Book) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onBookClick(book) }
            .padding(20.dp)
            .alpha(0.2f)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = book.Title, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Text(text = "Pages: ${book.Pages}", maxLines = 1, overflow = TextOverflow.Ellipsis)
                Text(text = "Extension: ${book.Extension}", maxLines = 1, overflow = TextOverflow.Ellipsis)
            }
        }
    }
}
}








/*
   fun uploadImage(imageUri: Uri, serverUrl: String, context: Context) {
    val client = OkHttpClient()

    // Convert Uri to File
    val file = File(imageUri.path!!)
Log.d("img", "uploadImage: $file")
    // Create RequestBody from File
    val requestBody = MultipartBody.Builder()
        .setType(MultipartBody.FORM)
        .addFormDataPart("file", "${username_true}.jpg",
            file.asRequestBody("image/jpg".toMediaTypeOrNull()))
        .build()

    val request = Request.Builder()
        .url(serverUrl)
        .post(requestBody)
        .build()

    client.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            e.printStackTrace()
        }

        override fun onResponse(call: Call, response: Response) {
            if (!response.isSuccessful) {
                throw IOException("Unexpected code $response")
            }

            // Log the response
            Log.d("Server Response", "Response from server: ${response.body?.string()}")
        }
    })
}

 */