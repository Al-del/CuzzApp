package com.example.cuzzapp

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
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FabPosition
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberImagePainter
import com.example.cuzzapp.ui.theme.CuzzAppTheme
import com.google.firebase.database.*
import kotlinx.coroutines.launch
import points
import seach_querr
import url_photo
import username_true

class Shop : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val gradientBrush = Brush.linearGradient(
                colors = listOf(
                    Color(0xFF345E2A),
                    Color(0xFF403182)
                ),
                start = Offset(0f, 0f),
                end = Offset.Infinite
            )
            var searchQuery by remember { mutableStateOf(seach_querr) }
            val scaffoldState = rememberScaffoldState()

            Drawer(scaffoldState, searchQuery, backgroundColor =  SolidColor(Color(0xFF262323)),onSearchQueryChange = { searchQuery = it }) {
            dini(Modifier.background(brush = gradientBrush))
            }
  }
    }

    data class ShopItem(
        val name: String,
        val photo: String,
        val price: Int
    )

    fun fetchShopItems(callback: (List<Shop_item>) -> Unit) {
        val db = FirebaseDatabase.getInstance().getReference("Shop")
        val shopItems = mutableListOf<Shop_item>()

        db.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (snapshot in dataSnapshot.children) {
                    val name = snapshot.child("Name").getValue(String::class.java) ?: ""
                    val photo = snapshot.child("Photo").getValue(String::class.java) ?: ""
                    val price = snapshot.child("Price").getValue(Int::class.java) ?: 0
                    val description = snapshot.child("description").getValue(String::class.java) ?: ""
                    val item = Shop_item(name, photo, price, description)
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
    fun dini(modifier: Modifier){
        val navController = rememberNavController() // Create a NavController


            Scaffold(
                bottomBar = { AppNavigator(navController) }
            ) {


                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFF262323)) // Set background color to 262323
                ) {

                    val shopItems = remember { mutableStateOf(listOf<Shop_item>()) }
                  LaunchedEffect(key1 = true) { // key1 = true ensures this block runs once
    fetchShopItems { items ->
        shopItems.value = items
    }
}

// Now, pass shopItems.value to the shop Composable function
shop(modifier = modifier, shopitem = shopItems.value.toMutableList())
                }
            }


    }
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun shop(modifier: Modifier, shopitem: MutableList<Shop_item>){
    val context = LocalContext.current

    //Iterate through all od the shop items list and print each name
    Log.d("Shop","VIATA")
    for (item in shopitem) {

        Log.d("Shop","VIATA ${item.name}")
    }
    Scaffold(

        modifier = modifier.fillMaxSize()
        ,
        )

    {
        LazyColumn(modifier = modifier.fillMaxSize()) {
            itemsIndexed(shopitem) { index, achievement ->
                // Now you have access to both the index and the achievement
                ShopItem(achievement, index)
                Spacer(modifier = modifier.padding(8.dp))
            }
        }
    }
}
    @Composable
    fun ShopItem(item: Shop_item?, indx: Int) {
        val painter = rememberImagePainter(data = item?.image_link)

        // Display your achievement here
        if (item != null) {
            Box(
                modifier = Modifier
                    .requiredWidth(width = getScreenWidthDp().dp)
                    .requiredHeight(height = 184.dp)
                    .clip(shape = RoundedCornerShape(20.dp))
                    .background(color = Color.White)
                   ,
            ) {
                androidx.compose.material3.Text(
                    textAlign = TextAlign.End,
                    lineHeight = 1.sp,
                    text = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                color = Color.Black,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium
                            )
                        ) {}
                        withStyle(
                            style = SpanStyle(
                                color = Color.Black,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium
                            )
                        ) { append(item.price.toString()) }
                    },
                    modifier = Modifier
                        .align(alignment = Alignment.TopStart)
                        .offset(
                            x = 16.dp,
                            y = 94.dp
                        )
                )
                androidx.compose.material3.Text(
                    text = item.name,
                    color = Color.Black,
                    textAlign = TextAlign.End,
                    lineHeight = 0.8.em,
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier
                        .align(alignment = Alignment.TopStart)
                        .offset(
                            x = 16.dp,
                            y = 68.dp
                        )
                )
                Image(
                    painter = painter,
                    contentDescription = "image 219",
                    modifier = Modifier
                        .align(alignment = Alignment.TopStart)
                        .offset(
                            x = 185.dp,
                            y = 20.dp
                        )
                        .requiredWidth(width = 167.dp)
                        .requiredHeight(height = 140.dp)
                )
                Box(
                    modifier = Modifier
                        .align(alignment = Alignment.TopStart)
                        .offset(
                            x = 16.dp,
                            y = 16.dp
                        )
                        .requiredSize(size = 40.dp)
                        .clip(shape = RoundedCornerShape(50.dp))
                        .background(color = Color(0xffb0f41f))
                ) {
                    androidx.compose.material3.Text(
                        text = indx.toString(),
                        color = Color.Black,
                        textAlign = TextAlign.End,
                        lineHeight = 1.em,
                        style = TextStyle(
                            fontSize = 16.sp
                        ),
                        modifier = Modifier
                            .align(alignment = Alignment.TopStart)
                            .offset(
                                x = 17.dp,
                                y = 12.dp
                            )
                            .requiredWidth(width = 6.dp)
                    )
                }
                androidx.compose.material3.Text(
                    lineHeight = 1.sp,
                    text = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                color = Color.Black,
                                fontSize = 12.sp
                            )
                        ) {}
                        withStyle(
                            style = SpanStyle(
                                color = Color.Black,
                                fontSize = 12.sp
                            )
                        ) { append(item.description) }
                    },
                    modifier = Modifier
                        .align(alignment = Alignment.TopStart)
                        .offset(
                            x = 16.dp,
                            y = 120.dp
                        )
                        .requiredWidth(width = 174.dp)
                )
            }
        }
    }
}
class Shop_item(
    var name: String = "",
    var image_link: String = "",
    var price: Int = 0,
    var description: String = ""
)
