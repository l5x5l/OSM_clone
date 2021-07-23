package com.example.osm.select

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.example.osm.R
import com.example.osm.databinding.ActivitySelectBinding
import com.example.osm.game.GameActivity

class SelectActivity : AppCompatActivity() {

    private lateinit var binding : ActivitySelectBinding
    private lateinit var recyclerViewAdapter: CategoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectBinding.inflate(layoutInflater)

        val dataList = ArrayList<CategoryData>()
        dataList.add(CategoryData("tutorial", R.drawable.ic_launcher_background, false))
        dataList.add(CategoryData("story", R.drawable.ic_launcher_background, true))
        dataList.add(CategoryData("store", R.drawable.ic_launcher_background, true))
        dataList.add(CategoryData("practice", R.drawable.ic_launcher_background, true))
        dataList.add(CategoryData("collection", R.drawable.ic_launcher_background, true))
        dataList.add(CategoryData("setting", R.drawable.ic_launcher_background, true))

        val gridLayoutManager = CustomGridLayoutManager(this, 3)
        binding.recyclerview.layoutManager = gridLayoutManager

        recyclerViewAdapter = CategoryAdapter(this, dataList)
        binding.recyclerview.adapter = recyclerViewAdapter

        binding.recyclerview.addItemDecoration(CategoryItemDecoration(this))

        setContentView(binding.root)
    }

    fun goToGame() {
        val intent = Intent(this, GameActivity::class.java)
        startActivity(intent)
    }
}