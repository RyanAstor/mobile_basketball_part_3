package com.bignerdranch.android.project_1

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import java.util.*

class GameDetailViewModel: ViewModel() {
    private val gameRepository = GameRepository.get()
    private val gameIdLiveData = MutableLiveData<UUID>()

    var gameLiveData: LiveData<Game?> =
        Transformations.switchMap(gameIdLiveData) { gameId ->
            gameRepository.getGame(gameId)
        }

    fun loadGame(gameId: UUID) {
        gameIdLiveData.value = gameId
    }

    fun saveGame(game: Game) {
        gameRepository.updateGame(game)
    }

    fun addGame(game: Game) {
        gameRepository.addGame(game)
    }
}