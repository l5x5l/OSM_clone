package com.example.osm.game

import android.graphics.Point
import android.graphics.Rect
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import com.example.osm.R
import com.example.osm.databinding.ActivityGameBinding

class GameActivity : AppCompatActivity() {

    private lateinit var gameView : GameView
    private lateinit var binding : ActivityGameBinding
    private var isPlaying = true

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

        binding.pauseButton.setOnClickListener {
            if (isPlaying){
                gameView.pause()
                binding.pauseView.visibility = View.VISIBLE
                isPlaying = false
                binding.pauseButton.setImageResource(R.drawable.start_button)
            } else {
                gameView.resume()
                binding.pauseView.visibility = View.GONE
                isPlaying = true
                binding.pauseButton.setImageResource(R.drawable.pause_button)
            }
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
        if (isPlaying){ // 사용자가 일시중지를 하지 않은 상태에서 onPause
            gameView.pause()
            binding.pauseView.visibility = View.VISIBLE
        }
    }

    override fun onResume() {
        super.onResume()
        if (isPlaying){
            gameView.resume()
            binding.pauseView.visibility = View.GONE
        }
        else {
            // 이러면 draw 가 안되서 화면이 안그려진다
            gameView.oneDraw()
        }
    }

}