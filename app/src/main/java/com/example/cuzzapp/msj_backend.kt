package com.example.cuzzapp

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.future.await
import kotlinx.coroutines.launch
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
            SendMessageComposable(messagingService, username, "receiver") // replace "receiver" with the actual receiver's username
            FriendList(friends) { friend ->
                selectedFriend = friend
            }
        }
    }
}
@Composable
fun FriendList(friends: List<String>, onFriendClick: (String) -> Unit) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(friends) { friend ->
            Text(friend, modifier = Modifier.clickable { onFriendClick(friend) })
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

        MessageList(messages)
    }
}
@Composable
fun MessageCard(message: Message) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize() // Make the column fill the card
                .padding(16.dp)
        ) {
            Text(text = "Sender: ${message.sender}")
            Text(text = "Receiver: ${message.receiver}")
            Text(text = "Message: ${message.text}")
        }
    }
}
@Composable
fun MessageList(messages: List<Message>) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(messages) { message ->
            MessageCard(message)
        }
    }
}
@Composable
fun AddFriendComposable(messagingService: MessagingService, username: String) {
    var friendName by remember { mutableStateOf("") }

    TextField(
        value = friendName,
        onValueChange = { friendName = it },
        label = { Text("Friend's Name") }
    )

    Button(onClick = {
        messagingService.addFriend(username, friendName)
        friendName = ""
    }) {
        Text("Add Friend")
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