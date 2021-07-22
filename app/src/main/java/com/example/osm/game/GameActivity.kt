package com.example.osm.game

import android.graphics.Point
import android.graphics.Rect
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import com.example.osm.R
import com.example.osm.databinding.ActivityGameBinding

class GameActivity : AppCompatActivity() {

    private lateinit var gameView : GameView
    private lateinit var binding : ActivityGameBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityGameBinding.inflate(layoutInflater)

        gameView = if (Build.VERSION.SDK_INT >= 30){
            var point: Rect = windowManager.currentWindowMetrics.bounds
            GameView(this, point.width(), point.height())
        } else {
            var point = Point()
            windowManager.defaultDisplay.getSize(point)
            GameView(this, point.x, point.y)
        }

        //setContentView(gameView)
        setContentView(binding.root)
        binding.frameLayout.addView(gameView)


        if (Build.VERSION.SDK_INT >= 30){
            //or WindowInsets.Type.navigationBars() 이걸 추가해도 화면 터치하면 그냥 다시 나온다...
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        }
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