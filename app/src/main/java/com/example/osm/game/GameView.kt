package com.example.osm.game

import android.content.Context
import android.graphics.Paint
import android.util.Log
import android.view.SurfaceView
import com.example.osm.R

class GameView(context : Context, screenX : Int, screenY : Int) : SurfaceView(context), Runnable{

    private lateinit var thread : Thread
    private var isPlaying = false
    private val screenX = screenX
    private val screenY = screenY
    private val noteList = ArrayList<Note>()
    private val trashNoteList = ArrayList<Note>()
    private val paint = Paint()

    private var count = 0

    override fun run() {
        while(isPlaying){
            update()
            draw()
            sleep()
        }
    }

    private fun update() {
        if (count % 9 == 0)
            newNote(0)

        for (note in noteList) {
            note.y += 30
            if (note.y  >= screenY / 2 - note.height / 2){
                //noteList.remove(note)
                trashNoteList.add(note)
            }
        }

        for (note in trashNoteList){
            noteList.remove(note)
        }

        trashNoteList.clear()

    }

    private fun draw() {
        if(holder.surface.isValid){
            var canvas = holder.lockCanvas()
            canvas.drawColor(resources.getColor(R.color.white))
            for(note in noteList){
                //drawColor를 이쪽에 넣으면 이전 2프레임이 반복된다, 왜?
                canvas.drawBitmap(note.note, note.x.toFloat(), note.y.toFloat(), paint)
            }
            holder.unlockCanvasAndPost(canvas)
        }
    }

    private fun sleep() {
        try {
            Thread.sleep(17)
            count += 1
        } catch (e : InterruptedException){
            e.printStackTrace()
        }
    }

    fun resume() {
        thread = Thread(this)
        isPlaying = true
        thread.start()
    }

    fun pause() {
        isPlaying = false
        thread.join()
    }

    public fun newNote(direction : Int){
        val note = Note(resources, direction)
        note.x = screenX / 2 - note.width / 2
        note.y = 0 - note.height
        noteList.add(note)
    }

}