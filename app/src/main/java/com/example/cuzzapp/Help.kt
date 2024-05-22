import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.cuzzapp.HomeScreen
import com.example.cuzzapp.R
import com.example.cuzzapp.RankingScreen
import com.example.cuzzapp.ui.theme.ProfileScreen
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.LifecycleOwner
import com.example.cuzzapp.ui.theme.*
import com.google.firebase.database.FirebaseDatabase
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

public var url_photo:String = ""
public var points:Int = 0
val API_KEY = "AIzaSyDOHuQ-1zNUgHOLOPJGpK8lK8ySFfjErS0"
val YOUTUBE_API_SERVICE_NAME = "youtube"
val YOUTUBE_API_VERSION = "v3"
sealed class Screen(val route: String, val label: String, val icon: Int) {
    object Home : Screen("home", "Home", R.drawable.home)
    object Search : Screen("search", "Ranking", R.drawable.ranking)
    object Profile : Screen("profile", "Profile", R.drawable.profile)
}
@Composable
fun AppNavigator() {
    val navController = rememberNavController()
    NavHost(navController, startDestination = Screen.Home.route) {
        composable(Screen.Home.route) { HomeScreen() }
        composable(Screen.Search.route) { RankingScreen() }
        composable(Screen.Profile.route) { ProfileScreen() }
    }
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    BottomNavigation(
        backgroundColor = LighterRed, // Set the background color of the BottomNavigation
        contentColor = Pink // Set the default content color of the BottomNavigation
    ) {
        val items = listOf(Screen.Home, Screen.Search, Screen.Profile)

        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        items.forEach { screen ->
            BottomNavigationItem(
                icon = { Icon(painterResource(screen.icon), contentDescription = null) },
                label = { Text(screen.label) },
                selected = currentRoute == screen.route,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                        launchSingleTop = true
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
                            accountsRef.child(username_true).child("Points").setValue(points+ 100)
                        }
                    }
                })
            }
        }
    )
}