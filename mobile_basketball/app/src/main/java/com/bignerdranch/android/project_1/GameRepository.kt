package com.bignerdranch.android.project_1

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.bignerdranch.android.project_1.database.GameDatabase
import java.io.File
import java.util.*
import java.util.concurrent.Executors

private const val DATABASE_NAME = "game-database"

class GameRepository private constructor(context: Context) {

    private val database : GameDatabase = Room.databaseBuilder(
        context.applicationContext,
        GameDatabase::class.java,
        DATABASE_NAME
    ).build()

    private val gameDao = database.gameDao()
    private val executor = Executors.newSingleThreadExecutor()
    private val filesDir = context.applicationContext.filesDir

    fun getGames(): LiveData<List<Game>> = gameDao.getGames()

    fun getGame(id: UUID): LiveData<Game?> = gameDao.getGame(id)

    fun updateGame(game: Game) {
        executor.execute {
            gameDao.updateGame(game)
        }
    }

    fun addGame(game: Game) {
        executor.execute {
            gameDao.addGame(game)
        }
    }

    fun getPhotoFileA(game: Game): File = File(filesDir, game.photoFileNameA)
    fun getPhotoFileB(game: Game): File = File(filesDir, game.photoFileNameB)


    companion object {
        private var INSTANCE: GameRepository? = null
        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = GameRepository(context)
            }
        }
        fun get(): GameRepository {
            return INSTANCE ?:
            throw IllegalStateException("GameRepository must be initialized")
        }
    }
}