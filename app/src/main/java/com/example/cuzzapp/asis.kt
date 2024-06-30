package com.example.cuzzapp

import Drawer_final
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberImagePainter
import com.example.cuzzapp.ui.theme.CuzzAppTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import points
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.Callback
import retrofit2.Response
import url_photo

class asis : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val scaf_state = rememberScaffoldState()
            Drawer_final(scaf_state, {final()})
        }
    }

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @Composable
    fun ChatScreen() {
        var message by remember { mutableStateOf("") }
        val messages = remember { mutableStateListOf<String>() }
        messages.add("Cuza: Hello! How can I help you today?")
        Scaffold(
            modifier = Modifier.background(color = Color(0xff000000)),
            bottomBar = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    TextField(
                        value = message,
                        onValueChange = { message = it },
                        modifier = Modifier.weight(1f)
                    )
                    Button(
                        onClick = {
                            getChatGPTResponse(message) { response ->
                                messages.add("User: $message")
                                messages.add("Cuza: $response")
                                message = ""
                            }
                        }
                    ) {
                        Text(text = "Send")
                    }
                }
            }
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color(0xff000000))
            ) {
                items(messages.size) { index ->
                    Row(
                        modifier = Modifier.fillMaxWidth()  .offset(y = 40.dp),
                        horizontalArrangement = if (messages[index].startsWith("User:")) Arrangement.End else Arrangement.Start
                    ) {
                        Box(
                            modifier = Modifier
                                .wrapContentWidth()
                                .wrapContentSize(Alignment.Center)
                                .padding(10.dp)
                                .clip(shape = RoundedCornerShape(20.dp))
                                .background(color = Color(0xff9d9d9d))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = messages[index],
                                color = if (messages[index].startsWith("User:")) Color(0xffDC5F00) else Color(
                                    0xffCF0A0A
                                ),
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                    }
                }
            }
        }
    }


    data class ChatGPTRequest(
        val model: String,
        val messages: List<Message>
    )

    data class Message(
        val role: String,
        val content: String
    )

    data class ChatGPTResponse(
        val id: String,
        val choices: List<Choice>
    )

    data class Choice(
        val index: Int,
        val message: Message
    )


    interface OpenAIApi {
        @Headers(
            "Content-Type: application/json",
            "Authorization: Bearer sk-proj-VMQAv8g6fquZjMD1U0y8T3BlbkFJ5FSxP3Yi3XNcGzUrrgBX"
        )
        @POST("v1/chat/completions")
        fun getChatResponse(@Body request: ChatGPTRequest): Call<ChatGPTResponse>
    }

    object RetrofitClient {
        private const val BASE_URL = "https://api.openai.com/"

        val instance: OpenAIApi by lazy {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            retrofit.create(OpenAIApi::class.java)
        }
    }


    @Composable
    fun ChatGPTComposable(question: String) {
        var responseText by remember { mutableStateOf("Awaiting response...") }

        LaunchedEffect(question) {
            withContext(Dispatchers.IO) {
                getChatGPTResponse(question) { response ->
                    responseText = response
                }
            }
        }

        Text(responseText)
    }

    fun getChatGPTResponse(question: String, callback: (String) -> Unit) {
        val message = Message(role = "user", content = question)
        val request = ChatGPTRequest(model = "gpt-3.5-turbo", messages = listOf(message))

        val call = RetrofitClient.instance.getChatResponse(request)
        call.enqueue(object : Callback<ChatGPTResponse> {
            override fun onResponse(
                call: Call<ChatGPTResponse>,
                response: Response<ChatGPTResponse>
            ) {
                if (response.isSuccessful) {
                    val chatResponse = response.body()
                    if (chatResponse != null && chatResponse.choices.isNotEmpty()) {
                        callback(chatResponse.choices[0].message.content)
                    } else {
                        callback("No response received from ChatGPT.")
                    }
                } else {
                    callback("Failed to get response: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<ChatGPTResponse>, t: Throwable) {
                callback("Request failed: ${t.message}")
            }
        })
    }

    @SuppressLint(
        "UnusedMaterialScaffoldPaddingParameter",
        "UnusedMaterial3ScaffoldPaddingParameter"
    )
    @Composable
    fun TopScreenContent() {
        var searchQuery by remember { mutableStateOf("") }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .offset(y = 20.dp),
            contentAlignment = Alignment.BottomCenter,

            ) {
            Box(modifier = Modifier.offset(x = -65.dp, y = -805.dp)) {
                Rectangle2(searchQuery, { newQuery -> searchQuery = newQuery })
            }
            LoadImageFromUrl(url = url_photo, points = points)

        }
    }
    @Composable
    fun final(){
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {

                ChatScreen()

        }
    }
    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    @Composable
    fun MyDrawer_3(scaffoldState: ScaffoldState) {
        val coroutineScope = rememberCoroutineScope()
        val navController = rememberNavController() // Create a NavController
        val context = LocalContext.current // Get the local context to use startActivity
        androidx.compose.material.Scaffold(
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

                    androidx.compose.material.Button(
                        onClick = {
                            val intent = Intent(context, Shop::class.java)
                            context.startActivity(intent)

                        },
                        shape = RoundedCornerShape(80.dp),
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xff5d5d5d)),
                        modifier = Modifier
                            .requiredWidth(width = 170.dp)
                            .requiredHeight(height = 40.dp)
                            .clickable {
                                val intent = Intent(context, asis::class.java)
                                context.startActivity(intent)
                            }
                    ) {
                        Text("Shop")
                    }
                    Spacer(modifier = Modifier.height(24.dp)) // Add bigger space
                    androidx.compose.material.Button(
                        onClick = { },
                        shape = RoundedCornerShape(80.dp),
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xff5d5d5d)),
                        modifier = Modifier
                            .requiredWidth(width = 170.dp)
                            .requiredHeight(height = 40.dp)
                            .clickable {
                                val intent = Intent(context, RankingScreen::class.java)
                                context.startActivity(intent)
                            }
                    ) {
                        Text("Ranking")
                    }
                    Spacer(modifier = Modifier.height(24.dp)) // Add bigger space
                    androidx.compose.material.Button(
                        onClick = {

                            val inent = Intent(context, HomeScreen::class.java)
                            context.startActivity(inent)
                        },
                        shape = RoundedCornerShape(80.dp),
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xff5d5d5d)),
                        modifier = Modifier
                            .requiredWidth(width = 170.dp)
                            .requiredHeight(height = 40.dp)
                            .clickable {
                                val intent = Intent(context, HomeScreen::class.java)
                                context.startActivity(intent)
                            }
                    ) {
                        Text("Home")
                    }
                    Spacer(modifier = Modifier.height(24.dp)) // Add bigger space
                    androidx.compose.material.Button(
                        onClick = { },
                        shape = RoundedCornerShape(80.dp),
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xff5d5d5d)),
                        modifier = Modifier
                            .requiredWidth(width = 170.dp)
                            .requiredHeight(height = 40.dp)
                            .clickable {
                                val intent = Intent(context, Profile::class.java)
                                context.startActivity(intent)
                            }
                    ) {
                        Text("Profile")
                    }

                }
            },
            content = {




},

            )
    }
}
