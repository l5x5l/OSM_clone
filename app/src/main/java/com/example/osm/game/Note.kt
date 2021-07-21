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
    val note: Bitmap = Bitmap.createScaledBitmap(note_bitmap, note_bitmap.width/8, note_bitmap.height/8, false)
    val width = note.width
    val height = note.height
}