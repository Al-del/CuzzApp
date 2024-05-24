package com.example.cuzzapp

import Drawer_final
import android.annotation.SuppressLint
import android.content.ContentValues.TAG
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
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.MutableLiveData
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberImagePainter
import com.example.cuzzapp.Shop.ShopItem
import com.example.cuzzapp.ui.theme.CuzzAppTheme
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import descriptiones
import kotlinx.coroutines.tasks.await
import points
import url_photo
import username_for_all
import username_true

class Profile : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CuzzAppTheme {
                var searchQuery by remember { mutableStateOf("") }
                val scaffoldState = rememberScaffoldState()

                Drawer_final(scaffoldState, { ProfileScreen() })

             //       ProfileScreen()

            }
        }
    }

}
var photo_URL:String=""
var descriptiones_:String=""
var pointss: Int? = 0
data class ShopItemsResult(val photoUrl: String, val description: String, val points: Int)

suspend fun fetchShopItems(username: String): ShopItemsResult {
    val database = FirebaseDatabase.getInstance()
    val accountsRef = database.getReference("accounts")
    var photoUrl = ""
    var description = ""
    var points = 0

    val snapshot = accountsRef.get().await()
    for (child in snapshot.children) {
        val account = child.getValue(User::class.java)
        if (account != null) {
            if (account.name == username) {
                photoUrl = account.photoUrl
                description = account.state
                points = account.Points
                break
            }
        }
    }
    return ShopItemsResult(photoUrl, description, points)
}
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ProfileScreen() {
    var photoUrl by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var usernameState by remember { mutableStateOf(username_for_all) }
    val descriptionState = remember { mutableStateOf(TextFieldValue(descriptiones_)) }
    val navController = rememberNavController() // Create a NavController
    var searchQuery by remember { mutableStateOf("") }

    val gradientBrush = Brush.verticalGradient(
        colors = listOf(Color(0xFFED213A), Color(0xFF93291E)),
        startY = 0f,
        endY = 500f
    )
    var desc_true by remember {
        mutableStateOf(descriptiones_)
    }

    LaunchedEffect(key1 = username_for_all) {
        val (fetchedPhotoUrl, fetchedDescription, points) = fetchShopItems(username_for_all)
        photoUrl = fetchedPhotoUrl
        description = fetchedDescription
        pointss = points
    }
    println("URL PHOTO___: $photo_URL")
    println("URL PHOTO_: $photoUrl")
val painter = rememberImagePainter(photoUrl)

    Scaffold(
        modifier =Modifier.background( Color(0xFF262323)), // Set the background color
        bottomBar = {AppNavigator(navController) }
    ) {
        Box(modifier = Modifier.fillMaxSize().background(Color(0xFF262323))) {

                LoadImageFromUrl(url = url_photo, points = points)
                Rectangle2(searchQuery, { newQuery -> searchQuery = newQuery })


                Column(
                modifier = Modifier
                    .requiredHeight(525.dp)
                    .requiredWidth(350.dp)
                    .align(Alignment.Center)
                    .padding(16.dp)
                    .background(brush = gradientBrush)
                    ,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {


                Spacer(modifier = Modifier.height(24.dp))

                // Replace with your actual profile picture
                Image(
                    painter = painter,
                    contentDescription = "Profile Picture",
                    modifier = Modifier
                        .size(100.dp, 100.dp) // Ensure the image is square
                        .clip(CircleShape) // This makes the image circular
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Replace with actual name

                Text(text = usernameState, style = MaterialTheme.typography.displayMedium, color = Color.White)

                Spacer(modifier = Modifier.height(24.dp))

                // Replace with actual description
                if (descriptiones_.isBlank()) {
                    desc_true = "Nu exista inca nicio descriere"
                }
                Text(text = desc_true, style = MaterialTheme.typography.displayMedium, color = Color.White)
                Text(text = "Points: $pointss", style = MaterialTheme.typography.displayMedium, color = Color.White)

                Spacer(modifier = Modifier.height(24.dp))

            }
        }
 if (username_for_all == username_true) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Spacer(Modifier.weight(1f)) // This will push the row to the bottom

        Row(
            verticalAlignment = Alignment.Bottom,
            modifier = Modifier.fillMaxWidth()
                .offset(y = (-80).dp) // Move the row up by 16.dp
        ) {
            OutlinedTextField(
                value = descriptionState.value,
                onValueChange = { descriptionState.value = it },
                label = { Text("Modify the description") },
                modifier = Modifier
                    .weight(0.8f) // Fill 80% of the parent's width
                    .padding(end = 8.dp) // Add some padding between the text field and the button
            )
val gradient_2 = Brush.verticalGradient(
    colors = listOf(Color(0xffFDC830), Color(0xffF37335)),
    startY = 0f,
    endY = 500f
)

Box(
    modifier = Modifier
        .size(100.dp, 50.dp) // Set the size of the Box
        .clip(CutCornerShape(topStart = 3.dp, topEnd = 3.dp)) // Clip the Box to a circle
        .background(brush = gradient_2) // Set the background to the gradient
        .clickable { // Make the Box clickable
            desc_true = descriptionState.value.text
            descriptiones_ = descriptionState.value.text
            val database = FirebaseDatabase.getInstance()

            // Get a reference to the "accounts" node
            val accountsRef = database.getReference("accounts")

            accountsRef.child(username_for_all).child("state").setValue(descriptiones_)
            //Clear the text field
            descriptionState.value = TextFieldValue("")
        },
    contentAlignment = Alignment.Center // Center the content inside the Box
) {
    Text("Updateaza descriere") // The content of the Box
}
        }
    }
}
    }
}


