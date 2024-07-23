package com.example.cuzzapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.cuzzapp.ui.theme.CuzzAppTheme
import com.google.firebase.firestore.FirebaseFirestore
data class Question(
    val text: String = "",
    var options: List<String> = listOf(),
    var correctAnswer: String = "",
    val selectedOptions: List<String> = listOf() // Assuming this field exists based on the log, add it if not present
)
data class QuizInfo(
    var title: String = "",
    var path: String = ""
)
class Add_Question : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var searchQuery by remember { mutableStateOf("") }
            val scaffoldState = rememberScaffoldState()

            Drawer(scaffoldState, searchQuery, backgroundColor = SolidColor(Color(0xFF6A5AE0)) ,onSearchQueryChange = { searchQuery = it }) {
                Box(modifier = Modifier.fillMaxSize().background(Color(0xFF6A5AE0))) {

                AddQuestionScreen()

                }
            }
        }
    }
}

@Composable
fun AddQuestionScreen() {
    var questionText by remember { mutableStateOf("") }
    var optionText by remember { mutableStateOf("") }
    val questions = remember { mutableStateListOf<Question>() }
    var selectedOptionIndex by remember { mutableStateOf(-1) }
    var Title by remember { mutableStateOf("") }
    val buttonColors = listOf(Color(0xffc60929), Color(0xff0542b9), Color(0xff0aa3a3), Color(0xffeb670f))

    Column(modifier = Modifier.padding(16.dp)) {
       Box(
    modifier = Modifier
        .fillMaxWidth()
        .requiredHeight(height = 228.dp)
        .clip(shape = RoundedCornerShape(30.dp))
) {
    OutlinedTextField(
        value = Title,
        onValueChange = {
            Title = it
            selectedOptionIndex = -1 // Reset selection when question changes
        },
        modifier = Modifier.align(Alignment.TopCenter),
        label = {
            Text(
                "Title",
                modifier = Modifier.align(Alignment.TopCenter),
                color = Color.White, // Set text color to white
                fontWeight = FontWeight.Bold // Set text weight to bold
            )
        }
    )
           Spacer(modifier = Modifier.height(16.dp))
           Button(
               onClick = {
                   pushAllQuestionsToFirestore(questions, Title)
               },
               modifier = Modifier.padding(top = 8.dp).align(Alignment.BottomCenter)
           ) {
               Text("Push All to Firestore")
           }
    Box(
        modifier = Modifier
            .requiredSize(size = 90.dp)
            .clip(shape = CircleShape)
            .align(Alignment.BottomCenter)
            .background(color = Color.White.copy(alpha = 0.1f))
    )
    Box(
        modifier = Modifier
            .requiredSize(size = 90.dp)
            .clip(shape = CircleShape)
            .align(Alignment.CenterEnd)
            .background(color = Color.White.copy(alpha = 0.1f))
    )
    Box(
        modifier = Modifier
            .requiredSize(size = 90.dp)
            .clip(shape = CircleShape)
            .align(Alignment.TopStart)
            .background(color = Color.White.copy(alpha = 0.1f))
    )
           questions.find { it.text == questionText }?.let { currentQuestion ->
               LazyVerticalGrid(
                   columns = GridCells.Fixed(2), // Set two items per row
                   modifier = Modifier.padding(top = 8.dp).align(Alignment.Center) // Apply padding at the top
               ) {
                   items(currentQuestion.options) { option ->
                       val index = currentQuestion.options.indexOf(option)
                       Button(
                           onClick = {
                               selectedOptionIndex = index
                               currentQuestion.correctAnswer = option
                           },
                           colors = ButtonDefaults.buttonColors(containerColor = buttonColors[index % buttonColors.size])
                       ) {
                           Text(option)
                       }
                   }
               }
           }

}

Box(
    modifier = Modifier
        .requiredWidth(width = 381.dp)
        .requiredHeight(height = 270.dp)
        .align(Alignment.CenterHorizontally)
        .clip(shape = RoundedCornerShape(20.dp))
        .background(color = Color(0xfff0efed))
        .zIndex(1f) // Ensure this box is drawn on top
        .padding(14.dp)
) {
    Column(modifier = Modifier.align(Alignment.Center)) {


        OutlinedTextField(
            value = questionText,
            onValueChange = {
                questionText = it
                selectedOptionIndex = -1 // Reset selection when question changes
            },
            label = { Text("New Question") }
        )
        OutlinedTextField(
            value = optionText,
            onValueChange = { optionText = it },
            label = { Text("Option") }
        )
        Button(
            onClick = {
                if (questionText.isNotEmpty() && optionText.isNotEmpty()) {
                    val question = questions.find { it.text == questionText }
                        ?: Question(questionText).also {
                            questions.add(it)
                            selectedOptionIndex = -1 // Reset selection for new question
                        }
                    question.options += optionText
                    optionText = "" // Reset the option text field
                }
            },
            modifier = Modifier.padding(top = 8.dp).align(Alignment.CenterHorizontally)
        ) {
            Text("Add Option")
        }
    }
    }


        Spacer(modifier = Modifier.height(20.dp))
        questions.forEachIndexed { index, question ->
            Text(
                "${index + 1}. ${question.text} - Correct Answer: ${question.correctAnswer}",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp // Assuming the default size is 16.sp, 6 units bigger would be 22.sp
            )
        }
    }
}
fun pushAllQuestionsToFirestore(questions: List<Question>, collectionId: String) {
    val db = FirebaseFirestore.getInstance()
    val batch = db.batch()
    val quizRef = db.collection("quizzes").document(collectionId) // Reference to the quiz document

    questions.forEach { question ->
        val docRef = db.collection(collectionId).document() // Document reference for each question
        batch.set(docRef, question)
    }

    batch.commit()
        .addOnSuccessListener {
            Log.d("Firestore", "Batch write succeeded.")
            // After successfully adding questions, update the quizzes collection
            val quizInfo = QuizInfo(collectionId, "/$collectionId") // Assuming path is the collection ID
            db.collection("quizzes").document(collectionId).set(quizInfo)
                .addOnSuccessListener { Log.d("Firestore", "Quiz info updated successfully.") }
                .addOnFailureListener { e -> Log.w("Firestore", "Error updating quiz info", e) }
        }
        .addOnFailureListener { e ->
            Log.w("Firestore", "Batch write failed.", e)
        }
}
