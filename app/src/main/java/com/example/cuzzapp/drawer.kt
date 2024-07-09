package com.example.cuzzapp

import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.Divider
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import backcolor
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
            content()
        },
bottomBar = {
    Box(
        modifier = Modifier
            .fillMaxWidth() // Fill the width of the screen
            .height(55.dp) // Set the height of the Box
            .zIndex(1f), // Ensure the FAB is drawn over the Box

        contentAlignment = Alignment.BottomCenter // Center the content within the Box
    ) {
        FloatingActionButton(
            onClick = { },
            containerColor = Color(0xff613eea),
            modifier = Modifier
                .offset(y = -10.dp)
                .zIndex(1f) // Ensure the FAB is drawn over the Box
        ) {
            Icon(
                painter = painterResource(id = R.drawable.store),
                contentDescription = "shop",
                modifier = Modifier // Fill the FAB size
                    .clickable{
                        val intent = Intent(context, Shop::class.java)
                        startActivity(context, intent, null)
                    }
            )
        }
    }
    StatusHomeModeDarkPreview()
},
        topBar = { }

    )
}
@Composable
fun AppNavigator(navController: NavHostController) {
    NavHost(navController, startDestination = Screen.Home.route) {
        composable(Screen.Home.route) { HomeScreen() }
        composable(Screen.Ranking.route) { RankingScreen() }
        composable(Screen.Profile.route) { Profile() }
        composable(Screen.Quizz.route) { if(state == "Student"){ Quizz_student() }else{ Quizz_teacher()} }
    }
}

@Composable
fun StatusHomeModeDark(modifier: Modifier = Modifier) {
val contex = LocalContext.current
    Box(
        modifier = modifier

    ) {

    // Floating Action Button centered and overlaid over the other content

        Box(
            modifier = modifier
                .align(alignment = Alignment.TopStart)
                .offset(x = 0.dp,
                    y = 32.dp)
                .fillMaxWidth()
                .requiredHeight(height = 75.dp)
                .clip(shape = RoundedCornerShape(topStart = 32.dp, topEnd = 22.dp))

        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = modifier
                    .requiredWidth(width = 428.dp)
                    .requiredHeight(height = 75.dp)
                    .padding(horizontal = 25.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.Start)
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(5.dp, Alignment.Top),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = modifier
                            .requiredWidth(width = 70.dp)
                            .requiredHeight(height = 75.dp)
                            .padding(horizontal = 15.dp,
                                vertical = 12.5.dp)

                    ) {
                        Frame866()
                    }
                    Column(
                        verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.Top),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = modifier
                            .requiredWidth(width = 70.dp)
                            .requiredHeight(height = 75.dp)
                            .padding(horizontal = 15.dp,
                                vertical = 12.5.dp)
                    ) {
                        Icon(
                            modifier = Modifier.clickable{


                            },
                            painter = painterResource(id = R.drawable.achievement),
                            contentDescription = "search")


                    }
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.Start)
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.Top),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = modifier
                            .requiredWidth(width = 70.dp)
                            .requiredHeight(height = 75.dp)
                            .padding(horizontal = 15.dp,
                                vertical = 12.5.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.path),
                            contentDescription = "Path")

                    }
                    Column(
                        verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.Top),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = modifier
                            .requiredWidth(width = 70.dp)
                            .requiredHeight(height = 75.dp)
                            .padding(horizontal = 15.dp,
                                vertical = 12.5.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.profile),
                            contentDescription = "user")

                    }
                }
            }


        }
    }
}

@Composable
public  fun StatusHomeModeDarkPreview() {
    Box(modifier = Modifier.clipToBounds() // This will prevent the content from being clipped
        .background(color = Color(0xff5755FE)).clip(RoundedCornerShape(30.dp)),
        contentAlignment = Alignment.BottomCenter
    ) {
    StatusHomeModeDark(Modifier.clipToBounds() // This will prevent the content from being clipped
        .align(Alignment.BottomCenter).offset(y = (-5).dp))

}
}
@Composable
fun Frame866(modifier: Modifier = Modifier) {
    Column(
        verticalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .clip(shape = RoundedCornerShape(topStart = 39.dp, topEnd = 39.dp, bottomStart = 39.dp))
            .background(color = Color(0xff613eea))
            .border(border = BorderStroke(1.dp, Color(0xff0b4af5)),
                shape = RoundedCornerShape(topStart = 39.dp, topEnd = 39.dp, bottomStart = 39.dp))
            .padding(all = 10.dp)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.home),
            contentDescription = "wallet",
            modifier = Modifier
                .requiredSize(size = 19.dp))
    }
}