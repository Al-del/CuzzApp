package com.example.cuzzapp

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.room.Dao
import com.example.cuzzapp.ui.theme.CuzzAppTheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import coil.compose.rememberImagePainter
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class Learning_pathways_profesor : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            val scaffoldState = rememberScaffoldState()
            Drawer(scaffoldState = scaffoldState) {

                Box(modifier =Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    LearningPath()
                }
            }


        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun LearningPath() {
    var showDialog by remember { mutableStateOf(false) }
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var isCompleted by remember { mutableStateOf(false) }
    var learningPathStages by remember { mutableStateOf(listOf<LearningPathStage>()) }

    var backgroundColor by remember { mutableStateOf(Color(0xffFFFFFF)) }
    var completedColor by remember { mutableStateOf(Color.Green) }
    var uncompletedColor by remember { mutableStateOf(Color.Gray) }

    if (showDialog) {
        AlertDialog(

            onDismissRequest = { showDialog = false },
            title = { Text("Add a new stage") },
            text = {
                Column {
                    TextField(value = title, onValueChange = { title = it }, label = { Text("Title") })
                    TextField(value = description, onValueChange = { description = it }, label = { Text("Description") })
                    Switch(checked = isCompleted, onCheckedChange = { isCompleted = it })
                    ColorPicker(
                        selectedColor = backgroundColor,
                        onColorSelected = { backgroundColor = it },
                        label = "Background Color"
                    )
                    ColorPicker(selectedColor = completedColor,
                        onColorSelected = { completedColor = it },
                         label = "Completed Color")
                    ColorPicker(         selectedColor = uncompletedColor,
                        onColorSelected = { uncompletedColor = it }, label = "Uncompleted Color")
                }
            },
            confirmButton = {
                Button(onClick = {
                    learningPathStages = learningPathStages + LearningPathStage(title, description, isCompleted)
                    showDialog = false
                }) {
                    Text("Add")
                }
            }
        )
    }

    // Create a list of Uri? with the same size as learningPathStages
    var imageUris by remember { mutableStateOf(List(learningPathStages.size) { null as Uri? }) }

    // Update imageUris list whenever learningPathStages changes
    LaunchedEffect(learningPathStages) {
        imageUris = List(learningPathStages.size) { null as Uri? }
    }

    // Create a list of ActivityResultLauncher<String> with the same size as learningPathStages
    val selectImageLaunchers = List(learningPathStages.size) { index ->
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            // Update the corresponding Uri in the list when an image is selected
            imageUris = imageUris.toMutableList().also { it[index] = uri }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    TextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Title") },
                        colors = TextFieldDefaults.textFieldColors(containerColor = Color.Transparent)
                    )
                },
                colors = topAppBarColors(containerColor = completedColor),
                actions = {
                    val database = Firebase.database
                    val myRef = database.getReference("LearningPathStages/${title}")
                    val storage = Firebase.storage

                    IconButton(onClick = {
                        // Save all the cards to Firebase Realtime Database
                        learningPathStages.forEachIndexed { index, stage ->
                            // Create a unique key for each card
                            val key = myRef.push().key
                            if (key != null) {
                                // Create a HashMap to store the card data
                                val cardData = hashMapOf(
                                    "title" to stage.title,
                                    "description" to stage.description,
                                    "isCompleted" to stage.isCompleted,
                                    "backgroundColor" to backgroundColor.toString(),
                                    "completedColor" to completedColor.toString(),
                                    "uncompletedColor" to uncompletedColor.toString()
                                )

                                    // Save the card data to Firebase Realtime Database
                                    myRef.child(key).setValue(cardData)

                            }
                        }
                    }) {
                        Icon(Icons.Default.Add, contentDescription = "Add")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }
    ) {
        Box(modifier = Modifier.fillMaxSize().background(backgroundColor), contentAlignment = Alignment.Center) {
            LazyColumn(modifier = Modifier.align(Alignment.Center).padding(top = 70.dp) // Add padding at the top equal to the height of the TopAppBar
            ) {
                itemsIndexed(learningPathStages) { index, stage ->
                    LearningPathStageCard(
                        stage,
                        alignToEnd = if (index == 0) false else index % 4 == 2 || index % 4 == 3,
                        modifier = Modifier
                            .offset(x = when (index%8) {
                                0 -> 0.dp
                                1-> 50.dp
                                2-> 100.dp
                                3-> 50.dp
                                4-> 0.dp
                                5 -> -50.dp
                                6-> -100.dp
                                7-> -130.dp
                                else -> 0.dp // Adjust this value for the 9th card and onwards
                            }),
                        completedColor = completedColor,
                        uncompletedColor = uncompletedColor,
                        backgroundColor = backgroundColor
                    )

                }
                }
            }

        }
    }



@Composable
fun LearningPathStageCard(
    stage: LearningPathStage,
    alignToEnd: Boolean,
    backgroundColor: Color,
    completedColor: Color,
    uncompletedColor: Color,
    modifier: Modifier = Modifier
) {
    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(text = stage.title) },
            text = { Text(text = stage.description) },
            confirmButton = {
                Button(onClick = { showDialog = false }) {
                    Text("OK")
                }
            }
        )
    }

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(color = if (stage.isCompleted) completedColor else uncompletedColor)
                .clickable { showDialog = true },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stage.title,
                color = backgroundColor
            )
        }
    }
}

data class LearningPathStage(
    val title: String = "",
    val description: String = "",
    val isCompleted: Boolean = false,
    val imageUrl: String? = null,
    val backgroundColor: String = "",
    val completedColor: String = "",
    val uncompletedColor: String = ""
)
@Dao
interface LearningPathStageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(stage: LearningPathStageEntity)
}

@Entity
data class LearningPathStageEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val title: String,
    val description: String,
    val isCompleted: Boolean
)
@Composable
fun ColorPicker(selectedColor: Color, onColorSelected: (Color) -> Unit, label: String) {
    val colors = listOf(
        Color.Red, Color.Green, Color.Blue, Color.Yellow, Color.Cyan,
        Color.Magenta, Color.Gray, Color(0xff4b4b4b), Color(0xffFFFFFF),
        Color.LightGray, // Silver color
        Color(0xFFFFC800), // Gold color
        Color(0xFFFF9600),
        Color(0xFFCE82FF),
        Color(0xFF2B70C9),
    )

    Column {
        Text(text = label)
        LazyRow {
            items(colors) { color ->
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .background(color = color)
                        .border(
                            width = if (color == selectedColor) 5.dp else 1.dp,
                            color = if (color == selectedColor) Color.Black else Color.Gray
                        )
                        .clickable { onColorSelected(color) }
                )
            }
        }
    }
}
@Composable
fun ImagePicker() {
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    imageUri?.let { uri ->
        val image: Painter = rememberImagePainter(data = uri)
        Image(painter = image, contentDescription = "Selected Image")
    }
}
@Composable
fun loadImageFromStorage() {
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    val selectImageLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        imageUri = uri
    }

    Button(onClick = { selectImageLauncher.launch("image/*") }) {
        Text("Select Photo")
    }

    imageUri?.let { uri ->
        val image: Painter = rememberImagePainter(data = uri)
        Image(painter = image, contentDescription = "Selected Image")
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LearningPathTopBar(completedColor: Color) {
    var title by remember { mutableStateOf("") }

    TopAppBar(
        title = {
            TextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Title") },
                colors = TextFieldDefaults.textFieldColors(containerColor = Color.Transparent)
            )
        },
        colors = topAppBarColors(containerColor = completedColor),
        actions = {
            IconButton(onClick = { /* Handle overflow */ }) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }
    )
}
