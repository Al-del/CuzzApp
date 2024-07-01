package com.example.cuzzapp

import Drawer_final
import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberImagePainter
import com.example.cuzzapp.ui.theme.CuzzAppTheme
import com.google.firebase.database.*
import kotlinx.coroutines.launch
import points
import url_photo
import username_true

class Shop : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val scaffoldState = rememberScaffoldState()
            Drawer_final(scaffoldState, { dini() })
        }
    }

    data class ShopItem(
        val name: String,
        val photo: String,
        val price: Int
    )

    fun fetchShopItems(callback: (List<ShopItem>) -> Unit) {
        val db = FirebaseDatabase.getInstance().getReference("Shop")
        val shopItems = mutableListOf<ShopItem>()

        db.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (snapshot in dataSnapshot.children) {
                    val name = snapshot.child("Name").getValue(String::class.java) ?: ""
                    val photo = snapshot.child("Photo").getValue(String::class.java) ?: ""
                    val price = snapshot.child("Price").getValue(Int::class.java) ?: 0
                    val item = ShopItem(name, photo, price)
                    shopItems.add(item)
                    println(item) // Print each item

                }
                callback(shopItems)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        })

    }

    @Composable
    fun ShopList(shopItems: List<ShopItem>) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(shopItems) { item ->
                ShopItemCard(item)
            }
        }
    }



    var loadImage by mutableStateOf(false)
@Composable
fun LoadImageFromUrl(url: String) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val painter = rememberImagePainter(
        data = url,
        builder = {
            listener(
                onStart = { scope.launch { Toast.makeText(context, "Started loading $url", Toast.LENGTH_SHORT).show() } },
                onSuccess = { _, _ -> scope.launch { Toast.makeText(context, "Successfully loaded $url", Toast.LENGTH_SHORT).show() } },
                onError = { _, throwable -> scope.launch { Toast.makeText(context, "Failed to load $url: ${throwable.drawable}", Toast.LENGTH_SHORT).show() } },
                onCancel = { scope.launch { Toast.makeText(context, "Cancelled loading $url", Toast.LENGTH_SHORT).show() } }
            )
        }
    )
    Image(painter = painter, contentDescription = null)
}
    // Reference to the Firebase database
    val db = FirebaseDatabase.getInstance().getReference("UserPoints")

    // Function to decrease points
    fun decreasePoints(puncte_ : Int) {
        val database = FirebaseDatabase.getInstance()

        // Get a reference to the "accounts" node
        val accountsRef = database.getReference("accounts")

        // Update the points to 100
        points-= puncte_
        accountsRef.child(username_true).child("points").setValue(points)
        loadImage = true

    }
@Composable
fun ShopItemCard(item: Shop.ShopItem) {
    val gradientBrush = Brush.verticalGradient(
        colors = listOf(Color(0xFFCF0A0A), Color(0xFF262323)),
        startY = 0f,
        endY = 500f
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .offset(x = 0.dp, y = 100.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize() // Make the column fill the card
                .background(brush = gradientBrush) // Apply the gradient
                .padding(16.dp)
        ) {
            val painter = rememberImagePainter(data = item.photo)
            Image(
                painter = painter,
                contentDescription = null,
                modifier = Modifier
                    .clickable {
                        decreasePoints(item.price)
                    }
                    .fillMaxWidth() // Make the image fill the maximum width
                    .height(100.dp) // Keep the height as 100.dp
            )

            Text(text = "Name: ${item.name}", color = Color.White)
            Text(text = "Price: ${item.price}", color = Color.White)
            Button(
                onClick = { decreasePoints(item.price) },
                modifier = Modifier.padding(top = 16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF964B00)) // Set the button background color to brown
            ) {
                Text("Get ${item.name}")
            }
        }
    }

}
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @Composable
    fun dini(){
        val navController = rememberNavController() // Create a NavController


            Scaffold(
                bottomBar = { AppNavigator(navController) }
            ) {
                var searchQuery by remember { mutableStateOf("calculus") }


                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFF262323)) // Set background color to 262323
                ) {
                    LoadImageFromUrl(url = url_photo, points = points)
                    Rectangle2(searchQuery, { newQuery -> searchQuery = newQuery })
                    if (loadImage) {
                        LoadImageFromUrl(url = url_photo, points = points)
                        loadImage = false
                    }
                    val shopItems = remember { mutableStateOf(listOf<ShopItem>()) }

                    // Fetch the shop items and update the state
                    fetchShopItems { items ->
                        shopItems.value = items
                    }

                    // Observe the state in your Composable UI
                    ShopList(shopItems.value)
                }
            }


    }

}

