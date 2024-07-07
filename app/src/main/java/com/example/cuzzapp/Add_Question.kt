package com.example.cuzzapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.OutlinedTextField
import androidx.compose.material3.Button
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
            AddQuestionScreen()

        }
    }
}

@Composable
fun AddQuestionScreen() {
    var questionText by remember { mutableStateOf("") }
    var optionText by remember { mutableStateOf("") }
    val questions = remember { mutableStateListOf<Question>() }
    var selectedOptionIndex by remember { mutableStateOf(-1) } // Index of the selected radio button
    var Title by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)) {
        OutlinedTextField(
            value = Title,
            onValueChange = {
                Title = it
                selectedOptionIndex = -1 // Reset selection when question changes
            },
            label = { Text("Title") }
        )
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
                    question.options+=optionText
                    optionText = "" // Reset the option text field
                }
            },
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text("Add Option")
        }
        questions.find { it.text == questionText }?.let { currentQuestion ->
            Column {
                currentQuestion.options.forEachIndexed { index, option ->
                    Row(verticalAlignment = Alignment.CenterVertically) {

                        RadioButton(
                            selected = selectedOptionIndex == index,
                            onClick = {
                                selectedOptionIndex = index
                                currentQuestion.correctAnswer = option
                            }
                        )
                        Text(option)
                    }
                }
            }
        }
        Button(
            onClick = {
                questionText = "" // Reset question text field after adding a question
                optionText = "" // Reset option text field
                selectedOptionIndex = -1 // Reset selected option index
            },
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text("Add Question")
        }
        Button(
    onClick = {
        pushAllQuestionsToFirestore(questions,Title)
    },
    modifier = Modifier.padding(top = 8.dp)
) {
    Text("Push All to Firestore")
}
        Spacer(modifier = Modifier.height(16.dp))
        questions.forEachIndexed { index, question ->
            Text("${index + 1}. ${question.text} - Correct Answer: ${question.correctAnswer}")
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
