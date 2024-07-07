package com.example.cuzzapp

import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberImagePainter
import com.example.cuzzapp.ui.theme.LightOrange
import com.example.cuzzapp.ui.theme.LightYellow
import com.example.cuzzapp.ui.theme.LighterRed
import com.example.cuzzapp.ui.theme.Pink
import state
import url_photo
import username_for_all
import username_true
import viewedProfile

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun Drawer(
    scaffoldState: ScaffoldState,
    content: @Composable () -> Unit // Changed parameter here
) {
    val coroutineScope = rememberCoroutineScope()
    val navController = rememberNavController() // Create a NavController
    val context = LocalContext.current // Get the local context to use startActivity
    Scaffold(
        scaffoldState = scaffoldState,
        drawerContent = {
            Column(
                modifier = Modifier
                    .fillMaxSize() // Fill the parent
                    .background(Color(0xFFD9D9D9)), // Set background color
                verticalArrangement = Arrangement.Center, // Center vertically
                horizontalAlignment = Alignment.CenterHorizontally // Center horizontally
            ) {
                Box(
                ) {
                    val painter = rememberImagePainter(data = url_photo)
                    Image(
                        painter = painter,
                        contentDescription = null,
                        modifier = Modifier
                            .clickable {

                            }
                            .size(200.dp) // Set the size of the image
                            .clip(CircleShape) // Clip the image to a circle
                    )
                }
                Spacer(modifier = Modifier.height(24.dp)) // Add bigger space

                Button(
                    onClick = {
                        val intent = Intent(context, asis::class.java)
                        startActivity(context, intent, null)

                    },
                    shape = RoundedCornerShape(80.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xff5d5d5d)),
                    modifier = Modifier
                        .requiredWidth(width = 170.dp)
                        .requiredHeight(height = 40.dp)
                        .clickable {
                            val intent = Intent(context, asis::class.java)
                            startActivity(context, intent, null)
                        }
                ) {
                    Text("Asistenta")
                }
                Spacer(modifier = Modifier.height(24.dp)) // Add bigger space
                Button(
                    onClick = {
                        Log.d("Drawer", "Ranking button clicked")
                        val intent = Intent(context, Image__to_book::class.java)
                        startActivity(context, intent, null)
                    },
                    shape = RoundedCornerShape(80.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xff5d5d5d)),
                    modifier = Modifier
                        .requiredWidth(width = 170.dp)
                        .requiredHeight(height = 40.dp)
                        .clickable {

                        }
                ) {
                    Text("Image to bookd")
                }
                Spacer(modifier = Modifier.height(24.dp)) // Add bigger space
                Button(
                    onClick = {
                        val intent = Intent(context, HomeScreen::class.java)
                        startActivity(context, intent, null)
                    },
                    shape = RoundedCornerShape(80.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xff5d5d5d)),
                    modifier = Modifier
                        .requiredWidth(width = 170.dp)
                        .requiredHeight(height = 40.dp)
                ) {
                    Text("Home")
                }
                Spacer(modifier = Modifier.height(24.dp)) // Add bigger space
                Button(
                    onClick = {
                        val intent = Intent(context, Profile::class.java)
                        startActivity(context, intent, null)

                    },
                    shape = RoundedCornerShape(80.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xff5d5d5d)),
                    modifier = Modifier
                        .requiredWidth(width = 170.dp)
                        .requiredHeight(height = 40.dp)
                ) {
                    Text("Profile")
                }
                Spacer(modifier = Modifier.height(24.dp)) // Add bigger space

                Button(
                    onClick = {
                        val intent = Intent(context, Shop::class.java)
                        startActivity(context, intent, null)

                    },
                    shape = RoundedCornerShape(80.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xff5d5d5d)),
                    modifier = Modifier
                        .requiredWidth(width = 170.dp)
                        .requiredHeight(height = 40.dp)
                        .clickable {
                            val intent = Intent(context, asis::class.java)
                            startActivity(context, intent, null)
                        }
                ) {
                    Text("Shop")
                }
                Spacer(modifier = Modifier.height(24.dp)) // Add bigger space

                Button(
                    onClick = {
                        val intent = Intent(context, messj::class.java)
                        startActivity(context, intent, null)

                    },
                    shape = RoundedCornerShape(80.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xff5d5d5d)),
                    modifier = Modifier
                        .requiredWidth(width = 170.dp)
                        .requiredHeight(height = 40.dp)

                ) {
                    Text("Messages")
                }
            }


            Button(
                onClick = {
                    if(state == "Student"){
                        val intent = Intent(context, Learningpath_student::class.java)
                        startActivity(context, intent, null)

                    }else{
                        val intent = Intent(context, Learning_pathways_profesor::class.java)
                        startActivity(context, intent, null)

                    }

                },
                shape = RoundedCornerShape(80.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xff5d5d5d)),
                modifier = Modifier
                    .requiredWidth(width = 170.dp)
                    .requiredHeight(height = 40.dp)
                    .offset(y = -90.dp, x = 78.dp)
            ) {
                Text("Pathways")
            }
            Button(
                onClick = {
                    val intent = Intent(context, Other_Portofolios::class.java)
                    ContextCompat.startActivity(context, intent, null)
                    viewedProfile=username_for_all

                },
                shape = RoundedCornerShape(80.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xff5d5d5d)),
                modifier = Modifier
                    .offset(x = 78.dp, y = -35.dp)
                    .requiredWidth(width = 170.dp)
                    .requiredHeight(height = 40.dp)

            ) {
                Text("Achievements")
            }

        },
        content = {
            content() // Use the generic composable function parameter
        },
        bottomBar = { AppNavigator(navController) } // Pass the NavController to AppNavigator

    )
}
@Composable
fun AppNavigator(navController: NavHostController) { // Add NavController as a parameter
    NavHost(navController, startDestination = Screen.Home.route) {
        composable(Screen.Home.route) { HomeScreen() }
        composable(Screen.Ranking.route) { RankingScreen() }
        composable(Screen.Profile.route) { Profile() }
        composable(Screen.Quizz.route) { if(state == "Student"){ Quizz_student() }else{ Quizz_teacher()} }
    }
    BottomNavigationBar()
}

@Composable
fun BottomNavigationBar() {
    val context = LocalContext.current // Get the local context to use startActivity
    val navController = rememberNavController() // Remember a NavController
    BottomNavigation(
        backgroundColor = LighterRed, // Set the background color of the BottomNavigation
        contentColor = Pink // Set the default content color of the BottomNavigation
    ) {
        val items = listOf(Screen.Home, Screen.Ranking, Screen.Profile, Screen.Quizz)
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        items.forEachIndexed { index, screen ->
            BottomNavigationItem(
                icon = { Icon(painterResource(screen.icon), contentDescription = null) },
                label = { Text(screen.label) },
                selected = currentRoute == screen.route, // Add this line
                onClick = {
                    when(index) {
                        0 -> {
                            val intent = Intent(context, HomeScreen::class.java)
                            context.startActivity(intent)
                        }
                        1 -> {
                            val intent = Intent(context, RankingScreen::class.java)
                            context.startActivity(intent)
                        }
                        2 -> {
                            username_for_all = username_true
                            val intent = Intent(context, Profile::class.java)
                            context.startActivity(intent)
                        }
                        3 -> {
                            if(state == "Student"){
                                val intent = Intent(context, Quizz_student::class.java)
                                startActivity(context, intent, null)

                            }else{
                                val intent = Intent(context, Quizz_teacher::class.java)
                                startActivity(context, intent, null)

                            }
                        }
                    }
                },
                selectedContentColor = LightOrange, // Set the color of the selected item
                unselectedContentColor = LightYellow // Set the color of the unselected item
            )
        }
    }
}