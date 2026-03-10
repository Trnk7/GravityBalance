package com.example.gravityBalls

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorManager
import android.hardware.SensorEventListener
import android.util.Log
import android.view.View

class GameView(
    context: Context,
    private val listener: OnGameOverListener
) : View(context), Runnable, SensorEventListener {

    interface OnGameOverListener {
        fun onGameOver(score: Int)
    }

    private var thr: Thread? = null
    private var running = false
    private val sensorMgr = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val sensor = sensorMgr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    private val sensitivity=0.5f
    var gX=0f
    var gY=0f
    var ball: Ball=Ball()
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
        val isRunning = ball.update(width.toFloat(), height.toFloat(),gX,gY)

        if(isRunning==1){
            Log.d("game OVer","gameeeee")
            stop()
            // Ensure UI work (starting an Activity) runs on the main thread.
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
            gX = event.values[0]*sensitivity*0.5f
            gY = event.values[1]*sensitivity*0.5f
        }
    }
   

}
