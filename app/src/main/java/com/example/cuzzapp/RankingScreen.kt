package com.example.cuzzapp

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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.Divider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.google.firebase.database.*
import kotlinx.coroutines.async
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.rememberNavController
import points
import url_photo
import username_for_all

class RankingScreen : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val scaffoldState = rememberScaffoldState()
            Drawer(scaffoldState = scaffoldState) {
                // Your content
                Scaffold(
                    bottomBar = { BottomNavigationBar() }
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        DisplayUsers()
                        TopScreenContent()
                    }
                }
            }

        }
    }


    suspend fun fetchAndSortUsers(): Result<List<User>> = suspendCoroutine { continuation ->
        val userList = mutableListOf<User>()

        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("accounts")

        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                userList.clear()
                for (userSnapshot in dataSnapshot.children) {
                    val user = userSnapshot.getValue(User::class.java)
                    user?.let { userList.add(it) }
                }

                userList.sortByDescending { it.Points }

                for (user in userList) {
                    Log.i("User", "User: ${user.name}, Points: ${user.Points}")
                }

                continuation.resume(Result.success(userList))
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("TAG__)", "Failed to read value.", error.toException())
                continuation.resume(Result.failure(Exception("Failed to fetch data")))
            }
        })
    }


    @Composable
    fun DisplayUsers() {
        // Create a coroutine scope
        val coroutineScope = rememberCoroutineScope()

        // Create a state to hold the result
        val resultState = remember { mutableStateOf<Result<List<User>>?>(null) }

        // Start the coroutine when the composable is first launched
        LaunchedEffect(Unit) {
            resultState.value = coroutineScope.async { fetchAndSortUsers() }.await()
        }

        // Use the result
        val result = resultState.value

        if (result != null) {
            if (result.isSuccess) {
                val users = result.getOrNull()
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFF1C1C1C))
                        .offset(y = 105.dp)
                        .fillMaxSize()
                ) { // Ensure the Box takes up all available space

                    users?.let {
                        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                            for (user in it) {
                                // Display a divider
                                Divider(
                                    color = Color.Gray,
                                    modifier = Modifier.requiredWidth(width = 355.dp)
                                )

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 16.dp, start = 16.dp, end = 16.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    // Display user icon
                                    Box(
                                        modifier = Modifier.size(50.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Image(
                                            painter = rememberImagePainter(user.photoUrl),
                                            contentDescription = "User Icon",
                                            modifier = Modifier.clip(CircleShape)
                                                .clickable {
                                                    username_for_all = user.name
                                                    val intent = Intent(this@RankingScreen, Profile::class.java)
                                                    startActivity(intent)
                                                }
                                        )
                                    }

                                    // Display user name
                                    Text(
                                        text = user.name,
                                        color = Color(0xFFCF0A0A),
                                        fontSize = 20.sp,
                                        textAlign = TextAlign.Center,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.weight(1f)
                                    )

                                    // Display user points
                                    Text(
                                        text = user.Points.toString(),
                                        color = Color(0xFFCF0A0A),
                                        fontSize = 25.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                // Display a divider
                                Divider(
                                    color = Color.Gray,
                                    modifier = Modifier.requiredWidth(width = 400.dp)
                                )
                            }
                        }
                    }
                }
            } else {
                // Handle the error
                Text(text = "Failed to fetch users")
            }
        }
    }


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



@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MyDrawer_2(scaffoldState: ScaffoldState) {
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

                Button(
                    onClick = {
val intent = Intent(context, asis::class.java)
startActivity(intent)

                    },
                    shape = RoundedCornerShape(80.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xff5d5d5d)),
                    modifier = Modifier
                        .requiredWidth(width = 170.dp)
                        .requiredHeight(height = 40.dp)
                ) {
                    Text("Asistenta")
                }
                Spacer(modifier = Modifier.height(24.dp)) // Add bigger space
                Button(
                    onClick = { },
                    shape = RoundedCornerShape(80.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xff5d5d5d)),
                    modifier = Modifier
                        .requiredWidth(width = 170.dp)
                        .requiredHeight(height = 40.dp)
                ) {
                    Text("Ranking")
                }
                Spacer(modifier = Modifier.height(24.dp)) // Add bigger space
                Button(
                    onClick = { },
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
                    onClick = { },
                    shape = RoundedCornerShape(80.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xff5d5d5d)),
                    modifier = Modifier
                        .requiredWidth(width = 170.dp)
                        .requiredHeight(height = 40.dp)
                ) {
                    Text("Profile")
                }

            }
        },
        content = {

        },
        bottomBar = { AppNavigator(navController) } // Pass the NavController to AppNavigator

    )
}
}