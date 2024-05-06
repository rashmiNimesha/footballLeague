package com.example.coursework_2_football

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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


lateinit var clubDetailsdao: ClubDetailsDao
var listClub = mutableListOf<ClubDetails>()

class SearchClubsByLeague : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database = Room.databaseBuilder(
            this,
            AppDatabase::class.java, "mydatabase"
        ).build()
        clubDetailsdao = database.getClubDao()

        setContent {
            SearchClubsByLeagueFun()
        }
    }
}

@Composable
fun SearchClubsByLeagueFun() {
    var clubDetails by rememberSaveable { mutableStateOf(" ") }
    var searchKeyword by rememberSaveable { mutableStateOf("") }
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
        // text filed user can enter search keyword
        item {
            Spacer(modifier = Modifier.height(50.dp))
            TextField(
                value = searchKeyword, onValueChange = { searchKeyword = it },
                label = { Text("Name of a football league") },
            )
        }
        item {
            Spacer(modifier = Modifier.height(50.dp))
            Row {
                Button(
                    onClick = {
                        scope.launch {
                            clubDetails = retrieveClubs(searchKeyword)
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
                Button(
                    onClick = {
                        scope.launch {
                            clubDetails = retrieveClubs(searchKeyword)
                            listClub.forEach { clu ->
                                clubDetailsdao.insertTeam(clu)
                            }

                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF75A488)),
                    modifier = Modifier
                        .padding(4.dp)
                        .width(160.dp),
                ) {
                    Text(
                        text = "Save clubs to DB",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
            Spacer(modifier = Modifier.width(20.dp))
            Text(
                text = clubDetails
            )
        }
    }
}

suspend fun retrieveClubs(keyword: String): String {
    val url_string = "https://www.thesportsdb.com/api/v1/json/3/search_all_teams.php?l=${keyword}"
    val url = URL(url_string)
    val con: HttpURLConnection = url.openConnection() as HttpURLConnection
    var stb = StringBuilder()    // collect all JSON str

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
    return parseJSON(stb.toString())
}

fun parseJSON(stb: String): String {
    val jsonObj = JSONObject(stb)    // returned JSON from web API
    var allTeams = StringBuilder()
    var jsonArray: JSONArray = jsonObj.optJSONArray("teams") ?: JSONArray()

    if (jsonArray.length() == 0) {  // check whether keyword has a reuslt or not
        return "No clubs found!!"
    }
// extract all the books from the JSON array
    for (i in 0 until jsonArray.length()) {
        val team = jsonArray.getJSONObject(i)
        val idTeam = team.getString("idTeam")
        val name = team.getString("strTeam")
        val shortName = team.getString("strTeamShort")
        val alternateName = team.getString("strAlternate")
        val formedYear = team.getString("intFormedYear")
        val league = team.getString("strLeague")
        val stadium = team.getString("strStadium")
        val stadiumLocation = team.getString("strStadiumLocation")
        val stadiumCapacity = team.getString("intStadiumCapacity")
        val website = team.getString("strWebsite")
        val jerseyUrl = team.getString("strTeamJersey")
        val logoUrl = team.getString("strTeamLogo")

        allTeams.append(
            "Team ID: $idTeam\n" +
                    "Name            : $name\n" +
                    "Short Name      : $shortName\n" +
                    "Alternate Name  : $alternateName\n" +
                    "Formed Year     : $formedYear\n" +
                    "League          : $league\n" +
                    "Stadium         : $stadium\n" +
                    "Stadium Location: $stadiumLocation\n" +
                    "Stadium Capacity: $stadiumCapacity\n" +
                    "Website         : $website\n" +
                    "Jersey URL      : $jerseyUrl\n" +
                    "Logo URL        : $logoUrl\n\n"
        )
        val clubDetails = ClubDetails(
            idTeam,
            name,
            shortName,
            alternateName,
            formedYear,
            league,
            stadium,
            stadiumLocation,
            stadiumCapacity,
            website,
            jerseyUrl,
            logoUrl
        )

        listClub.add(clubDetails)


    }

    return allTeams.toString()
}
