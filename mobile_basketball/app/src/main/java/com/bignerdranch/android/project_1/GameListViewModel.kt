package com.bignerdranch.android.project_1

import androidx.lifecycle.ViewModel
import kotlin.random.Random

//private val charPool : List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')

class GameListViewModel: ViewModel() {
    private val gameRepository = GameRepository.get()

    //For inserting in Dummy Data
//    init {
//        for (i in 0 until 150) {
//            val game = Game()
//            game.teamAName = randomName()
//            game.teamBName = randomName()
//            game.teamAScore = Random.nextInt(0, 100)
//            game.teamBScore = Random.nextInt(0, 100)
//            gameRepository.addGame(game)
//        }
//    }

    val gameListLiveData = gameRepository.getGames()
}

//For inserting in Dummy Data
//private fun randomName(): String {
//    return "Team " + (1..8)
//        .map { Random.nextInt(0, charPool.size) }
//        .map(charPool::get)
//        .joinToString("")
//}