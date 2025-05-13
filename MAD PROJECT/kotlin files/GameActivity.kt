package com.example.mindgrid

import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.widget.Button
import android.widget.GridLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random

class GameActivity : AppCompatActivity() {

    private lateinit var gridLayout: GridLayout
    private lateinit var tvMoves: TextView
    private lateinit var tvTimer: TextView
    private lateinit var btnShuffle: Button

    private var buttons = ArrayList<Button>()
    private var emptyIndex = 8
    private var moveCount = 0

    private var startTime = 0L
    private val handler = Handler()
    private val timerRunnable = object : Runnable {
        override fun run() {
            val elapsedTime = (SystemClock.elapsedRealtime() - startTime) / 1000
            tvTimer.text = "Time: ${elapsedTime}s"
            handler.postDelayed(this, 1000)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        gridLayout = findViewById(R.id.gridLayout)
        tvMoves = findViewById(R.id.tvMoves)
        tvTimer = findViewById(R.id.tvTimer)
        btnShuffle = findViewById(R.id.btnShuffle)

        setupBoard()
        btnShuffle.setOnClickListener {
            shuffleBoard()
            startTimer()
        }
    }

    private fun setupBoard() {
        for (i in 0..8) {
            val btn = Button(this)
            btn.text = if (i != 8) (i + 1).toString() else ""
            btn.textSize = 24f
            btn.setOnClickListener { moveTile(i) }

            buttons.add(btn)
            gridLayout.addView(btn, 250, 250)
        }
        emptyIndex = 8
    }

    private fun shuffleBoard() {
        val numbers = (1..8).toMutableList()
        numbers.shuffle(Random(System.currentTimeMillis()))
        numbers.add(0) // Representing the empty tile

        for (i in 0..8) {
            val text = if (numbers[i] == 0) "" else numbers[i].toString()
            buttons[i].text = text
        }

        emptyIndex = numbers.indexOf(0)
        moveCount = 0
        tvMoves.text = "Moves: $moveCount"
    }

    private fun moveTile(index: Int) {
        val neighbors = listOf(
            index - 1, index + 1,
            index - 3, index + 3
        ).filter { it in 0..8 && isValidMove(index, it) }

        if (emptyIndex in neighbors) {
            buttons[emptyIndex].text = buttons[index].text
            buttons[index].text = ""
            emptyIndex = index
            moveCount++
            tvMoves.text = "Moves: $moveCount"

            if (isSolved()) {
                showCompletionDialog()
            }
        }
    }


    private fun isValidMove(from: Int, to: Int): Boolean {
        val fromRow = from / 3
        val fromCol = from % 3
        val toRow = to / 3
        val toCol = to % 3
        return (fromRow == toRow && kotlin.math.abs(fromCol - toCol) == 1) ||
                (fromCol == toCol && kotlin.math.abs(fromRow - toRow) == 1)
    }

    private fun startTimer() {
        startTime = SystemClock.elapsedRealtime()
        handler.removeCallbacks(timerRunnable)
        handler.post(timerRunnable)
    }

    override fun onDestroy() {
        handler.removeCallbacks(timerRunnable)
        super.onDestroy()
    }

    private fun isSolved(): Boolean {
        for (i in 0..7) {
            if (buttons[i].text != (i + 1).toString()) return false
        }
        return buttons[8].text == ""
    }
    private fun showCompletionDialog() {
        handler.removeCallbacks(timerRunnable)
        val elapsedTime = (SystemClock.elapsedRealtime() - startTime) / 1000

        val message = "Congratulations! You solved the puzzle in $moveCount moves and $elapsedTime seconds."

        val dialog = androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("Puzzle Solved!")
            .setMessage(message)
            .setCancelable(false)
            .setPositiveButton("Play Again") { _, _ ->
                shuffleBoard()
                startTimer()
            }
            .setNegativeButton("Exit") { _, _ ->
                finish()
            }
            .create()

        dialog.show()
    }

}
