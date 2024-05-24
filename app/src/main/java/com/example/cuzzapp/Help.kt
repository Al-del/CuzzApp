import android.annotation.SuppressLint
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.cuzzapp.HomeScreen
import com.example.cuzzapp.R
import com.example.cuzzapp.RankingScreen
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.ScaffoldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.LifecycleOwner
import coil.compose.rememberImagePainter
import com.example.cuzzapp.Profile
import com.example.cuzzapp.Register
import com.example.cuzzapp.Shop
import com.example.cuzzapp.asis
import com.example.cuzzapp.ui.theme.*
import com.google.firebase.database.FirebaseDatabase
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

public var url_photo:String = ""
public var points:Int = 0
public var descriptiones:String = ""
val API_KEY = "AIzaSyAOYoOXNP1sIOPeIErbIO9pbM0WtAjLomo"
val YOUTUBE_API_SERVICE_NAME = "youtube"
val YOUTUBE_API_VERSION = "v3"
sealed class Screen(val route: String, val label: String, val icon: Int) {
    object Home : Screen("home", "Home", R.drawable.home)
    object Ranking : Screen("search", "Ranking", R.drawable.ranking)
    object Profile : Screen("profile", "Profile", R.drawable.profile)
}
@Composable
fun AppNavigator() {
    val navController = rememberNavController()
    NavHost(navController, startDestination = Screen.Home.route) {
        composable(Screen.Home.route) { HomeScreen() }
        composable(Screen.Ranking.route) { RankingScreen() }
        composable(Screen.Profile.route) { Profile() }
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
        val items = listOf(Screen.Home, Screen.Ranking, Screen.Profile)
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
                            val intent = Intent(context, Profile::class.java)
                            context.startActivity(intent)
                        }
                    }
                },
                selectedContentColor = LightOrange, // Set the color of the selected item
                unselectedContentColor = LightYellow // Set the color of the unselected item
            )
        }
    }
}

class video_one{
    var title:String = ""
    var description:String = ""
    var url:String = ""
    var thumbnail:String = ""
    var id:String = ""

}
public var video_l:video_one = video_one()
public var username_true:String = ""
@Composable
fun YouTubePlayer_ok(
    youtubeVideoId: String,
    lifecycleOwner: LifecycleOwner
){
    val context = LocalContext.current // Get the local context to show the toast
    AndroidView(
        modifier = Modifier
            .offset(x = 0.dp, y = 50.dp)
            .fillMaxWidth()
            .padding(8.dp)
            .clip(RoundedCornerShape(16.dp)),
        factory = { context->
            YouTubePlayerView(context = context).apply {
                lifecycleOwner.lifecycle.addObserver(this)

                addYouTubePlayerListener(object: AbstractYouTubePlayerListener(){
                    override fun onReady(youTubePlayer: YouTubePlayer) {
                        youTubePlayer.loadVideo(youtubeVideoId, 0f)
                    }

                    override fun onStateChange(youTubePlayer: YouTubePlayer, state: PlayerConstants.PlayerState) {
                        super.onStateChange(youTubePlayer, state)
                        if (state == PlayerConstants.PlayerState.ENDED) {
                            Toast.makeText(context, "Video is over", Toast.LENGTH_SHORT).show()

                            // Get a reference to the Firebase database
                            val database = FirebaseDatabase.getInstance()

                            // Get a reference to the "accounts" node
                            val accountsRef = database.getReference("accounts")

                            // Update the points to 100
                            points+= 100
                            accountsRef.child(username_true).child("points").setValue(points)
                        }
                    }
                })
            }
        }
    )
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter",
    "UnusedMaterial3ScaffoldPaddingParameter"
)
@Composable
fun Drawer_final(scaffoldState: ScaffoldState, action: @Composable () -> Unit) {
    val coroutineScope = rememberCoroutineScope()
    val navController = rememberNavController() // Create a NavController
    val context = LocalContext.current // Get the local context to use startActivity
    androidx.compose.material.Scaffold(
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
                androidx.compose.material.Button(
                    onClick = {
                        val intent = Intent(context, asis::class.java)
                        context.startActivity(intent)

                    },
                    shape = RoundedCornerShape(80.dp),
                    colors = androidx.compose.material.ButtonDefaults.buttonColors(backgroundColor = Color(0xff5d5d5d)),
                    modifier = Modifier
                        .requiredWidth(width = 170.dp)
                        .requiredHeight(height = 40.dp)
                        .clickable {
                            val intent = Intent(context, asis::class.java)
                            context.startActivity(intent)
                        }
                ) {
                    Text("Asistenta")
                }
                Spacer(modifier = Modifier.height(24.dp)) // Add bigger space

                androidx.compose.material.Button(
                    onClick = {
                        val intent = Intent(context, Shop::class.java)
                        context.startActivity(intent)

                    },
                    shape = RoundedCornerShape(80.dp),
                    colors = androidx.compose.material.ButtonDefaults.buttonColors(backgroundColor = Color(0xff5d5d5d)),
                    modifier = Modifier
                        .requiredWidth(width = 170.dp)
                        .requiredHeight(height = 40.dp)
                        .clickable {
                            val intent = Intent(context, asis::class.java)
                            context.startActivity(intent)
                        }
                ) {
                    Text("Shop")
                }
                Spacer(modifier = Modifier.height(24.dp)) // Add bigger space
                androidx.compose.material.Button(
                    onClick = { },
                    shape = RoundedCornerShape(80.dp),
                    colors = androidx.compose.material.ButtonDefaults.buttonColors(backgroundColor = Color(0xff5d5d5d)),
                    modifier = Modifier
                        .requiredWidth(width = 170.dp)
                        .requiredHeight(height = 40.dp)
                        .clickable {
                            val intent = Intent(context, RankingScreen::class.java)
                            context.startActivity(intent)
                        }
                ) {
                    Text("Ranking")
                }
                Spacer(modifier = Modifier.height(24.dp)) // Add bigger space
                androidx.compose.material.Button(
                    onClick = {

                        val inent = Intent(context, HomeScreen::class.java)
                        context.startActivity(inent)
                    },
                    shape = RoundedCornerShape(80.dp),
                    colors = androidx.compose.material.ButtonDefaults.buttonColors(backgroundColor = Color(0xff5d5d5d)),
                    modifier = Modifier
                        .requiredWidth(width = 170.dp)
                        .requiredHeight(height = 40.dp)
                        .clickable {
                            val intent = Intent(context, HomeScreen::class.java)
                            context.startActivity(intent)
                        }
                ) {
                    Text("Home")
                }
                Spacer(modifier = Modifier.height(24.dp)) // Add bigger space
                androidx.compose.material.Button(
                    onClick = { },
                    shape = RoundedCornerShape(80.dp),
                    colors = androidx.compose.material.ButtonDefaults.buttonColors(backgroundColor = Color(0xff5d5d5d)),
                    modifier = Modifier
                        .requiredWidth(width = 170.dp)
                        .requiredHeight(height = 40.dp)
                        .clickable {
                            val intent = Intent(context, Profile::class.java)
                            context.startActivity(intent)
                        }
                ) {
                    Text("Profile")
                }

            }
        },
        content = {

            action()
        },

        )
}