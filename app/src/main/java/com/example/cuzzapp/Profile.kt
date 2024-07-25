package com.example.cuzzapp

import achivement_other
import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.MutableLiveData
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.compose.rememberImagePainter
import com.example.cuzzapp.Shop.ShopItem
import com.example.cuzzapp.ui.theme.CuzzAppTheme
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import descriptiones
import get_achievements_from_db
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.future.await
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import points
import url_photo
import username_for_all
import username_true
import viewedProfile

class Profile : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val user_ = intent.getStringExtra("userr_to")
        val url_photo_prof = intent.getStringExtra("img_link")

        setContent {
            val messagingService = MessagingService()

            Toast.makeText(this, "The user is: $user_", Toast.LENGTH_SHORT).show()
                var searchQuery by remember { mutableStateOf("") }
            val scaffoldState = rememberScaffoldState()

            Drawer(scaffoldState, searchQuery, backgroundColor = Brush.linearGradient(
                colors = listOf(
                    Color(0xFFeb3f21),
                    Color(0xFF1ed4b8)
                ),
                start = Offset(0f, 1f),
                end = Offset.Infinite
            ),onSearchQueryChange = { searchQuery = it }) {
                Log.d(TAG, "onCreate: $url_photo_prof")

                if (url_photo_prof != null) {
                    ProfileScreen(url_photo_prof,user_,messagingService)
                }
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
                achivement_other = ArrayList<achievementuriUSER?>()
               achivement_other = get_achievements_from_db(child, achivement_other)
                break
            }
        }
    }
    return ShopItemsResult(photoUrl, description, points)
}
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ProfileScreen(url_photo_prof: String, user_to_show: String?, messagingService: MessagingService) {
    var photoUrl by remember { mutableStateOf("") }
    val context = LocalContext.current
    var triggerFetch by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    var selectedFriend by remember { mutableStateOf<String?>(null) }
    var friends by remember { mutableStateOf<List<String>>(emptyList()) }
    LaunchedEffect(key1 = Unit) {
        coroutineScope.launch {
            friends = user_to_show?.let { messagingService.getFriends(it).await() }!!
            Toast.makeText(context, "Friends: $friends", Toast.LENGTH_SHORT).show()
        }
    }
    // Coroutine launch logic moved here
    if (triggerFetch && user_to_show != null) {
        LaunchedEffect(user_to_show) {
            // Update your UI state with the result here
            val intent = Intent(context, Other_Portofolios::class.java)
            intent.putExtra("userr", user_to_show)
            startActivity(context, intent, null)
            triggerFetch = false // Reset trigger to avoid re-fetching
        }
    }

    println("URL PHOTO___: $photo_URL")
    println("URL PHOTO_: $photoUrl")
    val painter = rememberImagePainter(url_photo_prof)

    var dynamicHeight by remember { mutableStateOf(380) } // Initial height in dp

    Column(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            modifier = Modifier
                .weight(1f)
                .requiredWidth(width = getScreenWidthDp().dp)
                .requiredHeight(height = dynamicHeight.dp.coerceAtLeast(100.dp)), // Use dynamic height but ensure it's at least 100.dp
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                Color(0xFFeb3f21),
                                Color(0xFF1ed4b8)
                            ),
                            start = Offset(0f, 1f),
                            end = Offset.Infinite
                        )
                    )
                ,
                horizontalAlignment = Alignment.End
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    Button(
                        onClick = {
                            triggerFetch = true // Set trigger to launch coroutine

                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF639e55)),
                        modifier = Modifier.align(Alignment.BottomEnd)
                    ) {
                        Text(text = "See Portofolio")
                    }
                    Spacer(modifier = Modifier.height(24.dp))

                    // Replace with your actual profile picture
                    Image(
                        painter = painter,
                        contentDescription = "Profile Picture",
                        contentScale = ContentScale.Crop,

                        modifier = Modifier
                            .size(150.dp) // Adjust the size to make the image bigger
                            .align(Alignment.Center) // Align the image to the center of the Box
                            .offset(y = (-50).dp) // Move the image up by 16.dp
                            .border(
                                BorderStroke(4.dp, Color.White),
                                CircleShape
                            )
                            .padding(4.dp)

                            .clip(CircleShape)
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(top = 200.dp) // Adjust the padding as needed
                            .offset(y = (-30).dp) // Move the row up by 16.dp
                    ) {
                        if (user_to_show != null) {
                            Text(
                                text = user_to_show,
                                style = TextStyle(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    color = Color.White),
                                modifier = Modifier.padding(end = 48.dp), // Add padding to separate the two texts

                            )
                        }
                        Text(
                            text = "Points: $pointss",
                            style = TextStyle(
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = Color.White),
                            modifier = Modifier, // Add padding to separate the two texts

                        )
                    }
                }


            }
            if (username_for_all == username_true) {
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Spacer(Modifier.weight(1f)) // This will push the row to the bottom

                    Row(
                        verticalAlignment = Alignment.Bottom,
                        modifier = Modifier
                            .fillMaxWidth()
                            .offset(y = (-80).dp) // Move the row up by 16.dp
                    ) {



                    }
                }
            }
        }
        val screenHeightDp = getScreenHeightInDp(context) // Get the screen height in dp
        val minHeight = (screenHeightDp / 2 )-25 // Calculate half of the screen height

        Box(
            contentAlignment = Alignment.BottomCenter,
            modifier = Modifier
                .size(height = dynamicHeight.dp, width = getScreenWidthDp().dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xff212121))
                .pointerInput(Unit) {
                    detectDragGestures { _, dragAmount ->
                        val heightChange = dragAmount.y.toInt()
                        // Ensure the new height does not go below half of the screen height
                        dynamicHeight =
                            (dynamicHeight - heightChange).coerceIn(minHeight, screenHeightDp)
                    }
                }
        ) {

            LaunchedEffect(key1 = Unit) {
                coroutineScope.launch {
                    friends = user_to_show?.let { messagingService.getFriends(it).await() }!!
                }
            }

            if (selectedFriend != null) {
                if (user_to_show != null) {
                    FriendMessagesScreen(user_to_show, selectedFriend!!, messagingService)
                }
            } else {
                Column(modifier = Modifier.offset(y = 50.dp)) {
                    Show_friend_list(friends) { friend ->
                        selectedFriend = friend
                    }
                }
            }
        }
    }

}

data class FriendProfileData(val photoUrl: String?, val points: Int?)
fun fetchFriendPhotoUrl(friend: String): StateFlow<FriendProfileData> {
    val database = FirebaseDatabase.getInstance()
    val accountsRef = database.getReference("accounts/$friend")
    val profileDataStateFlow = MutableStateFlow<FriendProfileData>(FriendProfileData(null, null))

    accountsRef.addValueEventListener(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            val value = snapshot.getValue<User>()
            profileDataStateFlow.value = FriendProfileData(value?.photoUrl, value?.Points)
        }

        override fun onCancelled(error: DatabaseError) {
            Log.w("Profile", "Failed to read value.", error.toException())
        }
    })

    return profileDataStateFlow
}
@Composable
fun Show_friend_list(friends: List<String>, onFriendClick: (String) -> Unit) {
    val context = LocalContext.current
    val triggerFetch = remember { mutableStateOf(false) }

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(friends) { friend ->
            // Observe the photo URL and points StateFlow
            val profileData = fetchFriendPhotoUrl(friend).collectAsState(initial = FriendProfileData(null, null)).value
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween // Adjust arrangement to space between elements
            ) {
                Column(modifier = Modifier.weight(1f)) { // Adjust weight to give more space to text and image
                    Text(
                        text = friend,
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = Color.White),
                    )
                    // Display points
                    profileData.points?.let {
                        Text(
                            text = "Points: $it",
                            style = TextStyle(
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = Color.White),
                        )
                    }
                }
                // Wrap AsyncImage in a Box for alignment
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                ) {
                    if (profileData.photoUrl != null) {
                        AsyncImage(
                            model = profileData.photoUrl,
                            contentDescription = "Friend's Profile Picture",
                            modifier = Modifier
                                .size(100.dp)
                                .clip(CircleShape)
                                .clickable { onFriendClick(friend) }
                                .border(BorderStroke(4.dp, Color.White), CircleShape)
                                .padding(4.dp)
                        )
                    }
                }
                Button(
                    onClick = {
                        val intent = Intent(context, Other_Portofolios::class.java)
                        intent.putExtra("userr", friend)
                        startActivity(context, intent, null)
                    },
                    modifier = Modifier.align(Alignment.CenterVertically)
                ) {
                    Text("See Portfolio")
                }
            }
        }
    }
}
fun getScreenHeightInDp(context: Context): Int {
    val metrics = DisplayMetrics()
    val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    windowManager.defaultDisplay.getMetrics(metrics)
    val screenHeightInPixels = metrics.heightPixels
    val density = context.resources.displayMetrics.density
    return (screenHeightInPixels / density).toInt()
}

