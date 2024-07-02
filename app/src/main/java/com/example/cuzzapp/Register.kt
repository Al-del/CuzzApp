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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
        takePictureLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                imageBitmap.value = result.data?.extras?.get("data") as? Bitmap
            }
        }
        setContent {
        //    Register_form(this, takePictureLauncher, imageBitmap)
            LogInPreview()
        }
    }
}






@Composable
fun LogIn(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    var usernames by remember { mutableStateOf(TextFieldValue()) }
    var password_ by remember { mutableStateOf(TextFieldValue()) }
    var navigateToOtherActivity by remember { mutableStateOf(false) }
    Box(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(color = Color(0xff1c1c1e))
    ) {
        Box(
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(
                    x = 35.dp,
                    y = 438.dp
                )
                .requiredWidth(width = 290.dp)
                .requiredHeight(height = 50.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(shape = RoundedCornerShape(80.dp))
                    .background(color = Color(0xff2c3e50)))
            Button(
    onClick = {

        val database = FirebaseDatabase.getInstance()
        val accountsRef = database.getReference("accounts")

// Replace these with the actual username and password you are looking for
        val usernameToFind = usernames.text
        val passwordToFind = password_.text

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
                        achivement = get_achievements_from_db(userSnapshot, achivement)
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
    },
    modifier = Modifier
        .fillMaxSize()
        .fillMaxWidth(0.5f)
        .clip(RoundedCornerShape(10)),
    colors = ButtonDefaults.buttonColors(
        contentColor = Color.White, // Change this to your desired color
        backgroundColor = Color(0xff2C3E50))
) {
    Text("Log in", color = Color.White)
}

        }
        Text(
            textAlign = TextAlign.Center,
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(
                    color = Color.White,
                    fontSize = 24.sp)
                ) {append("Nu ai cont inca?")}
                withStyle(style = SpanStyle(
                    color = Color(0xff3840ff),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Black,
                    fontStyle = FontStyle.Italic)) {append("Inregistreaza-te aici!")}},
            modifier = Modifier
                .align(alignment = Alignment.Center)
                .offset(
                    x = 0.dp,
                    y = 165.dp
                )
                .requiredWidth(width = 300.dp)
                .requiredHeight(height = 60.dp)
                .clickable {
                    val intent = Intent(context, MainActivity::class.java)
                    context.startActivity(intent)
                }
        )

        Divider(
            color = Color.White,
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(
                    x = 35.dp,
                    y = 309.dp
                )
                .requiredWidth(width = 290.dp))
        Divider(
            color = Color.White,
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(
                    x = 36.dp,
                    y = 387.dp
                )
                .requiredWidth(width = 290.dp))
        ProvideTextStyle(TextStyle(color = Color.White)) {


            TextField(
                value = usernames,
                onValueChange = {
                    usernames = it
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .align(alignment = Alignment.Center)

                    .offset(
                        x = (-50.5).dp,
                        y = (-135).dp
                    )
                    .requiredWidth(width = 230.dp)
                    .requiredHeight(height = 50.dp),

                //  shape = RoundedCornerShape(8.dp),

                colors = TextFieldDefaults.textFieldColors(
                    textColor = Color.White,
                    backgroundColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent, //hide the indicator
                    unfocusedIndicatorColor = Color.Transparent
                ),
                label = { Text("Username") },
            )


            /*  TextField(
                value = usernames,
                onValueChange = { usernames = it },
                colors = TextFieldDefaults.textFieldColors(
                    textColor = Color.White // Set the text color to white
                ),
                modifier = Modifier
                    .align(alignment = Alignment.Center)
                    .offset(
                        x = (-50.5).dp,
                        y = (-135).dp
                    )
                    .requiredWidth(width = 230.dp)
                    .requiredHeight(height = 28.dp)
            )*/


            TextField(
                value = password_,
                onValueChange = {password_ = it},
                modifier = Modifier
                    .align(alignment = Alignment.Center)
                    .offset(
                        x = (-46).dp,
                        y = (-71).dp
                    )
                    .requiredWidth(width = 230.dp)
                    .requiredHeight(height = 50.dp),
                colors = TextFieldDefaults.textFieldColors(
                    textColor = Color.White,
                    backgroundColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent, //hide the indicator
                    unfocusedIndicatorColor = Color.Transparent
                ),
                label = { Text("Password") },
            )
        }
        Text(
            text = "Ti-ai uitat parola?",
            color = Color.White,
            textAlign = TextAlign.Center,
            style = TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight.Black),
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(
                    x = 5.dp,
                    y = 400.dp
                )
                .requiredWidth(width = 217.dp)
                .requiredHeight(height = 22.dp))
        Image(
            painter = painterResource(id = R.drawable.email),
            contentDescription = "mail",
            colorFilter = ColorFilter.tint(Color.White),
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(
                    x = 301.dp,
                    y = 281.dp
                )
                .requiredSize(size = 24.dp))
        Image(
            painter = painterResource(id = R.drawable.lock),
            contentDescription = "lock",
            colorFilter = ColorFilter.tint(Color.White),
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(
                    x = 300.dp,
                    y = 353.dp
                )
                .requiredSize(size = 24.dp))
        Image(
            painter = painterResource(id = R.drawable.cuzzapp),
            contentDescription = "Icon",

            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(
                    x = 100.dp,
                    y = 70.dp
                )
                .requiredSize(size = 170.dp)
                .clip(shape = RoundedCornerShape(100.dp)))

    }
LaunchedEffect(navigateToOtherActivity) {
    if (navigateToOtherActivity) {
        val intent = Intent(context, HomeScreen()::class.java)
        context.startActivity(intent)
    }
}
}

@Preview(widthDp = 360, heightDp = 800)
@Composable
private fun LogInPreview() {
    LogIn(Modifier)
}
