package com.example.cuzzapp


import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
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
import androidx.compose.material.*
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.key.Key.Companion.Notification
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
import androidx.core.app.NotificationCompat
import androidx.navigation.compose.rememberNavController
import com.example.cuzzapp.ui.theme.LightOrange
import com.example.cuzzapp.ui.theme.LightYellow
import com.example.cuzzapp.ui.theme.LighterRed
import com.example.cuzzapp.ui.theme.Pink
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import state
import java.io.ByteArrayOutputStream

class MainActivity : ComponentActivity() {
    private lateinit var takePictureLauncher: ActivityResultLauncher<Intent>
    private val imageBitmap = mutableStateOf<Bitmap?>(null)

    @ExperimentalAnimationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, HydrationReminderReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        // Schedule the alarm to repeat every 30 minutes
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            System.currentTimeMillis(),
            30*1000*60, // 30 minutes in milliseconds
            pendingIntent
        )

        val breakReminderIntent = Intent(this, BreakReminderReceiver::class.java)
        val breakReminderPendingIntent = PendingIntent.getBroadcast(this, 1, breakReminderIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
// Schedule the alarm to repeat every 2 hours
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            System.currentTimeMillis(),
            120*60*1000, // 2 hours in milliseconds
            breakReminderPendingIntent
        )
        enableEdgeToEdge()
        takePictureLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    imageBitmap.value = result.data?.extras?.get("data") as? Bitmap
                }
            }
        setContent {
            //  RegisterPreview()
          //  LoginScreen1Preview()
            RegisterPrev()

        }
    }
    @SuppressLint("SuspiciousIndentation")
    @Composable
    fun Register_Screen(modifier: Modifier = Modifier) {
        var email by remember { mutableStateOf("") }
        var username by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
    val context = LocalContext.current
        Box(
            contentAlignment = Alignment.TopEnd,
            modifier = modifier
                .fillMaxSize()
                .clip(shape = RoundedCornerShape(26.dp))
                .background(color = Color(0xffb51316))
        ) {
            Image(
                painter = painterResource(id = R.drawable.saly16),
                contentDescription = "Saly-16",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .blur(
                        radius = 9.dp,
                        edgeTreatment = BlurredEdgeTreatment.Unbounded
                    ))
            Box(
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(
                        x = (-215).dp,
                        y = 126.dp
                    )
                    .requiredWidth(width = 904.dp)
                    .requiredHeight(height = 1037.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(shape = CircleShape)
                        .background(
                            brush = Brush.radialGradient(
                                0f to Color(0xffb379df),
                                1f to Color(0xff360060),
                                center = Offset(198.5f, 198.5f),
                                radius = 198.5f
                            )
                        ))
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(shape = CircleShape)
                        .background(
                            brush = Brush.radialGradient(
                                0f to Color(0xffb379df),
                                1f to Color(0xff360060),
                                center = Offset(198.5f, 198.5f),
                                radius = 198.5f
                            )
                        ))

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .offset(y = 150.dp)
                        .clip(shape = RoundedCornerShape(159.dp))
                        .background(
                            brush = Brush.radialGradient(
                                0f to Color(0xffb379df),
                                0.77f to Color(0xffcc5854).copy(alpha = 0.08f),
                                1f to Color(0xffb379df),
                                center = Offset(68.48f, 168.96f),
                                radius = 567.79f
                            )
                        )
                        )
            }
            Box(
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(
                        x = 51.dp,
                        y = 281.dp
                    )
                    .requiredWidth(width = 330.dp)
                    .requiredHeight(height = 73.dp)
            ) {
                Text(
                    text = "Get Started",
                    color = Color(0xffefefef),
                    style = TextStyle(
                        fontSize = 40.328765869140625.sp),
                    )

            }
            Box(
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(
                        x = 58.dp,
                        y = 689.dp
                    )

                    .requiredWidth(width = 314.dp)
                    .requiredHeight(height = 50.dp)
            ) {
                Box(
                    modifier = Modifier
                        .requiredWidth(width = 314.dp)
                        .requiredHeight(height = 50.dp)
                        .offset(y = -50.dp)
                        .clip(shape = RoundedCornerShape(15.dp))
                        .clickable {

                            val user = User().apply {
                                name = username // Replace with actual username
                                Points = 0
                                state = "" // Replace with actual state
                                role = "Student"

                            }
                            user.email = email
                            user.password = password
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
                        .background(
                            brush = Brush.linearGradient(
                                0f to Color(0xff9c3fe4),
                                1f to Color(0xffc65647),
                                start = Offset(0f, 15.68f),
                                end = Offset(320f, 23.31f)
                            )
                        )

                ) {
                    Text(
                        text = "Sign up",
                        color = Color.White,
                        style = TextStyle(
                            fontSize = 17.918184280395508.sp,
                            fontWeight = FontWeight.Medium
                        ),
                        modifier = Modifier
                            .align(alignment = Alignment.TopStart)
                            .offset(
                                x = 130.dp,
                                y = 11.dp
                            )
                    )
                }
            }
            Box(
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(
                        x = 59.dp,
                        y = 513.dp
                    )
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
                            .requiredWidth(width = 314.dp)
                            .requiredHeight(height = 55.dp)
                            .offset(y = -50.dp)
                            .clip(shape = RoundedCornerShape(8.dp))

                            .background(
                                brush = Brush.radialGradient(
                                    0f to Color.White,
                                    0.77f to Color.White.copy(alpha = 0.22f),
                                    1f to Color.White,
                                    center = Offset(48.1f, 11.57f),
                                    radius = 398.85f
                                )
                            ))
                    TextField(
                        value = username,
                        onValueChange = { username = it },
                        textStyle = TextStyle(
                            color = Color(0xffa4a4a4),
                            fontSize = 14.329999923706055.sp,
                            fontWeight = FontWeight.Medium
                        ),
                        modifier = Modifier
                            .align(alignment = Alignment.TopStart)
                            .offset(x = 38.dp, y = -50.dp),
                        colors = TextFieldDefaults.textFieldColors(
                            backgroundColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent
                        ),
                        label = { Text("Username") }
                    )
                }
                Image(
                    painter = painterResource(id = R.drawable.profilus),
                    contentDescription = "Vector",
                    modifier = Modifier
                        .fillMaxSize()
                        .offset(x = 100.dp, y = -50.dp))

            }
            Text(
                text = "Email Adress",
                color = Color(0xffa4a4a4),
                style = TextStyle(
                    fontSize = 14.329999923706055.sp,
                    fontWeight = FontWeight.Medium),
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(
                        x = 58.dp,
                        y = 336.dp
                    ))
            Text(
                text = "Your Name",
                color = Color(0xffa4a4a4),
                style = TextStyle(
                    fontSize = 14.329999923706055.sp,
                    fontWeight = FontWeight.Medium),
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(
                        x = 58.dp,
                        y = 435.dp
                    ))

            Text(
                text = "Password",
                color = Color(0xffa4a4a4),
                style = TextStyle(
                    fontSize = 14.329999923706055.sp,
                    fontWeight = FontWeight.Medium),
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(
                        x = 58.dp,
                        y = 520.dp
                    ))
            Box(
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(
                        x = 59.dp,
                        y = 607.dp
                    )
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
                            .requiredWidth(width = 314.dp)
                            .requiredHeight(height = 55.dp)
                            .offset(y = -60.dp)
                            .background(
                                brush = Brush.radialGradient(
                                    0f to Color.White,
                                    0.77f to Color.White.copy(alpha = 0.22f),
                                    1f to Color.White,
                                    center = Offset(48.1f, 11.57f),
                                    radius = 398.85f
                                )
                            ))
                    TextField(
                        value = password,
                        onValueChange = { password = it },
                        textStyle = TextStyle(
                            color = Color(0xffa4a4a4),
                            fontSize = 14.329999923706055.sp,
                            fontWeight = FontWeight.Medium
                        ),
                        modifier = Modifier
                            .align(alignment = Alignment.TopStart)
                            .offset(x = 38.dp, y = -57.dp),
                        colors = TextFieldDefaults.textFieldColors(
                            backgroundColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent
                        ),
                        label = { Text(text ="Password")}
                    )
                }
                Image(
                    painter = painterResource(id = R.drawable.key),
                    contentDescription = "Vector",
                    modifier = Modifier
                        .fillMaxSize()
                        .offset(x = 100.dp, y = -60.dp))

            }
            Box(
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(
                        x = (-14).dp,
                        y = (-37).dp
                    )
                    .requiredWidth(width = 477.dp)
                    .requiredHeight(height = 418.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.frontshapes),
                    contentDescription = "front shapes",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .align(alignment = Alignment.TopStart)
                        .offset(
                            x = (-0.000013662312994711101).dp,
                            y = 283.00000273045316.dp
                        )
                        .requiredWidth(width = 65.dp)
                        .requiredHeight(height = 54.dp)
                        .blur(
                            radius = 22.dp,
                            edgeTreatment = BlurredEdgeTreatment.Unbounded
                        )
                        .rotate(degrees = 66.77f))
                Image(
                    painter = painterResource(id = R.drawable.frontshapes),
                    contentDescription = "front shapes",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .align(alignment = Alignment.TopStart)
                        .offset(
                            x = 10.000067256999046.dp,
                            y = 18.000020851152385.dp
                        )
                        .requiredWidth(width = 104.dp)
                        .requiredHeight(height = 85.dp)
                        .blur(
                            radius = 20.dp,
                            edgeTreatment = BlurredEdgeTreatment.Unbounded
                        )
                        .rotate(degrees = 33.41f))
                Image(
                    painter = painterResource(id = R.drawable.frontshapes),
                    contentDescription = "front shapes",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .align(alignment = Alignment.TopStart)
                        .offset(
                            x = 351.9999802561506.dp,
                            y = 319.9999873249726.dp
                        )
                        .requiredWidth(width = 102.dp)
                        .requiredHeight(height = 69.dp)
                        .blur(
                            radius = 4.dp,
                            edgeTreatment = BlurredEdgeTreatment.Unbounded
                        )
                        .rotate(degrees = 18.69f))
                Image(
                    painter = painterResource(id = R.drawable.frontshapes),
                    contentDescription = "front shapes",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .align(alignment = Alignment.TopStart)
                        .offset(
                            x = 358.9999802561506.dp,
                            y = (-0.00001267502739210613).dp
                        )
                        .requiredWidth(width = 102.dp)
                        .requiredHeight(height = 69.dp)
                        .blur(
                            radius = 4.dp,
                            edgeTreatment = BlurredEdgeTreatment.Unbounded
                        )
                        .rotate(degrees = 18.69f))
            }
            Box(
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(
                        x = 58.dp,
                        y = 419.dp
                    )
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
                            .requiredWidth(width = 314.dp)
                            .requiredHeight(height = 55.dp)
                            .offset(y = -50.dp)
                            .clip(shape = RoundedCornerShape(8.dp))

                            .background(
                                brush = Brush.radialGradient(
                                    0f to Color.White,
                                    0.77f to Color.White.copy(alpha = 0.22f),
                                    1f to Color.White,
                                    center = Offset(48.1f, 11.57f),
                                    radius = 398.85f
                                )
                            ))
                }

TextField(
    value = email,
    onValueChange = { email = it },
    textStyle = TextStyle(
        color = Color(0xffa4a4a4),
        fontSize = 14.329999923706055.sp,
        fontWeight = FontWeight.Medium
    ),
    modifier = Modifier
        .align(alignment = Alignment.TopStart)
        .offset(x = 30.dp, y = -50.dp),
    colors = TextFieldDefaults.textFieldColors(
        backgroundColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent,
        focusedIndicatorColor = Color.Transparent
    ),
    label = { Text("Email address") }
)
                Image(
                    painter = painterResource(id = R.drawable.email),
                    contentDescription = "Vector",
                    modifier = Modifier
                        .fillMaxSize()
                        .offset(y = -45.dp, x = 100.dp))
            }
            Box(
                modifier = Modifier
                    .align(alignment = Alignment.TopEnd)
                    .offset(
                        y = 689.dp
                    )
                    .requiredWidth(width = 338.dp)
                    .requiredHeight(height = 238.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.om_phone),
                    contentDescription = "Avatar",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize())
                Box(
                    modifier = Modifier
                        .align(alignment = Alignment.TopStart)
                        .offset(
                            x = 85.dp,
                            y = 15.000002313716038.dp
                        )
                        .requiredWidth(width = 40.dp)
                        .requiredHeight(height = 40.dp)
                        .clip(shape = CircleShape)
                        .blur(
                            radius = 4.dp,
                            edgeTreatment = BlurredEdgeTreatment.Unbounded
                        )
                        .rotate(degrees = 0.4f)
                        .background(color = Color(0xffe1dfdf)))
                Image(
                    painter = painterResource(id = R.drawable.image1),
                    contentDescription = "image 1",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .align(alignment = Alignment.TopStart)
                        .offset(
                            x = 89.dp,
                            y = 20.dp
                        )
                        .requiredWidth(width = 24.dp)
                        .requiredHeight(height = 30.dp))
            }
            Text(
                text = "Have an account?",
                color = Color(0xffa4a4a4),
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium),
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(
                        x = 40.dp,
                        y = 754.dp
                    )
                    .requiredWidth(width = 303.dp)
                    .requiredHeight(height = 23.dp))
            Text(
                text = "Login",
                color = Color.White,
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium),
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(
                        x = 72.dp,
                        y = 804.dp
                    )
                    .clickable {
                        val intent = Intent(context, Register::class.java)
                        startActivity(intent)
                    }
                    .requiredWidth(width = 163.dp)
                    .requiredHeight(height = 36.dp))
            Image(
                painter = painterResource(id = R.drawable.om_book),
                contentDescription = "Illustration",
                contentScale = ContentScale.Crop,
                alignment = Alignment.TopEnd,
                modifier = Modifier
                  )
            Button(
                onClick = {
                    val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    takePictureLauncher.launch(takePictureIntent)
                },
                modifier = Modifier
                    .align(Alignment.Center)
                    .offset(y = 195.dp, x = 50.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent)
            ) {
                Text(
                    text = "Take profile picture",
                    color = Color(0xffa4a4a4),
                    style = TextStyle(
                        fontSize = 11.329999923706055.sp,
                        fontWeight = FontWeight.Medium
                ))
            }
        }

    }

    @Composable
    private fun RegisterPrev() {
        Register_Screen(Modifier)
    }
}


class HydrationReminderReceiver : BroadcastReceiver() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context, intent: Intent) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                "hydration_reminder",
                "Hydration Reminder",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(notificationChannel)
        }

        val notification = NotificationCompat.Builder(context, "hydration_reminder")
            .setContentTitle("Nu uita sa te hidratezi!")
            .setContentText("Apa este foart iportanta si racoritoare!\uD83E\uDD5B")
            .setSmallIcon(R.drawable.ic_launcher_foreground) // Replace with your app's icon
            .build()
        notificationManager.notify(0, notification)
    }
}

class BreakReminderReceiver : BroadcastReceiver() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context, intent: Intent) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                "break_reminder",
                "Break Reminder",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(notificationChannel)
        }

        val notification = NotificationCompat.Builder(context, "break_reminder")
            .setContentTitle("Ia o pauza!")
            .setContentText("Pune-ti sangele in miscare!\uD83C\uDFC3")
            .setSmallIcon(R.drawable.ic_launcher_foreground) // Replace with your app's icon
            .build()
        notificationManager.notify(1, notification)
    }
}