package com.example.mindgrid
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var btnStartGame: Button
    private lateinit var btnHowToPlay: Button
    private lateinit var btnExit: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnStartGame = findViewById(R.id.btnStartGame)
        btnHowToPlay = findViewById(R.id.btnHowToPlay)
        btnExit = findViewById(R.id.btnExit)

        btnStartGame.setOnClickListener {
            val intent = Intent(this, GameActivity::class.java)
            startActivity(intent)
        }

        btnHowToPlay.setOnClickListener {
            Toast.makeText(this, "Rearrange the tiles in order from 1 to 8 using the empty space. Press 'Shuffle' to start.", Toast.LENGTH_LONG).show()
        }

        btnExit.setOnClickListener {
            finishAffinity()
        }
    }
}
