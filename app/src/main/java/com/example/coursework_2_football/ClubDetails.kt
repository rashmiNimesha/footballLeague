package com.example.coursework_2_football

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "club_details")
data class ClubDetails(
    @PrimaryKey()
    var idTeam: String,
    var strTeam: String?,
    var strTeamShort: String,
    var strAlternate: String,
    var intFormedYear: String,
    var strLeague: String,
    var strStadium:String,
    var strStadiumLocation:String,
    var intStadiumCapacity:String,
    var strWebsite:String,
    var strTeamJersey:String,
    var strTeamLogo:String


)