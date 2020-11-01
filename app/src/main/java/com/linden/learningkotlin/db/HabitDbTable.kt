package com.linden.learningkotlin.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.linden.learningkotlin.Habit
import com.linden.learningkotlin.db.HabitEntry.DESCR_COL
import com.linden.learningkotlin.db.HabitEntry.IMAGE_COL
import com.linden.learningkotlin.db.HabitEntry.TABLE_NAME
import com.linden.learningkotlin.db.HabitEntry.TITLE_COL
import com.linden.learningkotlin.db.HabitEntry._ID
import java.io.ByteArrayOutputStream

class HabitDbTable(context: Context) {

    private val TAG: String = "HabitDbTable"
    private val dbHelper = HabitTrainerDb(context)

    fun store(habit: Habit): Long {
        val db = dbHelper.writableDatabase

        val values = ContentValues()
        with(values) {
            put(TITLE_COL, habit.title)
            put(DESCR_COL, habit.description)
            put(IMAGE_COL, toByteArray(habit.image))
        }

        Log.e(TAG, "Writing to DB")
        return db.transaction {
            insert(TABLE_NAME, null, values)
        }
    }

    fun readAllHabits(): List<Habit> {
        val columns = arrayOf(_ID, TITLE_COL, DESCR_COL, IMAGE_COL)
        val order = "${_ID} ASC"
        val db = dbHelper.readableDatabase
        val cursor = db.doQuery(TABLE_NAME, columns, orderBy = order)
        return parseFromCursor(cursor)
    }

    private fun parseFromCursor(cursor: Cursor): MutableList<Habit> {
        val habits = mutableListOf<Habit>()
        while(cursor.moveToNext()) {
            val title = cursor.getString(TITLE_COL)
            val desc = cursor.getString(DESCR_COL)
            val bitmap = cursor.getBitmap(IMAGE_COL)
            habits.add(Habit(title, desc, bitmap))
        }
        cursor.close()
        return habits
    }

    private fun toByteArray(bitmap: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream)
        return stream.toByteArray()
    }
}

private fun SQLiteDatabase.doQuery(tableName: String, columns: Array<String>, selection: String? = null, selectionArgs: Array<String>? = null, groupBy: String? = null, having: String? = null, orderBy: String? = null): Cursor {
    return query(tableName, columns, selection, selectionArgs, groupBy, having, orderBy)
}

private fun Cursor.getString(columnName: String): String = this.getString(getColumnIndex(columnName))

private fun Cursor.getBitmap(columnName: String): Bitmap {
    val bytes = getBlob(getColumnIndex(columnName))
    return BitmapFactory.decodeByteArray(bytes, 0 , bytes.size)
}

private inline fun <T> SQLiteDatabase.transaction(function: SQLiteDatabase.() -> T): T {
    beginTransaction()
    val result = try {
        val returnValue = function()
        setTransactionSuccessful()
        returnValue
    } finally {
        endTransaction()
    }
    close()
    return result
}