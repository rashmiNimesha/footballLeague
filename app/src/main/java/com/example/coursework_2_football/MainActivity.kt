package com.example.coursework_2_football

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.room.Room
import com.example.coursework_2_football.ui.theme.Coursework_2_FootballTheme
import kotlinx.coroutines.launch

lateinit var database: AppDatabase
lateinit var footballLeaguedao: FootballLeagueDao

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        database = Room.databaseBuilder(
            this,
            AppDatabase::class.java, "mydatabase"
        ).build()
        footballLeaguedao = database.getDao()
        setContent {
            HomeScreen(::nav)
        }
    }

    fun nav(activityName: String) {   //Navigate using intents
        val intent = when (activityName) {
            "AddLeaguesToDB" -> Intent(this, AddLeaguesActivity::class.java)
            "SearchClubsByLeague" -> Intent(this, SearchClubsByLeague::class.java)
            "SearchForClubsActivity" -> Intent(this, SearchForClubsActivity::class.java)
            else -> {
                null
            }
        }
        startActivity(intent)

    }
}

@Composable
fun HomeScreen(onClicked: (String) -> Unit) {
    val scope = rememberCoroutineScope()
    var idLeague by rememberSaveable { mutableStateOf(0) }
    var strLeague by rememberSaveable { mutableStateOf("") }
    var strSport by rememberSaveable { mutableStateOf("") }
    var strLeagueAlternate by rememberSaveable { mutableStateOf("") }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),

        horizontalAlignment = Alignment.CenterHorizontally
    ) {


        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF75A488))
                    .padding(start = 60.dp),
                verticalAlignment = Alignment.CenterVertically,
                //  horizontalArrangement = Arrangement.Center
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
            Box(modifier = Modifier.fillMaxSize()) {
                Image(
                    painter = painterResource(id = R.drawable.imge),
                    contentDescription = "",
                    modifier = Modifier
                        .fillMaxSize()
                        .height(200.dp),
                    contentScale = ContentScale.FillBounds // This scales the image to fill the entire Box
                )
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Add your content here
                }
            }


        }

        item {
            Row {
                Button(onClick = {
                    try {
                        scope.launch {
                            footballLeaguedao.insertLeague(
                                FootBallLeague(
                                    idLeague = idLeague,
                                    strLeague = strLeague,
                                    strSport = strSport,
                                    strLeagueAlternate = strLeagueAlternate
                                )
                            )
                        }
                    } catch (e: NumberFormatException) {

                    }

                }) {
                    Text(text = "Add League")
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(50.dp))
            // when button clicks navigate to guess the hints page
            Row(modifier = Modifier.padding(vertical = 10.dp)) {
                Button(
                    onClick = {
                        onClicked("AddLeaguesToDB")
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF75A488)),
                    modifier = Modifier
                        .padding(4.dp)
                        .width(300.dp),
                ) {
                    Text(
                        "Add Leagues to DB",
                        color = Color.White,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        item {   //// when button clicks navigate to guess the hints page
            Row(modifier = Modifier.padding(vertical = 10.dp)) {
                Button(
                    onClick = {
                        onClicked("SearchClubsByLeague")
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF75A488)),
                    modifier = Modifier
                        .padding(4.dp)
                        .width(300.dp),
                ) {
                    Text(
                        "Search Clubs By League",
                        color = Color.White,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        item {   // when button clicks navigate to guess the hints page
            Row(modifier = Modifier.padding(vertical = 10.dp)) {
                Button(
                    onClick = {
                        onClicked("SearchForClubsActivity")
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF75A488)),
                    modifier = Modifier
                        .padding(4.dp)
                        .width(300.dp),
                ) {
                    Text(
                        "Search For Clubs",
                        color = Color.White,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }


    }


}


