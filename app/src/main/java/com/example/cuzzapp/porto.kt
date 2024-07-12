package com.example.cuzzapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import coil.compose.rememberImagePainter
import com.example.cuzzapp.ui.theme.CuzzAppTheme
import com.google.firebase.database.FirebaseDatabase
import descriptiones
import kotlinx.coroutines.tasks.await
import points
import seach_querr
import url_photo
import username_true

class porto : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            var searchQuery by remember { mutableStateOf(seach_querr) }
            val scaffoldState = rememberScaffoldState()

            Drawer(scaffoldState, searchQuery, onSearchQueryChange = { searchQuery = it }) {
            val context = LocalContext.current // Get the local context to use startActivity

                Scaffold(

                    modifier = Modifier.fillMaxSize(),
                    floatingActionButton = {
                        FloatingActionButton(onClick = {
                            val intent = Intent(context, adaugare::class.java)
                            ContextCompat.startActivity(context, intent, null)
                        }) {
                            Icon(Icons.Filled.Add, contentDescription = "Add")
                        }
                    },
                    floatingActionButtonPosition = FabPosition.End, // Position the FAB at the end (right side for LTR, left side for RTL)

                ) { innerPadding ->
                    Column(modifier = Modifier.fillMaxSize().background(Color.White).padding(innerPadding))
                    {

                        Box(
                            modifier = Modifier
                                .fillMaxWidth().background(Color.Blue)
                        )
                        {

                            Row(modifier = Modifier.fillMaxWidth()) {
                                Image(
                                    painter = rememberImagePainter(url_photo),
                                    contentDescription = "Profile photo",
                                    modifier = Modifier
                                        .padding(16.dp)
                                        .size(100.dp)
                                        .clip(CircleShape)
                                )
                                androidx.compose.material.Text(text = username_true, color = Color.Black)
                            }

                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Column(modifier = Modifier.fillMaxSize().background(Color.White),
                            horizontalAlignment = Alignment.CenterHorizontally,)
                        {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                            ) {
                                Spacer(modifier = Modifier.width(7.dp))

                                Box(Modifier.width(175.dp).background(Color.Black).height((100.dp)))
                                {
                                    Column(modifier = Modifier.fillMaxSize().padding(16.dp))
                                    {
                                        androidx.compose.material.Text("Username: $username_true")
                                        androidx.compose.material.Text("Description: $descriptiones")
                                        androidx.compose.material.Text("Points: $points")
                                    }

                                }
                                Spacer(modifier = Modifier.width(16.dp))
                                Box(Modifier.width(175.dp).background(Color.Black).height((100.dp)))
                                {
                                    Column(modifier = Modifier.fillMaxSize().padding(16.dp))
                                    {
                                        androidx.compose.material.Text("Username: $username_true")
                                        androidx.compose.material.Text("Description: $descriptiones")
                                        androidx.compose.material.Text("Points: $points")
                                    }

                                }

                            }
                            Spacer(modifier = Modifier.height(16.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                            ) {
                                Spacer(modifier = Modifier.width(7.dp))
                                Box(Modifier.width(175.dp).background(Color.Black).height((100.dp)))
                                {
                                    Column(modifier = Modifier.fillMaxSize().padding(16.dp))
                                    {
                                        androidx.compose.material.Text("Username: $username_true")
                                        androidx.compose.material.Text("Description: $descriptiones")
                                        androidx.compose.material.Text("Points: $points")
                                    }

                                }
                                Spacer(modifier = Modifier.width(16.dp))
                                Box(Modifier.width(175.dp).background(Color.Black).height((100.dp)))
                                {
                                    Column(modifier = Modifier.fillMaxSize().padding(16.dp))
                                    {
                                        androidx.compose.material.Text("Username: $username_true")
                                        androidx.compose.material.Text("Description: $descriptiones")
                                        androidx.compose.material.Text("Points: $points")
                                    }

                                }

                            }
                        }



                    }
                }

            }

        }
    }
}


@Composable
fun ProfileInfo(username: String) {
    var photoUrl by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var usernameState by remember { mutableStateOf(username) }
    val painter = rememberImagePainter(photoUrl)
    LaunchedEffect(key1 = username) {
        val (fetchedPhotoUrl, fetchedDescription, fetchedUsername) = fetchUserData(username)
        photoUrl = fetchedPhotoUrl
        description = fetchedDescription
        usernameState = fetchedUsername
    }
    Row(modifier= Modifier.fillMaxSize())
    {
        Image(
            painter = painter,
            contentDescription = "Profile photo",
            modifier = Modifier
                .padding(16.dp)
                .size(100.dp)
                .clip(CircleShape)
        )
        androidx.compose.material.Text("Username: $usernameState")
    }


    // Display the photo using your preferred method
}

suspend fun fetchUserData(username: String): Triple<String, String, String> {
    // Get a reference to the database
    val database = FirebaseDatabase.getInstance()

    // Get a reference to the user's data
    val userRef = database.getReference("users").child(username)

    // Fetch the user's data
    val snapshot = userRef.get().await()

    // Extract the photo URL, description, and username from the snapshot
    val photoUrl = snapshot.child("photoUrl").getValue(String::class.java) ?: ""
    val description = snapshot.child("description").getValue(String::class.java) ?: ""
    val username = snapshot.child("username").getValue(String::class.java) ?: ""

    return Triple(photoUrl, description, username)
}
