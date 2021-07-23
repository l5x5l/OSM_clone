package com.example.osm.game

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.example.osm.R

class Center(resources: Resources, size : Int) {
    val center_bitmap : Bitmap = BitmapFactory.decodeResource(resources, R.drawable.center_thick)
    val centerSize = (size * 1.3).toInt()
    val center : Bitmap = Bitmap.createScaledBitmap(center_bitmap, centerSize, centerSize, false)
    var width = center.width
    var height = center.height
}