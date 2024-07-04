package com.example.cuzzapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.cuzzapp.ui.theme.CuzzAppTheme

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

}