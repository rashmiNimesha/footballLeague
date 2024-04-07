package com.example.coursework_2_football

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "football_leagues")
data class FootBallLeague(
    @PrimaryKey(autoGenerate = true)
    var idLeague: Int= 0,
    var strLeague: String?,
    var strSport: String,
    var strLeagueAlternate: String
)


