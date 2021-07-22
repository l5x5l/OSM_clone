package com.example.osm.game

import android.content.Context
import android.graphics.Paint
import android.util.Log
import android.view.SurfaceView
import com.example.osm.R

import kotlin.collections.ArrayList
import kotlin.random.Random


class GameView(context : Context, screenX : Int, screenY : Int) : SurfaceView(context), Runnable{

    private lateinit var thread : Thread
    private var isPlaying = false
    private val screenX = screenX
    private val screenY = screenY
    private val trashNoteList = ArrayList<Note>()
    private val paint = Paint()
    private var count = 0
    private var noteSize = 200
    private val speed = 30
    // 순서대로 위, 오른쪽, 아래, 왼쪽 노트
    private val noteList = listOf<ArrayList<Note>>(ArrayList<Note>(), ArrayList<Note>(), ArrayList<Note>(), ArrayList<Note>())
    private val positionList = listOf<IntArray>(intArrayOf((screenX - noteSize) / 2, 0 - noteSize), intArrayOf(screenX, (screenY - noteSize) / 2),
            intArrayOf((screenX - noteSize) / 2, screenY), intArrayOf(0 - noteSize, (screenY - noteSize) / 2))
    private val noteSpeedList = listOf<IntArray>(intArrayOf(0, speed), intArrayOf(-speed, 0), intArrayOf(0, -speed), intArrayOf(speed, 0))



    override fun run() {
        while(isPlaying){
            update()
            draw()
            sleep()
        }
    }

    private fun update() {
        if (count % 9 == 0){
            val temp = Random.nextInt(4)
            newNote(temp)
        }

        for (OneDirectionNoteList in noteList){
            for (note in OneDirectionNoteList) {

                val temp = note.getDirection()

                note.x += noteSpeedList[temp][0]
                note.y += noteSpeedList[temp][1]
                if (check(temp, note.y, note.x)){
                    //noteList.remove(note)
                    trashNoteList.add(note)
                }
            }

            for (note in trashNoteList){
                OneDirectionNoteList.remove(note)
            }
        }

        trashNoteList.clear()

    }

    private fun draw() {
        if(holder.surface.isValid){
            var canvas = holder.lockCanvas()
            canvas.drawColor(resources.getColor(R.color.white))

            for (OneDirectionNoteList in noteList){
                for(note in OneDirectionNoteList){
                    //drawColor를 이쪽에 넣으면 이전 2프레임이 반복된다, 왜?
                    canvas.drawBitmap(note.note, note.x.toFloat(), note.y.toFloat(), paint)
                }
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
        val note = Note(resources, direction, noteSize)
        note.x = positionList[direction][0] //screenX / 2 - note.width / 2
        note.y = positionList[direction][1]
        noteList[direction].add(note)
    }

    private fun check(direction : Int, y : Int, x : Int) : Boolean{
        return when (direction) {
            0 -> { y >= (screenY  - noteSize) / 2 }
            1 -> { x <= (screenX - noteSize) / 2 }
            2 -> { y <= (screenY - noteSize) / 2 }
            else -> { x >= (screenX - noteSize) / 2 }
        }
    }


}