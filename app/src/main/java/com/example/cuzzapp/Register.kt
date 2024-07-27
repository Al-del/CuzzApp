package com.example.cuzzapp

import achivement
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
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
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Divider
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
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
import androidx.compose.ui.zIndex
import com.example.cuzzapp.ui.theme.LightOrange
import com.example.cuzzapp.ui.theme.LightYellow
import com.example.cuzzapp.ui.theme.LighterRed
import com.example.cuzzapp.ui.theme.Pink
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import descriptiones
import get_achievements_from_db
import learningPath
import mail
import points
import state
import url_photo
import username_true
import java.io.ByteArrayOutputStream


class Register : ComponentActivity() {
    private lateinit var takePictureLauncher: ActivityResultLauncher<Intent>
    private val imageBitmap = mutableStateOf<Bitmap?>(null)

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
            //    Register_form(this, takePictureLauncher, imageBitmap)
           // LogInPreview()
            RegisterPrev()
        }
    }

    @Composable
    fun LoginScreen1(modifier: Modifier = Modifier) {
        var username by remember { mutableStateOf("") }
        var selectedRole by remember { mutableStateOf("Student") }
        val context = LocalContext.current
        var password by remember { mutableStateOf("") }
        var navigateToOtherActivity by remember { mutableStateOf(false) }
        Box(
            modifier = modifier
                .fillMaxSize()
                .clip(shape = RoundedCornerShape(26.dp))
                .background(color = Color(0xff151316))
        ) {
            Column(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .offset(y = 390.dp)
                    .fillMaxWidth()
            )
            {
                Text("Select Role", color = LightOrange)
                Row {
                    RadioButton(
                        selected = selectedRole == "Student",
                        onClick = {
                            selectedRole = "Student"
                        }
                    )
                    Text("Student", color = LightOrange)
                }
                Row {
                    RadioButton(
                        selected = selectedRole == "Teacher",
                        onClick = { selectedRole = "Teacher" }
                    )
                    Text("Teacher", color = LightOrange)
                }
            }
            Background(
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
            )
            Box(
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(
                        x = (-32).dp,
                        y = 120.dp
                    )
                    .fillMaxSize()
            ) {
                Image(
                    painter = painterResource(id = R.drawable.left),
                    contentDescription = "front shapes",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .align(alignment = Alignment.TopEnd)
                        .offset(
                            x = (-405.2572844753013).dp,
                            y = 16.00000273045316.dp
                        )
                        .requiredWidth(width = 65.dp)
                        .requiredHeight(height = 54.dp)
                        .blur(
                            radius = 22.dp,
                            edgeTreatment = BlurredEdgeTreatment.Unbounded
                        )
                        .rotate(degrees = 66.77f)
                )
                Image(
                    painter = painterResource(id = R.drawable.right),
                    contentDescription = "front shapes",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .align(alignment = Alignment.TopEnd)
                        .offset(
                            x = (-16.590870573927532).dp,
                            y = 52.99998732497261.dp
                        )
                        .requiredWidth(width = 102.dp)
                        .requiredHeight(height = 69.dp)
                        .blur(
                            radius = 4.dp,
                            edgeTreatment = BlurredEdgeTreatment.Unbounded
                        )
                        .rotate(degrees = 18.69f)
                )
                Image(
                    painter = painterResource(id = R.drawable.left),
                    contentDescription = "Saly-16",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .align(alignment = Alignment.TopEnd)
                        .offset(
                            x = (-0.403045654296875).dp,
                            y = 0.dp
                        )
                        .requiredWidth(width = 90.dp)
                        .requiredHeight(height = 197.dp)
                        .blur(
                            radius = 9.dp,
                            edgeTreatment = BlurredEdgeTreatment.Unbounded
                        )
                )
            }
            Box(
                modifier = Modifier
                    .align(alignment = Alignment.Center)
                    .offset(
                        y = 70.dp
                    )
                    .requiredWidth(width = 314.dp)
                    .requiredHeight(height = 50.dp)
            ) {
                Box(
                    modifier = Modifier
                        .align(alignment = Alignment.TopEnd)
                        .offset(
                            x = 0.dp,
                            y = 0.dp
                        )
                        .fillMaxHeight()
                        .requiredWidth(width = 314.dp)
                        .clip(shape = RoundedCornerShape(15.dp))
                        .background(
                            brush = Brush.linearGradient(
                                0f to Color(0xff9c3fe4),
                                1f to Color(0xffc65647),
                                start = Offset(0f, 15.68f),
                                end = Offset(320f, 23.31f)
                            )
                        )
                        .clickable {

                            val database = FirebaseDatabase.getInstance()
                            val accountsRef = database.getReference("accounts")

// Replace these with the actual username and password you are looking for
                            val usernameToFind = username
                            val passwordToFind = password

                            accountsRef.addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onDataChange(dataSnapshot: DataSnapshot) {
                                    for (userSnapshot in dataSnapshot.children) {
                                        val user = userSnapshot.getValue(User::class.java)
                                        if (user?.name == usernameToFind && user.password == passwordToFind) {
                                            // User found
                                            println("User found: ${user.name}")
                                            println("Link to profile: ${user.photoUrl}")
                                            println("Points: ${user.Points}")
                                            println("Stateteus: ${user.Points} ${user.email}")
                                            username_true = user.name
                                            state = user.role
                                            url_photo = user.photoUrl
                                            points = user.Points
                                            descriptiones = user.state
                                            learningPath = user.learningPath
                                            mail = user.email
                                            achivement =
                                                get_achievements_from_db(userSnapshot, achivement)
                                            navigateToOtherActivity = true

                                        }
                                    }
                                }

                                override fun onCancelled(databaseError: DatabaseError) {
                                    // Handle possible errors.
                                    println("The read failed: " + databaseError.code)
                                }
                            })
                            Log.d("TAG", "onClick: $state")
                        })
                Box(
                    modifier = Modifier
                        .align(alignment = Alignment.TopEnd)
                        .offset(
                            x = 0.dp,
                            y = 20.dp
                        )
                        .fillMaxHeight()
                        .requiredWidth(width = 314.dp)
                        .clip(shape = RoundedCornerShape(15.dp))
                        .background(
                            brush = Brush.linearGradient(
                                0f to Color(0xff9c3fe4),
                                1f to Color(0xffc65647),
                                start = Offset(0f, 15.68f),
                                end = Offset(320f, 23.31f)
                            )
                        )
                )
                Text(
                    text = "Sign in",
                    color = Color.White,
                    style = TextStyle(
                        fontSize = 17.918184280395508.sp,
                        fontWeight = FontWeight.Medium
                    ),
                    modifier = Modifier
                        .align(alignment = Alignment.Center)
                        .offset(
                            y = 20.dp
                        )
                        .fillMaxHeight()
                )
            }
            Box(
                modifier = Modifier
                    .align(alignment = Alignment.Center)
                    .offset(y = (-120).dp, x = 20.dp)
                    .requiredWidth(width = 314.dp)
                    .requiredHeight(height = 55.dp)
            ) {
                Box(
                    modifier = Modifier
                        .requiredWidth(width = 314.dp)
                        .requiredHeight(height = 55.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .align(alignment = Alignment.Center)
                            .offset(
                                x = -20.dp,
                                y = 0.dp
                            )
                            .requiredWidth(width = 314.dp)
                            .requiredHeight(height = 55.dp)
                            .clip(shape = RoundedCornerShape(8.dp))
                            .background(
                                brush = Brush.radialGradient(
                                    0f to Color.White,
                                    0.77f to Color.White,
                                    1f to Color.White,
                                    center = Offset(48.1f, 11.57f),
                                    radius = 398.85f
                                )
                            )
                    )
                }
                Image(
                    painter = painterResource(id = R.drawable.email),
                    contentDescription = "Vector",
                    modifier = Modifier
                        .align(alignment = Alignment.TopEnd)
                        .offset(
                            x = (-286.61309242248535).dp,
                            y = 20.dp
                        )
                        .requiredWidth(width = 25.dp)
                        .requiredHeight(height = 25.dp)
                )

            }

            TextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Username", color = Color.Black) },
                textStyle = TextStyle(
                    color = Color(0xffa4a4a4),
                    fontSize = 14.329999923706055.sp,
                    fontWeight = FontWeight.Medium
                ),
                modifier = Modifier
                    .align(Alignment.Center)
                    .offset(y = -(120).dp, x = 40.dp)
            )

         Box(
    modifier = Modifier
        .align(Alignment.Center)
        .offset(y = -45.dp, x = 40.dp)
        .zIndex(1f) // Ensure the TextField is above other elements
) {
    TextField(
        value = password,
        onValueChange = { password = it },
        label = { Text("Password", color = Color.Black) },
        textStyle = TextStyle(
            fontSize = 14.329999923706055.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Black
        ),
        visualTransformation = PasswordVisualTransformation(),
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Black,
            focusedIndicatorColor = Color.Black
        )
    )
}

            Box(
                modifier = Modifier
                    .align(alignment = Alignment.Center)
                    .offset(y = (-40).dp)
                    .requiredWidth(width = 314.dp)
                    .requiredHeight(height = 55.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                ) {
                    Box(
                        modifier = Modifier
                            .align(alignment = Alignment.TopCenter)
                            .offset(
                                x = 0.dp,
                                y = 0.dp
                            )
                            .requiredWidth(width = 314.dp)
                            .requiredHeight(height = 55.dp)
                            .clip(shape = RoundedCornerShape(8.dp))
                            .background(
                                brush = Brush.radialGradient(
                                    0f to Color.White,
                                    0.77f to Color.White,
                                    1f to Color.White,
                                    center = Offset(48.1f, 11.57f),
                                    radius = 398.85f
                                )
                            )

                    )
                }
                Image(
                    painter = painterResource(id = R.drawable.lock),
                    contentDescription = "Vector",
                    modifier = Modifier
                        .align(alignment = Alignment.TopEnd)
                        .offset(
                            x = (-270.69103050231934).dp,
                            y = 19.6986083984375.dp
                        )
                        .requiredSize(size = 25.dp)
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(
                        3.0304877758026123.dp,
                        Alignment.Start
                    ),
                    modifier = Modifier
                        .align(alignment = Alignment.TopEnd)
                        .offset(
                            x = (-185).dp,
                            y = 25.dp
                        )
                ) {

                }
            }
            Box(
                modifier = Modifier
                    .align(alignment = Alignment.TopCenter)
                    .offset(

                        y = 51.dp
                    )
                    .requiredWidth(width = 365.dp)
                    .requiredHeight(height = 102.dp)
            ) {
                Text(
                    text = "Cuzz we care",
                    color = Color(0xffa4a4a4),
                    style = TextStyle(
                        fontSize = 34.329999923706055.sp,
                        fontWeight = FontWeight.Medium
                    ),
                    modifier = Modifier
                        .align(alignment = Alignment.TopEnd)
                        .offset(
                            x = (-140).dp,
                            y = 81.dp
                        )
                )
                Box(
                    modifier = Modifier
                        .requiredWidth(width = 365.dp)
                        .requiredHeight(height = 60.dp)
                ) {
                    Text(
                        text = "Login to CuzzApp!",
                        color = Color(0xffefefef),
                        style = TextStyle(
                            fontSize = 40.328765869140625.sp
                        ),
                        modifier = Modifier
                            .align(alignment = Alignment.TopEnd)
                            .offset(

                                y = 0.dp
                            )
                    )
                }
            }
            Image(
                painter = painterResource(id = R.drawable.om),
                contentDescription = "Illustration",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .align(alignment = Alignment.BottomEnd)
                    .offset(
                        x = 43.dp,
                        y = 133.dp
                    )
                    .requiredWidth(width = 368.dp)
                    .requiredHeight(height = 384.dp)
            )
            Text(
                text = "Don’t have an account?",
                color = Color(0xffa4a4a4),
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium
                ),
                modifier = Modifier
                    .align(alignment = Alignment.Center)
                    .offset(
                        y = 550.dp
                    )
                    .clickable {
                        val intent = Intent(context, MainActivity::class.java)
                        startActivity(intent)
                    }
                    .fillMaxHeight()
                    .requiredWidth(width = 303.dp)
            )
        }
        LaunchedEffect(navigateToOtherActivity) {
            if (navigateToOtherActivity) {
                val intent = Intent(context, HomeScreen()::class.java)
                context.startActivity(intent)
            }
        }
    }


    @Composable
    fun Background(modifier: Modifier = Modifier) {
        Box(
            modifier = modifier
                .fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.radialGradient(
                            0f to Color(0xffb379df),
                            1f to Color(0xff360060),
                            center = Offset(198.5f, 294.21f),
                            radius = 198.5f
                        )
                    )
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.radialGradient(
                            0f to Color(0xffc45647),
                            1f to Color(0xffd25a63),
                            center = Offset(198.5f, 294.21f),
                            radius = 198.5f
                        )
                    )
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(shape = RoundedCornerShape(59.dp))
                    .background(
                        brush = Brush.radialGradient(
                            0f to Color(0xffb379df),
                            0.77f to Color(0xffcc5854).copy(alpha = 0.08f),
                            1f to Color(0xffb379df),
                            center = Offset(71.46f, 261.33f),
                            radius = 592.52f
                        )
                    )
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.radialGradient(
                            0f to Color(0xffb379df),
                            1f to Color(0xff360060),
                            center = Offset(186.64f, 546.44f),
                            radius = 186.64f
                        )
                    )
            )
        }
    }
    @Preview
    @Composable
    fun RegisterPrev() {
        LoginScreen1(Modifier)
    }
}