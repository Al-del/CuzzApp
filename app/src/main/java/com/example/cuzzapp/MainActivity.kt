package com.example.cuzzapp

import AppNavigator
import BottomNavigationBar
import android.app.Activity
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
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.*
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import com.example.cuzzapp.ui.theme.LightOrange
import com.example.cuzzapp.ui.theme.LightYellow
import com.example.cuzzapp.ui.theme.LighterRed
import com.example.cuzzapp.ui.theme.Pink
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream

class MainActivity : ComponentActivity() {
    private lateinit var takePictureLauncher: ActivityResultLauncher<Intent>
    private val imageBitmap = mutableStateOf<Bitmap?>(null)

    @ExperimentalAnimationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        takePictureLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    imageBitmap.value = result.data?.extras?.get("data") as? Bitmap
                }
            }
        setContent {
            RegisterPreview()
        }
    }

    @ExperimentalAnimationApi
    @Composable
    fun LoginPage() {
        val visible = remember { mutableStateOf(true) }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(LighterRed),
            contentAlignment = Alignment.Center
        ) {
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
                    colors = ButtonDefaults.buttonColors(
                        contentColor = Pink,
                        containerColor = LightYellow
                    ),
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
                            //GO to form Register
                            val intent = Intent(this@MainActivity, Register::class.java)
                            startActivity(intent)

                        },
                        colors = ButtonDefaults.buttonColors(
                            contentColor = Pink,
                            containerColor = LightYellow
                        ),
                        modifier = Modifier.fillMaxWidth(0.8f)
                    ) {
                        Text(text = "Don't have an account? Register now!")
                    }
                }
            }
        }
    }

    @Composable
    fun Register(modifier: Modifier = Modifier) {
        var usernames by remember { mutableStateOf(TextFieldValue()) }
        var password_ by remember { mutableStateOf(TextFieldValue()) }
        var confirmPassword by remember { mutableStateOf(TextFieldValue()) }

        Box(
            modifier = modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .background(color = Color(0xff1c1c1e))
            ) {
                Component1(
                    modifier = Modifier
                        .align(alignment = Alignment.TopStart)
                        .offset(
                            x = 35.dp,
                            y = 438.dp
                        ),

                )
                Text(
                    textAlign = TextAlign.Center,
                    text = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                color = Color.White,
                                fontSize = 24.sp
                            )
                        ) { append("Ai deja cont?\n") }
                        withStyle(
                            style = SpanStyle(
                                color = Color(0xff3840ff),
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Black,
                                fontStyle = FontStyle.Italic
                            )
                        ) { append("Logheaza-te aici!") }
                    },
                    modifier = Modifier
                        .align(alignment = Alignment.Center)
                        .offset(
                            x = 0.dp,
                            y = 165.dp
                        )
                        .requiredWidth(width = 300.dp)
                        .requiredHeight(height = 60.dp)
                        .clickable {
                            val intent = Intent(this@MainActivity, Register::class.java)
                            startActivity(intent)
                        }
                )
                Divider(
                    color = Color.White,
                    modifier = Modifier
                        .align(alignment = Alignment.TopStart)
                        .offset(
                            x = 40.dp,
                            y = 244.dp
                        )
                        .requiredWidth(width = 290.dp)
                )
                Divider(
                    color = Color.White,
                    modifier = Modifier
                        .align(alignment = Alignment.TopStart)
                        .offset(
                            x = 40.dp,
                            y = 322.dp
                        )
                        .requiredWidth(width = 290.dp)
                )

                TextField(
                    value = usernames,
                    onValueChange = { usernames = it },
                    label = { Text("username", color = LightOrange) },
                    modifier = Modifier
                        .align(alignment = Alignment.TopCenter)
                        .offset(y = 180.dp)
                        .fillMaxWidth(0.8f),
                    colors = androidx.compose.material.TextFieldDefaults.textFieldColors(
                        textColor = Color.White,
                        backgroundColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent, //hide the indicator
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                )
                TextField(
                    value = password_,
                    onValueChange = { password_ = it },
                    label = { Text("Parola", color = LightOrange) },
                    modifier = Modifier
                        .align(alignment = Alignment.TopCenter)
                        .offset(
                            y = 250.dp
                        )// Adjust the offset to position the TextField above the first divider
                        .fillMaxWidth(0.8f),
                    colors = androidx.compose.material.TextFieldDefaults.textFieldColors(
                        textColor = Color.White,
                        backgroundColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent, //hide the indicator
                        unfocusedIndicatorColor = Color.Transparent
                    ),

                )
                Divider(
                    color = Color.White,
                    modifier = Modifier
                        .align(alignment = Alignment.TopStart)
                        .offset(
                            x = 40.dp,
                            y = 390.dp
                        )
                        .requiredWidth(width = 290.dp)
                )
                TextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    label = { Text("Confirmare Parola", color = LightOrange) },
                    modifier = Modifier
                        .align(alignment = Alignment.TopCenter)
                        .offset(
                            y = 320.dp
                        )// Adjust the offset to position the TextField above the first divider
                        .fillMaxWidth(0.8f),
                    colors = TextFieldDefaults.textFieldColors(
                        textColor = Color.White,
                        backgroundColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent, //hide the indicator
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                )
                Image(
                    painter = painterResource(id = R.drawable.email),
                    contentDescription = "mail",
                    colorFilter = ColorFilter.tint(Color.White),
                    modifier = Modifier
                        .align(alignment = Alignment.TopStart)
                        .offset(
                            x = 300.dp,
                            y = 215.dp
                        )
                        .requiredSize(size = 24.dp)
                )
                Image(
                    painter = painterResource(id = R.drawable.lock),
                    contentDescription = "lock",
                    colorFilter = ColorFilter.tint(Color.White),
                    modifier = Modifier
                        .align(alignment = Alignment.TopStart)
                        .offset(
                            x = 300.dp,
                            y = 290.dp
                        )
                        .requiredSize(size = 24.dp)
                )
                Image(
                    painter = painterResource(id = R.drawable.lock),
                    contentDescription = "lock",
                    colorFilter = ColorFilter.tint(Color.White),
                    modifier = Modifier
                        .align(alignment = Alignment.TopStart)
                        .offset(
                            x = 300.dp,
                            y = 354.dp
                        )
                        .requiredSize(size = 24.dp)
                )
                Button(
                    onClick = {
                        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                        takePictureLauncher.launch(takePictureIntent)
                    },
                    modifier = Modifier
                        .align(Alignment.Center) // Center the button
                        .offset(y = 10.dp) // Move the button down
                        .fillMaxWidth(0.5f)
                        .clip(RoundedCornerShape(10)), // Adjust the corner radius as needed
                    colors = ButtonDefaults.buttonColors(
                        contentColor = Pink,
                        containerColor = Color(0xff2C3E50))
                ) {
                    Text(text = "Take picture")
                }
             imageBitmap.value?.let { bitmap ->
    Image(
        bitmap = bitmap.asImageBitmap(),
        contentDescription = "Captured image",
        modifier = Modifier
            .offset(y = 30.dp,
                x = 10.dp) // Move the image down
            .size(50.dp) // Set the size of the image
            .clip(CircleShape) // Clip the image to a circle
            .align(Alignment.TopStart) // Align the image to the top left of the screen
    )
}
            }
        }
        Box(
            modifier = modifier
                //.align(alignment = Alignment.TopStart)
                .offset(
                    x = 35.dp,
                    y = 438.dp
                )
                .requiredWidth(width = 290.dp)
                .requiredHeight(height = 50.dp)

        ) {
            Box(
                modifier = Modifier
                     .align(Alignment.Center) // Center the box
                    .offset(y = 50.dp) // Move the box down
                    .fillMaxWidth(0.5f)
                    .clip(RoundedCornerShape(10)) // Adjust the corner radius as needed
                    .requiredWidth(width = 290.dp)
                    .requiredHeight(height = 50.dp)
                    .fillMaxSize()
                    .clip(shape = RoundedCornerShape(80.dp))
                    .background(color = Color(0xff2c3e50))

            )
            Text(
                text = "REGISTER",
                color = Color.White,
                textAlign = TextAlign.Center,
                style = TextStyle(
                    fontSize = 19.sp,
                    shadow = Shadow(
                        color = Color.Black.copy(alpha = 0.25f),
                        offset = Offset(0f, 4f),
                        blurRadius = 4f
                    )
                ),

                modifier = Modifier
                    .align(Alignment.Center) // Center the button
                    .offset(y = 50.dp) // Move the button down
                    .fillMaxWidth(0.5f)
                    .clip(RoundedCornerShape(10)) // Adjust the corner radius as needed
                    .clickable {

                        val user = User().apply {
                            name = usernames.text // Replace with actual username
                            password = password_.text // Replace with actual password
                            Points = 0
                            state = "" // Replace with actual state
                        }

                        // Get a reference to the Firebase database
                        val database = FirebaseDatabase.getInstance()

                        // Get a reference to the "accounts" node
                        val accountsRef = database.getReference("accounts")

                        // Convert the Bitmap to a ByteArray
                        val baos = ByteArrayOutputStream()
                        imageBitmap.value?.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                        val data = baos.toByteArray()

                        // Get a reference to Firebase Storage
                        val storage = Firebase.storage

                        // Create a storage reference
                        val storageRef = storage.reference

                        // Create a reference to the file you want to upload
                        val imageRef = storageRef.child("images/${user.name}.jpg")

                        // Upload the file to Firebase Storage
                        val uploadTask = imageRef.putBytes(data)

                        // Register observers to listen for when the download is done or if it fails
                        uploadTask.addOnFailureListener {
                            // Handle unsuccessful uploads
                        }.addOnSuccessListener { taskSnapshot ->
                            // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
                            // Get the download URL of the uploaded file
                            imageRef.downloadUrl.addOnSuccessListener { uri ->
                                // Add the download URL to the User object
                                user.photoUrl = uri.toString()

                                // Write the User object to the "accounts" node
                                accountsRef.child(user.name).setValue(user)
                            }
                        }
                    }
            )
        }
    }

    @Composable
    fun Component1(modifier: Modifier = Modifier) {

    }

    @Preview(widthDp = 360, heightDp = 800)
    @Composable
    private fun RegisterPreview() {
        Register(Modifier)
    }
}