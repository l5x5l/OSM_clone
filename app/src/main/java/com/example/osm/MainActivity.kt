package com.example.osm

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.marginTop
import com.example.osm.databinding.ActivityMainBinding
import com.example.osm.game.GameActivity
import com.example.osm.select.SelectActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private val handler = Handler(Looper.getMainLooper())
    var title_thread = titleThread()
    var start_button_thread = startButtonThread()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        binding.mainLayout.setOnClickListener { goToSelectActivity() }

        setContentView(binding.root)

        if (Build.VERSION.SDK_INT >= 30){
            //or WindowInsets.Type.navigationBars() 이걸 추가해도 화면 터치하면 그냥 다시 나온다...
            //상단바를 한번 보이면 그 이후로 계속 보이게 된다.
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        }
    }

    override fun onResume() {
        super.onResume()
        title_thread = titleThread()
        title_thread.start()
        start_button_thread = startButtonThread()
        start_button_thread.start()
    }

    override fun onPause() {
        super.onPause()
        title_thread.stopThread()
        title_thread.join()
        start_button_thread.stopThread()
        start_button_thread.join()
    }

    fun goToSelectActivity() {
        val intent = Intent(this, SelectActivity::class.java)
        startActivity(intent)
    }

    inner class titleThread() : Thread() {
        var running = true

        override fun run() {
            val titleLayoutParams = binding.gameTitle.layoutParams as ConstraintLayout.LayoutParams
            val MarginTopUpperLimit = (binding.gameTitle.marginTop * 1.1).toInt()
            val MarginTopLowerLimit = (binding.gameTitle.marginTop * 0.9).toInt()
            var titleMarginTop = binding.gameTitle.marginTop
            var speed = 2

            while(running){
                titleMarginTop += speed

                if (titleMarginTop >= MarginTopUpperLimit) {
                    speed = -2
                } else if (titleMarginTop <= MarginTopLowerLimit) {
                    speed = 2
                }
                titleLayoutParams.setMargins(0, titleMarginTop, 0, 0)
                handler.post{
                    binding.gameTitle.layoutParams = titleLayoutParams
                }
                Thread.sleep(100)
            }
        }

        fun stopThread(){
            running = false
        }
    }

    inner class startButtonThread() : Thread() {
        var running = true

        override fun run() {
            var isInvisible = false

            while(running){
                handler.post{
                    if (isInvisible){
                        binding.playButton.visibility = View.INVISIBLE
                    } else {
                        binding.playButton.visibility = View.VISIBLE
                    }
                }
                isInvisible = !isInvisible
                Thread.sleep(500)
            }
        }

        fun stopThread(){
            running = false
        }

    }


}