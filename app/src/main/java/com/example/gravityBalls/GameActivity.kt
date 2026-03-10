package com.example.gravityBalls


import android.content.Intent
import android.os.Bundle

import androidx.appcompat.app.AppCompatActivity


class GameActivity: AppCompatActivity() {
    private lateinit var gameView: GameView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        gameView= GameView(this, object : GameView.OnGameOverListener {
            override fun onGameOver(score: Int) {
                val intent = Intent(this@GameActivity, GameOverActivity::class.java)
                intent.putExtra("score", score)
                startActivity(intent)
                finish()
            }
        })
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