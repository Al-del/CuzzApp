package com.example.cuzzapp

import AppNavigator
import BottomNavigationBar
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController

val LighterRed = Color(0xFFFA7070)
val Pink = Color(0xFFFEFDED)
val LightOrange = Color(0xFFC6EBC5)
val LightYellow = Color(0xFFC6EBC5)

class MainActivity : ComponentActivity() {
    @ExperimentalAnimationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            Scaffold(
                bottomBar = { BottomNavigationBar(navController) }
            ) { paddingValues ->
                Box(Modifier.padding(paddingValues)) {
                    AppNavigator()
                    LoginPage()
                }
            }
        }
    }
    @ExperimentalAnimationApi
    @Composable
    fun LoginPage() {
        val visible = remember { mutableStateOf(true) }
        Box(modifier = Modifier.fillMaxSize().background(LighterRed), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Welcome Back!",
                    style = MaterialTheme.typography.displayMedium,
                    color = Pink
                )
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = TextFieldValue(),
                    onValueChange = {},
                    label = { Text("Email", color = LightOrange) },
                    modifier = Modifier.fillMaxWidth(0.8f)
                )
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = TextFieldValue(),
                    onValueChange = {},
                    label = { Text("Password", color = LightOrange) },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(0.8f)
                )
                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = { /* Handle login */ },
                    colors = ButtonDefaults.buttonColors(contentColor = Pink, containerColor = LightYellow),
                    modifier = Modifier.fillMaxWidth(0.8f)
                ) {
                    Text(text = "Log in")
                }

                Spacer(modifier = Modifier.height(24.dp))
                AnimatedVisibility(
                    visible = visible.value,
                    enter = slideInVertically(initialOffsetY = { it }, animationSpec = tween(1000))
                ) {
                    Button(
                        onClick = {
                            visible.value = false
                        },
                        colors = ButtonDefaults.buttonColors(contentColor = Pink, containerColor = LightYellow),
                        modifier = Modifier.fillMaxWidth(0.8f)
                    ) {
                        Text(text = "Go to Second Activity")
                    }
                }
            }
        }
    }
}
