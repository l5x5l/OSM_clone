package com.example.osm.game

import android.content.Context
import android.graphics.Paint
import android.view.MotionEvent
import android.view.SurfaceView
import androidx.core.content.res.ResourcesCompat
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
    private val paint2 = Paint()
    private var count = 0
    private var noteSize = 200
    private val speed = 30
    // 배경화면
    private var background1 = Background(screenX, screenY, resources)
    private var background2 = Background(screenX, screenY, resources)
    // 순서대로 위, 오른쪽, 아래, 왼쪽 노트
    private val noteImage = listOf<Int>(R.drawable.up, R.drawable.right, R.drawable.down, R.drawable.left)
    private val noteList = listOf<ArrayList<Note>>(ArrayList<Note>(), ArrayList<Note>(), ArrayList<Note>(), ArrayList<Note>())
    private val clearNoteList = listOf<ArrayList<Note>>(ArrayList<Note>(), ArrayList<Note>(), ArrayList<Note>(), ArrayList<Note>())
    private val positionList = listOf<IntArray>(intArrayOf((screenX - noteSize) / 2, 0 - noteSize), intArrayOf(screenX, (screenY - noteSize) / 2),
            intArrayOf((screenX - noteSize) / 2, screenY), intArrayOf(0 - noteSize, (screenY - noteSize) / 2))
    private val noteSpeedList = listOf<IntArray>(intArrayOf(0, speed), intArrayOf(-speed, 0), intArrayOf(0, -speed), intArrayOf(speed, 0))
    // 판정 범위 변수
    private val perfect = 100
    private val good = 200
    private val bad = 350
    // 가운데 부분
    private val center : Center = Center(resources, noteSize)
    // 점수 부분
    private var score = 0
    // hp 부분
    private var hp = 10
    private val hp_cell = "\u25a0"
    // 종료 부분
    private val endTime = 700
    // 노트 생성 주기
    private val noteInterval = 30
    private val startTime = 100

    // screenRatio
    var screenRatioX = 1920f / screenX
    var screenRatioY = 1080f / screenY

    init {
        // 배경화면 이동용도 background2의 x좌표 설정
        background2.x = screenX
        // score 글자 색과 글자 크기 지정
        paint.color = resources.getColor(R.color.black, null)
        //paint.setTypeface(resources.getFont(R.font.dunggeunmo)) <- above api level 26
        paint.typeface = ResourcesCompat.getFont(context, R.font.dunggeunmo)
        paint.textSize = 100F
        // hp 의 색상을 흰색으로
        paint2.color = resources.getColor(R.color.white, null)
        paint2.typeface = ResourcesCompat.getFont(context, R.font.dunggeunmo)
        paint2.textSize = 100F
    }

    override fun run() {
        while(isPlaying){
            update()
            clearNote()
            checkIsEnd()    //test!!
            draw()
            sleep()
        }
    }

    private fun update() {

        background1.x -= (10 * screenRatioX).toInt()
        background2.x -= (10 * screenRatioX).toInt()
        if (background1.x + background1.background.width < 0) background1.x = screenX
        if (background2.x + background2.background.width < 0) background2.x = screenX

        if (count % noteInterval == 0 && count >= startTime && count <= endTime - noteInterval * 2){
            val temp = Random.nextInt(4)
            newNote(temp)
        }


        for (OneDirectionNoteList in noteList){
            for (note in OneDirectionNoteList) {

                val temp = note.getDirection()

                note.x += noteSpeedList[temp][0]
                note.y += noteSpeedList[temp][1]
                if (note.stat == "none" && check(temp, note.x, note.y)){
                    note.stat = "fail"
                    clearNoteList[temp].add(note)
                }
            }
        }

    }

    private fun draw() {
        if(holder.surface.isValid){
            var canvas = holder.lockCanvas()
            //canvas.drawColor(resources.getColor(R.color.black))
            canvas.drawBitmap(background1.background, background1.x.toFloat(), background1.y.toFloat(), paint)
            canvas.drawBitmap(background2.background, background2.x.toFloat(), background2.y.toFloat(), paint)

            canvas.drawText(score.toString(), 50F, (screenY - 100).toFloat(), paint)
            canvas.drawText(hp_cell.repeat(hp), 50F, 75F, paint2)

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

    public fun oneDraw(){
        draw()
    }

    public fun newNote(direction : Int){
        val note = Note(resources, direction, noteSize, noteImage[direction])
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
            if (pointer_count > 5)
                pointer_count = 5
        }

        if (event != null) {
            when (event.action and MotionEvent.ACTION_MASK) {
                MotionEvent.ACTION_DOWN -> {            // single touch
                    val temp = checkArea(event.x.toInt(), event.y.toInt())
                    if (!noteList[temp].isNullOrEmpty()){
                        val distance = getDistance(noteList[temp][0].x + noteSize / 2, noteList[temp][0].y + noteSize / 2)
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
                    //Log.d("터치 이벤트", "multi touch!")

                }
            }
        }

        return true
    }

    private fun clearNote() {
        for (i in 0..3){
            for(note in clearNoteList[i]){
                noteList[i].remove(note)
                //Log.d("clearNote", note.stat)
                when(note.stat) {
                    "perfect" -> score += 3000
                    "good" -> score += 1500
                    "bad" -> score += 500
                    "fail" -> {
                        hp--
                        // hp 줄인 다음, hp가 0 이하인지 비교
                    }
                }
            }
            clearNoteList[i].clear()
        }
    }

    // 원래같으면, 제곱을 한 다음 더한 후 루트를 씌우지만, 여기서는 그럴 필요가 없다
    // x와 y 좌표는 bitmap 의 중앙 좌료를 의미한다.
    private fun getDistance(x : Int, y : Int) : Int {
        return abs(x - screenX / 2) + abs(y - screenY / 2)
    }

    private fun checkIsEnd() {
        if (hp <= 0){
            (context as GameActivity).goToResult(false)
        }

        if (count >= endTime) {
            (context as GameActivity).goToResult(true, score)
        }
    }

}