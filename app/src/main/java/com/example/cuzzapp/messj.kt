package com.example.cuzzapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cuzzapp.ui.theme.CuzzAppTheme
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.async
import kotlinx.coroutines.future.await
import kotlinx.coroutines.launch
import username_true
import java.util.concurrent.CompletableFuture

data class Person_msj(
    val username: String,
    val friends: List<String>,
    val messages: List<String>,
    val who_msj: List<Boolean>,

)
val User_msj_list: Person_msj? = null
class messj : ComponentActivity() {
    @SuppressLint("CoroutineCreationDuringComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val messagingService = MessagingService()

        enableEdgeToEdge()
        setContent {
            MessagingScreen(username = username_true, messagingService = messagingService)

        }
    }
}
class ChatViewModel : ViewModel() {
    private val _messages = MutableLiveData<List<Message>>()
    val messages: LiveData<List<Message>> = _messages

    private val database = FirebaseDatabase.getInstance()
    private val messagesRef = database.getReference("messages/${username_true}")

    init {
        messagesRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val newMessages = dataSnapshot.children.mapNotNull { it.getValue(Message::class.java) }
                _messages.value = newMessages
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error
            }
        })
    }

    fun sendMessage(message: Message) {
        val key = messagesRef.push().key
        if (key != null) {
            messagesRef.child(key).setValue(message)
        }
    }
}
@Composable
fun AddFriendButton() {
    var showInput by remember { mutableStateOf(false) }
    var friendName by remember { mutableStateOf("") }
    val couroutineScope = rememberCoroutineScope()
    Column {
        Button(onClick = { showInput = true }) {
            Text("Show Add Friend")
        }

        if (showInput) {
            TextField(
                value = friendName,
                onValueChange = { friendName = it },
                label = { Text("Friend's Name") }
            )

          Button(onClick = {
    couroutineScope.launch {
        println("Friend $friendName added")
        showInput = false
        val exists = async { check_if_user_exists(friendName).await() }.await()
        if (exists) {
            Log.d("Friend $friendName exists", "Friend $friendName exists")

        } else {
            Log.d("Friend $friendName does not exist", "Friend $friendName does not exist")
        }
        friendName = ""
    }
}) {
    Text("Add Friend")
}
        }
    }
}

fun check_if_user_exists(usernameToFind: String): CompletableFuture<Boolean> {
    val future = CompletableFuture<Boolean>()
    val database = FirebaseDatabase.getInstance()
    val accountsRef = database.getReference("accounts")

    accountsRef.addListenerForSingleValueEvent(object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            var found = false
            for (userSnapshot in dataSnapshot.children) {
                val user = userSnapshot.getValue(User::class.java)
                if (user?.name == usernameToFind) {
                    found = true
                    break
                } else {
                    if (user != null) {
                        Log.d("User not found", "${user.name} ${usernameToFind}")
                    }
                }
            }
            future.complete(found)
        }

        override fun onCancelled(databaseError: DatabaseError) {
            // Handle possible errors.
            println("The read failed: " + databaseError.code)
            future.completeExceptionally(databaseError.toException())
        }
    })

    return future
}
fun retrieveData(username: String): CompletableFuture<Person_msj?> {
    val future = CompletableFuture<Person_msj?>()
    val database = FirebaseDatabase.getInstance()
    val messagesRef = database.getReference("messages/$username")

    messagesRef.addListenerForSingleValueEvent(object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            if (dataSnapshot.exists()) {
                val retrievedData = dataSnapshot.getValue(Person_msj::class.java)
                future.complete(retrievedData)
            } else {
                messagesRef.setValue(User_msj_list)
                future.complete(User_msj_list)
            }
        }

        override fun onCancelled(databaseError: DatabaseError) {
            println("The read failed: " + databaseError.code)
            future.completeExceptionally(databaseError.toException())
        }
    })

    return future
}