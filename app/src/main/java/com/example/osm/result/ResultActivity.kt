package com.example.osm.result

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.osm.R
import com.example.osm.databinding.ActivityResultBinding

class ResultActivity : AppCompatActivity() {

    private lateinit var binding : ActivityResultBinding
    private lateinit var scoreRecyclerAdapter: scoreRecyclerAdapter
    private lateinit var preferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private var scoreList = ArrayList<Int>()
    private val keyList = listOf("first", "second", "third", "fourth", "fifth")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        preferences = getSharedPreferences("score", MODE_PRIVATE)
        editor = preferences.edit()

        for (i in 0..4) {
            scoreList.add(preferences.getInt(keyList[i], 0))
        }

        binding = ActivityResultBinding.inflate(layoutInflater)

        val linearLayoutManager = LinearLayoutManager(this)
        binding.scoreRecyclerview.layoutManager = linearLayoutManager

        scoreRecyclerAdapter = scoreRecyclerAdapter(this, scoreList)
        binding.scoreRecyclerview.adapter = scoreRecyclerAdapter

        setContentView(binding.root)
    }
}