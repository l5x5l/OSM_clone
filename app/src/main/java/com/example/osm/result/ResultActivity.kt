package com.example.osm.result

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
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

        if (isClear){
            binding.resultText.text = "clear"
        } else {
            binding.resultText.text = "game\nover"
            binding.scoreRecyclerview.visibility = View.GONE
        }

        binding.homeButton.setOnClickListener { finish() }
        binding.retryButton.setOnClickListener { goToGame() }

        preferences = getSharedPreferences("score", MODE_PRIVATE)
        editor = preferences.edit()

        for (i in 0..4) {
            scoreList.add(preferences.getInt(keyList[i], 0))
        }

        val linearLayoutManager = CustomLinearLayoutManager(this)
        binding.scoreRecyclerview.layoutManager = linearLayoutManager

        scoreRecyclerAdapter = scoreRecyclerAdapter(this, scoreList)
        binding.scoreRecyclerview.adapter = scoreRecyclerAdapter

        setContentView(binding.root)
    }

    fun goToGame() {
        val intent = Intent(this, GameActivity::class.java)
        startActivity(intent)
        finish()
    }
}