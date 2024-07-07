package com.example.cuzzapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import com.example.cuzzapp.ui.theme.CuzzAppTheme
import com.google.firebase.firestore.FirebaseFirestore

class Quizz_teacher : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val scaffoldState = rememberScaffoldState()
            Drawer(scaffoldState = scaffoldState) {
                Teacher_Quizz() // Adjusted to call without homeScreen parameter
            }

        }
    }
}

@Composable
fun Teacher_Quizz(){
    val context = LocalContext.current // Get the local context to use startActivity
    CircularButton(onClick = {

        val intent = Intent(context, Add_Question()::class.java)
        context.startActivity(intent)
    })
}

@Composable
fun CircularButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.size(56.dp), // Adjust size as needed

        shape = CircleShape // This makes the button circular
    ) {
        Text("Click")
    }
}