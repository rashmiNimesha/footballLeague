package com.example.coursework_2_football

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "football_leagues")
data class FootBallLeague(
    @PrimaryKey()
    var idLeague: Int,
    var strLeague: String?,
    var strSport: String,
    var strLeagueAlternate: String
)


