package com.example.gravityBalls

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.ComponentActivity


class StartMenuActivity : ComponentActivity() {
    private lateinit var playBtn: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mainmenu)

        playBtn = findViewById(R.id.playAgain)
        playBtn.setOnClickListener {
            start()
        }
    }
    private fun start(){
        val intent = Intent(this, GameActivity::class.java)
        startActivity(intent)
    }

}

