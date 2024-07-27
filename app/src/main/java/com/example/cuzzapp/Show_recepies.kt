package com.example.cuzzapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.lifecycleScope
import coil.compose.AsyncImage
import com.example.cuzzapp.FoodPair
import com.example.cuzzapp.USR
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import username_true
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class Show_recpie(){
    var ingredients: List<String>? = null
    var title: String? = null
    var image: String? = null
    var video: String? = null
    var instructions: String? = null
}
class Show_recepies : ComponentActivity() {
    private val foodListState = mutableStateOf<List<FoodPair>?>(null)
    object Keys{
        init {
            System.loadLibrary("native-lib")
        }
        external fun App_id() :String
        external fun API_edeman() :String
        external fun API_spoonacular() :String
        external fun API_chat_gpt() :String
        external fun APIKeys() :String
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var searchQuery by remember { mutableStateOf("") }
            val scaffoldState = rememberScaffoldState()


            Drawer(scaffoldState, searchQuery, backgroundColor = SolidColor(Color(0xFF6A5AE0)) ,onSearchQueryChange = { searchQuery = it }) {


                foodListState.value?.let {
                    FoodList(
                        it,
                        lifecycleScope,
                        context = this@Show_recepies
                    )
                }

        }
        }

        lifecycleScope.launch {
            val list = get_list_of_foods(username_true)
            // Update the state with the loaded list
            foodListState.value = list
        }
    }
}

@Composable
fun FoodList(foodList: List<FoodPair>, lifecycleScope: LifecycleCoroutineScope, context: Context) {


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFF6A5AE0)) // Apply the gradient background to the entire activity
            .clip(RoundedCornerShape(16.dp)) // Apply rounded corners
    ) {
        LazyColumn(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp)) // Apply rounded corners
        ) {
            items(foodList) { item ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp) // Increased padding around the card
                        .clip(RoundedCornerShape(16.dp)) // Apply rounded corners
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize() // Make the column fill the card
                            .padding(24.dp) // Increased padding inside the card
                    ) {
                        AsyncImage(
                            model = item.second,
                            contentDescription = null,
                            modifier = Modifier
                                .clickable {
                                    lifecycleScope.launch {
                                        val recipeUrl = withContext(Dispatchers.IO) {
                                            getEdamamRecipes(
                                                Show_recepies.Keys.API_edeman(),
                                                Show_recepies.Keys.App_id(),
                                                item.first
                                            )
                                        }
                                        val obj_d = withContext(Dispatchers.IO) {
                                            getSpoonacularRecipes(
                                                Show_recepies.Keys.API_spoonacular(),
                                                recipeUrl.toString(),
                                                recepie_Name = item.first
                                            )
                                        }
                                        val intent = Intent(context, Recepie_idk::class.java).apply {
                                            putExtra("title", obj_d.title)
                                            putExtra("image", obj_d.image)
                                            putExtra("ingredients", obj_d.ingredients.toString())
                                            putExtra("video", obj_d.video)
                                            putExtra("instrutions", obj_d.instructions)
                                            putExtra("urlus", recipeUrl)
                                        }
                                        context.startActivity(intent)
                                    }
                                }
                                .fillMaxWidth() // Make the image fill the maximum width
                                .height(200.dp) // Increased height for the image
                        )

                        Text(text = "Name: ${item.first}", color = Color.Black) // Set text color to black for better contrast
                        // Add more details as needed
                    }
                }
            }
        }
    }
}
suspend fun getEdamamRecipes(apiKey: String, appId: String, food: String, numResults: Int = 5): String? {
    val baseUrl = "https://api.edamam.com/search"

    val client = OkHttpClient()

    // Set up parameters for the API request
    val url = "$baseUrl?q=$food&app_id=$appId&app_key=$apiKey&to=$numResults"

    // Make the API request
    val request = Request.Builder()
        .url(url)
        .build()

    val response = client.newCall(request).execute()

    // Transform the response into a JSON
    val jsonData = JSONObject(response.body?.string())

    // Get the recipe URL
    val hits = jsonData.getJSONArray("hits")
    if (hits.length() > 0) {
        val firstHit = hits.getJSONObject(0)
        val recipe = firstHit.getJSONObject("recipe")
        val recipeUrl = recipe.getString("url")
        Log.d("kilo", "Recipe URL is: $recipeUrl")
        return recipeUrl
    } else {
        return null  // No recipes found for the specified food
    }
}
suspend fun getSpoonacularRecipes(apiKey: String, urlus: String, numResults: Int = 5,recepie_Name: String?): Show_recpie {
    val baseUrl = "https://api.spoonacular.com/recipes/extract"

    val client = OkHttpClient()

    // Set up parameters for the API request
    val url = "$baseUrl?url=$urlus&apiKey=$apiKey"

    // Make the API request
    val request = Request.Builder()
        .url(url)
        .build()

    val response = client.newCall(request).execute()
    //Log.d("kilo", "Response is: ${response.body?.string()}")
    // Transform the response into a JSON
    var responseBody: String? = null

    try {
        responseBody = response.body?.string()
    } catch (e: Exception) {
        // Handle the exception
        e.printStackTrace()
    } finally {
        response.body?.close()
    }
    Log.d("kilo", "Response is: $responseBody")
    val jsonData = responseBody?.let { JSONObject(it) }
    //show them into some texts
    // Get the recipe URL
    try {


        val title = jsonData?.getString("title")
        val image = jsonData?.getString("image")
        val ingredients = jsonData?.getJSONArray("extendedIngredients")
        val ingredients_list = mutableListOf<String>()
        if (ingredients != null) {
            for (i in 0 until ingredients.length()) {
                val ingredient = ingredients.getJSONObject(i)
                val name = ingredient.getString("name")
                ingredients_list.add(name)
            }
        }
        val instructions = jsonData?.getString("instructions")
        Log.d("kilo", "Title is: $title")
        Log.d("kilo", "Image is: $image")
        Log.d("kilo", "Instructions are: $instructions")
        Log.d("kilo", "Ingredients are: $ingredients")
        val recipe = Show_recpie()
        recipe.title = title
        recipe.image = image
        recipe.ingredients = ingredients_list
        recipe.instructions = instructions
        /* TO DO  get a video*/
        // Return the response as a string
        val a = recepie_Name?.let { getSpoonacularRecipeVideo(apiKey, it) }
        recipe.video=a
        Log.d("kilo", "Response is: ${a}")
        return recipe
//    return response.body?.string()
    }catch (e: Exception) {
        // Handle the exception
        e.printStackTrace()
        Log.d("kilo", "Response is: ${e}")
        return Show_recpie()
    }
}
suspend fun getSpoonacularRecipeVideo(apiKey: String, recipeName: String): String? {
    try {
        val baseUrl = "https://api.spoonacular.com/food/videos/search"

        val client = OkHttpClient()

        // Set up parameters for the API request
        val url = "$baseUrl?query=$recipeName&apiKey=$apiKey"

        // Make the API request
        val request = Request.Builder()
            .url(url)
            .build()

        val response = client.newCall(request).execute()

        // Transform the response into a JSON
        val jsonData = JSONObject(response.body?.string())
        Log.d("kilo", "Response is: ${jsonData} ${recipeName}")
        // Get the video URL
        val videos = jsonData.getJSONArray("videos")
        if (videos.length() > 0) {
            val firstVideo = videos.getJSONObject(0)
            val videoUrl = firstVideo.getString("youTubeId")

            return "https://www.youtube.com/watch?v=$videoUrl"
        } else {
            return null  // No videos found for the specified recipe
        }
    }catch (e: Exception) {
        // Handle the exception
        e.printStackTrace()
        return null
    }
}
@Composable
fun LoadImageFromUrl(url: String) {
    AsyncImage(
        model = url,
        contentDescription = "Translated description of what the image contains",
        modifier = Modifier
            .size(200.dp)
            .clip(CircleShape)
            .border(
                width = 2.dp,
                color = Color.Black,
                shape = CircleShape
            )
    )
}
suspend fun get_list_of_foods(userName: String): MutableList<FoodPair>? = suspendCoroutine { continuation ->
    // Get a reference to the Firebase database
    val database = FirebaseDatabase.getInstance()

    // Modify this line to use the specific path. Assuming "foodsList" is the main node and it contains child nodes named after usernames
    val userFoodsRef = database.getReference("recepies").child(userName)

    // Listen for a single snapshot of the data at this path
    userFoodsRef.addListenerForSingleValueEvent(object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            if (dataSnapshot.exists()) {
                // The user's food list exists in the database
                val list = mutableListOf<FoodPair>()
                dataSnapshot.children.forEach { snapshot ->
                    val first = snapshot.child("first").getValue(String::class.java) ?: ""
                    val second = snapshot.child("second").getValue(String::class.java) ?: ""
                    list.add(FoodPair(first, second))
                }
                Log.d("get_list_of_foods", "Fetched list: $list")
                continuation.resume(list)
            } else {
                // No data found at the specified path
                Log.d("get_list_of_foods", "No data found for user: $userName")
                continuation.resume(null)
            }
        }

        override fun onCancelled(databaseError: DatabaseError) {
            // Handle possible errors
            Log.e("get_list_of_foods", "Database error: ${databaseError.message}")
            continuation.resumeWithException(databaseError.toException())
        }
    })
}