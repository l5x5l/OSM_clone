package com.example.osm

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isInvisible
import androidx.core.view.marginTop
import com.example.osm.databinding.ActivityMainBinding
import com.example.osm.game.GameActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private val handler = Handler(Looper.getMainLooper())
    var title_thread = titleThread()
    var start_button_thread = startButtonThread()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        binding.mainLayout.setOnClickListener { goToGame() }

        setContentView(binding.root)
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

    fun goToGame() {
        val intent = Intent(this, GameActivity::class.java)
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