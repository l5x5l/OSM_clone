package com.example.osm.game

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.example.osm.R

class Note(resources: Resources, direction : Int, size : Int = 200) {
    private val direction = direction
    var x = 0
    var y = 0
    val note_bitmap: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.pngwing_com)
    //note_bitmap.width/8
    val note : Bitmap = Bitmap.createScaledBitmap(note_bitmap, size, size, false)
    val width = note.width
    val height = note.height
    var stat = "none"

    fun getDirection() : Int{
        return direction
    }
}