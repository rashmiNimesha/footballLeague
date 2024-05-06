package com.example.coursework_2_football

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Size
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.room.Room
import com.example.coursework_2_football.ui.theme.Coursework_2_FootballTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

var listClub_ = mutableListOf<ClubDetails>()
class SearchForClubsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database = Room.databaseBuilder(
            this,
            AppDatabase::class.java, "mydatabase"
        ).build()
        clubDetailsdao = database.getClubDao()
        setContent {
            SearchForClubsFromDB()
        }
    }
}


@Composable
fun SearchForClubsFromDB (){
    var keyword by rememberSaveable { mutableStateOf("") }
    var searchResults by rememberSaveable { mutableStateOf((listOf <ClubDetails>())) }
    val scope = rememberCoroutineScope()
    var searchNotFound by rememberSaveable { mutableStateOf(false) }

    LazyColumn (
        modifier = Modifier
            .fillMaxSize(),

        horizontalAlignment = Alignment.CenterHorizontally
    ){
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF75A488))
                    .padding(start = 60.dp),
                verticalAlignment = Alignment.CenterVertically,

                ) {
                Text(
                    modifier = Modifier.padding(10.dp),
                    text = "FOOTBALL LEAGUES",
                    fontFamily = FontFamily.SansSerif,
                    color = Color.Black,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                )
                Spacer(
                    modifier = Modifier.weight(1f)
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(50.dp))
            TextField(value = keyword, onValueChange = { keyword = it },
                label = { Text("Name of a football league/ club") },)
        }

        item {
            Spacer(modifier = Modifier.height(50.dp))

            Button(onClick = {
                scope.launch {
                    val result = clubDetailsdao.searchClubs(keyword)
                    if (result.isEmpty()) {
                        searchNotFound = true // Set flag if search results are empty
                    } else {
                        searchResults = result
                        searchNotFound = false // Reset flag if search results are not empty
                    }
                }
            },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF75A488)),
                modifier = Modifier
                    .padding(4.dp)
                    .width(160.dp),) {
                Text(
                    "Search",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )

            }

        }
        if (searchNotFound) { // Display message if search not found
            item {
                Text(
                    text = "Search not found",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.Red,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
        } else {
            items(searchResults) { club ->
                ClubListItem(club)
            }
        }

    }
}

@Composable
fun ClubListItem(club: ClubDetails) {

    var imgBitmap by rememberSaveable { mutableStateOf<ImageBitmap?>(null) }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp),
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
                LaunchedEffect(key1 = club.strTeamLogo) {
                    val bitmapImg= withContext(Dispatchers.IO){
                        fetchImageBitMap(club.strTeamLogo)
                    }
                    imgBitmap= bitmapImg
                }
            imgBitmap.let {
                    if (it != null) {
                        Image(
                            bitmap = it,
                            contentDescription = "Club Logo",
                            modifier = Modifier
                                .width(100.dp)
                                .height(100.dp)
                        )
                    }
                }
                Text(text = "IdTeam      : ${club.idTeam}", fontWeight = FontWeight.Normal, fontSize = 16.sp)
                Text(text = "Name        : ${club.strTeam}", fontWeight = FontWeight.Normal, fontSize = 16.sp)
                Text(text = "strTeamShort: ${club.strTeamShort}", fontWeight = FontWeight.Normal,fontSize = 16.sp)
                Text(text = "strAlternate: ${club.strAlternate}", fontWeight = FontWeight.Normal,fontSize = 16.sp)
                Text(text = "idLeague    : ${club.intFormedYear}", fontWeight = FontWeight.Normal,fontSize = 16.sp)
                Text(text = "strLeague   : ${club.strLeague}", fontWeight = FontWeight.Normal,fontSize = 16.sp)
                Text(text = "idLeague    : ${club.strStadium}", fontWeight = FontWeight.Normal,fontSize = 16.sp)
                Text(text = "League      : ${club.strStadiumLocation}", fontWeight = FontWeight.Normal,fontSize = 16.sp)
                Text(text = "idLeague    : ${club.intStadiumCapacity}", fontWeight = FontWeight.Normal,fontSize = 16.sp)
                Text(text = "idLeague    : ${club.strWebsite}", fontWeight = FontWeight.Normal,fontSize = 16.sp)
                Text(text = "idLeague    : ${club.strTeamJersey}", fontWeight = FontWeight.Normal,fontSize = 16.sp)
                Text(text = "idLeague    : ${club.strTeamLogo}", fontWeight = FontWeight.Normal,fontSize = 16.sp)

                Spacer(modifier = Modifier.height(8.dp))

        }
    }
}


fun fetchImageBitMap(url: String):ImageBitmap{
    val connection= URL(url).openConnection() as HttpURLConnection
    connection.doInput= true
    connection.connect()

    val inputStream= connection.inputStream
    val bitmap= BitmapFactory.decodeStream(inputStream)
    return bitmap.asImageBitmap()
}


