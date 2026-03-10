package com.example.gravityBalls

import android.os.Bundle
import android.widget.Button

import androidx.activity.ComponentActivity


class MainActivity : ComponentActivity() {
    private lateinit var playBtn: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mainmenu)

        playBtn= findViewById(R.id.button)
        playBtn.setOnClickListener {
            start()
        }
    }
    fun start(){

    }

}

