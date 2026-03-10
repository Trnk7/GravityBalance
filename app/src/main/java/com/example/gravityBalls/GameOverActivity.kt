package com.example.gravityBalls

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class GameOverActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gameover)

        val restartBtn=findViewById<Button>(R.id.playAgain)
        val scoreText = findViewById<TextView>(R.id.textView)
        val score = intent.getIntExtra("score",0)
        scoreText.text = getString(R.string.score, score)
        restartBtn.setOnClickListener{
            val intent = Intent(this,GameActivity::class.java)
            startActivity(intent)
            finish()
        }

    }
}