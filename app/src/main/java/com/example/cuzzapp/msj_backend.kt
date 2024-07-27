package com.example.cuzzapp

import android.content.Intent
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.core.content.ContextCompat.startActivity
import coil.compose.AsyncImage
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.future.await
import kotlinx.coroutines.launch
import username_true
import java.util.concurrent.CompletableFuture
import kotlin.math.roundToInt

data class User_(
    val username: String,
    val friends: MutableList<String> = mutableListOf(),
    val messages: MutableList<Message> = mutableListOf()
)
data class Message(
    val sender: String = "",
    val receiver: String = "",
    val text: String = ""
)
class MessagingService {
    private val database = FirebaseDatabase.getInstance()



    fun addFriend(username: String, friendUsername: String) {
        // Add friend to user's list of friends
        val userRef = database.getReference("users/$username/friends")
        userRef.push().setValue(friendUsername)

        // Add user to friend's list of friends
        val friendRef = database.getReference("users/$friendUsername/friends")
        friendRef.push().setValue(username)
    }

    fun sendMessage(sender: String, receiver: String, text: String) {
        val message = Message(sender, receiver, text)

        // Add message to sender's list of messages
        val senderMessagesRef = database.getReference("messages/$sender/$receiver")
        senderMessagesRef.push().setValue(message)

        // Add message to receiver's list of messages
        val receiverMessagesRef = database.getReference("messages/$receiver/$sender")
        receiverMessagesRef.push().setValue(message)
    }

    fun getFriends(username: String): CompletableFuture<List<String>> {
        val future = CompletableFuture<List<String>>()
        val friendsRef = database.getReference("users/$username/friends")

        friendsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val friends = dataSnapshot.children.mapNotNull { it.getValue(String::class.java) }
                future.complete(friends)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                println("The read failed: " + databaseError.code)
                future.completeExceptionally(databaseError.toException())
            }
        })

        return future
    }
    fun getFriendImageUrl(friend: String): CompletableFuture<String> {
    val future = CompletableFuture<String>()
    val userRef = database.getReference("accounts/$friend/photoUrl")

    userRef.addListenerForSingleValueEvent(object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            Log.d("kilo", dataSnapshot.toString())
            val imageUrl = dataSnapshot.getValue(String::class.java)
            if (imageUrl != null) {
                future.complete(imageUrl)
            } else {
                future.completeExceptionally(Exception("Image URL not found"))
            }
        }

        override fun onCancelled(databaseError: DatabaseError) {
            future.completeExceptionally(databaseError.toException())
        }
    })

    return future
}

fun getFriendPoints(friend: String): CompletableFuture<Int> {
    val future = CompletableFuture<Int>()
    val userRef = database.getReference("accounts/$friend/points")

    userRef.addListenerForSingleValueEvent(object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            val points = dataSnapshot.getValue(Int::class.java)
            if (points != null) {
                future.complete(points)
            } else {
                future.completeExceptionally(Exception("Points not found"))
            }
        }

        override fun onCancelled(databaseError: DatabaseError) {
            future.completeExceptionally(databaseError.toException())
        }
    })

    return future
}

    fun getFriendRole(friend: String): CompletableFuture<String> {
        val future = CompletableFuture<String>()
        val userRef = database.getReference("accounts/$friend/role")
Log.d("kilo",friend)
        userRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {""
                val role = dataSnapshot.getValue(String::class.java)
                if (role != null) {
                    future.complete(role)
                } else {
                    future.completeExceptionally(Exception("Role not  found"))
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                future.completeExceptionally(databaseError.toException())
            }
        })

        return future
    }

    fun getMessages(sender: String, receiver: String): CompletableFuture<List<Message>> {
    val future = CompletableFuture<List<Message>>()
    val messagesRef = database.getReference("messages/$sender/$receiver")

    messagesRef.addValueEventListener(object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            val messages = dataSnapshot.children.mapNotNull { it.getValue(Message::class.java) }
            future.complete(messages)
        }

        override fun onCancelled(databaseError: DatabaseError) {
            println("The read failed: " + databaseError.code)
            future.completeExceptionally(databaseError.toException())
        }
    })

    return future
}
}
@Composable
fun MessagingScreen(username: String, messagingService: MessagingService) {
    val coroutineScope = rememberCoroutineScope()
    var studentFriends by remember { mutableStateOf<List<String>>(emptyList()) }
    var teacherFriends by remember { mutableStateOf<List<String>>(emptyList()) }
    var selectedFriend by remember { mutableStateOf<String?>(null) }
    var boxHeight by remember { mutableStateOf(400.dp) } // State for Box height
    var dragOffset by remember { mutableStateOf(0f) } // State for drag offset

    LaunchedEffect(key1 = Unit) {
        coroutineScope.launch {
            val allFriends = messagingService.getFriends(username).await()
            val studentList = mutableListOf<String>()
            val teacherList = mutableListOf<String>()

            allFriends.forEach { friend ->
                val role = messagingService.getFriendRole(friend).await()
                if (role == "Student") {
                    studentList.add(friend)
                } else {
                    teacherList.add(friend)
                }
            }

            studentFriends = studentList
            teacherFriends = teacherList
        }
    }

    if (selectedFriend != null) {
        FriendMessagesScreen(username, selectedFriend!!, messagingService)
    } else {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF6A5AE0))
        ) {
            Column {
                AddFriendComposable(messagingService, username)
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f), // Ensure it takes available space
                    contentPadding = PaddingValues(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(teacherFriends) { friend ->
                        var imageUrl by remember { mutableStateOf<String?>(null) }
                        var points by remember { mutableStateOf<Int?>(null) }

                        LaunchedEffect(friend) {
                            coroutineScope.launch {
                                imageUrl = messagingService.getFriendImageUrl(friend).await()
                                points = messagingService.getFriendPoints(friend).await()
                            }
                        }

                        Box(
                            modifier = Modifier
                                .requiredWidth(167.dp)
                                .requiredHeight(102.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xfff7f7f7))
                                .clickable { selectedFriend = friend },
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                imageUrl?.let {
                                    AsyncImage(
                                        model = it,
                                        contentDescription = "Teacher's Profile Picture",
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier
                                            .size(50.dp)
                                            .clip(CircleShape)
                                    )
                                }
                                Text(friend)
                                points?.let {
                                    Text("Points: $it")
                                }
                            }
                        }
                    }
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth() // Adjust width to fill the entire screen
                        .height(boxHeight) // Set initial height to 400 dp
                        .padding(20.dp)
                        .clip(RoundedCornerShape(16.dp)) // Rounded corners
                        .background(Color.White) // White background
                        .draggable(
                            orientation = Orientation.Vertical,
                            state = rememberDraggableState { delta ->
                                dragOffset += delta
                                boxHeight = (boxHeight + delta.dp).coerceAtLeast(100.dp) // Minimum height of 100 dp
                            }
                        )
                ) {
                    Text("Students", style = MaterialTheme.typography.bodyLarge, modifier = Modifier.align(Alignment.TopCenter))

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp) // Padding inside the box
                    ) {
                        items(studentFriends) { friend ->
                            var imageUrl by remember { mutableStateOf<String?>(null) }
                            var points by remember { mutableStateOf<Int?>(null) }

                            LaunchedEffect(friend) {
                                coroutineScope.launch {
                                    imageUrl = messagingService.getFriendImageUrl(friend).await()
                                    points = messagingService.getFriendPoints(friend).await()
                                }
                            }

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .requiredHeight(100.dp) // Make the box bigger
                                    .padding(10.dp)
                                    .clip(RoundedCornerShape(16.dp)) // Rounded corners
                                    .background(Color(0xFF6A5AE0))
                                    .clickable { selectedFriend = friend },
                                contentAlignment = Alignment.Center
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier.padding(16.dp)
                                ) {
                                    imageUrl?.let {
                                        AsyncImage(
                                            model = it,
                                            contentDescription = "Student's Profile Picture",
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier
                                                .size(50.dp)
                                                .clip(CircleShape)
                                        )
                                    }
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text(friend, color = Color.White)
                                        points?.let {
                                            Text("Points: $it", color = Color.White)
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
}
@Composable
fun Show_friend_list_(friends: List<String>, onFriendClick: (String) -> Unit) {
    val context = LocalContext.current
    val triggerFetch = remember { mutableStateOf(false) }

    LazyColumn(modifier = Modifier
        .fillMaxSize()
        .background(Color(0xff212121))) {
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
                            contentScale = ContentScale.Crop,

                            contentDescription = "Friend's Profile Picture",
                            modifier = Modifier
                                .size(100.dp)
                                .clip(CircleShape)
                                .clickable { onFriendClick(friend) }
                                .border(BorderStroke(4.dp, Color.White), CircleShape)
                                .padding(4.dp)
                        )
                    } else {
                        Text("Loading image...", Modifier.padding(4.dp))
                    }
                }

            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FriendMessagesScreen(username: String, friendUsername: String, messagingService: MessagingService) {
    val coroutineScope = rememberCoroutineScope()
    var messages by remember { mutableStateOf<List<Message>>(emptyList()) }
    var messageText by remember { mutableStateOf("") }
    var friendImageUrl by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(key1 = friendUsername) {
        coroutineScope.launch {
            messages = messagingService.getMessages(username, friendUsername).await()
            friendImageUrl = messagingService.getFriendImageUrl(friendUsername).await()
        }
    }

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
            friendImageUrl?.let {
                AsyncImage(
                    model = it,
                    contentDescription = "Friend's Profile Picture",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = friendUsername,
                color = Color.White,
                style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold)
            )
        }

        // Message list
        MessageList(messages, username, Modifier.weight(1f).offset(y = -70.dp))

        // Message input field
        Row(
            verticalAlignment = Alignment.Bottom, // Align items to the bottom
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFECE5DD))
                .padding(8.dp)
                .offset(y = -70.dp)
        ) {
            TextField(
                value = messageText,
                onValueChange = { messageText = it },
                placeholder = { Text("Type a message") },
                modifier = Modifier.weight(1f),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )
            IconButton(onClick = {
                messagingService.sendMessage(username, friendUsername, messageText)
                messageText = ""
                coroutineScope.launch {
                    messages = messagingService.getMessages(username, friendUsername).await()
                }
            }) {
                Icon(imageVector = Icons.Default.Send, contentDescription = "Send Message")
            }
        }
    }
}

@Composable
fun MessageCard(message: Message, isCurrentUser: Boolean) {
    val backgroundColor = if (isCurrentUser) Color(0xFFDCF8C6) else Color(0xFF2F80ED)
    val alignment = if (isCurrentUser) Alignment.CenterEnd else Alignment.CenterStart

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        contentAlignment = alignment
    ) {
        //Make a varuale with a cardcolor
        Card(
            shape = RoundedCornerShape(16.dp), // Rounded corners
            modifier = Modifier
                .widthIn(max = 250.dp)
                .padding(4.dp),
            colors = CardDefaults.cardColors(backgroundColor) // Set the card's background color
        ) {
            Text(
                text = message.text,
                modifier = Modifier.padding(8.dp),
                style = TextStyle(fontSize = 16.sp)
            )
        }
    }
}

@Composable
fun MessageList(messages: List<Message>, currentUser: String, modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp),
        reverseLayout = true
    ) {
        items(messages.reversed()) { message ->
            MessageCard(message, message.sender == currentUser)
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddFriendComposable(messagingService: MessagingService, username: String) {
    var friendName by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .requiredHeight(190.dp)
            // Assuming the parent Box has a defined height, replace ParentHeight with actual value if known
            .height(IntrinsicSize.Min)
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color(0xFFeb3f21),
                        Color(0xFF1ed4b8)
                    ),
                    start = Offset(0f, 0f),
                    end = Offset.Infinite
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
        TextField(
    value = friendName,
    onValueChange = { friendName = it },
    label = { Text("Friend's Name") },
    modifier = Modifier
        .fillMaxWidth(0.8f)
        .offset(y = 7.dp), // Adjust width as needed
    shape = RoundedCornerShape(8.dp), // Rounded corners
    colors = TextFieldDefaults.textFieldColors(
        containerColor = Color.White.copy(alpha = 0.5f), // Adjust transparency with alpha
        unfocusedIndicatorColor = Color.Transparent, // Hide the indicator when unfocused
        focusedIndicatorColor = Color.Transparent // Hide the indicator when focused
    )
)

            Spacer(modifier = Modifier.height(8.dp)) // Space between TextField and Button

            Button(
                onClick = {
                    messagingService.addFriend(username, friendName)
                    friendName = ""
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFDD47)), // Set the background color
                modifier = Modifier
            ) {
                Text("Add Friend")
            }
        }
    }
}