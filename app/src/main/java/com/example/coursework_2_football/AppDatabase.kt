package com.example.coursework_2_football

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [FootBallLeague::class, ClubDetails::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getDao(): FootballLeagueDao
    abstract fun getClubDao(): ClubDetailsDao
}