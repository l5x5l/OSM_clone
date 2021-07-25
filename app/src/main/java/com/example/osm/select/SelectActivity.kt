package com.example.osm.select

import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import androidx.recyclerview.widget.GridLayoutManager
import com.example.osm.R
import com.example.osm.databinding.ActivitySelectBinding
import com.example.osm.game.GameActivity

class SelectActivity : AppCompatActivity() {

    private lateinit var binding : ActivitySelectBinding
    private lateinit var recyclerViewAdapter: CategoryAdapter
    // bgmPlayer 관련
    private var musicThread = MusicThread()
    private var bgmPoint = 0
    private val out = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectBinding.inflate(layoutInflater)

        val dataList = ArrayList<CategoryData>()
        dataList.add(CategoryData("practice", R.drawable.practice, false))
        dataList.add(CategoryData("story", R.drawable.story, true))
        dataList.add(CategoryData("store", R.drawable.store, true))
        dataList.add(CategoryData("challenge", R.drawable.challenge, true))
        dataList.add(CategoryData("collection", R.drawable.collection, true))
        dataList.add(CategoryData("setting", R.drawable.setting, true))

        val gridLayoutManager = CustomGridLayoutManager(this, 3)
        binding.recyclerview.layoutManager = gridLayoutManager

        recyclerViewAdapter = CategoryAdapter(this, dataList)
        binding.recyclerview.adapter = recyclerViewAdapter

        binding.recyclerview.addItemDecoration(CategoryItemDecoration(this))

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
        musicThread = MusicThread()
        musicThread.start()
    }

    override fun onPause() {
        super.onPause()
        musicThread.stopThread()
    }

    // 왜 animation 을 onDestroy 에 넣으면 안되는가?
    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.fadein, R.anim.fadeout)
    }

    fun goToGame() {
        val intent = Intent(this, GameActivity::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.fadein, R.anim.fadeout)
    }

    inner class MusicThread() : Thread() {
        private lateinit var bgmPlayer : MediaPlayer

        override fun run() {
            bgmPlayer = MediaPlayer.create(out, R.raw.select_bgm)
            bgmPlayer.isLooping = true
            bgmPlayer.seekTo(bgmPoint)
            bgmPlayer.start()
        }

        fun stopThread() {
            bgmPlayer.pause()
            bgmPoint = bgmPlayer.currentPosition
            bgmPlayer.reset()
            bgmPlayer.release()
            this.interrupt()
        }
    }
}