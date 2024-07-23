package com.example.cuzzapp

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.cuzzapp.Short_Meal_obj

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
    var obj = Short_Meal_obj()

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

                    Drawer(scaffoldState, searchQuery, backgroundColor = SolidColor(Color(0xFFffffff)) ,onSearchQueryChange = { searchQuery = it }) {

                    val navController = rememberNavController()
                    NavHost(navController, startDestination = "recipeList") {
                        composable("recipeList") {
                            LazyColumn(modifier = Modifier.fillMaxSize()) {
                                items(recipes) { recipe ->
                                    Column(modifier = Modifier.padding(16.dp)) {
                                        Text(recipe.title, color = Color.Black)
                                        Text("ID: ${recipe.id}", color = Color.Black)
                                        LoadImageFromUrl(recipe.imageUrl)
                                        Button(onClick = {
                                            lifecycleScope.launch {
                                                val nutritionAnalysis =
                                                    getNutritionAnalysis(recipe.id, this@Recepies)
                                                Log.d(
                                                    "Nutrition Analysis",
                                                    nutritionAnalysis.joinToString()
                                                )
                                                //Show each nutrient in a column who has a back button that destroy the column
                                                //and show the recipe again
                                                setContent {
                                                    if (usernamuss != null) {
                                                        ShowNutritionAnalysis(
                                                            nutritionAnalysis,
                                                            navController,
                                                            this@Recepies,
                                                            usernamus = usernamuss,
                                                            food = recipe.title,
                                                            img_url = recipe.imageUrl,
                                                            username = usernamuss
                                                        )
                                                    } else {
                                                        ShowNutritionAnalysis(
                                                            nutritionAnalysis,
                                                            navController,
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
                                        //Add another button on the right
                                        Button(
                                            onClick = {
                                                // getUserParameters(usernamuss!!,recipe.title,recipe.imageUrl)
                                                if (usernamuss != null) {
                                                    updateUserRecipes(
                                                        username_true,
                                                        recipe.title,
                                                        recipe.imageUrl
                                                    )
                                                }
                                            },
                                            modifier = Modifier.align(Alignment.End)
                                        ) {
                                            Text(text = "Add ")
                                        }
                                    }
                                    //Log image URL
                                    Log.d("Image URL", recipe.imageUrl)
                                }
                            }
                            Box(modifier = Modifier.fillMaxSize()) {

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
        .url("https://api.spoonacular.com/recipes/findByIngredients?ingredients=$ingredient&number=5&apiKey=5df674c4fc0242e38d2d0dd5cd94ffac")
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
        .url("https://api.spoonacular.com/recipes/$recipeId/nutritionWidget.json?apiKey=5df674c4fc0242e38d2d0dd5cd94ffac")
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
fun ShowNutritionAnalysis(nutritionAnalysis: List<Pair<String, String>>, navController: NavController,context: Context,usernamus:String,food:String,img_url: String,username:String?) {
    Column {
        for (nutrient in nutritionAnalysis) {
            Text("${nutrient.first}: ${nutrient.second}")
        }


        Button(onClick = {
            //Read from firebase Register/usernamer
          //  val onj = getUserParameters(usernamus,food,img_url)
            if (username_true != null) {
                updateUserRecipes(username_true, food, img_url)
            }
        }) {
            Text(text = "Add ${usernamus} to your list")
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