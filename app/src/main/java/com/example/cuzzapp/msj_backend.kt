package com.example.cuzzapp

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

    fun addUser(username: String) {
        val userRef = database.getReference("users/$username")
        userRef.setValue(User_(username))
    }

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
    var friends by remember { mutableStateOf<List<String>>(emptyList()) }
    var selectedFriend by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(key1 = Unit) {
        coroutineScope.launch {
            friends = messagingService.getFriends(username).await()
        }
    }

    if (selectedFriend != null) {
        FriendMessagesScreen(username, selectedFriend!!, messagingService)
    } else {
        Column {
            AddFriendComposable(messagingService, username)
            Show_friend_list_(friends) { friend ->
                selectedFriend = friend
            }
        }
    }
}
@Composable
fun Show_friend_list_(friends: List<String>, onFriendClick: (String) -> Unit) {
    val context = LocalContext.current
    val triggerFetch = remember { mutableStateOf(false) }

    LazyColumn(modifier = Modifier.fillMaxSize().background(Color(0xff212121))) {
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
                    } else {
                        Text("Loading image...", Modifier.padding(4.dp))
                    }
                }

            }
        }
    }
}
@Composable
fun FriendMessagesScreen(username: String, friendUsername: String, messagingService: MessagingService) {
    val coroutineScope = rememberCoroutineScope()
    var messages by remember { mutableStateOf<List<Message>>(emptyList()) }
    var messageText by remember { mutableStateOf("") } // New state for message text

    LaunchedEffect(key1 = friendUsername) {
        coroutineScope.launch {
            messages = messagingService.getMessages(username, friendUsername).await()
        }
    }

    Column {
        TextField(
            value = messageText,
            onValueChange = { messageText = it },
            label = { Text("Message Text") }
        )

        Button(onClick = {
            messagingService.sendMessage(username, friendUsername, messageText)
            messageText = "" // Clear the message text after sending
            coroutineScope.launch {
                messages = messagingService.getMessages(username, friendUsername).await() // Reload messages after sending
            }
        }) {
            Text("Send Message")
        }

        MessageList(messages,username_true)
    }
}
@Composable
fun MessageCard(message: Message, isCurrentUser: Boolean) {
    val sentBrush = Brush.linearGradient(
        colors = listOf(
            Color(0xFFeb3f21),
            Color(0xFF1ed4b8)
        ),
        start = Offset(0f, 0f),
        end = Offset.Infinite
    )

    val receivedBrush = Brush.linearGradient(
        colors = listOf(
            Color(0xFF8B93FF),
            Color(0xFFEDD1FA),
            Color(0xFF4120A9)
        ),
        start = Offset(0f, 0f),
        end = Offset.Infinite
    )

    val backgroundBrush = if (isCurrentUser) sentBrush else receivedBrush

    Box(
        modifier = Modifier
            .fillMaxWidth()

    ) {
        Card(
            modifier = Modifier
                .width(200.dp)
                .align(if (isCurrentUser) Alignment.CenterEnd else Alignment.CenterStart)
                .padding(8.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(brush = backgroundBrush)

                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = message.text)
            }
        }
    }
}
@Composable
fun MessageList(messages: List<Message>, currentUser: String) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(messages) { message ->
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
    modifier = Modifier.fillMaxWidth(0.8f).offset(y = 7.dp), // Adjust width as needed
    shape = RoundedCornerShape(8.dp), // Rounded corners
    colors = TextFieldDefaults.textFieldColors(
        containerColor = Color.White.copy(alpha = 0.5f), // Adjust transparency with alpha
        unfocusedIndicatorColor = Color.Transparent, // Hide the indicator when unfocused
        focusedIndicatorColor = Color.Transparent // Hide the indicator when focused
    )
)

            Spacer(modifier = Modifier.height(8.dp)) // Space between TextField and Button

            Button(onClick = {
                messagingService.addFriend(username, friendName)
                friendName = ""
            }) {
                Text("Add Friend")
            }
        }
    }
}
@Composable
fun SendMessageComposable(messagingService: MessagingService, sender: String, receiver: String) {
    var messageText by remember { mutableStateOf("") }

    TextField(
        value = messageText,
        onValueChange = { messageText = it },
        label = { Text("Message Text") }
    )

    Button(onClick = {
        messagingService.sendMessage(sender, receiver, messageText)
        messageText = ""
    }) {
        Text("Send Message")
    }
}