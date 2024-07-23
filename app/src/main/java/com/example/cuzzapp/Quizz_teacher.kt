package com.example.cuzzapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import com.example.cuzzapp.ui.theme.CuzzAppTheme
import com.google.firebase.firestore.FirebaseFirestore
import seach_querr
import state

class Quizz_teacher : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var searchQuery by remember { mutableStateOf(seach_querr) }
            val scaffoldState = rememberScaffoldState()
            val quizViewModel = QuizViewModel()
            Scaffold(floatingActionButton = { Teacher_Quizz() }) {

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
}

@Composable
fun Teacher_Quizz(){
    val context = LocalContext.current // Get the local context to use startActivity
    CircularButton(onClick = {

        val intent = Intent(context, Add_Question()::class.java)
        context.startActivity(intent)
    })
}
@Composable
fun LiveQuizzes(
    modifier: Modifier = Modifier,
    widthDp: Dp = getScreenWidthDp().dp, // Default to full screen width
    maxHeightDp: Dp = 600.dp, // Example maximum height
    viewModel: QuizViewModel
) {
    var boxHeight by remember { mutableStateOf(304.dp) } // Initial height
    val proportion = boxHeight.value / maxHeightDp.value

    Box(
        modifier = modifier
            .requiredWidth(width = widthDp)
            .height(boxHeight)
            .wrapContentHeight(align = Alignment.CenterVertically)
            .draggable(
                orientation = Orientation.Vertical,
                state = rememberDraggableState { delta ->
                    boxHeight = (boxHeight - delta.dp).coerceIn(
                        100.dp,
                        maxHeightDp
                    ) // Adjust height within a range

                }
            ),
        contentAlignment = Alignment.BottomCenter
    ) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .clip(shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
                .background(color = Color.White)
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp * proportion, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(24.dp)
                .fillMaxHeight()
        ) {
            Text(
                text = "Live Quizzes",
                color = Color(0xff0c092a),
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium)
            )

            val quizzes by viewModel.quizzes.collectAsState()
            LazyColumn {
                items(quizzes) { quiz ->
                    Property1Integers(
                        question_name = quiz.title,
                        number_questions = quizzes.size,
                        viewModel = viewModel,
                        pathus = quiz.path,
                        titlus = quiz.title
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

@Composable
fun Property1Integers(modifier: Modifier = Modifier, question_name:String, number_questions:Int,viewModel: QuizViewModel,pathus : String , titlus : String) {
    val isClicked = remember { mutableStateOf(false) }
    val quizPath = remember { mutableStateOf("") }
    var quizTitle = remember {
        mutableStateOf("")
    }
    val fetchComplete = remember { mutableStateOf(false) }
    val quizzes by viewModel.quizzes.collectAsState()
    val quizSelected = remember { mutableStateOf(false) } // Step 1: State to track if a quiz has been selected
    if (quizSelected.value) { // Conditionally render based on quizSelected state
        if (isClicked.value && quizSelected.value) { // Ensure this part is rendered only when a quiz is selected
            FetchQuizAndLog(viewModel, quizPath.value, fetchComplete)
        }

        if (fetchComplete.value && quizSelected.value) {
            QuizQuestions(quizList = viewModel.Quiz_List, quizTitle.value)
        }
    }else{
    Box(
        modifier = modifier
            .requiredWidth(width = 327.dp)
            .requiredHeight(height = 80.dp)
    ) {
        // Background Box
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(shape = RoundedCornerShape(20.dp))
                .background(color = Color.White)
                .border(
                    border = BorderStroke(2.dp, Color(0xffefeefc)),
                    shape = RoundedCornerShape(20.dp)
                )
        )

        // Colored Box
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(shape = RoundedCornerShape(20.dp))
                .background(color = Color(0xffff8fa2))
                .padding(
                    start = 8.dp,
                    end = 255.dp,
                    top = 8.dp,
                    bottom = 8.dp
                )
        ) {
            Box(
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(
                        x = 8.dp,
                        y = 16.dp
                    )
                    .requiredWidth(width = 48.dp)
                    .requiredHeight(height = 66.dp)
                    .clip(shape = RoundedCornerShape(8.dp))
                    .background(color = Color.White)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.stats),
                    contentDescription = "Description here", // Provide an appropriate description or use null if the image is decorative
                    modifier = Modifier
                        .requiredSize(70.dp)
                        .align(Alignment.Center)
                )
            }
        }

        // Text Column - Moved to be the last child of the outer Box
        Column(
            verticalArrangement = Arrangement.spacedBy(6.dp, Alignment.Top),
            modifier = Modifier
                .fillMaxSize()
                .clickable {
                    isClicked.value = true
                    quizPath.value = pathus
                    quizTitle.value = titlus
                    fetchComplete.value = false
                    quizSelected.value =
                        true // Step 2: Set quizSelected to true when a quiz is clicked

                }
        ) {
            Text(
                text = question_name,
                color = Color(0xff660012),
                lineHeight = 9.38.em,
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                ),
                modifier = Modifier
                    .requiredWidth(width = 183.dp)
                    .offset(y = 30.dp)
                    .align(Alignment.CenterHorizontally)

            )
            Text(
                text = "$number_questions Quizzes",
                color = Color(0xff660012),
                lineHeight = 12.5.em,
                style = TextStyle(
                    fontSize = 12.sp
                ),
                modifier = Modifier
                    .requiredWidth(width = 183.dp)
                    .offset(y = 30.dp)
                    .align(Alignment.CenterHorizontally)
            )
        }

    }
    }

}
@Composable
fun LiveQuizzesPreview(viewModel: QuizViewModel) {
    LiveQuizzes(Modifier,viewModel = viewModel)
}
@Composable
fun CircularButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.size(56.dp), // Adjust size as needed

        shape = CircleShape // This makes the button circular
    ) {
        Text("+")
    }
}
@Composable
fun Card(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .requiredWidth(width = 327.dp)
            .requiredHeight(height = 232.dp)
    ) {
        Box(
            modifier = Modifier
                .requiredWidth(width = 327.dp)
                .requiredHeight(height = 232.dp)
        ) {
            Box(
                modifier = Modifier
                    .requiredWidth(width = 327.dp)
                    .requiredHeight(height = 232.dp)
                    .clip(shape = RoundedCornerShape(20.dp))
                    .background(color = Color(0xff9087e5)))
        }
        Property1Small(
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(
                    x = 91.dp,
                    y = 156.dp
                ))
        Image(
            painter = painterResource(id = R.drawable.img_2),
            contentDescription = "2",
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(
                    x = 248.dp,
                    y = 134.dp
                )
                .requiredWidth(width = 64.dp)
                .requiredHeight(height = 56.dp))
        Image(
            painter = painterResource(id = R.drawable.img_1),
            contentDescription = "1",
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(
                    x = 16.dp,
                    y = 16.dp
                )
                .requiredWidth(width = 48.dp)
                .requiredHeight(height = 48.dp))
        Text(
            text = "FEATURED",
            color = Color.White.copy(alpha = 0.8f),
            lineHeight = 10.em,
            style = TextStyle(
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium),
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(
                    x = 124.dp,
                    y = 32.dp
                )
                .wrapContentHeight(align = Alignment.CenterVertically))
        Text(
            text = "Do your own pathways",
            color = Color.White,
            textAlign = TextAlign.Center,
            lineHeight = 7.5.em,
            style = TextStyle(
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium),
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(
                    x = 63.dp,
                    y = 68.dp
                )
                .requiredWidth(width = 200.dp))
    }
}

@Composable
fun Property1Small(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    Row(
        horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .requiredHeight(height = 44.dp)
            .clip(shape = RoundedCornerShape(20.dp))
            .background(color = Color.White)
            .padding(
                horizontal = 16.dp,
                vertical = 12.dp
            )

    ) {
        Icon(
            painter = painterResource(id = R.drawable.property1findfriends),
            contentDescription = "Icon",
            modifier = Modifier
                .requiredSize(size = 20.dp))
        Text(
            text = "See pathways",
            color = Color(0xff6a5ae0),
            textAlign = TextAlign.Center,
            lineHeight = 10.em,
            style = TextStyle(
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium),
            modifier = Modifier
                .wrapContentHeight(align = Alignment.CenterVertically)
                .clickable {
                    if (state == "Student") {
                        val intent = Intent(context, Learningpath_student::class.java)
                        startActivity(context, intent, null)

                    } else {
                        val intent = Intent(context, Learning_pathways_profesor::class.java)
                        startActivity(context, intent, null)

                    }
                })
    }
}

@Preview(widthDp = 327, heightDp = 232)
@Composable
fun CardPreview() {
    Card(Modifier.offset(y = -100.dp))
}
@Composable
fun Cardus(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .requiredWidth(width = 327.dp)
            .requiredHeight(height = 84.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(shape = RoundedCornerShape(20.dp))
                .background(color = Color(0xffffccd5)))
        Box(
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(
                    x = 255.dp,
                    y = 16.dp
                )
                .requiredSize(size = 48.dp)
        ) {
            Box(
                modifier = Modifier
                    .align(alignment = Alignment.TopEnd)
                    .offset(
                        x = 0.dp,
                        y = 0.dp
                    )
                    .requiredSize(size = 48.dp)
                    .clip(shape = CircleShape)
                    .background(color = Color(0xffffb3c0)))
            Box(
                modifier = Modifier
                    .align(alignment = Alignment.TopEnd)
                    .offset(
                        x = 0.dp,
                        y = 0.dp
                    )
                    .requiredSize(size = 48.dp)
                    .clip(shape = CircleShape)
                    .background(color = Color(0xffff8fa2))
                    .border(
                        border = BorderStroke(3.dp, Color(0xffff8fa2)),
                        shape = CircleShape
                    ))
            Text(
                text = "100%",
                color = Color.White,
                textAlign = TextAlign.Center,
                lineHeight = 10.em,
                style = TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium),
                modifier = Modifier
                    .align(alignment = Alignment.TopEnd)
                    .offset(
                        x = (-10).dp,
                        y = 14.dp
                    )
                    .wrapContentHeight(align = Alignment.CenterVertically))
        }
        Text(
            text = "Procent profi / elevi activi",
            color = Color(0xff660012),
            lineHeight = 7.5.em,
            style = TextStyle(
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium),
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(
                    x = 56.dp,
                    y = 44.dp
                ))
        Text(
            text = "Profesori activi",
            color = Color(0xff660012).copy(alpha = 0.5f),
            lineHeight = 10.em,
            style = TextStyle(
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium),
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(
                    x = 24.dp,
                    y = 16.dp
                )
                .wrapContentHeight(align = Alignment.CenterVertically))
        Icon(
            painter = painterResource(id = R.drawable.property1music),
            contentDescription = "Icon",
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(
                    x = 24.dp,
                    y = 44.dp
                ))
    }
}

@Preview(widthDp = 327, heightDp = 84)
@Composable
fun CardusPreview() {
    Cardus(Modifier)
}
