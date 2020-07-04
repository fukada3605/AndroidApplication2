package com.example.myapplication4

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity5 : AppCompatActivity() {
    val context: Context = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.select_file_type)
        val mp3btn = findViewById<Button>(R.id.button)
        val wavbtn = findViewById<Button>(R.id.button2)
        mp3btn.setOnClickListener(View.OnClickListener { onSend(mp3btn) })
        wavbtn.setOnClickListener(View.OnClickListener { onSend(wavbtn) })
    }

    fun onSend(button: Button) {
        val intent = Intent(application, FileList::class.java)
        intent.putExtra("EndName", button.text)
        Log.d("入力情報",button.text.toString())
        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        finish()
    }

    //メニュー表示
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_help -> {
            }
            R.id.menu_back -> finish()
        }
        return true
    }
}