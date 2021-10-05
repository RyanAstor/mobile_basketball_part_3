package com.bignerdranch.android.project_1

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProviders
import java.util.*
import androidx.lifecycle.Observer
import com.bignerdranch.android.project_1.api.OpenWeatherApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.io.File

private const val TAG = "GameFragment"
private const val REQUEST_CODE_SAVE = 0
private const val REQUEST_PHOTO = 1
private const val ARG_GAME_ID = "game_id"

class GameFragment: Fragment() {

    /**
     * Required interface for hosting activities
     */
    interface Callbacks {
        fun displayList(winner: Char)
    }
    private var callbacks: Callbacks? = null

    private lateinit var game: Game
    private lateinit var photoFileA: File
    private lateinit var photoFileB: File
    private lateinit var photoUriA: Uri
    private lateinit var photoUriB: Uri
    private lateinit var threePointA: Button
    private lateinit var twoPointA: Button
    private lateinit var onePointA: Button
    private lateinit var threePointB: Button
    private lateinit var twoPointB: Button
    private lateinit var onePointB: Button
    private lateinit var resetButton: Button
    private lateinit var scoreA: TextView
    private lateinit var scoreB: TextView
    private lateinit var teamA: EditText
    private lateinit var teamB: EditText
    private lateinit var saveButton: Button
    private lateinit var nameA: String
    private lateinit var nameB: String
    private lateinit var displayButton: Button
    private lateinit var photoButtonA: ImageButton
    private lateinit var photoViewA: ImageView
    private lateinit var photoButtonB: ImageButton
    private lateinit var photoViewB: ImageView
    private lateinit var weatherText: TextView


    private val scoreViewModel: ScoreViewModel by lazy {
        ViewModelProviders.of(this).get(ScoreViewModel::class.java)
    }

    private val gameDetailViewModel: GameDetailViewModel by lazy {
        ViewModelProviders.of(this).get(GameDetailViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate(Bundle?) called")
        val id = arguments?.getSerializable(ARG_GAME_ID)
        if (id == null) {
            game = Game()
            gameDetailViewModel.addGame(game)
        }
        else {
            val gameId: UUID = id as UUID
            gameDetailViewModel.loadGame(gameId)
        }

        val flickrLiveData: LiveData<WeatherItem> = OpenWeatherFetcher().fetchWeather()
        flickrLiveData.observe(
            this,
            Observer { weatherItem ->
                Log.d(TAG, "Response received: $weatherItem")
                weatherText.text = weatherItem.toString()
            })
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
        val view = inflater.inflate(R.layout.fragment_game, container, false)

        val currentScoreA = savedInstanceState?.getInt(scoreViewModel.teamAObject.team, 0) ?: 0
        scoreViewModel.teamAObject.score = currentScoreA
        val currentScoreB = savedInstanceState?.getInt(scoreViewModel.teamBObject.team, 0) ?: 0
        scoreViewModel.teamBObject.score = currentScoreB

        threePointA = view.findViewById(R.id.three_point_A)
        twoPointA = view.findViewById(R.id.two_point_A)
        onePointA = view.findViewById(R.id.one_point_A)
        threePointB = view.findViewById(R.id.three_point_B)
        twoPointB = view.findViewById(R.id.two_point_B)
        onePointB = view.findViewById(R.id.one_point_B)
        resetButton = view.findViewById(R.id.reset_button)
        scoreA = view.findViewById(R.id.score_A)
        scoreB = view.findViewById(R.id.score_B)
        teamA = view.findViewById(R.id.team_A)
        teamB = view.findViewById(R.id.team_B)
        saveButton = view.findViewById(R.id.save_button)
        teamA.addTextChangedListener(textWatcherA)
        teamB.addTextChangedListener(textWatcherB)
        nameA = "Team A"
        nameB = "Team B"
        displayButton = view.findViewById(R.id.display_button)
        photoButtonA = view.findViewById(R.id.camera_A) as ImageButton
        photoViewA = view.findViewById(R.id.photo_A) as ImageView
        photoButtonB = view.findViewById(R.id.camera_B) as ImageButton
        photoViewB = view.findViewById(R.id.photo_B) as ImageView
        photoFileA = gameDetailViewModel.getPhotoFileA(game)
        photoUriA = FileProvider.getUriForFile(requireActivity(),
            "com.bignerdranch.android.project_1.fileprovider",
            photoFileA)
        photoFileB = gameDetailViewModel.getPhotoFileB(game)
        photoUriB = FileProvider.getUriForFile(requireActivity(),
            "com.bignerdranch.android.project_1.fileprovider",
            photoFileB)
        weatherText = view.findViewById(R.id.weather)

        threePointA.setOnClickListener { view: View ->
            scoreViewModel.updateScore('A', 3, nameA)
            scoreA.text = scoreViewModel.teamAObject.score.toString()
        }

        twoPointA.setOnClickListener { view: View ->
            scoreViewModel.updateScore('A', 2, nameA)
            scoreA.text = scoreViewModel.teamAObject.score.toString()
        }

        onePointA.setOnClickListener { view: View ->
            scoreViewModel.updateScore('A', 1, nameA)
            scoreA.text = scoreViewModel.teamAObject.score.toString()
        }

        threePointB.setOnClickListener { view: View ->
            scoreViewModel.updateScore('B', 3, nameB)
            scoreB.text = scoreViewModel.teamBObject.score.toString()
        }

        twoPointB.setOnClickListener { view: View ->
            scoreViewModel.updateScore('B', 2, nameB)
            scoreB.text = scoreViewModel.teamBObject.score.toString()
        }

        onePointB.setOnClickListener { view: View ->
            scoreViewModel.updateScore('B', 1, nameB)
            scoreB.text = scoreViewModel.teamBObject.score.toString()
        }

        resetButton.setOnClickListener { view: View ->
            scoreViewModel.resetScore()
            scoreA.text = "0"
            scoreB.text = "0"

            teamA.setText(R.string.title_A)
            teamB.setText(R.string.title_B)
        }

        saveButton.setOnClickListener {
            // Start SaveActivity
            updateGame()
            gameDetailViewModel.saveGame(game)
            val intent =
                context?.let { it1 -> SaveActivity.newIntent(it1,
                    game.teamAScore, game.teamBScore, game.teamAName, game.teamBName) }
            startActivityForResult(intent, REQUEST_CODE_SAVE)
        }

        displayButton.setOnClickListener {
            Log.d(TAG, "Display Button Clicked")
            updateGame()
            gameDetailViewModel.saveGame(game)

            val winner = when {
                scoreViewModel.teamAObject.score > scoreViewModel.teamBObject.score -> { 'A' }
                scoreViewModel.teamAObject.score < scoreViewModel.teamBObject.score -> { 'B' }
                else -> { 'T' }
            }
            callbacks?.displayList(winner)

        }

        updateScores()

        return view
    }

    private fun updateGame() {
        game.teamAScore = scoreViewModel.teamAObject.score
        game.teamBScore = scoreViewModel.teamBObject.score
        game.teamAName = scoreViewModel.teamAObject.name
        game.teamBName  = scoreViewModel.teamBObject.name
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        gameDetailViewModel.gameLiveData.observe(
            viewLifecycleOwner,
            Observer { game ->
                game?.let {
                    this.game = game
                    scoreViewModel.teamAObject.name = game.teamAName
                    scoreViewModel.teamAObject.score = game.teamAScore
                    scoreViewModel.teamBObject.name = game.teamBName
                    scoreViewModel.teamBObject.score = game.teamBScore
                    updateUI()
                }
            })
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
        requireActivity().revokeUriPermission(photoUriA,
            Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
        requireActivity().revokeUriPermission(photoUriB,
            Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
    }

    override fun onActivityResult(requestCode: Int,
                                  resultCode: Int,
                                  data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK) {
            return
        }
        if (requestCode == REQUEST_CODE_SAVE) {
            scoreViewModel.winner =
                data?.getStringExtra(EXTRA_WINNING_TEAM).toString()
            scoreViewModel.foundWinner =
                data?.getBooleanExtra(EXTRA_WINNER_FOUND, false) ?: false
        }
        if (requestCode == REQUEST_PHOTO) {
            requireActivity().revokeUriPermission(photoUriA,
                Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            updatePhotoViewA()
            requireActivity().revokeUriPermission(photoUriB,
                Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            updatePhotoViewB()
        }
    }

    private val textWatcherA = object : TextWatcher {
        override fun afterTextChanged(p0: Editable?) {
        }

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            nameA = p0.toString()
            scoreViewModel.updateScore('A', 0, nameA)
        }
    }

    private val textWatcherB = object : TextWatcher {
        override fun afterTextChanged(p0: Editable?) {
        }

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            nameB = p0.toString()
            scoreViewModel.updateScore('B', 0, nameB)
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart() called")

        photoButtonA.apply {
            val packageManager: PackageManager = requireActivity().packageManager
            val captureImage = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            val resolvedActivity: ResolveInfo? =
                packageManager.resolveActivity(captureImage,
                    PackageManager.MATCH_DEFAULT_ONLY)

            if (resolvedActivity == null) {
                isEnabled = false
            }
            setOnClickListener {
                captureImage.putExtra(MediaStore.EXTRA_OUTPUT, photoUriA)
                val cameraActivities: List<ResolveInfo> =
                    packageManager.queryIntentActivities(captureImage,
                        PackageManager.MATCH_DEFAULT_ONLY)
                for (cameraActivity in cameraActivities) {
                    requireActivity().grantUriPermission(
                        cameraActivity.activityInfo.packageName,
                        photoUriA,
                        Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                }
                startActivityForResult(captureImage, REQUEST_PHOTO)
            }
        }

        photoButtonB.apply {
            val packageManager: PackageManager = requireActivity().packageManager
            val captureImage = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            val resolvedActivity: ResolveInfo? =
                packageManager.resolveActivity(captureImage,
                    PackageManager.MATCH_DEFAULT_ONLY)

            if (resolvedActivity == null) {
                isEnabled = false
            }
            setOnClickListener {
                captureImage.putExtra(MediaStore.EXTRA_OUTPUT, photoUriB)
                val cameraActivities: List<ResolveInfo> =
                    packageManager.queryIntentActivities(captureImage,
                        PackageManager.MATCH_DEFAULT_ONLY)
                for (cameraActivity in cameraActivities) {
                    requireActivity().grantUriPermission(
                        cameraActivity.activityInfo.packageName,
                        photoUriB,
                        Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                }
                startActivityForResult(captureImage, REQUEST_PHOTO)
            }
        }
    }

    private fun updateUI() {
        scoreA.text = game.teamAScore.toString()
        scoreB.text = game.teamBScore.toString()
        teamA.setText(game.teamAName)
        teamB.setText(game.teamBName)
        updatePhotoViewA()
        updatePhotoViewB()
    }

    private fun updatePhotoViewA() {
        if (photoFileA.exists()) {
            val bitmap = getScaledBitmap(photoFileA.path, requireActivity())
            photoViewA.setImageBitmap(bitmap)
        } else {
            photoViewA.setImageDrawable(null)
        }
    }
    private fun updatePhotoViewB() {
        if (photoFileB.exists()) {
            val bitmap = getScaledBitmap(photoFileB.path, requireActivity())
            photoViewB.setImageBitmap(bitmap)
        } else {
            photoViewB.setImageDrawable(null)
        }
    }

    companion object {
        fun newInstance(gameId: UUID): GameFragment {
            val args = Bundle().apply {
                putSerializable(ARG_GAME_ID, gameId)
            }
            return GameFragment().apply {
                arguments = args
            }
        }
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume() called")
        val messageResId = when {
            scoreViewModel.foundWinner ->
                if (scoreViewModel.teamAObject.score == scoreViewModel.teamBObject.score) {
                    getString(R.string.tie)
                }
                else {
                    scoreViewModel.winner + " " + getString(R.string.winning_toast)
                }
            else -> return
        }
        Toast.makeText(activity, messageResId, Toast.LENGTH_SHORT).show()
        scoreViewModel.foundWinner = false
    }
    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause() called")
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        Log.i(TAG, "onSaveInstanceState")
        savedInstanceState.putInt(scoreViewModel.teamAObject.team, scoreViewModel.teamAObject.score)
        savedInstanceState.putInt(scoreViewModel.teamBObject.team, scoreViewModel.teamBObject.score)
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop() called")
        updateGame()
        gameDetailViewModel.saveGame(game)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy() called")
    }

    private fun updateScores() {
        scoreA.text = scoreViewModel.teamAObject.score.toString()
        scoreB.text = scoreViewModel.teamBObject.score.toString()
    }
 }