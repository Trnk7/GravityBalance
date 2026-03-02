package com.example.gravityBalls

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import kotlin.math.abs
import kotlin.math.sqrt

class Ball(var x: Float, var y: Float) {
    private val paint = Paint()
    private val clr = Color.argb(255,(0..255).random(),(0..255).random(),(0..255).random())

    private var r = (50..80).random().toFloat()
    private var mass = r/2

    private val slop=0.5f

    private val friction=0.9f
    private val percent=0.2f
    private var restitution=0.9f
    private var dx = 0f
    private var dy = 0f

    fun update(w: Float, h: Float, gravityX: Float, gravityY: Float) {
        x += dx
        y += dy
        dx-=gravityX
        dy+=gravityY

        if (x - r < 0f) {
            resolveWall(1f,0f,r-x)
        }
        if (x + r > w) {
            resolveWall(-1f,0f,x+r-w)
        }
        if (y - r < 0f) {
            resolveWall(0f,1f,r-y)
        }
        if (y+r >h) {
            resolveWall(0f,-1f,y+r-h)
        }

        if(abs(dx) <gravityX) dx=0f
        if(abs(dy) <gravityY) dy=0f
    }

    fun draw(gravityBalls: Canvas) {
        paint.color = clr
        gravityBalls.drawCircle(x, y, r, paint)
    }

    fun collide(other: Ball){
        val distX = other.x-x
        val distY = other.y-y
        val distance = sqrt((distX * distX + distY * distY).toDouble()).toFloat()
        if(distance<=r+other.r){
            val overlap = maxOf((r + other.r - distance)-slop,0f)/(mass+other.mass)*percent


            val nx = distX/distance
            val ny = distY/distance

            x-=nx*overlap*other.mass
            y-=ny*overlap*other.mass
            other.x+=nx*overlap*mass
            other.y+=ny*overlap*mass

            val dvx = other.dx-dx
            val dvy = other.dy-dy

            val impSpeed=dvx*nx+dvy*ny
            restitution = if(abs(impSpeed)>2) 0.5f else 0f
            if(impSpeed>-0.5f) return
            val imp=((1+restitution)*impSpeed)/(mass+other.mass)
            dx+=imp*other.mass*nx
            dy+=imp*other.mass*ny
            other.dx-=imp*mass*nx
            other.dy-=imp*mass*ny
        }
    }
    fun resolveWall(nx: Float, ny: Float, pen: Float){
        val correction = maxOf(pen-slop,0f)
        x+=correction*nx
        y+=correction*ny

        val vel = dx*nx+dy*ny
        if(vel<0f){
            val res = if(abs(vel)>2)  restitution else 0f
            dx-=(1+res)*vel*nx
            dy-=(1+res)*vel*ny

            dx-=(dx*-ny+dy*nx)*(1-friction)*(-ny)
            dy-=(dx*ny+dy*nx)*(1-friction)*(nx)
        }
    }
}
