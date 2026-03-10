package com.example.gravityBalls

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint

class Ball(var x: Float=200f, var y: Float=200f) {
    private val paint = Paint()
    private val clr = Color.argb(255,(0..255).random(),(0..255).random(),(0..255).random())

    private var r = (50..80).random().toFloat()

    private var dx = 0f
    private var dy = 0f
    var gameOver = false
    var score=0

    fun update(w: Float, h: Float, gravityX: Float, gravityY: Float){
        if(gameOver)return
        score++

        dx -= gravityX
        dy += gravityY

        dx *= 0.98f
        dy *= 0.98f
        x += dx
        y += dy

        if (x - r < 0f || x + r > w || y - r < 0f || y + r > h) {
            gameOver = true
        }
    }

    fun draw(cnv: Canvas) {
        paint.color = clr
        cnv.drawCircle(x, y, r, paint)
        paint.color = Color.BLACK
        paint.textSize = 50f
        cnv.drawText("Score: $score", 10f, 50f, paint)
    }

}
