package com.example.cuzzapp

import achivement
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.example.cuzzapp.components.MyImageArea
import com.example.cuzzapp.ui.theme.CuzzAppTheme
import com.google.firebase.Firebase
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.storage
import com.jerry.jetpack_take_select_photo_image_2.viewmodel.MainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import username_for_all
import username_true
import java.io.File

class adaugare : ComponentActivity() {
    val mdl = MainViewModel()
    private lateinit var selectImageLauncher: ActivityResultLauncher<Intent>
    private val selectedImageUri = mutableStateOf<Uri?>(null)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        selectImageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                selectedImageUri.value = result.data?.data
            }
        }
        setContent {

            val scaffoldState = rememberScaffoldState()
            Drawer(scaffoldState = scaffoldState) {

                val selectedImageUri = remember { mutableStateOf<Uri?>(null) }

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .wrapContentSize(Alignment.Center)
                            .background(Color.Gray) // optional, to visualize the box
                            .padding(innerPadding)
                    ) {
                        Box(
                            modifier = Modifier
                                .background(Color.White) // optional, to visualize the box
                        ) {
                            Column(
                                modifier = Modifier
                                    .padding(16.dp)
                                    .fillMaxSize(),
                                verticalArrangement = Arrangement.spacedBy(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) { var textFieldValue1 by remember { mutableStateOf(TextFieldValue("")) }

                                TextField(
                                    value = textFieldValue1,
                                    onValueChange = { textFieldValue1 = it },
                                    label = { Text("Disciplina") },
                                    modifier = Modifier.width(350.dp)
                                )
                                var textFieldValue2 by remember { mutableStateOf(TextFieldValue("")) }

                                TextField(
                                    value = textFieldValue2,
                                    onValueChange = { textFieldValue2 = it },
                                    label = { Text("Numele Concursului") },
                                    modifier = Modifier.width(350.dp)
                                )

                                var textFieldValue3 by remember { mutableStateOf(TextFieldValue("")) }
                                TextField(
                                    value = textFieldValue3,
                                    onValueChange = { textFieldValue3 = it },
                                    label = { Text("Premiul obtinut") },
                                    modifier = Modifier.width(350.dp)
                                )

                                var textFieldValue4 by remember { mutableStateOf(TextFieldValue("")) }
                                TextField(
                                    value = textFieldValue4,
                                    onValueChange = { textFieldValue4 = it },
                                    label = { Text("Mai multe informatii") },
                                    modifier = Modifier.height(300.dp).width(350.dp)
                                )
                                Row(modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.Center)
                                {


                                    Button(
                                        onClick = {
                                            val achievement_ = achievementuriUSER().apply {
                                                materie = textFieldValue1.text
                                                numeConcurs = textFieldValue2.text
                                                Premiu = textFieldValue3.text
                                                informatii = textFieldValue4.text
                                                linkimagine = selectedImageUri.value.toString()
                                            }
                                            val database = FirebaseDatabase.getInstance()
                                            val accountsRef = database.getReference("accounts")
                                            //add achivement to achivement list

                                            //Add achivement_ to achivement list

                                            // Get a reference to Firebase Storage
                                            val storage = Firebase.storage

                                            // Create a reference to the location where you want to upload the image
                                            val storageRef = storage.reference.child("images/${achievement_.numeConcurs}.jpg")

                                            // Upload the image to Firebase Storage
                                            val uploadTask = storageRef.putFile(selectedImageUri.value!!)

                                            // Handle success and failure
                                            uploadTask.addOnSuccessListener {
                                                // The image was successfully uploaded
                                                // Now you can get the download URL
                                                storageRef.downloadUrl.addOnSuccessListener { uri ->
                                                    // Update the linkimagine field with the download URL
                                                    achievement_.linkimagine = uri.toString()
                                                    val database_ = FirebaseDatabase.getInstance()
                                                    val userAchievementsRef = database_.getReference("accounts/$username_true/achievements")
                                                    val newAchievementRef = userAchievementsRef.push()
                                                    Log.d("AddAchievement", "Adding achievement to Firebase Database $userAchievementsRef")
                                                    newAchievementRef.setValue(achievement_).addOnCompleteListener { task ->
                                                        if (task.isSuccessful) {
                                                            Log.d("AddAchievement", "Achievement added successfully")
                                                        } else {
                                                            Log.e("AddAchievement", "Failed to add achievement", task.exception)
                                                        }
                                                        // Now you can save the achievement to Firebase Database
                                                    }
                                                }.addOnFailureListener {
                                                    // The image upload failed
                                                    // Handle the error
                                                }

                                            }
                                        },
                                    ) {
                                        Text("Adauga")
                                    }

                                    UploadImageButton(selectedImageUri)
                                }
                                Image(
                                    painter = rememberImagePainter(selectedImageUri.value),
                                    contentDescription = "Uploaded image",
                                    modifier = Modifier
                                        .size(200.dp)
                                        .border(1.dp, Color.Black)
                                        .clickable {
                                            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                                            selectImageLauncher.launch(intent)
                                        }
                                )
                            }
                        }
                    }

                }
            }

        }
    }
}

@Composable
fun UploadImageButton(selectedImageUri: MutableState<Uri?>) {
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            // Handle the selected image URI
            selectedImageUri.value = uri
        }
    )

    Button(onClick = { launcher.launch("image/*") }) {
        Text("Upload Image")
        Spacer(Modifier.width(8.dp))
        Icon(Icons.Filled.Upload, "Upload image")
    }

}

