package com.example.coursework_2_football

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface clubDao{
    @Query("SELECT * FROM club_details")
    suspend fun getAllLeagues(): List<ClubDetails>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLeague(league: ClubDetails)
}