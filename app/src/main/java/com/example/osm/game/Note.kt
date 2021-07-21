package com.example.osm.game

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.example.osm.R

class Note(resources: Resources, direction : Int) {
    private val direction = direction
    var x = 0
    var y = 0
    val note_bitmap: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.pngwing_com)
    val note: Bitmap = Bitmap.createScaledBitmap(note_bitmap, note_bitmap.width/4, note_bitmap.height/4, false)
    val width = note.width/4
    val height = note.height/4
}