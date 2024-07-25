package com.example.cuzzapp

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
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.InputChip
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.em
import androidx.compose.ui.zIndex
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import points
import seach_querr
import url_photo
import username_for_all

class RankingScreen : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var searchQuery by remember { mutableStateOf(seach_querr) }
            val scaffoldState = rememberScaffoldState()

            Drawer(scaffoldState, searchQuery, backgroundColor = SolidColor(Color(0xFF6A5AE0)) ,onSearchQueryChange = { searchQuery = it }) {
            // Your content

             Box(modifier = Modifier.fillMaxSize().background(Color(0xFF6A5AE0))) {
                    PodiumPreview()
                 Box(
                     modifier = Modifier
                         .size(400.dp) // Set the size of the box
                         .background(Color(0xffeee6f0), shape = RoundedCornerShape(25.dp)) // Apply white background and rounded corners
                         .clip(RoundedCornerShape(25.dp)) // Clip the box to have rounded corners
                         .align(Alignment.BottomCenter) // Align the box to the bottom center of the screen
                         .zIndex(0f)
                 ) {
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
                     if (result != null && result.isSuccess) {

                         Column(
                         modifier = Modifier
                             .fillMaxSize()
                             .padding(20.dp)
                     ) {
                         Box(
                             modifier = Modifier
                                 .fillMaxWidth()
                                 .weight(1f)
                                 .verticalScroll(rememberScrollState())
                         ) {
                             Column(
                                 modifier = Modifier
                                     .fillMaxWidth()
                             ) {
                                 result.getOrNull()?.drop(3)?.forEachIndexed { index, user ->
                                     Box(
                                         modifier = Modifier
                                             .requiredWidth(width = 400.dp)
                                             .requiredHeight(height = 100.dp)
                                             .clip(shape = RoundedCornerShape(20.dp))
                                             .background(color = Color.White)
                                             .padding(16.dp)
                                             .clickable {
                                                    username_for_all = user.name
                                                    val intent = Intent(
                                                        this@RankingScreen,
                                                        Profile::class.java
                                                    )
                                                    intent.putExtra("userr_to", user.name)
                                                    intent.putExtra("img_link", user.photoUrl)

                                                    startActivity(intent)
                                             }
                                     ) {
                                         Row(
                                             modifier = Modifier.fillMaxSize(),
                                             verticalAlignment = Alignment.CenterVertically
                                         ) {
                                             AsyncImage(
                                                 model = user.photoUrl,
                                                 contentDescription = "Profile Picture",
                                                 contentScale = ContentScale.Crop,
                                                 modifier = Modifier
                                                     .size(75.dp)
                                                     .clip(CircleShape)
                                             )
                                             Spacer(modifier = Modifier.width(8.dp))
                                             Text(
                                                 text = "${index + 4}. ${user.name}",
                                                 modifier = Modifier.weight(1f),
                                                 style = TextStyle(
                                                     fontSize = 16.sp,
                                                     fontWeight = FontWeight.Bold
                                                 )
                                             )
                                             Text(
                                                 text = "${user.Points} QP",
                                                 style = TextStyle(fontSize = 16.sp)
                                             )
                                         }
                                     }
                                     Spacer(modifier = Modifier.height(8.dp))
                                 }
                             }
                         }

                     }
                 }
                 }
                }

              /*  Scaffold(
                    bottomBar = { StatusHomeModeDarkPreview() }
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        DisplayUsers()
                        TopScreenContent()
                    }
                }*/

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
                // Check if the user's role is "Student" before adding to the list
                if (user?.role == "Student") {
                    userList.add(user)
                }
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
                                            modifier = Modifier
                                                .clip(CircleShape)
                                                .clickable {
                                                    username_for_all = user.name
                                                    val intent = Intent(
                                                        this@RankingScreen,
                                                        Profile::class.java
                                                    )
                                                    intent.putExtra("userr_to", user.name)
                                                    intent.putExtra("img_link", user.photoUrl)

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


    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun Podium(modifier: Modifier = Modifier) {
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
        if (result != null && result.isSuccess) {

            Log.d("kilo", result.toString())
            for (user in result?.getOrNull()!!) {
                Log.i("kilo", "User: ${user.name}, Points: ${user.Points} ${user.photoUrl}")
            }
            Box(
                modifier = modifier
                    .requiredWidth(width = 318.dp)
                    .requiredHeight(height = 432.dp)
                    .offset(x = 40.dp)
            ) {
                Box(
                    modifier = Modifier
                        .align(alignment = Alignment.TopStart)
                        .offset(
                            x = 214.dp,
                            y = 88.dp
                        )
                        .requiredWidth(width = 104.dp)
                        .requiredHeight(height = 326.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .align(alignment = Alignment.TopStart)
                            .offset(
                                x = 0.dp,
                                y = 146.dp
                            )
                            .requiredWidth(width = 104.dp)
                            .requiredHeight(height = 180.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .requiredWidth(width = 104.dp)
                                .requiredHeight(height = 180.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .align(alignment = Alignment.TopStart)
                                    .offset(
                                        x = 0.dp,
                                        y = 16.dp
                                    )
                                    .requiredWidth(width = 104.dp)
                                    .requiredHeight(height = 164.dp)
                                    .background(color = Color(0xff9087e5))
                            )
                            Image(
                                painter = painterResource(id = R.drawable.rectangle94),
                                contentDescription = "Rectangle 94",
                                modifier = Modifier
                                    .requiredWidth(width = 104.dp)
                                    .requiredHeight(height = 16.dp)
                            )
                        }
                        Text(
                            text = "3",
                            color = Color.White,
                            textAlign = TextAlign.Center,
                            lineHeight = 2.33.em,
                            style = TextStyle(
                                fontSize = 60.sp
                            ),
                            modifier = Modifier
                                .align(alignment = Alignment.TopStart)
                                .offset(
                                    x = 32.dp,
                                    y = 16.dp
                                )
                                .wrapContentHeight(align = Alignment.CenterVertically)
                        )
                    }
                    Box(
                        modifier = Modifier
                            .align(alignment = Alignment.TopStart)
                            .offset(
                                x = 5.dp,
                                y = 0.dp
                            )
                            .requiredWidth(width = 93.dp)
                            .requiredHeight(height = 138.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .align(alignment = Alignment.Center)
                                .offset(
                                    x = 16.dp,
                                    y = 0.dp
                                )
                                .requiredSize(size = 60.dp)
                        ) {
                            // Property1MenProperty29(modifier = Modifier, img_url = "https://plus.unsplash.com/premium_photo-1664478383014-e8bc930be7c2?fm=jpg&q=60&w=3000&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MXx8cmFuZG9tJTIwcGVvcGxlfGVufDB8fDB8fHww")
                            AsyncImage(
                                model = result.getOrNull()!![2].photoUrl,
                                contentDescription = "YAY",
                                modifier = Modifier.clip(CircleShape)
                            )

                        }
                        Box(
                            modifier = Modifier
                                .align(alignment = Alignment.TopStart)
                                .offset(
                                    x = 0.dp,
                                    y = 76.dp
                                )
                                .requiredWidth(width = 93.dp)
                                .requiredHeight(height = 62.dp)
                        ) {
                            InputChip(
                                label = {
                                    Text(
                                        text = "${result.getOrNull()!![2].Points} QP",
                                        color = Color.White,
                                        lineHeight = 12.5.em,
                                        style = TextStyle(
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Medium
                                        )
                                    )
                                },
                                shape = RoundedCornerShape(12.dp),
                                colors = FilterChipDefaults.filterChipColors(
                                    containerColor = Color(0xff9087e5)
                                ),
                                selected = true,
                                onClick = { },
                                modifier = Modifier
                                    .align(alignment = Alignment.TopStart)
                                    .offset(
                                        x = 8.dp,
                                        y = 28.dp
                                    )
                            )

                        }
                    }
                }
                Box(
                    modifier = Modifier
                        .align(alignment = Alignment.TopStart)
                        .offset(
                            x = 0.dp,
                            y = 56.dp
                        )
                        .requiredWidth(width = 104.dp)
                        .requiredHeight(height = 326.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .align(alignment = Alignment.TopStart)
                            .offset(
                                x = 0.dp,
                                y = 146.dp
                            )
                            .requiredWidth(width = 104.dp)
                            .requiredHeight(height = 180.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .requiredWidth(width = 104.dp)
                                .requiredHeight(height = 180.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .align(alignment = Alignment.TopStart)
                                    .offset(
                                        x = 0.dp,
                                        y = 16.dp
                                    )
                                    .requiredWidth(width = 104.dp)
                                    .requiredHeight(height = 164.dp)
                                    .background(color = Color(0xff9087e5))
                            )
                            Image(
                                painter = painterResource(id = R.drawable.rectangle94),
                                contentDescription = "Rectangle 94",
                                modifier = Modifier
                                    .requiredWidth(width = 104.dp)
                                    .requiredHeight(height = 16.dp)
                            )
                        }
                        Text(
                            text = "2",
                            color = Color.White,
                            textAlign = TextAlign.Center,
                            lineHeight = 1.75.em,
                            style = TextStyle(
                                fontSize = 80.sp
                            ),
                            modifier = Modifier
                                .align(alignment = Alignment.TopStart)
                                .offset(
                                    x = 28.dp,
                                    y = 16.dp
                                )
                                .wrapContentHeight(align = Alignment.CenterVertically)
                        )
                    }
                    Box(
                        modifier = Modifier
                            .align(alignment = Alignment.TopStart)
                            .offset(
                                x = 6.dp,
                                y = 0.dp
                            )
                            .requiredWidth(width = 93.dp)
                            .requiredHeight(height = 138.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .align(alignment = Alignment.TopStart)
                                .offset(
                                    x = 0.dp,
                                    y = 76.dp
                                )
                                .requiredWidth(width = 93.dp)
                                .requiredHeight(height = 62.dp)
                        ) {

                            InputChip(
                                label = {
                                    Text(
                                        text = "${result.getOrNull()!![1].Points} QP",
                                        color = Color.White,
                                        textAlign = TextAlign.Center,
                                        lineHeight = 12.5.em,
                                        style = TextStyle(
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Medium
                                        )
                                    )
                                },
                                shape = RoundedCornerShape(12.dp),
                                colors = FilterChipDefaults.filterChipColors(
                                    containerColor = Color(0xff9087e5)
                                ),
                                selected = true,
                                onClick = { },
                                modifier = Modifier
                                    .align(alignment = Alignment.TopStart)
                                    .offset(
                                        x = 9.dp,
                                        y = 28.dp
                                    )
                            )
                        }
                        Box(
                            modifier = Modifier
                                .align(alignment = Alignment.Center)
                                .requiredSize(size = 60.dp)
                        ) {
                            AsyncImage(
                                model = result.getOrNull()!![1].photoUrl,
                                contentDescription = "YAY",
                                modifier = Modifier.clip(CircleShape)
                            )

                        }
                    }
                }
                Box(
                    modifier = Modifier
                        .align(alignment = Alignment.TopStart)
                        .offset(
                            x = 104.dp,
                            y = 0.dp
                        )
                        .requiredWidth(width = 110.dp)
                        .requiredHeight(height = 432.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .align(alignment = Alignment.TopStart)
                            .offset(
                                x = 0.dp,
                                y = 170.dp
                            )
                            .requiredWidth(width = 110.dp)
                            .requiredHeight(height = 262.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .requiredWidth(width = 110.dp)
                                .requiredHeight(height = 262.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .align(alignment = Alignment.TopStart)
                                    .offset(
                                        x = 0.dp,
                                        y = 16.dp
                                    )
                                    .requiredWidth(width = 110.dp)
                                    .requiredHeight(height = 246.dp)
                                    .background(
                                        brush = Brush.linearGradient(
                                            0f to Color(0xff9087e5),
                                            1f to Color(0xffcdc9f3),
                                            start = Offset(55f, 0f),
                                            end = Offset(51.06f, 173f)
                                        )
                                    )
                            )
                            Image(
                                painter = painterResource(id = R.drawable.rectangle94),
                                contentDescription = "Rectangle 94",
                                modifier = Modifier
                                    .requiredWidth(width = 110.dp)
                                    .requiredHeight(height = 16.dp)
                            )
                        }
                        Text(
                            text = "1",
                            color = Color.White,
                            textAlign = TextAlign.Center,
                            lineHeight = 1.4.em,
                            style = TextStyle(
                                fontSize = 100.sp
                            ),
                            modifier = Modifier
                                .align(alignment = Alignment.TopStart)
                                .offset(
                                    x = 33.dp,
                                    y = 16.dp
                                )
                                .wrapContentHeight(align = Alignment.CenterVertically)
                        )
                    }
                    Box(
                        modifier = Modifier
                            .align(alignment = Alignment.TopStart)
                            .offset(
                                x = 8.dp,
                                y = 0.dp
                            )
                            .requiredWidth(width = 94.dp)
                            .requiredHeight(height = 162.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .align(alignment = Alignment.TopStart)
                                .offset(
                                    x = 0.dp,
                                    y = 100.dp
                                )
                                .requiredWidth(width = 94.dp)
                                .requiredHeight(height = 62.dp)
                        ) {
                            Text(
                                text = result.getOrNull()!![0].name,
                                color = Color.White,
                                textAlign = TextAlign.Center,
                                lineHeight = 9.38.em,
                                style = TextStyle(
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            )
                            InputChip(
                                label = {
                                    Text(
                                        text = "${result.getOrNull()!![0].Points} QP",
                                        color = Color.White,
                                        textAlign = TextAlign.Center,
                                        lineHeight = 12.5.em,
                                        style = TextStyle(
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Medium
                                        )
                                    )
                                },
                                shape = RoundedCornerShape(12.dp),
                                colors = FilterChipDefaults.filterChipColors(
                                    containerColor = Color(0xff9087e5)
                                ),
                                selected = true,
                                onClick = { },
                                modifier = Modifier
                                    .align(alignment = Alignment.TopStart)
                                    .offset(
                                        x = 8.dp,
                                        y = 28.dp
                                    )
                            )
                        }
                        // Property1MenProperty29(modifier = Modifier,img_url = "https://plus.unsplash.com/premium_photo-1664478383014-e8bc930be7c2?fm=jpg&q=60&w=3000&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MXx8cmFuZG9tJTIwcGVvcGxlfGVufDB8fDB8fHww")
                        val img_urls: String = result.getOrNull()!![0].photoUrl
                        Log.d("kilo", img_urls)
                        AsyncImage(
                            model = img_urls,
                            contentDescription = "YAY",
                            modifier = Modifier.clip(CircleShape)
                                .size(100.dp)
                        )


                        // Property1Gold()
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

        }else{
            Box(
                modifier = modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Loading...")
            }
        }
    }




        @Preview(widthDp = 318, heightDp = 432)
        @Composable
        fun PodiumPreview() {
            Podium(Modifier)
        }

}
