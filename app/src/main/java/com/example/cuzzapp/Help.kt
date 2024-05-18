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
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.compose.ui.res.painterResource
import com.example.cuzzapp.ui.theme.*

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
                        popUpTo(navController.graph.startDestinationId)
                        launchSingleTop = true
                    }
                },
                selectedContentColor = LightOrange, // Set the color of the selected item
                unselectedContentColor = LightYellow // Set the color of the unselected item
            )
        }
    }
}