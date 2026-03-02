package com.example.gravityBalls

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorManager
import android.hardware.SensorEventListener
import android.view.View
import android.view.MotionEvent


class GameView(context: Context): View(context), Runnable, SensorEventListener {
    private var thr: Thread? = null
    private var touchX=0f
    private var touchY=0f
    private var spawn=false
    private val balls =mutableListOf<Ball>()
    private val maxBalls=20
    private var running = false

    private val sensorMngr = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val sensor = sensorMngr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    private val sensitivity=0.5f
    var gX=0f
    var gY=0f

    override fun run() {
        val ball = Ball(200f, 200f)
        balls.add(ball)
        
        while (running) {
            if (width > 0 && height > 0) {
                update()
                postInvalidate()
            }
            try {
                Thread.sleep(1000 / 30)
            } catch (e: InterruptedException) {
                e.printStackTrace()
                break
            }
        }
    }

    private fun update() {
        if(spawn) {
            if(balls.size>=maxBalls){
                balls.removeAt(0)
            }
            balls.add(Ball(touchX, touchY))
            spawn = false
        }
        balls.forEach { ball -> ball.update(width.toFloat(), height.toFloat(),gX,gY) }
        repeat(4) {
            for (i in balls.indices) {
                for (j in i + 1 until balls.size) {
                    balls[i].collide(balls[j])
                }
            }
        }
    }

    override fun onDraw(gravityBalls: Canvas) {
        super.onDraw(gravityBalls)
        gravityBalls.drawColor(Color.WHITE) // Clear screen with white
        balls.forEach { ball -> ball.draw(gravityBalls) }
    }

    fun start() {
        if (running) return
        running = true
        thr = Thread(this)
        thr?.start()
        sensorMngr.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)
    }

    fun stop() {
        running = false
        try {
            thr?.join()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        sensorMngr.unregisterListener(this)

    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                touchX=event.x
                touchY=event.y
                spawn=true
            }
        }
        return true
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_ACCELEROMETER) {
            gX = event.values[0]*sensitivity
            gY = event.values[1]*sensitivity

        }
    }


}
