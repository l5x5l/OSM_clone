package com.example.osm.game

import android.graphics.Point
import android.graphics.Rect
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import com.example.osm.R

class GameActivity : AppCompatActivity() {

    private lateinit var gameView : GameView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        gameView = if (Build.VERSION.SDK_INT >= 30){
            var point: Rect = windowManager.currentWindowMetrics.bounds
            GameView(this, point.width(), point.height())
        } else {
            var point = Point()
            windowManager.defaultDisplay.getSize(point)
            GameView(this, point.x, point.y)
        }

        setContentView(gameView)

        if (Build.VERSION.SDK_INT >= 30){
            window.insetsController?.hide(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
        } else {
            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        }

        gameView.newNote(0)

    }

    override fun onPause() {
        super.onPause()
        gameView.pause()
    }

    override fun onResume() {
        super.onResume()
        gameView.resume()
    }

}