package com.example.cnv

import android.os.Bundle
import android.widget.Toast

import androidx.activity.ComponentActivity


class MainActivity : ComponentActivity() {
    private lateinit var gameView: GameView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        gameView = GameView(this)
        setContentView(gameView)
    }

    override fun onResume() {
        super.onResume()
        gameView.start()
    }

    override fun onPause() {
        super.onPause()
        gameView.stop()
    }
}

