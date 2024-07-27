package com.example.cuzzapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.zIndex
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.example.cuzzapp.ui.theme.CuzzAppTheme
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.database.getValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import learningPath
import points
import username_true
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


class Learningpath_student : ComponentActivity() {
    @SuppressLint("CoroutineCreationDuringComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var searchQuery by remember { mutableStateOf("") }
            val scaffoldState = rememberScaffoldState()

            Drawer(scaffoldState, searchQuery, backgroundColor = SolidColor(Color(0xFF6A5AE0)) ,onSearchQueryChange = { searchQuery = it }) {

                val database = Firebase.database
                val myRef = database.getReference("LearningPathStages")
                val pathwayTitle = remember { mutableStateOf(emptyList<String>()) }

                // Launch a new coroutine
                lifecycleScope.launch {
                    // Fetch data from Firebase in the IO dispatcher
                    pathwayTitle.value = withContext(Dispatchers.IO) {
                        fetchPathwayTitle(myRef)
                    }
                    //Exclude from the pathwaysTitle the elements from learningPath
                    pathwayTitle.value = pathwayTitle.value.filter { !learningPath.contains(it) }
                }

                // Update UI after fetching data
                LearningPathUI(pathwayTitle)
            }

        }
    }
    @Composable
    fun LearningPathUI(pathwayTitle: MutableState<List<String>>) {
        val is_clicked = remember { mutableStateOf(false) }
        var path by remember { mutableStateOf("") }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF6A5AE0))
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(pathwayTitle.value.chunked(2)) { titles ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    titles.forEach { title ->
                        Column(
                            modifier = Modifier,
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(130.dp)
                                    .clip(CircleShape)
                                    .background(
                                        brush = Brush.verticalGradient(
                                            colors = listOf(Color(0xFFEA8D8D), Color(0xFFA890FE)),
                                            startY = 0f,
                                            endY = Float.POSITIVE_INFINITY
                                        )
                                    )
                                    .border(2.dp, Color.Black, CircleShape)
                                    .clickable {
                                        is_clicked.value = true
                                        path = title
                                    }
                            )
                            {
                                if(title.contains("programare")) {
                                    Image(
                                        painter = rememberImagePainter(

                                            data = R.drawable.laptop, // Replace with your image resource
                                            builder = {
                                                crossfade(true)
                                            }
                                        ),
                                        contentDescription = "Background image",
                                        modifier = Modifier.fillMaxSize().scale(0.4f) // Fill the Box with the image
                                    )
                                }
                                else if(title.contains("matematica"))
                                {
                                    Image(
                                        painter = rememberImagePainter(

                                            data = R.drawable.laptop, // Replace with your image resource
                                            builder = {
                                                crossfade(true)
                                            }
                                        ),
                                        contentDescription = "Background image",
                                        modifier = Modifier.fillMaxSize().scale(0.4f) // Fill the Box with the image
                                    )
                                }
                                else if(title.contains("robotica"))
                                {
                                    Image(
                                        painter = rememberImagePainter(

                                            data = R.drawable.robot, // Replace with your image resource
                                            builder = {
                                                crossfade(true)
                                            }
                                        ),
                                        contentDescription = "Background image",
                                        modifier = Modifier.fillMaxSize().scale(0.4f) // Fill the Box with the image
                                    )
                                }
                                else if(title.contains("fizica"))
                                {
                                    Image(
                                        painter = rememberImagePainter(

                                            data = R.drawable.fizica, // Replace with your image resource
                                            builder = {
                                                crossfade(true)
                                            }
                                        ),
                                        contentDescription = "Background image",
                                        modifier = Modifier.fillMaxSize().scale(0.4f) // Fill the Box with the image
                                    )
                                }
                                else
                                {
                                    Image(
                                        painter = rememberImagePainter(

                                            data = R.drawable.caiet, // Replace with your image resource
                                            builder = {
                                                crossfade(true)
                                            }
                                        ),
                                        contentDescription = "Background image",
                                        modifier = Modifier.fillMaxSize().scale(0.4f) // Fill the Box with the image
                                    )
                                }
                            }
                            Text(
                                text = title,
                                modifier = Modifier
                                    .padding(top = 8.dp)
                            )
                        }
                    }
                }
            }
            // Spacer(modifier = Modifier.weight(1f)) // Add this line to fill the space below

        }

        if (is_clicked.value) {
            Log.d("LearningPathStageCardus", "Path: $path")

            Loaf_evr(path)
        }
    }
    // Wrap Firebase operation in a suspending function
    private suspend fun fetchPathwayTitle(myRef: DatabaseReference): List<String> {
        return suspendCoroutine { continuation ->
            myRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val titles = dataSnapshot.children.map { it.key.toString() }
                    continuation.resume(titles)
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle database error
                }
            })
        }
    }

    @Composable
    fun LearningPathStageCardus(
        stage: LearningPathStage,
        index: Int,
        learningPathStages: List<LearningPathStage>,
        globalCheckboxChecked: Boolean,
        modifier: Modifier = Modifier
    ) {
        val backgroundColor = stage.backgroundColor.toColor()
        val completedColor = stage.completedColor.toColor()
        val uncompletedColor = stage.uncompletedColor.toColor()

        var showDialog by remember { mutableStateOf(false) }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text(text = stage.title) },
                text = {
                    Column {
                        Text(text = stage.description)
                    }
                },
                confirmButton = {
                    Button(onClick = { showDialog = false }) {
                        Text("OK")
                    }
                }
            )
        }

        Row(
            modifier = modifier
                .fillMaxSize()
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
                ),
            horizontalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(20.dp)) // Add this line to make the Box have rounded corners
                    .clip(CircleShape)
                    .background(color = if (globalCheckboxChecked) completedColor else if (stage.isCompleted) completedColor else uncompletedColor)
                    .clickable { showDialog = true }
                    .zIndex(1f), // Set zIndex to 1 to make it appear on top
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stage.title,
                    color = backgroundColor
                )
            }

        }
    }

    fun String.toColor(): Color {
        return when {
            this.startsWith("Color(") -> {
                val colorComponents = this
                    .removePrefix("Color(")
                    .removeSuffix(")")
                    .split(",")
                    .map { it.trim() }

                val r = colorComponents[0].toFloat()
                val g = colorComponents[1].toFloat()
                val b = colorComponents[2].toFloat()
                val a = colorComponents[3].toFloat()

                Color(r, g, b, a)
            }

            else -> Color.Black // Default color if the string format is not recognized
        }
    }

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @Composable
    fun Loaf_evr(path: String) {
        Log.d("LearningPathStageCardus", "Path: $path")
        val database = Firebase.database
        val myRef = database.getReference("LearningPathStages/$path")
        var learningPathStages by remember { mutableStateOf(listOf<LearningPathStage>()) }
        var scaffoldBackgroundColor by remember { mutableStateOf(Color.Red) }
        var globalCheckboxChecked by remember { mutableStateOf(false) }
        val context = LocalContext.current
        // Launch a new coroutine
        LaunchedEffect(key1 = path) {
            // Fetch data from Firebase in the IO dispatcher
            learningPathStages = withContext(Dispatchers.IO) {
                fetchLearningPathStages(myRef)
            }
            // Set the Scaffold's background color
            scaffoldBackgroundColor = if (learningPathStages.isNotEmpty()) {
                learningPathStages[0].backgroundColor.toColor()
            } else {
                Color.White // Default color if there are no stages
            }
        }


        var showSlideBox by remember { mutableStateOf(false) }
        var showDialog by remember { mutableStateOf(false) }

        Scaffold(
            modifier = Modifier.fillMaxSize().background(Color.White),
            floatingActionButton = {
                FloatingActionButton(onClick = {
                    val database = FirebaseDatabase.getInstance()

                    // Get a reference to the "accounts" node
                    val accountsRef = database.getReference("accounts")

                    // Update the points to 100
                    points += 100
                    accountsRef.child(username_true).child("points").setValue(points)
                    learningPath += path
                    accountsRef.child(username_true).child("learningPath").setValue(learningPath)

                    // Show the dialog
                    showDialog = true
                }, modifier = Modifier.offset(y = - 60.dp)) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Toggle Slide Box")
                }
            }
        ) {
            Box(
                modifier = Modifier
                    .background(scaffoldBackgroundColor)
                    .fillMaxSize()
                    .padding(bottom = 65.dp)
                    .clip(RoundedCornerShape(16.dp))
            ) {
                LazyColumn {
                    itemsIndexed(learningPathStages) { index, stage ->
                        LearningPathStageCardus(stage, index, learningPathStages, globalCheckboxChecked)
                    }
                }
            }

            if (showDialog) {
                Dialog(
                    onDismissRequest = { showDialog = false },
                    properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true)
                ) {
                    Box(
                        modifier = Modifier
                            .background(Color.White, shape = RoundedCornerShape(8.dp))
                            .padding(16.dp)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(text = "Congratulations!", style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold))
                            Spacer(modifier = Modifier.height(16.dp))
                            AsyncImage(
                                model = "https://media.tenor.com/F0UWHBTt6xQAAAAj/congratulations-congrats.gif", // Replace with your GIF URL
                                contentDescription = "Congratulations Animation",
                                modifier = Modifier.size(200.dp)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(text = "Congratulations for learning something new!")
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(onClick = {
                                showDialog = false
                                // Navigate back to home screen
                                val intent = Intent(context, HomeScreen::class.java)
                                ContextCompat.startActivity(context, intent, null)
                            }) {
                                Text("Go Back Home")
                            }
                        }
                    }
                }
            }
        }
    }

    // Wrap Firebase operation in a suspending function
    suspend fun fetchLearningPathStages(myRef: DatabaseReference): List<LearningPathStage> {
        return suspendCoroutine { continuation ->
            myRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val stages = dataSnapshot.children.mapNotNull { it.getValue<LearningPathStage>() }
                    continuation.resume(stages)
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle database error
                }
            })
        }
    }

}

/* if(stage.imageUrl != null) {
     Log.d("LearningPathStageCardus", "Image URL: ${stage.imageUrl}")
     // Load image from URL
     stage.imageUrl?.let { url ->
         Image(
             painter = rememberImagePainter(data = url),
             contentDescription = "Stage Image",
             modifier = Modifier
                 .size(100.dp)
                 .clip(CircleShape)
         )
     }
 }*/