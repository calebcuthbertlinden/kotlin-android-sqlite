package com.linden.learningkotlin.db

import android.provider.BaseColumns

val DATABASE_NAME = "habittrainer.db"
val DATABASE_VERSION = 24

object HabitEntry : BaseColumns {

    val TABLE_NAME = "habit"
    val _ID = "id"
    val DESCR_COL = "description"
    val TITLE_COL = "title"
    val IMAGE_COL = "image"
}