package com.example.cuzzapp

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONObject
import username_true
import java.io.IOException
data class FoodPair(
    var first: String = "",
    var second: String = ""
)
data class USR(
    // on below line creating variables
    // for employee name, contact number
    // and address
    var username: String?="",
    var password: String?="",
    //List of string
    var list: List<FoodPair>?
)
data class Recipe(val title: String, val imageUrl: String, val id: String)
class Recepies : ComponentActivity() {

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val param = intent.getStringExtra("recepie")
        val usernamuss = intent.getStringExtra("username")
        lifecycleScope.launch {
            val recipes = param?.let { ingredient ->
                getRecipes(ingredient, this@Recepies)
            } ?: emptyList()

            // Now you can use the recipes list
            // For example, you can print it to the log
            Log.d("Recipes", recipes.joinToString())
            lifecycleScope.launch {
                val recipes = param?.let { ingredient ->
                    getRecipes(ingredient, this@Recepies)
                } ?: emptyList()
                setContent {
               var searchQuery by remember { mutableStateOf("") }
val scaffoldState = rememberScaffoldState()

Drawer(
    scaffoldState = scaffoldState,
    searchQuery = searchQuery,
    backgroundColor = Brush.linearGradient(
        colors = listOf(
            Color(0xFF345E2A),
            Color(0xFF403182)
        ),
        start = Offset(0f, 0f),
        end = Offset.Infinite
    ),
    onSearchQueryChange = { searchQuery = it }
) {
    val navController = rememberNavController()
    NavHost(navController, startDestination = "recipeList") {
        composable("recipeList") {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                Color(0xFF345E2A),
                                Color(0xFF403182)
                            ),
                            start = Offset(0f, 0f),
                            end = Offset.Infinite
                        )
                    )
            ) {
                items(recipes) { recipe ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .clip(RoundedCornerShape(16.dp))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(recipe.title, color = Color.Black)
                            Text("ID: ${recipe.id}", color = Color.Black)
                            LoadImageFromUrl(recipe.imageUrl)
                            Button(onClick = {
                                lifecycleScope.launch {
                                    val nutritionAnalysis = getNutritionAnalysis(recipe.id, this@Recepies)
                                    Log.d("Nutrition Analysis", nutritionAnalysis.joinToString())
                                    setContent {
                                        if (usernamuss != null) {
                                            ShowNutritionAnalysis(
                                                nutritionAnalysis,
                                                this@Recepies, // Pass the current ComponentActivity
                                                this@Recepies,
                                                usernamus = usernamuss,
                                                food = recipe.title,
                                                img_url = recipe.imageUrl,
                                                username = usernamuss
                                            )
                                        } else {
                                            ShowNutritionAnalysis(
                                                nutritionAnalysis,
                                                this@Recepies, // Pass the current ComponentActivity
                                                this@Recepies,
                                                usernamus = "unknown",
                                                food = recipe.title,
                                                img_url = recipe.imageUrl,
                                                username = usernamuss
                                            )
                                        }
                                    }
                                }
                            }) {
                                Text("Nutrition Analysis")
                            }
                            Button(
                                onClick = {
                                    if (usernamuss != null) {
                                        updateUserRecipes(username_true, recipe.title, recipe.imageUrl)
                                    }
                                },
                                modifier = Modifier.align(Alignment.End)
                            ) {
                                Text(text = "Add ")
                            }
                        }
                    }
                    Log.d("Image URL", recipe.imageUrl)
                }
            }
        }
    }
}
                }
            }
        }
    }
}
//5&apiKey=5df674c4fc0242e38d2d0dd5cd94ffac
suspend fun getRecipes(ingredient: String, context: Context): List<Recipe> = withContext(Dispatchers.IO) {
    val client = OkHttpClient()
    val request = Request.Builder()
        .url("https://api.spoonacular.com/recipes/findByIngredients?ingredients=$ingredient&number=5&apiKey=${Show_recepies.Keys.API_spoonacular()}")
        .build()

    try {
        val response = client.newCall(request).execute()
        val body = response.body?.string()
        val jsonArray = JSONArray(body)
        val recipeList = mutableListOf<Recipe>()
        for (i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)
            val recipe = jsonObject.getString("title")
            val imageUrl = jsonObject.getString("image")
            val id = jsonObject.getString("id")
            recipeList.add(Recipe(recipe, imageUrl, id))
        }

        recipeList
    } catch (e: IOException) {
        emptyList<Recipe>()
    }
}

//ake a function that call the api and get the nutrition analysis
suspend fun getNutritionAnalysis(recipeId: String, context: Context): List<Pair<String, String>> = withContext(Dispatchers.IO){
    val client = OkHttpClient()
    val request = Request.Builder()
        .url("https://api.spoonacular.com/recipes/$recipeId/nutritionWidget.json?apiKey=${Show_recepies.Keys.API_spoonacular()}")
        .build()

    try {
        val response = client.newCall(request).execute()
        val body = response.body?.string()
        val jsonObject = JSONObject(body)
        val nutrientsArray = jsonObject.getJSONArray("nutrients")
        val recipeList = mutableListOf<Pair<String, String>>()

        for (i in 0 until nutrientsArray.length()) {
            val nutrientObject = nutrientsArray.getJSONObject(i)
            val nutrientName = nutrientObject.getString("name")
            var nutrientAmount = nutrientObject.getString("amount")
            nutrientAmount+=" "+nutrientObject.getString("unit")
            recipeList.add(Pair(nutrientName, nutrientAmount))
        }

        return@withContext recipeList
    } catch (e: IOException) {
        emptyList<Pair<String, String>>()
    }
}
@Composable
fun ShowNutritionAnalysis(
    nutritionAnalysis: List<Pair<String, String>>,
    activity: ComponentActivity,
    context: Context,
    usernamus: String,
    food: String,
    img_url: String,
    username: String?
) {

    val primaryColor = Color(0xFF6A5AE0)
    val secondaryColor = Color(0xFFE0B8F2)
    val accentColor = Color(0xFFD1E8F2)
    val primaryTextColor = Color.Black
    var searchQuery by remember { mutableStateOf("") }
    val scaffoldState = rememberScaffoldState()

    Drawer(
        scaffoldState = scaffoldState,
        searchQuery = searchQuery,
        backgroundColor = Brush.linearGradient(
            colors = listOf(
                primaryColor,
                primaryColor
            ),
            start = Offset(0f, 0f),
            end = Offset.Infinite
        ),
        onSearchQueryChange = { searchQuery = it }
    ) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(primaryColor)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Back button at the top
        Button(
            onClick = { activity.finish() },
            colors = ButtonDefaults.buttonColors(containerColor = secondaryColor),
            modifier = Modifier.align(Alignment.Start)
        ) {
            Text(text = "Back", color = Color.White)
        }

        // Food image at the top center
        AsyncImage(
            model = img_url,
            contentDescription = null,
            modifier = Modifier
                .size(200.dp)
                .clip(RoundedCornerShape(16.dp)),
            contentScale = androidx.compose.ui.layout.ContentScale.Crop
        )

        // Food name below the image
        Text(
            text = food,
            style = TextStyle(
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = accentColor
            ),
            modifier = Modifier.padding(top = 16.dp)
        )

        // Nutrients list in the center
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .background(secondaryColor, RoundedCornerShape(8.dp))
                .padding(16.dp)
        ) {
            LazyColumn(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(nutritionAnalysis) { nutrient ->
                    Text("${nutrient.first}: ${nutrient.second}", color = primaryTextColor)
                }
            }
        }

        // Button at the bottom to add the recipe to the user's list
        Button(
            onClick = {
                if (username != null) {
                    updateUserRecipes(username, food, img_url)
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = primaryColor),
            modifier = Modifier
                .padding(top = 16.dp)
                .align(Alignment.CenterHorizontally)
        ) {
            Text(text = "Add ${usernamus} to your list", color = Color.White)
        }
    }
}
}


fun updateUserRecipes(userName: String, foodus: String, img_url: String) {
    // Get a reference to the Firebase database
    val database = FirebaseDatabase.getInstance()

    // Change the reference to point to "recepies/username"
    val userRecipesRef = database.getReference("recepies").child(userName)

    // Listen for a single snapshot of the data
    userRecipesRef.addListenerForSingleValueEvent(object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            // Initialize a mutable list to hold the updated recipes
            val updatedRecipes = mutableListOf<FoodPair>()

            // Check if the snapshot exists and has children
            if (dataSnapshot.exists() && dataSnapshot.hasChildren()) {
                // Iterate over the existing recipes and add them to the list
                dataSnapshot.children.forEach { snapshot ->
                    val recipe = snapshot.getValue(FoodPair::class.java)
                    recipe?.let { updatedRecipes.add(it) }
                }
            }

            // Add the new recipe to the list
            updatedRecipes.add(FoodPair(foodus, img_url))

            // Optional: Remove any placeholder or invalid entries from the list
            // This step depends on your application's logic

            // Push the updated list back to the database
            userRecipesRef.setValue(updatedRecipes).addOnSuccessListener {
                Log.d("UpdateUserRecipes", "Recipes updated successfully.")
            }.addOnFailureListener { exception ->
                Log.e("UpdateUserRecipes", "Failed to update recipes.", exception)
            }
        }

        override fun onCancelled(databaseError: DatabaseError) {
            // Handle possible errors
            Log.e("UpdateUserRecipes", "Database error: ${databaseError.message}")
        }
    })
}