package com.example.coursework_2_football

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FootballLeagueDao {
    @Query("SELECT * FROM football_leagues")
    suspend fun getAllLeagues(): List<FootBallLeague>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLeague(league: FootBallLeague)


}