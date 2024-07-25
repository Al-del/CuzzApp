package com.example.cuzzapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.compose.rememberImagePainter
import com.example.cuzzapp.ui.theme.CuzzAppTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.future.await
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import points
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.Callback
import retrofit2.Response
import seach_querr
import url_photo

class asis : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            var searchQuery by remember { mutableStateOf(seach_querr) }
            val scaffoldState = rememberScaffoldState()

            Drawer(scaffoldState, searchQuery, backgroundColor = SolidColor(Color.Black), show_Top_bar = false ,onSearchQueryChange = { searchQuery = it }) {

            final() // Adjusted to call without homeScreen parameter
            }
        }
    }

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @Composable
    fun ChatScreen() {
        var message by remember { mutableStateOf("") }
        val messages = remember { mutableStateListOf<String>() }
        messages.add("Cuza: Hello! How can I help you today?")
        Scaffold(
            modifier = Modifier.background(color = Color(0xffFFFFFF)),
            bottomBar = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .offset(y = -70.dp)
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
               TextField(
                value = message,
                onValueChange = { message = it },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(26.dp) // Adjust the corner radius as needed
            )
                    IconButton(
                        onClick = {
                            getChatGPTResponse(message) { response ->
                                messages.add("User: $message")
                                messages.add("Cuza: $response")
                                message = ""
                            }
                        },
                    ) {
                        Icon(imageVector = Icons.Default.Send, contentDescription = "Send Message")
                    }
                }
            }
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color(0xffFFFFFF))
            ) {
                items(messages.size) { index ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .offset(y = 40.dp),
                        horizontalArrangement = if (messages[index].startsWith("User:")) Arrangement.End else Arrangement.Start
                    ) {
                        Box(
                            modifier = Modifier
                                .wrapContentWidth()
                                .wrapContentSize(Alignment.Center)
                                .padding(10.dp)
                                .clip(shape = RoundedCornerShape(20.dp))
                                .background(color =  if (messages[index].startsWith("User:")) Color(0xFFEBEAEA) else Color(0xFF9747FF))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = messages[index],
                                color = if (messages[index].startsWith("User:")) Color.Black else Color.White,
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


        @POST("v1/chat/completions")
        fun getChatResponse(@Body request: ChatGPTRequest): Call<ChatGPTResponse>
    }


    object RetrofitClient {
        private const val BASE_URL = "https://api.openai.com/"

        // This replaces the direct use in annotations
        private fun getApiKey(): String {
            return Show_recepies.Keys.API_chat_gpt()
        }
        private val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(Interceptor { chain ->
                val original = chain.request()
                val requestBuilder = original.newBuilder()
                    .header("Authorization", "Bearer ${getApiKey()}")
                    .header("Content-Type", "application/json")
                val request = requestBuilder.build()
                chain.proceed(request)
            })
            .build()

        val instance: OpenAIApi by lazy {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            retrofit.create(OpenAIApi::class.java)
        }
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
    @Preview
    @Composable
    fun final(){
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Top bar with friend's name and profile picture
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .requiredHeight(80.dp)
                        .background(Color(0xFFf75E54))
                        .padding(8.dp)
                        .offset(y = 20.dp)
                ) {
                    AsyncImage(
                        model = R.drawable.cuzascan,
                        contentDescription = "Friend's Profile Picture",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                    )

                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "CuzzGenius",
                        color = Color.White,
                        style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    )
                }
                  ChatScreen()

            }

        }
    }

}