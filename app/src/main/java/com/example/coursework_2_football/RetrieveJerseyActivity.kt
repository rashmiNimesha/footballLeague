package com.example.coursework_2_football

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.room.Room
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL


class RetrieveJersey : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database = Room.databaseBuilder(
            this,
            AppDatabase::class.java, "mydatabase"
        ).build()
        clubDetailsdao = database.getClubDao()
        setContent {
            RetrieveTShirts()

        }
    }
}

@Composable
fun RetrieveTShirts() {
    var keyword by rememberSaveable { mutableStateOf("") }
    var clubDetails by rememberSaveable { mutableStateOf(" ") }
    val scope = rememberCoroutineScope()


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
            TextField(
                value = keyword, onValueChange = { keyword = it },
                label = { Text("Name of a club") },
            )
        }

        item {
            Spacer(modifier = Modifier.height(50.dp))
            Row {
                Button(
                    onClick = {
                        scope.launch {
                            clubDetails = retrieveClubss(keyword)
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF75A488)),
                    modifier = Modifier
                        .padding(4.dp)
                        .width(160.dp),
                ) {
                    Text(
                        " Retrieve Clubs",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
                Spacer(modifier = Modifier.width(15.dp))

            }
        }
    }
}
suspend fun retrieveClubss(keyword: String): String {
    val url_string = "https://www.thesportsdb.com/api/v1/json/3/search_all_teams.php?l=${keyword}"
    val url = URL(url_string)
    val con: HttpURLConnection = url.openConnection() as HttpURLConnection

    var stb = StringBuilder()

    withContext(Dispatchers.IO) {
        try {
            var bf = BufferedReader(InputStreamReader(con.inputStream))
            var line: String? = bf.readLine()
            while (line != null) {
                stb.append(line + "\n")
                line = bf.readLine()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            con.disconnect()
        }
    }
    return parseJSONN(stb.toString())
}

 fun parseJSONN(stb: String): String {

    val json = JSONObject(stb)

    var allTeams = StringBuilder()

    var jsonArray: JSONArray = json.optJSONArray("teams") ?: JSONArray()

    if (jsonArray.length() == 0) {

        return "No clubs found!!"
    }

    for (i in 0 until jsonArray.length()) {
        val team = jsonArray.getJSONObject(i)
        val idTeam = team.getString("idTeam")
        val name = team.getString("strTeam")
        val shortName = team.getString("strTeamShort")


        allTeams.append(
                    "Team ID: $idTeam\n" +
                    "Name            : $name\n" +
                    "Short Name      : $shortName\n"

        )





    }

    return allTeams.toString()
}
