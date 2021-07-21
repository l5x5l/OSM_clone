package com.example.osm

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.osm.databinding.ActivityMainBinding
import com.example.osm.game.GameActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        binding.playButton.setOnClickListener { goToGame() }

        setContentView(binding.root)
    }

    fun goToGame() {
        val intent = Intent(this, GameActivity::class.java)
        startActivity(intent)
    }
}