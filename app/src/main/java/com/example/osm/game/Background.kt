package com.example.osm.game

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.example.osm.R

class Background(screenx : Int, screeny : Int, res : Resources) {
    var x : Int = 0
    var y : Int = 0
    var background_bitmap : Bitmap = BitmapFactory.decodeResource(res, R.drawable.background_space)
    var background = Bitmap.createScaledBitmap(background_bitmap, screenx, screeny, false)

}