import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Button
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
import com.example.cuzzapp.achievementuriUSER
import com.example.cuzzapp.asis
import com.example.cuzzapp.ui.theme.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
val backcolor = Color(0xFF9FA8EB)

public var url_photo:String = ""
var learningPath:List<String> = emptyList()
var viewedProfile:String = ""
var seach_querr:String = "Calculus"
public var username_for_all:String = ""
public var state:String = ""
public var mail:String = ""
public var points:Int = 0
public var descriptiones:String = ""
public var achivement:   MutableList<achievementuriUSER?> = ArrayList<achievementuriUSER?>()
var achivement_other : MutableList<achievementuriUSER?> = ArrayList<achievementuriUSER?>()



sealed class Screen(val route: String, val label: String, val icon: Int) {
    object Home : Screen("home", "Home", R.drawable.home)
    object Ranking : Screen("search", "Ranking", R.drawable.ranking)
    object Profile : Screen("profile", "Profile", R.drawable.profile)
    object Quizz : Screen("profile", "Quizz", R.drawable.ranking)

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
    lifecycleOwner: LifecycleOwner,
    modifier: Modifier = Modifier // Add this line to accept a Modifier parameter
) {
    val context = LocalContext.current
    Column(modifier = modifier) { // Apply the modifier to the Column
        AndroidView(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.38f)
                .padding(8.dp)
                .clip(RoundedCornerShape(16.dp)),
            factory = { context ->
                    YouTubePlayerView(context = context).apply {
                        lifecycleOwner.lifecycle.addObserver(this)

                        addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
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
                                    points += 100
                                    accountsRef.child(username_true).child("points").setValue(points)
                                }
                            }
                        })
                    }
                }
            )

        }
    }

@SuppressLint("UnusedMaterialScaffoldPaddingParameter",
    "UnusedMaterial3ScaffoldPaddingParameter"
)
fun get_achievements_from_db(
    userSnapshot: DataSnapshot,
    achivement: MutableList<achievementuriUSER?>
): MutableList<achievementuriUSER?>{
    try {
        if (userSnapshot.hasChild("achievements")) {
            val yourInnerArrayListSnapShot: DataSnapshot =
                userSnapshot.child("achievements")
            Log.d("TAG", "onDataChange: ${yourInnerArrayListSnapShot}")
            for (innerTemp in yourInnerArrayListSnapShot.children) {
                Log.d("TAG", "onDataChange: ${innerTemp.value}")
                val one_elemen_achivement: achievementuriUSER? =
                    innerTemp.getValue(achievementuriUSER::class.java)
                achivement.add(one_elemen_achivement)
            }

        }
    }
    catch (e:Exception){
        Log.d("TAG", "onDataChange: ${e}")

    }
    return achivement
}