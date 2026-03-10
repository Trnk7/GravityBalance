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

@SuppressLint("ViewConstructor")
class GameView : View, Runnable, SensorEventListener {

    private val listener: OnGameOverListener

    constructor(context: Context, listener: OnGameOverListener) : super(context) {
        this.listener = listener
        this.sensorMgr = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        this.sensor = sensorMgr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        this.ball = Ball()
    }

    interface OnGameOverListener {
        fun onGameOver(score: Int)
    }

    private var thr: Thread? = null
    private var running = false
    private val sensorMgr: SensorManager
    private val sensor: Sensor?
    private val sensitivity=0.5f
    var gX=0f
    var gY=0f
    var ball: Ball
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        ball.x=w/2f
        ball.y=h/2f
    }
    override fun run() {

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
        ball.update(width.toFloat(), height.toFloat(),gX,gY)

        if(ball.gameOver){
            stop()
            post {
                listener.onGameOver(ball.score)
            }
        }

    }
    override fun onDraw(cnv: Canvas) {
        super.onDraw(cnv)
        cnv.drawColor(Color.WHITE) // Clear screen with white
        ball.draw(cnv)

    }
    fun start() {
        if (running) return
        running = true
        thr = Thread(this)
        thr?.start()
        sensorMgr.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)
    }
    fun stop() {
        running = false
        sensorMgr.unregisterListener(this)

        // Avoid joining from the same thread to prevent deadlock.
        if (Thread.currentThread() === thr) return

        try {
            thr?.join()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
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
