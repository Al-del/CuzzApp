package com.example.cuzzapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
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
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.lifecycleScope
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
            val scaffoldState = rememberScaffoldState()
            Drawer(scaffoldState = scaffoldState) {

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
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.CenterStart) {
            Column {
                for (title in pathwayTitle.value) {
                    Text(text = title, modifier = Modifier
                        .padding(16.dp)
                        .offset(y = 10.dp)
                        .clickable {
                            is_clicked.value = true
                            path = title
                            //    Log.d("LearningPathStageCardus", "Path: $path")

                        })
                }
            }
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
            .fillMaxWidth()
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

@Composable
fun Loaf_evr(path: String) {
    Log.d("LearningPathStageCardus", "Path: $path")
    val database = Firebase.database
    val myRef = database.getReference("LearningPathStages/$path")
    var learningPathStages by remember { mutableStateOf(listOf<LearningPathStage>()) }
    var scaffoldBackgroundColor by remember { mutableStateOf(Color.Red) }
    var globalCheckboxChecked by remember { mutableStateOf(false) }

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


    Box(modifier = Modifier.fillMaxSize().background(scaffoldBackgroundColor)) {
        Scaffold(
            modifier = Modifier.fillMaxSize().background(scaffoldBackgroundColor),
        ) { innerPadding ->
            Column {
                Checkbox(
                    checked = globalCheckboxChecked,
                    onCheckedChange = { isChecked ->
                        globalCheckboxChecked = isChecked
                        // Get a reference to the Firebase database
                        if (globalCheckboxChecked) {
                            val database = FirebaseDatabase.getInstance()

                            // Get a reference to the "accounts" node
                            val accountsRef = database.getReference("accounts")

                            // Update the points to 100
                            points += 100
                            accountsRef.child(username_true).child("points").setValue(points)
                            learningPath += path
                            accountsRef.child(username_true).child("learningPath").setValue(learningPath)
                        }
                    }
                )
                LazyColumn(modifier = Modifier.padding(innerPadding).background(scaffoldBackgroundColor)) {
                    itemsIndexed(learningPathStages) { index, stage ->
                        Log.d("LearningPathStageCardus", "Stage: $stage")
                        LearningPathStageCardus(stage, index, learningPathStages, globalCheckboxChecked)
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