package com.example.cuzzapp

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import coil.compose.rememberImagePainter
import com.chaquo.python.Python
import com.chaquo.python.android.AndroidPlatform
import com.example.cuzzapp.ui.theme.CuzzAppTheme
import com.example.cuzzapp.ui.theme.Pink
import org.osmdroid.library.BuildConfig
import java.io.File
import java.util.Date
import java.util.Locale
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import username_true
import java.io.ByteArrayOutputStream
class book_class{
    var url:String = ""
    var user:String = ""
}
class Image__to_book : ComponentActivity() {
    private lateinit var takePictureLauncher: ActivityResultLauncher<Intent>
    private val imageBitmap = mutableStateOf<Bitmap?>(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if( !Python.isStarted() ) {
            Python.start( AndroidPlatform( this ) )
        }
        enableEdgeToEdge()
        takePictureLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    imageBitmap.value = result.data?.extras?.get("data") as? Bitmap
                }
            }
        setContent {
            CuzzAppTheme {
                ok()
            }
        }
    }
    @Composable
    fun ok() {
        Box(modifier = Modifier.fillMaxSize()) {
            Button(modifier = Modifier
                .align(Alignment.Center) // Center the button
                .offset(y = 400.dp) // Move the button down
                .fillMaxWidth(0.5f)
                .clip(RoundedCornerShape(10)), // Adjust the corner radius as needed
                colors = ButtonDefaults.buttonColors(
                    contentColor = Pink,
                    containerColor = Color(0xff2C3E50)
                )
                ,onClick = {

                    val py = Python.getInstance()
                    val module = py.getModule( "/src/test.py" )
                    val pred = module["model_predicts"]
                    val result = pred?.callAttr(username_true)
                    Log.d("TAG", "ok: $result")

        }) {
                Text(text = "Run AI")
            }
            Button(
                onClick = {
                    val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    takePictureLauncher.launch(takePictureIntent)
                },
                modifier = Modifier
                    .align(Alignment.Center) // Center the button
                    .offset(y = 300.dp) // Move the button down
                    .fillMaxWidth(0.5f)
                    .clip(RoundedCornerShape(10)), // Adjust the corner radius as needed
                colors = ButtonDefaults.buttonColors(
                    contentColor = Pink,
                    containerColor = Color(0xff2C3E50)
                )
            ) {
                Text(text = "Take picture")
            }
            imageBitmap.value?.let { bitmap ->
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = "Captured image",
                    modifier = Modifier
                        .scale(1.5f) // Scale the image
                        .offset(y = 30.dp, x = 10.dp) // Move the image down
                        .size(400.dp) // Set the size of the image
                        .align(Alignment.Center) // Align the image to the center of the screen
     )

                // Get a reference to the Firebase database
                val database = FirebaseDatabase.getInstance()

                // Get a reference to the "accounts" node
                val accountsRef = database.getReference("books")

                // Convert the Bitmap to a ByteArray
                val baos = ByteArrayOutputStream()
                imageBitmap.value?.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                val data = baos.toByteArray()

                // Get a reference to Firebase Storage
                val storage = Firebase.storage

                // Create a storage reference
                val storageRef = storage.reference

                // Create a reference to the file you want to upload
                val imageRef = storageRef.child("book/${username_true}.jpg")

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
                        Log.d("TAG", "onSuccess: ${uri.toString()}")
                        //Push to realtime database the image url
                        val userr = book_class()
                        userr.url = uri.toString()
                        userr.user = username_true
                        accountsRef.child(username_true).setValue(userr)
                    }
                }






            }
        }
    }
}
