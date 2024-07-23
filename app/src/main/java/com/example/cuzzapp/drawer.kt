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
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Divider
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.InputChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
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
import coil.compose.AsyncImage
import coil.compose.rememberImagePainter
import com.example.cuzzapp.ui.theme.LightOrange
import com.example.cuzzapp.ui.theme.LightYellow
import com.example.cuzzapp.ui.theme.LighterRed
import com.example.cuzzapp.ui.theme.Pink
import mail
import points
import seach_querr
import state
import url_photo
import username_for_all
import username_true
import viewedProfile

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun Drawer(
    scaffoldState: ScaffoldState,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    backgroundColor : Brush,
    content: @Composable () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val navController = rememberNavController() // Create a NavController
    val context = LocalContext.current // Get the local context to use startActivity
    Scaffold(
        scaffoldState = scaffoldState,
        drawerContent = {
            Frame34627Preview()
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
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .background(brush = backgroundColor) // Use the passed backgroundColor
                    .padding(horizontal = 10.dp)
                    //add rounded corners
            ) {
                Box(modifier = Modifier.offset(y = 15.dp)) {
                    AsyncImage(
                        model = url_photo,
                        contentDescription = "plangus",
                        modifier = Modifier
                            .size(55.dp)
                            .offset(y = 10.dp)
                            .align(Alignment.CenterStart)
                            .clip(CircleShape), // Apply offset if needed
                        contentScale = ContentScale.Crop // Ensures the image fills the circle, cropping if necessary
                    )
                }


                TextField(
    value = searchQuery,
    onValueChange = { newValue ->
        onSearchQueryChange(newValue) // Correct way to update the state
    },
    modifier = Modifier
        .requiredHeight(50.dp)
        .requiredWidth(250.dp)
        .offset(x = 10.dp)
        .align(Alignment.Center)
        .background(Color.White, shape = RoundedCornerShape(8.dp)),
    leadingIcon = {
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = "Search",
            tint = Color.Gray
        )
    },
    colors = TextFieldDefaults.textFieldColors(
        backgroundColor = Color.White,
        cursorColor = Color.Black,
        leadingIconColor = Color.Gray,
        textColor = Color.Black,
        focusedIndicatorColor = Color.Transparent, // Hide the indicator
        unfocusedIndicatorColor = Color.Transparent
    ),
    placeholder = {
        Text(text = "Search ...", color = Color.Gray)
    },
    singleLine = true
)
                FloatingActionButton(
                    onClick = {
seach_querr = searchQuery
                        val intent = Intent(context, HomeScreen::class.java)
                        startActivity(context, intent, null)
                    },
                    containerColor = Color(0xFFE9FF97),
                    modifier = Modifier.size(56.dp) // Standard Material Design FAB size
                        .align(Alignment.CenterEnd) // Align the FAB to the end (right side for LTR, left side for RTL)
                        .offset(x = 10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = "Search",
                        tint = Color.Black
                    )
                }
            }
        }

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
val context = LocalContext.current
    Box(
        modifier = modifier

    ) {

    // Floating Action Button centered and overlaid over the other content

        Box(
            modifier = modifier
                .align(alignment = Alignment.TopStart)
                .offset(
                    x = 0.dp,
                    y = 32.dp
                )
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
                            .padding(
                                horizontal = 15.dp,
                                vertical = 12.5.dp
                            )

                    ) {
                        Frame866()
                    }
                    Column(
                        verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.Top),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = modifier
                            .requiredWidth(width = 70.dp)
                            .requiredHeight(height = 75.dp)
                            .padding(
                                horizontal = 15.dp,
                                vertical = 12.5.dp
                            )
                    ) {
                        Icon(
                            modifier = Modifier.clickable{
                                val intent = Intent(context, Other_Portofolios::class.java)
                                intent.putExtra("userr",username_true)
                                ContextCompat.startActivity(context, intent, null)
                                viewedProfile=username_for_all


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
                            .padding(
                                horizontal = 15.dp,
                                vertical = 12.5.dp
                            )
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.path),
                            contentDescription = "Path",
                        modifier= Modifier.clickable{
                            if (state == "Student") {
                                val intent = Intent(context, Learningpath_student::class.java)
                                startActivity(context, intent, null)

                            } else {
                                val intent = Intent(context, Learning_pathways_profesor::class.java)
                                startActivity(context, intent, null)

                            }
                        })

                    }
                    Column(
                        verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.Top),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = modifier
                            .requiredWidth(width = 70.dp)
                            .requiredHeight(height = 75.dp)
                            .padding(
                                horizontal = 15.dp,
                                vertical = 12.5.dp
                            )
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.profile),
                            contentDescription = "user",
                            modifier = Modifier.clickable {

                                val intent = Intent(context, Profile::class.java)
                                intent.putExtra("userr_to",username_true)
                                intent.putExtra("img_link",url_photo)
                                startActivity(context, intent, null)
                            })

                    }
                }
            }


        }
    }
}

@Composable
public  fun StatusHomeModeDarkPreview() {
    Box(modifier = Modifier
        .clipToBounds() // This will prevent the content from being clipped
        .background(color = backcolor)
        .clip(RoundedCornerShape(30.dp)),
        contentAlignment = Alignment.BottomCenter
    ) {
    StatusHomeModeDark(
        Modifier
            .clipToBounds() // This will prevent the content from being clipped
            .align(Alignment.BottomCenter)
            .offset(y = (-5).dp))

}
}
@Composable
fun Frame866(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    Column(
        verticalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier


            .padding(all = 10.dp)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.home),
            contentDescription = "wallet",
            modifier = Modifier
                .requiredSize(size = 45.dp)
                .offset(y = -10.dp)
                .clickable {
                    val intent = Intent(context, HomeScreen::class.java)
                    startActivity(context, intent, null)

                })
    }
}
@Composable
fun drawer_content(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    Column(
        verticalArrangement = Arrangement.spacedBy(147.dp, Alignment.Top),
        modifier = modifier
            .background(
                Brush.radialGradient(
                    0f to Color(0xffb379df),
                    1f to Color(0xff360060),
                    center = Offset(198.5f, 198.5f),
                    radius = 198.5f
                )
            )
            .fillMaxSize()
            .padding(all = 24.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(32.dp, Alignment.Top),
            modifier = Modifier
                .fillMaxWidth()
                .requiredHeight(height = 525.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(48.dp, Alignment.Start),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.Start),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Box(modifier = Modifier.offset(y = 30.dp)) {
                        Box(
                            modifier = Modifier
                                .requiredSize(size = 48.dp)
                                .clip(shape = RoundedCornerShape(48.dp))
                                .background(color = Color(0xffffd88d))
                        ) {
                            val painter = rememberImagePainter(data = url_photo)

                            Image(
                                painter = painter,
                                contentDescription = "81",
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(shape = RoundedCornerShape(256.9054260253906.dp))
                            )
                        }

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .offset(x = 70.dp)
                        ) {

                            Text(
                                text = username_true,
                                color = Color(0xfffcfcfc),
                                lineHeight = 1.6.em,
                                style = TextStyle(
                                    fontSize = 15.sp
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                            )
                            Text(
                                text = mail,
                                color = Color(0xffd1d3d4),
                                lineHeight = 1.em,
                                style = TextStyle(
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                            )
                        }
                    }
                }
            }
            Column(
                verticalArrangement = Arrangement.spacedBy(6.dp, Alignment.Top),
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = 50.dp)
                    .align(alignment = Alignment.CenterHorizontally)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.Start),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(shape = RoundedCornerShape(8.dp))
                        .padding(all = 12.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp, Alignment.Start),
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .weight(weight = 1f)
                    ) {

                        Text(
                            text = "Home",
                            color = Color(0xff6759ff),
                            lineHeight = 1.6.em,
                            style = TextStyle(
                                letterSpacing = 0.3.em,
                                brush = Brush.linearGradient(
                                    colors = listOf(Color.Yellow, Color.Magenta, Color.Cyan),
                                ),
                                fontSize = 20.sp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    val intent = Intent(context, HomeScreen::class.java)
                                    startActivity(context, intent, null)
                                })
                        }

                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.Start),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(shape = RoundedCornerShape(8.dp))
                        .padding(all = 12.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp, Alignment.Start),
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .weight(weight = 1f)
                    ) {

                        Text(
                            text = "Ranking",
                            color = Color(0xfff5f4f5),
                            lineHeight = 1.6.em,
                            style = TextStyle(
                                letterSpacing = 0.3.em,
                                brush = Brush.linearGradient(
                                    colors = listOf(Color.Red, Color.Magenta, Color.Yellow),
                                ),
                                fontSize = 20.sp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    val intent = Intent(context, RankingScreen::class.java)
                                    startActivity(context, intent, null)
                                })
                    }

                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.Start),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(shape = RoundedCornerShape(8.dp))
                        .padding(all = 12.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp, Alignment.Start),
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {

                        Text(
                            text = "Messages",
                            color = Color(0xfff5f5f5),
                            lineHeight = 1.6.em,
                            style = TextStyle(
                                letterSpacing = 0.3.em,
                                brush = Brush.linearGradient(
                                    colors = listOf(Color.Cyan, Color.Magenta, Color.Yellow),
                                ),
                                fontSize = 20.sp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    val intent = Intent(context, messj::class.java)
                                    startActivity(context, intent, null)
                                })
                    }
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.Start),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(shape = RoundedCornerShape(8.dp))
                        .padding(all = 12.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp, Alignment.Start),
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {

                        Text(
                            text = "Assistant",
                            color = Color(0xfff5f5f5),
                            lineHeight = 1.6.em,
                            style = TextStyle(
                                letterSpacing = 0.3.em,
                                brush = Brush.linearGradient(
                                    colors = listOf(Color.Yellow, Color.Magenta, Color.Red),
                                ),
                                fontSize = 20.sp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                        val intent =
                                            Intent(context, asis::class.java)
                                        startActivity(context, intent, null)


                                })
                    }
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.Start),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(shape = RoundedCornerShape(8.dp))
                        .padding(all = 12.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp, Alignment.Start),
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .weight(weight = 1f)
                    ) {

                        Text(
                            text = "Image to book",
                            color = Color(0xfff5f5f5),
                            lineHeight = 1.6.em,
                            style = TextStyle(
                                letterSpacing = 0.3.em,
                                brush = Brush.linearGradient(
                                    colors = listOf(Color.Magenta, Color.Cyan, Color.Yellow),
                                ),
                                fontSize = 20.sp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    val intent = Intent(context, Image__to_book::class.java)
                                    startActivity(context, intent, null)
                                })
                    }

                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.Start),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(shape = RoundedCornerShape(8.dp))
                        .padding(all = 12.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp, Alignment.Start),
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {

                        Text(
                            text = "Recepie",
                            color = Color(0xfff5f5f5),
                            lineHeight = 1.6.em,
                            style = TextStyle(
                                letterSpacing = 0.3.em,
                                brush = Brush.linearGradient(
                                    colors = listOf(Color.Red, Color.Magenta, Color.Yellow),
                                ),
                                fontSize = 20.sp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    val intent = Intent(context, Show_recepies::class.java)
                                    startActivity(context, intent, null)

                                })
                    }
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.Start),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(shape = RoundedCornerShape(8.dp))
                        .padding(all = 12.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp, Alignment.Start),
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {

                        Text(
                            text = "Quizz",
                            color = Color(0xfff5f5f5),
                            lineHeight = 1.6.em,
                            style = TextStyle(
                                letterSpacing = 0.3.em,
                                brush = Brush.linearGradient(
                                    colors = listOf(Color.Cyan, Color.Magenta, Color.Yellow),
                                ),
                                fontSize = 20.sp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    if (state == "Student") {
                                        val intent = Intent(context, Quizz_student::class.java)
                                        startActivity(context, intent, null)

                                    } else {
                                        val intent = Intent(context, Quizz_teacher::class.java)
                                        startActivity(context, intent, null)

                                    }
                                })
                    }
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.Start),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(shape = RoundedCornerShape(8.dp))
                        .padding(all = 12.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp, Alignment.Start),
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {

                        Text(
                            text = "Health",
                            color = Color(0xfff5f5f5),
                            lineHeight = 1.6.em,
                            style = TextStyle(
                                letterSpacing = 0.3.em,
                                brush = Brush.linearGradient(
                                    colors = listOf(Color.Cyan, Color.Magenta, Color.Yellow),
                                ),
                                fontSize = 20.sp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                        val intent = Intent(context, Main::class.java)
                                        startActivity(context, intent, null)


                                })
                    }

                }

            }
        }
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.Top),
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.Top),
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .requiredHeight(height = 2.dp)
                        .clip(shape = RoundedCornerShape(2.dp))
                        .background(color = Color(0xffd1d3d4).copy(alpha = 0.2f)))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.Start),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.Start),
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.light),
                            contentDescription = "UI icon/help/light")
                        Text(
                            text = "Account information",
                            color = Color(0xfff5f5f5),
                            lineHeight = 1.6.em,
                            style = TextStyle(
                                fontSize = 15.sp),
                            modifier = Modifier
                                .fillMaxWidth())
                    }
                }
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(shape = RoundedCornerShape(40.dp))
                    .background(color = Color.White.copy(alpha = 0.15f))
                    .padding(all = 4.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .weight(weight = 0.5f)
                        .clip(shape = RoundedCornerShape(32.dp))
                        .padding(
                            start = 8.dp,
                            end = 16.dp,
                            top = 4.dp,
                            bottom = 4.dp
                        )
                ) {

                    Text(
                        text = "Total points ${points}",
                        color = Color.White,
                        lineHeight = 1.6.em,
                        style = TextStyle(
                            fontSize = 15.sp))
                }
            }
        }
    }
}

@Preview(widthDp = 310, heightDp = 812)
@Composable
private fun Frame34627Preview() {
    drawer_content(Modifier)
}
@Composable
fun Sample(modifier: Modifier = Modifier) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(14.dp, Alignment.Start),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .requiredWidth(width = 450.dp)
    ) {
        Box(
            modifier = Modifier
                .requiredHeight(height = 60.dp)
                .weight(weight = 1f)
                .clip(shape = RoundedCornerShape(10.dp))
                .background(
                    brush = Brush.linearGradient(
                        0f to Color.White,
                        1f to Color(0xff42dfcf),
                        start = Offset(188f, 0f),
                        end = Offset(305.74f, 60f)
                    )
                )
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp, Alignment.Start),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .requiredWidth(width = 376.dp)
                    .requiredHeight(height = 60.dp)
                    .padding(horizontal = 8.dp)
            ) {
                RoundedDefaultBackgroundFalseIconSearchColorDefault()
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.Start),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "Search ... ",
                        color = Color(0xffabb7c2),
                        style = TextStyle(
                            fontSize = 18.sp))
                }
            }
            Box(
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(
                        x = 279.dp,
                        y = 1.5.dp
                    )
                    .requiredWidth(width = 116.dp)
                    .requiredHeight(height = 62.dp)
            ) {
                Box(
                    modifier = Modifier
                        .align(alignment = Alignment.TopStart)
                        .offset(
                            x = 34.dp,
                            y = 4.dp
                        )
                        .requiredSize(size = 42.dp)
                        .clip(shape = CircleShape)
                        .background(color = Color(0xfffbe014))
                        .border(
                            border = BorderStroke(2.dp, Color.White),
                            shape = CircleShape
                        ))

                Image(
                    painter = painterResource(id = R.drawable.lock),
                    contentDescription = "Vector 11",
                    modifier = Modifier
                        .align(alignment = Alignment.TopStart)
                        .offset(
                            x = 15.dp,
                            y = 12.5.dp
                        )
                        .requiredWidth(width = 10.dp)
                        .requiredHeight(height = 16.dp)
                        .border(border = BorderStroke(2.dp, Color.White)))
            }
        }
        RoundedCircleBackgroundFitIconMicColorWhite()
    }
}

@Composable
fun RoundedDefaultBackgroundFalseIconSearchColorDefault(modifier: Modifier = Modifier) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.Start),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .padding(all = 10.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.search),
            contentDescription = "search",
            modifier = Modifier
                .requiredSize(size = 24.dp))
    }
}

@Composable
fun RoundedCircleBackgroundFitIconMicColorWhite(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .requiredSize(size = 60.dp)
            .clip(shape = RoundedCornerShape(5.dp))
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.Start),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(all = 18.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.src),
                contentDescription = "mic",
                modifier = Modifier
                    .requiredSize(size = 24.dp))
        }
        Box(
            modifier = Modifier
                .align(alignment = Alignment.Center)
                .offset(
                    x = 0.dp,
                    y = 0.dp
                )
                .requiredSize(size = 60.dp)
                .clip(shape = RoundedCornerShape(32.dp))
                .background(
                    brush = Brush.linearGradient(
                        0f to Color(0xff1cd8d2),
                        1f to Color(0xff93edc7),
                        start = Offset(30.3f, 0f),
                        end = Offset(30.3f, 60f)
                    )
                ))
    }
}

@Preview(widthDp = 450, heightDp = 60)
@Composable
private fun SamplePreview() {
    Sample(Modifier)
}
