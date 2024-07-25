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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.graphics.SolidColor
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

            var searchQuery by remember { mutableStateOf("") }
            val scaffoldState = rememberScaffoldState()

            Drawer(scaffoldState, searchQuery, backgroundColor = SolidColor(Color(0xffb379df)) ,onSearchQueryChange = { searchQuery = it }) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 68.dp) // Assuming the height of the bottom bar is 56.dp
                        .padding(6.dp), // Add padding here
                    contentAlignment = Alignment.Center
                ){
                    LearningPath()
                }
            }


        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "UnusedMaterialScaffoldPaddingParameter",
    "SuspiciousIndentation"
)
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
        modifier = Modifier.fillMaxSize().background(
            brush = Brush.verticalGradient(
                colors = listOf(Color(0xFFEA8D8D), Color(0xFFA890FE)),
                startY = 0f,
                endY = Float.POSITIVE_INFINITY
            )
        ),
        floatingActionButton = {
            FloatingActionButton(modifier = Modifier.offset(y=-60.dp),onClick = { showDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }
    ) {
        Column(modifier = Modifier.fillMaxSize())
        {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(45.dp))
                    .padding(6.dp)
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(Color(0xFFa8c0ff), Color(0xFF3f2b96)),
                            startX = 0f,
                            endX = Float.POSITIVE_INFINITY
                        ))
                    .height(56.dp) ,// default height of TopAppBar

            ) {
                Row(modifier = Modifier.fillMaxSize(), horizontalArrangement = Arrangement.SpaceBetween) {
                    TextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Title") },

                        colors = TextFieldDefaults.textFieldColors(textColor = Color(0xFFffff66), backgroundColor= Color.Transparent,
                            focusedIndicatorColor = Color.Black,

                            )
                    )

                    val database = Firebase.database
                    val myRef = database.getReference("LearningPathStages/${title}")
                    val storage = Firebase.storage

                    IconButton(modifier = Modifier.offset(x=-15.dp, y=7.dp),
                        onClick = {

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
                        },

                        ) {
                        Icon(Icons.Default.Add, contentDescription = "Add")
                    }




                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(16.dp)) // Adjust the corner size as needed
                    .background(backgroundColor)
                    .padding(horizontal = 16.dp, vertical=5.dp),
                contentAlignment = Alignment.Center
            ) {              LazyColumn(
                modifier = Modifier.align(Alignment.Center)
                    .padding(top = 70.dp) // Add padding at the top equal to the height of the TopAppBar
            ) {
                itemsIndexed(learningPathStages) { index, stage ->
                    LearningPathStageCard(
                        stage,
                        alignToEnd = if (index == 0) false else index % 4 == 2 || index % 4 == 3,
                        modifier = Modifier
                            .offset(
                                x = when (index % 8) {
                                    0 -> 0.dp
                                    1 -> 50.dp
                                    2 -> 100.dp
                                    3 -> 50.dp
                                    4 -> 0.dp
                                    5 -> -50.dp
                                    6 -> -100.dp
                                    7 -> -130.dp
                                    else -> 0.dp // Adjust this value for the 9th card and onwards
                                }
                            )
                            .padding(horizontal = 6.dp),
                        completedColor = completedColor,
                        uncompletedColor = uncompletedColor,
                        backgroundColor = backgroundColor
                    )

                }
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


        Color(0xFF4dd0e1),
        Color(0xFF558799),
        Color(0xFF00162c),

        Color(0xFFf1f8e9),
        Color(0xFF4cbb17),
        Color(0xFF558b2f),

        Color(0xFFff6247),
        Color(0xFFd73022),
        Color(0xFF9d0002),

        Color(0xFFf1c3c3),
        Color(0xFFeda3b9),
        Color(0xFFf987c1),


        Color(0xFF9966cd),
        Color(0xFF855693),
        Color(0xFF6040ad),


        Color(0xFFf7a90a),
        Color(0xFFff7418),
        Color(0xFFe75a23),

        Color(0xFFf3d942),
        Color(0xFFefb51c),
        Color(0xFFbd9d22),

        Color(0xFF625551),
        Color(0xFF835332),
        Color(0xFF543b32),



        /*
        Color.Red, Color.Green, Color.Blue, Color.Yellow, Color.Cyan,
        Color.Magenta, Color.Gray, Color(0xff4b4b4b), Color(0xffFFFFFF),
        Color.LightGray, // Silver color
        Color(0xFFFFC800), // Gold color
        Color(0xFFFF9600),
        Color(0xFFCE82FF),
        Color(0xFF2B70C9),

         */
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
                colors = TextFieldDefaults.textFieldColors( Color.Transparent)
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