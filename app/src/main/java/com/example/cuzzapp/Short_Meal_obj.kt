package com.example.cuzzapp


import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.ModalDrawer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import androidx.compose.material3.rememberDrawerState
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController

class profil {
    var scor: Int =0
    var name: String =""
    var posts: Int =0
    var imagine:Int=0
}
class Short_Meal_obj {

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    fun ModalDrawerExample(
        context: Context,
        navController: NavController,
        username: String,
        activityContentScope: @Composable (state: DrawerState, scope: CoroutineScope) -> Unit
    ) {
        val profilSuccessful = remember { mutableStateOf(false) }
        val state = rememberDrawerState(initialValue = DrawerValue.Closed)
        val scope = rememberCoroutineScope()
        val navController = rememberNavController()

        ModalDrawer(
            modifier = Modifier.fillMaxSize(),
            drawerElevation = 5.dp,
            drawerShape = RoundedCornerShape(topEnd = 30.dp),
            gesturesEnabled = true,
            drawerContent = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.icoico),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(top = 16.dp)
                            .size(150.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Fit
                    )
                    Spacer(modifier = Modifier.padding(vertical = 16.dp))

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {


                        Button(
                            onClick = {
                                val intent = Intent(context, Show_recepies::class.java)
                                intent.putExtra("username", username)
                                context.startActivity(intent)
                            },
                            modifier = Modifier.height(55.dp).width(240.dp),
                            shape = RoundedCornerShape(5.dp),
                            colors = ButtonDefaults.buttonColors(Color(0xFFDDD6EA))
                        ) {
                            Text(text = "RECEPIES",
                                color = (Color(0XFF363457)),
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold
                                )
                        }

                        Spacer(modifier = Modifier.padding(vertical = 4.dp))
                        Button(
                            onClick = {
                                val intent = Intent(context, Main::class.java)
                                intent.putExtra("username", username)
                                context.startActivity(intent)
                            },
                            modifier = Modifier.height(55.dp).width(240.dp),
                            shape = RoundedCornerShape(5.dp),
                            colors = ButtonDefaults.buttonColors(Color(0xFFDDD6EA))
                        ) {
                            Text(text = "TAKE PICTURE",
                                color = (Color(0XFF363457)),
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.padding(vertical = 20.dp))
                        Button(
                            onClick = {
                                val intent = Intent(context, MainActivity::class.java)
                                context.startActivity(intent)
                            },
                            modifier = Modifier.height(50.dp).width(150.dp),
                            shape = RoundedCornerShape(5.dp),
                            colors = ButtonDefaults.buttonColors(Color(0xFFDDD6EA))
                        ) {
                            Text(
                                text = "LOGOUT",
                                color = (Color(0XFF363457)),
                                fontSize = 13.sp
                            )
                        }
                            Spacer(modifier = Modifier.height(150.dp)) // Add a spacer
                            androidx.compose.material.Text(
                                text = "by ShortCircuiT",
                                textAlign = TextAlign.Center,
                                color = Color(0XFFFFAE03)
                                // modifier = Modifier.align(Alignment.BottomCenter)
                            )

                        }
                    }

            }
        ) {
            activityContentScope(state, scope)
        }
    }

    @Composable
    fun profilepage(abc: profil, navController: NavController) {
        val state = rememberDrawerState(initialValue = DrawerValue.Closed)
        val scope = rememberCoroutineScope()

        ModalDrawer(
            //drawerState = state,
            gesturesEnabled = state.isOpen,
            drawerContent = {
                Button(onClick = {
                    navController.navigate("route_to_page_1")
                    scope.launch { state.close() }
                }) {
                    Text(text = "Page 1")
                }

                Button(onClick = {
                    navController.navigate("route_to_page_2")
                    scope.launch { state.close() }
                }) {
                    Text(text = "Page 2")
                }
            }
        ) {
            Card(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 150.dp, bottom = 250.dp, start = 16.dp, end = 16.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxSize(),
                ) {
                    Image(
                        painter = painterResource(id = abc.imagine),
                        contentDescription = abc.name,
                        modifier = Modifier
                            .size(200.dp)
                            .clip(CircleShape)
                            .border(
                                width = 2.dp,
                                color = Color.Black,
                                shape = CircleShape
                            ),
                        contentScale = ContentScale.Crop
                    )
                    Text(text = "Cuza")
                    Row(
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        profilestats(count = abc.scor, title = "Score")
                        profilestats(count = abc.posts, title = "Posts")
                    }

                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Button(onClick = {
                            scope.launch {
                                if (state.currentValue == DrawerValue.Open) {
                                    state.close()
                                } else {
                                    state.open()
                                }
                            }
                        }) {
                            Text(text = "Toggle Drawer")
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun profilestats(count: Int, title: String) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = count.toString(), fontWeight = FontWeight.Bold)
            Text(text = title)
        }
    }
}
