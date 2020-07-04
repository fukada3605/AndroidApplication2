package com.example.myapplication4

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.io.File
import kotlin.collections.ArrayList

class FileList : AppCompatActivity() {
    private val sdPath = externalStoragePath
    var pickedFileList: List<String> = ArrayList()
    var lv: ListView? = null
    var isintent: Intent? = null
    var intent1: Intent? = null
    private var myAsyncTask: MyAsyncTask? = null
    val context: Context = this
    var menu1: Menu? = null
    private var filetype: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tv = findViewById(R.id.listTest)
        myAsyncTask = MyAsyncTask(this)
        myAsyncTask?.setListean(createListean())
        isintent = intent;
        filetype = isintent?.getStringExtra("EndName")
        myAsyncTask?.setEndWith(filetype.toString())
        Log.d("パッチ情報",File(sdPath).toString())
        myAsyncTask?.setPath(File(sdPath))
        myAsyncTask?.execute()
        try {
            Class.forName("android.os.AsyncTask")
        } catch (e: ClassNotFoundException) {
        }
    }

    override fun onDestroy() {
        myAsyncTask!!.setListean(null)
        super.onDestroy()
        finish()
    }

    private fun createListean(): Listean {
        return object :Listean(){
            override fun onSuccess(fpstr: String?, list: List<String?>?) {
                intent1 = Intent(context, SubActivity::class.java)
                intent1?.putExtra("Text", fpstr)
                intent1?.putStringArrayListExtra("list", list as ArrayList<String?>?)
                startActivity(intent1)
            }
        }
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
            R.id.ViewPt7 -> {
                myAsyncTask!!.SETpt(7)
                myAsyncTask!!.onPostExecute("true")
            }
            R.id.ViewPt14 -> {
                myAsyncTask!!.SETpt(14)
                myAsyncTask!!.onPostExecute("true")
            }
            R.id.ViewPt21 -> {
                myAsyncTask!!.SETpt(21)
                myAsyncTask!!.onPostExecute("true")
            }
            R.id.ViewPt28 -> {
                myAsyncTask!!.SETpt(28)
                myAsyncTask!!.onPostExecute("true")
            }
            R.id.ViewPt35 -> {
                myAsyncTask!!.SETpt(35)
                myAsyncTask!!.onPostExecute("true")
            }
            R.id.ViewPt42 -> {
                myAsyncTask!!.SETpt(42)
                myAsyncTask!!.onPostExecute("true")
            }
        }
        return true
    }

    companion object {
        var tv: TextView? = null
        var TAG = "Test menu"// MOTOROLA 対応
        // Samsung 対応
        // 旧 Samsung + 標準 対応
        // HTC 対応
        // その他機種

        // 外部ストレージ(SDカード)のパスを取得する。
        val externalStoragePath: String?
            get() {
                var path: String?
                // MOTOROLA 対応
                path = System.getenv("EXTERNAL_ALT_STORAGE")
                if (path != null) {
                    return path
                }
                // Samsung 対応
                path = System.getenv("EXTERNAL_STORAGE2")
                if (path != null) {
                    return path
                }
                // 旧 Samsung + 標準 対応
                path = System.getenv("EXTERNAL_STORAGE")
                if (path == null) {
                    path = Environment.getExternalStorageDirectory().path
                }
                // HTC 対応
                val file = File("$path/ext_sd")
                if (file.exists()) {
                    path = file.path
                }
                // その他機種
                return path
            }
    }
}