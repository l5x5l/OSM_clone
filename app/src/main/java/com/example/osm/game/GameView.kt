package com.example.osm.game

import android.content.Context
import android.graphics.Paint
import android.util.Log
import android.view.MotionEvent
import android.view.SurfaceView
import com.example.osm.R
import java.lang.StrictMath.abs

import kotlin.collections.ArrayList
import kotlin.random.Random


class GameView(context : Context, screenX : Int, screenY : Int) : SurfaceView(context), Runnable{

    private lateinit var thread : Thread
    private var isPlaying = false
    private val screenX = screenX
    private val screenY = screenY
    private val paint = Paint()
    private var count = 0
    private var noteSize = 200
    private val speed = 30
    // 순서대로 위, 오른쪽, 아래, 왼쪽 노트
    private val noteList = listOf<ArrayList<Note>>(ArrayList<Note>(), ArrayList<Note>(), ArrayList<Note>(), ArrayList<Note>())
    private val clearNoteList = listOf<ArrayList<Note>>(ArrayList<Note>(), ArrayList<Note>(), ArrayList<Note>(), ArrayList<Note>())
    private val positionList = listOf<IntArray>(intArrayOf((screenX - noteSize) / 2, 0 - noteSize), intArrayOf(screenX, (screenY - noteSize) / 2),
            intArrayOf((screenX - noteSize) / 2, screenY), intArrayOf(0 - noteSize, (screenY - noteSize) / 2))
    private val noteSpeedList = listOf<IntArray>(intArrayOf(0, speed), intArrayOf(-speed, 0), intArrayOf(0, -speed), intArrayOf(speed, 0))
    // 판정 범위 변수
    private val perfect = 100
    private val good = 200
    private val bad = 350
    // 가운데 표시 변수
    private val center : Center = Center(resources, noteSize)


    // 테스트끝나면 이거 지우고 update 복원
    /*
    init {
        newNote(0)
        newNote(0)
        newNote(1)
        newNote(1)
        newNote(1)
        newNote(2)
        newNote(2)
        newNote(3)
        newNote(3)
        newNote(3)

        noteList[0][0].x = (screenX - noteSize) / 2
        noteList[0][0].y = (screenY - noteSize) / 2 - 150
        noteList[0][1].x = (screenX - noteSize) / 2
        noteList[0][1].y = (screenY - noteSize) / 2 - 350
        noteList[1][0].x = (screenX - noteSize) / 2 + 150
        noteList[1][0].y = (screenY - noteSize) / 2
        noteList[1][1].x = (screenX - noteSize) / 2 + 350
        noteList[1][1].y = (screenY - noteSize) / 2
        noteList[1][2].x = (screenX - noteSize) / 2 + 550
        noteList[1][2].y = (screenY - noteSize) / 2
        noteList[2][0].x = (screenX - noteSize) / 2
        noteList[2][0].y = (screenY - noteSize) / 2 + 150
        noteList[2][1].x = (screenX - noteSize) / 2
        noteList[2][1].y = (screenY - noteSize) / 2 + 350
        noteList[3][0].x = (screenX - noteSize) / 2 - 150
        noteList[3][0].y = (screenY - noteSize) / 2
        noteList[3][1].x = (screenX - noteSize) / 2 - 350
        noteList[3][1].y = (screenY - noteSize) / 2
        noteList[3][2].x = (screenX - noteSize) / 2 - 550
        noteList[3][2].y = (screenY - noteSize) / 2
    }
    */





    override fun run() {
        while(isPlaying){
            update()
            clearNote()
            draw()
            sleep()
        }
    }

    private fun update() {

        if (count % 30 == 0){
            val temp = Random.nextInt(4)
            newNote(temp)
        }


        for (OneDirectionNoteList in noteList){
            for (note in OneDirectionNoteList) {

                val temp = note.getDirection()

                note.x += noteSpeedList[temp][0]
                note.y += noteSpeedList[temp][1]
                if (note.stat == "none" && check(temp, note.x, note.y)){
                    //noteList.remove(note)
                    note.stat = "fail"
                    clearNoteList[temp].add(note)
                }
            }
        }

    }

    private fun draw() {
        if(holder.surface.isValid){
            var canvas = holder.lockCanvas()
            canvas.drawColor(resources.getColor(R.color.white))

            canvas.drawBitmap(center.center, ((screenX - center.width) / 2).toFloat(), ((screenY - center.height) / 2).toFloat(), paint)

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
            Thread.sleep(33)
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

    private fun check(direction : Int, x : Int, y : Int) : Boolean{
        return when (direction) {
            0 -> { y >= (screenY  - noteSize) / 2 }
            1 -> { x <= (screenX - noteSize) / 2 }
            2 -> { y <= (screenY - noteSize) / 2 }
            else -> { x >= (screenX - noteSize) / 2 }
        }
    }

    private fun checkArea(x : Int, y : Int) : Int {
        val result1 = (x - screenX / 2) - (y - screenY / 2)
        val result2 = - (x - screenX / 2) - (y - screenY / 2)

        return if (result1 >= 0 && result2 >= 0) 0
        else if(result1 >= 0 && result2 < 0) 1
        else if(result1 < 0 && result2 < 0) 2
        else 3
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        var pointer_count = event?.pointerCount
        if (pointer_count != null) {
            if (pointer_count > 5) pointer_count = 5
        }

        if (event != null) {
            when (event.action and MotionEvent.ACTION_MASK) {
                MotionEvent.ACTION_DOWN -> {            // single touch
                    val temp = checkArea(event.x.toInt(), event.y.toInt())
                    if (!noteList[temp].isNullOrEmpty()){
                        val distance = getDistance(noteList[temp][0].x + noteSize / 2, noteList[temp][0].y + noteSize / 2)
                        Log.d("ouTouchEvent", "area : " + temp.toString() + " and distance : " + distance.toString())
                        if(distance <= perfect){
                            noteList[temp][0].stat = "perfect"
                            clearNoteList[temp].add(noteList[temp][0])
                        } else if (distance <= good) {
                            noteList[temp][0].stat = "good"
                            clearNoteList[temp].add(noteList[temp][0])
                        } else if (distance <= bad) {
                            noteList[temp][0].stat = "bad"
                            clearNoteList[temp].add(noteList[temp][0])
                        }
                    }
                }
                MotionEvent.ACTION_POINTER_DOWN -> {    // multi touch
                    Log.d("터치 이벤트", "multi touch!")

                }
            }
        }

        return true
    }

    private fun clearNote() {
        for (i in 0..3){
            for(note in clearNoteList[i]){
                noteList[i].remove(note)
                Log.d("clearNote", note.stat)
            }
            clearNoteList[i].clear()
        }
    }

    // 원래같으면, 제곱을 한 다음 더한 후 루트를 씌우지만, 여기서는 그럴 필요가 없다
    // x와 y 좌표는 bitmap 의 중앙 좌료를 의미한다.
    private fun getDistance(x : Int, y : Int) : Int {
        return abs(x - screenX / 2) + abs(y - screenY / 2)
    }


}