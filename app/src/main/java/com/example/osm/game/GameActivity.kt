package com.example.osm.game

import android.content.Intent
import android.graphics.Point
import android.graphics.Rect
import android.os.*
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import com.example.osm.R
import com.example.osm.databinding.ActivityGameBinding
import com.example.osm.result.ResultActivity
import kotlin.concurrent.thread

class GameActivity : AppCompatActivity() {

    private lateinit var gameView : GameView
    private lateinit var binding : ActivityGameBinding
    private val handler = Handler(Looper.getMainLooper())
    private var isPlaying = true
    private lateinit var timer : CountDownTimer


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

        timer = object:CountDownTimer(3000, 1000) {
            var count = 3
            override fun onTick(millisUntilFinished: Long) {
                binding.pauseView.text = count.toString()
                count--
            }

            override fun onFinish() {
                gameView.resume()
                binding.pauseView.visibility = View.GONE
                binding.pauseView.text = "Pause"
                count = 3
                isPlaying = true
                binding.pauseButton.setImageResource(R.drawable.pause_button)
            }
        }

        binding.pauseButton.setOnClickListener {
            if (isPlaying){
                gameView.pause()
                binding.pauseView.visibility = View.VISIBLE
                isPlaying = false
                binding.pauseButton.setImageResource(R.drawable.start_button)
            } else {
                timer.start()
                /*gameView.resume()
                binding.pauseView.visibility = View.GONE
                isPlaying = true
                binding.pauseButton.setImageResource(R.drawable.pause_button)*/
            }
        }

        setContentView(binding.root)
        binding.frameLayout.addView(gameView)


        if (Build.VERSION.SDK_INT >= 30){
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        }
    }

    override fun onPause() {
        super.onPause()
        if (isPlaying){ // 사용자가 일시중지를 하지 않은 상태에서 onPause 가 되었을 때 (home 버튼)
            isPlaying = false
            gameView.pause()
            binding.pauseView.visibility = View.VISIBLE
            binding.pauseButton.setImageResource(R.drawable.start_button)
        }
    }

    override fun onResume() {
        super.onResume()

        if (isPlaying){ // 맨 처음 실행했을 때 gameView 가 작동되도록
            gameView.resume()
            binding.pauseView.visibility = View.GONE
        }
/*        else {
            gameView.resume()
            gameView.pause()
        }*/
    }

    fun goToResult(isClear : Boolean, score : Int = 0) {
        binding.pauseView.text = "loading..."
        val intent = Intent(this, ResultActivity::class.java)
        intent.putExtra("isClear", isClear)
        intent.putExtra("score", score)
        this.startActivity(intent)
        overridePendingTransition(R.anim.vertical_enter, R.anim.fadeout) // 이거 다음에 finish 오면 어쩌지
        finish()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.fadein, R.anim.fadeout)
    }

    // 망함
    fun waitSeconds(count : Int) {
        /*
        thread = Thread {
            for (i in count downTo 1) {
                handler.post {
                    binding.pauseView.text = i.toString()
                }
                //Log.d("GameActivity", binding.pauseView.text as String)
                Thread.sleep(1000)
            }
        }
        thread.start()*/

        for (i in count downTo 1) {

            binding.pauseView.text = i.toString()

            //Log.d("GameActivity", binding.pauseView.text as String)
            Thread.sleep(1000)
        }
    }

}