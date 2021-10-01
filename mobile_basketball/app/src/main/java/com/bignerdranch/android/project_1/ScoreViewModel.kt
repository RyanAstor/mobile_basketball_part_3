package com.bignerdranch.android.project_1

import android.util.Log
import androidx.lifecycle.ViewModel

private const val TAG = "ScoreViewModel"

class ScoreViewModel : ViewModel() {

    var teamAObject = Score("A", 0, "Team A")
    var teamBObject = Score("B", 0, "Team B")
    var winner = ""
    var foundWinner = false

    fun updateScore(team: Char, points: Int, name: String) {
        if(team == 'A') {
            teamAObject.score += points
            teamAObject.name = name
        } else if(team == 'B')  {
            teamBObject.score += points
            teamBObject.name = name
        }
        if(points == 0){
            Log.d(TAG, "Team name changed to $name")
        } else {
            Log.d(TAG, "$name scores $points point(s)")
        }
    }

    fun resetScore() {
        teamAObject.score = 0
        teamBObject.score = 0
        Log.d(TAG, "Score reset")
    }
}
