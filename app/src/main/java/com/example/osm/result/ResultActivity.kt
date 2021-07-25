package com.example.osm.result

import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.osm.R
import com.example.osm.databinding.ActivityResultBinding
import com.example.osm.game.GameActivity

class ResultActivity : AppCompatActivity() {

    private lateinit var binding : ActivityResultBinding
    private lateinit var scoreRecyclerAdapter: scoreRecyclerAdapter
    private lateinit var preferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private var scoreList = ArrayList<Int>()
    private val keyList = listOf("first", "second", "third", "fourth", "fifth")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val isClear = intent.getBooleanExtra("isClear", true)
        val score = intent.getIntExtra("score", 0)

        binding = ActivityResultBinding.inflate(layoutInflater)

        preferences = getSharedPreferences("score", MODE_PRIVATE)
        editor = preferences.edit()

        if (isClear){
            binding.resultText.text = "clear"
            binding.score.text = score.toString()
            for (i in 0..4) {
                scoreList.add(preferences.getInt(keyList[i], 0))
            }
            for (i in 0..4) {
                if (score > scoreList[i]) {
                    scoreList.add(i, score)
                    scoreList.removeLast()
                    break
                }
            }
            for (i in 0..4){
                editor.putInt(keyList[i], scoreList[i])
            }
            editor.commit()
        } else {
            binding.resultText.text = "game\nover"
            binding.scoreRecyclerview.visibility = View.GONE
            binding.score.visibility = View.GONE
        }

        binding.homeButton.setOnClickListener {
            finish()
            overridePendingTransition(R.anim.fadein, R.anim.vertical_exit)
        }
        binding.retryButton.setOnClickListener { goToGame() }

        val linearLayoutManager = CustomLinearLayoutManager(this)
        binding.scoreRecyclerview.layoutManager = linearLayoutManager

        scoreRecyclerAdapter = scoreRecyclerAdapter(this, scoreList)
        binding.scoreRecyclerview.adapter = scoreRecyclerAdapter

        setContentView(binding.root)

        if (Build.VERSION.SDK_INT >= 30){
            //or WindowInsets.Type.navigationBars() 이걸 추가해도 화면 터치하면 그냥 다시 나온다...
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        }
    }

    fun goToGame() {
        val intent = Intent(this, GameActivity::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.fadein, R.anim.vertical_exit)
        finish()
    }
}