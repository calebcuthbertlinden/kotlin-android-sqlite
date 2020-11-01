package com.linden.learningkotlin

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.linden.learningkotlin.db.HabitDbTable
import kotlinx.android.synthetic.main.create_habit_layout.*
import java.io.IOException

class CreateHabitActivity : AppCompatActivity() {

    private val TAG = CreateHabitActivity::class.java.simpleName
    private val REQUEST_CODE: Int = 1
    private val DB_ERROR: Long = -1L
    private var bitmapImage: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.create_habit_layout)
    }

    fun storeHabit(view: View) {
        Log.e(TAG, "About to write to DB")
        if (habitTitle.text.isBlank() || habitDescription.text.isBlank()) {
            displayErrorMessage("There was an error saving your Habit")
            return
        }

        val habit = Habit(habitTitle.text.toString(), habitDescription.text.toString(), bitmapImage!!)
        Log.e(TAG, habit.title + habit.description)
        val id = HabitDbTable(this).store(habit)
        if (id == DB_ERROR) {
            displayErrorMessage("Habit could not be added to DB")
        } else {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    fun displayErrorMessage(message: String) {
        errorText.text = message
        errorText.visibility = View.VISIBLE
    }

    fun chooseImage(view: View) {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        val chooser = Intent.createChooser(intent, getString(R.string.choose_image_chooser))
        startActivityForResult(chooser, REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            try {
                bitmapImage = MediaStore.Images.Media.getBitmap(contentResolver, data.data)
                imagePreview.setImageBitmap(bitmapImage)
            } catch (e: IOException) {
                Log.e(TAG, "There is no bitmap")
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}
