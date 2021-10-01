package com.bignerdranch.android.project_1.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.bignerdranch.android.project_1.Game

@Database(entities = [ Game::class ], version=1)
@TypeConverters(GameTypeConverters::class)
abstract class GameDatabase : RoomDatabase() {
    abstract fun gameDao(): GameDao
}