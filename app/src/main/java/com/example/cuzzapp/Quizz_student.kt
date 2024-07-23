package com.example.cuzzapp

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cuzzapp.ui.theme.CuzzAppTheme
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.offset
import androidx.compose.material.rememberScaffoldState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import com.itextpdf.io.source.ByteArrayOutputStream
import com.itextpdf.text.Document
import com.itextpdf.text.Paragraph
import com.itextpdf.text.pdf.PdfWriter
import java.io.File
import java.io.FileOutputStream
import com.itextpdf.kernel.pdf.PdfDocument as iTextPdfDocument

import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.itextpdf.io.image.ImageDataFactory
import com.itextpdf.styledxmlparser.jsoup.safety.Whitelist
import kotlinx.coroutines.Dispatchers
import username_for_all
import username_true
import java.io.OutputStream

class Quizz_student : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var searchQuery by remember { mutableStateOf("") }
            val scaffoldState = rememberScaffoldState()

            val quizViewModel = QuizViewModel()

            Drawer(scaffoldState, searchQuery, backgroundColor = SolidColor(Color(0xFF6A5AE0)), onSearchQueryChange = { searchQuery = it }) {
                Box(modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF6A5AE0))) { // Fill the parent to allow alignment
                    Box(modifier = Modifier.align(Alignment.Center)) {
                        CardPreview()
                    }
                    Box(modifier = Modifier.align(Alignment.BottomCenter)) {
                        LiveQuizzesPreview(quizViewModel)
                    }
                    Box(modifier = Modifier
                        .align(Alignment.Center)
                        .offset(y = -300.dp)) {
                        CardusPreview()
                    }

                }
            }
        }
    }
}

class QuizViewModel : ViewModel() {
    val _quizzes = MutableStateFlow<List<QuizInfo>>(emptyList())
    val quizzes: StateFlow<List<QuizInfo>> = _quizzes
    val Quiz_List = mutableListOf<Question>()
    init {
        fetchQuizzes()
    }

    private fun fetchQuizzes() {
        val db = FirebaseFirestore.getInstance()
        viewModelScope.launch {
            db.collection("quizzes").get()
                .addOnSuccessListener { result ->
                    val quizList = mutableListOf<QuizInfo>()
                    for (document in result) {
                        val quiz = document.toObject(QuizInfo::class.java)
                        quizList.add(quiz)
                    }
                    _quizzes.value = quizList
                }
                .addOnFailureListener { exception ->
                    // Handle the error appropriately
                }
        }
    }
 suspend fun fetchQuizByPathSuspend(path: String) = suspendCoroutine<Unit> { continuation ->
    val db = FirebaseFirestore.getInstance()
    db.collection(path).get()
        .addOnSuccessListener { result ->
            Quiz_List.clear() // Clear the list to avoid duplicating items
            for (document in result) {
                val quiz = document.toObject(Question::class.java)
                Quiz_List.add(quiz)
            }
            continuation.resume(Unit) // Resume the coroutine after fetching the data
        }
        .addOnFailureListener { exception ->
            Log.e("QuizViewModel", "Error fetching quiz by path: $path", exception)
            continuation.resumeWithException(exception)
        }
}
}

@Composable
fun QuizzesList(viewModel: QuizViewModel) {
    val isClicked = remember { mutableStateOf(false) }
    val quizPath = remember { mutableStateOf("") }
    var quizTitle = remember {
        mutableStateOf("")
    }
    val fetchComplete = remember { mutableStateOf(false) }
    val quizzes by viewModel.quizzes.collectAsState()
    val quizSelected = remember { mutableStateOf(false) } // Step 1: State to track if a quiz has been selected

    if (!quizSelected.value) { // Conditionally render based on quizSelected state
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column {
                quizzes.forEach { quiz ->
                    Button(onClick = {
                        isClicked.value = true
                        quizPath.value = quiz.path
                        quizTitle.value = quiz.title
                        fetchComplete.value = false
                        quizSelected.value = true // Step 2: Set quizSelected to true when a quiz is clicked
                    }) {
                        Text(text = quiz.title)
                    }
                }
            }
        }
    }

    if (isClicked.value && quizSelected.value) { // Ensure this part is rendered only when a quiz is selected
        FetchQuizAndLog(viewModel, quizPath.value, fetchComplete)
    }

    if (fetchComplete.value && quizSelected.value) {
        QuizQuestions(quizList = viewModel.Quiz_List, quizTitle.value)
    }
}

@Composable
fun FetchQuizAndLog(viewModel: QuizViewModel, quizPath: String, fetchComplete: MutableState<Boolean>) {
    LaunchedEffect(quizPath) {
        viewModel.fetchQuizByPathSuspend(quizPath)
        Log.d("QuizzesList", "Fetching quiz by path: ${viewModel.Quiz_List}")
        fetchComplete.value = true // Step 2: Mark fetch operation as complete
    }
}
@Composable
fun QuizQuestions(quizList: List<Question>, titlus:String) {
    var currentIndex by remember { mutableStateOf(0) }
    var selectedOptionIndex by remember { mutableStateOf(-1) }
    var score by remember { mutableStateOf(0) }
    var canCheckAnswer by remember { mutableStateOf(true) }
    val context = LocalContext.current
    val modifiedBitmap = remember { mutableStateOf<Bitmap?>(null) }

    Column(modifier = Modifier.padding(16.dp)) {
        if (quizList.isNotEmpty()) {
            Text(text = "Question: ${quizList[currentIndex].text}", style = MaterialTheme.typography.bodyLarge)

            quizList[currentIndex].options.forEachIndexed { index, option ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = selectedOptionIndex == index,
                        onClick = { selectedOptionIndex = index }
                    )
                    Text(option)
                }
            }

            Button(
                onClick = {
                    if (canCheckAnswer && quizList[currentIndex].options[selectedOptionIndex] == quizList[currentIndex].correctAnswer) {
                        score++
                    }else{
                        Toast.makeText(context, "Correct Answer: ${quizList[currentIndex].correctAnswer}", Toast.LENGTH_SHORT).show()
                    }
                    canCheckAnswer = false
                },
                enabled = canCheckAnswer,
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Text("Check Answer")
            }

            Button(
                onClick = {
                    if (currentIndex == quizList.size - 1){
                        val originalBitmap: Bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.certificate)
                        modifiedBitmap.value = add_text(originalBitmap, "Username: $username_true", 1000f ,2200f, 400f)
                      modifiedBitmap.value = add_text(modifiedBitmap.value!!, "Score: $score/${quizList.size}", 2300f, 2700f, 200f)
                        modifiedBitmap.value = add_text(modifiedBitmap.value!!, titlus, 900f, 2700f, 200f)
                        saveBitmapToDownloads(modifiedBitmap.value!!, "$titlus.jpg", context)
                    }else {
                        currentIndex = (currentIndex + 1) % quizList.size
                        selectedOptionIndex = -1
                        canCheckAnswer = true
                    }
                },
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Text(if (currentIndex == quizList.size - 1) "Generate Diploma" else "Next Question")
            }

            Text(text = "Score: $score", style = MaterialTheme.typography.bodyLarge)
            modifiedBitmap.value?.let {
                Image(bitmap = it.asImageBitmap(), contentDescription = "Modified Image", modifier = Modifier.fillMaxSize())
            }
        } else {
            Text("No questions available")
        }
    }
}

fun add_text(originalBitmap:Bitmap, textus:String, param_x:Float, param_y:Float, size:Float): Bitmap? {
    val mutableBitmap = originalBitmap.copy(Bitmap.Config.ARGB_8888, true)
    val canvas = Canvas(mutableBitmap)
    val paint = Paint().apply {
        color = android.graphics.Color.GREEN
        textSize = size
        typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
    }
    canvas.drawText(textus, param_x, param_y, paint)
    return mutableBitmap
}


fun saveBitmapToDownloads(bitmap: Bitmap, fileName: String, context: Context) {
    val outputStream: OutputStream
    try {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            // For Android Q and above
            val resolver = context.contentResolver
            val contentValues = android.content.ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
            }
            val imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
            outputStream = resolver.openOutputStream(imageUri!!)!!
            Log.d("saveBitmapToDownloads", "imageUri: $imageUri")
        } else {
            // For older versions
            val imagesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString()
            val image = File(imagesDir, fileName)
            outputStream = FileOutputStream(image)
        }
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        outputStream.flush()
        outputStream.close()
        Toast.makeText(context, "Diploma saved successfully", Toast.LENGTH_SHORT).show()
    } catch (e: Exception) {
        e.printStackTrace()
    }
}