package com.bignerdranch.android.project_1

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.*

private const val TAG = "GameListFragment"
private const val ARG_GAME_WIN = "Game_winner"

class GameListFragment: Fragment() {

    /**
     * Required interface for hosting activities
     */
    interface Callbacks {
        fun onGameSelected(gameId: UUID)
    }
    private var callbacks: Callbacks? = null

    private lateinit var gameRecyclerView: RecyclerView
    private var adapter: GameAdapter? = GameAdapter(emptyList())
    private lateinit var teamDisplay: String
    private lateinit var scoreDisplay: String

    private val gameListViewModel: GameListViewModel by lazy {
        ViewModelProviders.of(this).get(GameListViewModel::class.java)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks?
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_game_list, container, false)
        gameRecyclerView =
            view.findViewById(R.id.game_recycler_view) as RecyclerView
        gameRecyclerView.layoutManager = LinearLayoutManager(context)
        gameRecyclerView.adapter = adapter

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        gameListViewModel.gameListLiveData.observe(
            viewLifecycleOwner,
            Observer { games ->
                games?.let {
                    val winningTeam = arguments?.getSerializable(ARG_GAME_WIN)
                    val winnerList: MutableList<Game> = mutableListOf()
                    games.forEach {
                        if ((it.teamAScore > it.teamBScore) && (winningTeam == 'A')) {
                            winnerList.add(it)
                        }
                        else if ((it.teamAScore < it.teamBScore) && (winningTeam == 'B')) {
                            winnerList.add(it)
                        }
                        else if ((it.teamAScore == it.teamBScore) && (winningTeam == 'T')) {
                            winnerList.add(it)
                        }
                    }

                    Log.i(TAG, "Got games ${winnerList.size}")
                    updateUI(winnerList)
                }
            })
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }

    private fun updateUI(games: List<Game>) {
        adapter = GameAdapter(games)
        gameRecyclerView.adapter = adapter
    }

    private inner class GameHolder(view: View)
        : RecyclerView.ViewHolder(view), View.OnClickListener {

        private lateinit var game: Game

        private val dateTextView: TextView = itemView.findViewById(R.id.game_date)
        private val teamTextView: TextView = itemView.findViewById(R.id.team_display)
        private val logoImageView: ImageView = itemView.findViewById(R.id.winning_team_logo)
        private val scoreTextView: TextView = itemView.findViewById(R.id.game_score)

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(game: Game) {
            this.game = game
            dateTextView.text = game.date.toString()

            teamDisplay = "${game.teamAName}:${game.teamBName}"
            teamTextView.text = teamDisplay

            when {
                game.teamAScore > game.teamBScore -> {
                    logoImageView.setImageResource(R.drawable.team_a)
                }
                game.teamAScore < game.teamBScore -> {
                    logoImageView.setImageResource(R.drawable.team_b)
                }
                else -> {
                    logoImageView.setImageResource(0)
                }
            }

            scoreDisplay = "${game.teamAScore}:${game.teamBScore}"
            scoreTextView.text = scoreDisplay
        }

        override fun onClick(v: View) {
            callbacks?.onGameSelected(game.id)
        }
    }

    private inner class GameAdapter(var games: List<Game>)
        : RecyclerView.Adapter<GameHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
                : GameHolder {
            val view = layoutInflater.inflate(R.layout.list_item_game, parent, false)
            return GameHolder(view)
        }
        override fun getItemCount() = games.size
        override fun onBindViewHolder(holder: GameHolder, position: Int) {
            val game = games[position]
            holder.bind(game)
        }
    }

    companion object {
        fun newInstance(winner: Char): GameListFragment {
            val args = Bundle().apply {
                putSerializable(ARG_GAME_WIN, winner)
            }
            return GameListFragment().apply {
                arguments = args
            }
        }
    }
}