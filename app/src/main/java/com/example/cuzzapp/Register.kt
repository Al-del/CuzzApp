package com.example.cuzzapp

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat.startActivityForResult
import com.example.cuzzapp.ui.theme.LightOrange
import com.example.cuzzapp.ui.theme.LightYellow
import com.example.cuzzapp.ui.theme.LighterRed
import com.example.cuzzapp.ui.theme.Pink

class Register : ComponentActivity() {
    private lateinit var takePictureLauncher: ActivityResultLauncher<Intent>
    private val imageBitmap = mutableStateOf<Bitmap?>(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        takePictureLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                imageBitmap.value = result.data?.extras?.get("data") as? Bitmap
            }
        }
        setContent {
            Register_form(this, takePictureLauncher, imageBitmap)
        }
    }
}

@Composable
fun Register_form(context: Context, takePictureLauncher: ActivityResultLauncher<Intent>, imageBitmap: MutableState<Bitmap?>) {
    val visible = remember { mutableStateOf(true) }
    Box(modifier = Modifier.fillMaxSize().background(LighterRed), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Welcome to CuzzApp!",
                style = MaterialTheme.typography.displayMedium,
                color = Pink
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = TextFieldValue(),
                onValueChange = {},
                label = { Text("Username", color = LightOrange) },
                modifier = Modifier.fillMaxWidth(0.8f)
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
// Display the captured image
            imageBitmap.value?.let { bitmap ->
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = "Captured image",
                    modifier = Modifier
                        .size(200.dp) // Set the size of the image
                        .clip(CircleShape) // Clip the image to a circle
                )
            }
            Button(
                onClick = {
                    // Open the camera
                    val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    takePictureLauncher.launch(takePictureIntent)
                },
                colors = ButtonDefaults.buttonColors(contentColor = Pink, backgroundColor = LightYellow),
                modifier = Modifier.fillMaxWidth(0.8f)
            ) {
                Text(text = "Take a picture")
            }
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = { /* Handle registration */ },
                colors = ButtonDefaults.buttonColors(contentColor = Pink, backgroundColor = LightYellow),
                modifier = Modifier.fillMaxWidth(0.8f)
            ) {
                Text(text = "Register")
            }


            Spacer(modifier = Modifier.height(24.dp))
            AnimatedVisibility(
                visible = visible.value,
                enter = slideInVertically(initialOffsetY = { it }, animationSpec = tween(1000))
            ) {
                Button(
                    onClick = {
                        //GO back to Login

                    },
                    colors = ButtonDefaults.buttonColors(contentColor = Pink, backgroundColor = LightYellow),
                    modifier = Modifier.fillMaxWidth(0.8f)
                ) {
                    Text(text = "Already have an account? Log in now!")
                }
            }
        }
    }
}