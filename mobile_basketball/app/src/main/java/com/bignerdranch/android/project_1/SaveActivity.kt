package com.bignerdranch.android.project_1

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView

private const val TAG ="SaveActivity"

const val EXTRA_WINNING_TEAM = "com.bignerdranch.android.project_1.winning_team"
const val EXTRA_WINNER_FOUND = "com.bignerdranch.android.project_1.winner_found"
private const val EXTRA_SCORE_A =
    "com.bignerdranch.android.project_1.score_A"
private const val EXTRA_SCORE_B =
    "com.bignerdranch.android.project_1.score_B"
private const val EXTRA_TEAM_A =
    "com.bignerdranch.android.project_1.team_A"
private const val EXTRA_TEAM_B =
    "com.bignerdranch.android.project_1.team_B"

class SaveActivity : AppCompatActivity() {

    private lateinit var winningTeam: TextView
    private lateinit var whoWonButton: Button

    private var scoreA = 0
    private var scoreB = 0
    private var teamA = ""
    private var teamB = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate() called")
        setContentView(R.layout.activity_save)

        scoreA = intent.getIntExtra(EXTRA_SCORE_A, 0)
        scoreB = intent.getIntExtra(EXTRA_SCORE_B, 0)
        teamA = intent.getStringExtra(EXTRA_TEAM_A).toString()
        teamB = intent.getStringExtra(EXTRA_TEAM_B).toString()

        winningTeam = findViewById(R.id.winning_team)
        whoWonButton = findViewById(R.id.who_won)
        whoWonButton.setOnClickListener {
            val answerText = when {
                scoreA > scoreB -> teamA
                scoreA < scoreB -> teamB
                else -> getString(R.string.tie)
            }
            winningTeam.text = answerText
            setWinningTeamResult(answerText)
        }
    }

    private fun setWinningTeamResult(winningTeam: String) {
        val data = Intent().apply {
            putExtra(EXTRA_WINNING_TEAM, winningTeam)
            putExtra(EXTRA_WINNER_FOUND, true)
        }
        setResult(Activity.RESULT_OK, data)
        if(scoreA != scoreB) {
            Log.d(TAG, "$winningTeam won")
        }
        else
            Log.d(TAG, winningTeam)
    }

    companion object {
        fun newIntent(packageContext: Context, ScoreA: Int, ScoreB: Int, TeamA: String, TeamB: String): Intent {
            return Intent(packageContext, SaveActivity::class.java).apply {
                putExtra(EXTRA_SCORE_A, ScoreA)
                putExtra(EXTRA_SCORE_B, ScoreB)
                putExtra(EXTRA_TEAM_A, TeamA)
                putExtra(EXTRA_TEAM_B, TeamB)
            }
        }
    }
}