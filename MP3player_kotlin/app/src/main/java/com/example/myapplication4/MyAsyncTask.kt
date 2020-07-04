package com.example.myapplication4

import android.app.Activity
import android.content.Context
import android.os.AsyncTask
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.TextView
import java.io.File

//インターフェースでつなぎの役割を担い
//onSuccessメソッドでファイル名を送信

class MyAsyncTask(var context: Context) : AsyncTask<File?, Void?, String>() {
    var selectedItem: String? = null
    var endWITH = "mp3"
    var filelist = FileList()
    var listean1 : Listean?=null
    var file:File?=null
    private var pt = 21
    override fun doInBackground(vararg params: File?): String? {
        pickOnDir(file)
        return "hoge"
    }

    fun setEndWith(str: String) {
        endWITH = str
        Log.d("拡張子情報",endWITH)
    }
    fun setPath(file: File):Unit{
        this.file=file
    }
    fun pickOnDir(dir: File?) {
        Thread.sleep(800)
        var files = dir?.listFiles()
        if (files != null) {
            for (j in files.indices) {
                Log.d("テスト","LOG")
                if (files[j].isDirectory) {
                    Thread.sleep(800)
                    pickOnDir(files[j])
                } else if (files[j].isFile) {
                    if (files[j].name.endsWith(endWITH)) {
                        Log.d("テスト","見つかった")
                        filelist.pickedFileList.toMutableList().add(files[j].absolutePath)
                    } else {
                    }
                } else {
                }
            }
            return
        }
    }

    fun SETpt(x: Int) {
        pt = x
    }

    public override fun onPostExecute(results: String) {
        val adapter: ArrayAdapter<String?> = object : ArrayAdapter<String?>(context, R.layout.listview, filelist.pickedFileList) {
            override fun getView(position: Int, convertView: View, parent: ViewGroup): View {
                val view = super.getView(position, convertView, parent) as TextView
                view.textSize = pt.toFloat()
                return view
            }
        }
        filelist.lv = (context as Activity).findViewById(R.id.listView)
        filelist.lv?.adapter = adapter
        filelist.lv?.onItemClickListener = OnItemClickListener { parent, view, position, id -> // 選択した項目をTextViewにキャストした後、Stringにキャストする
            selectedItem = filelist.lv?.getItemAtPosition(position) as String
            if (listean1 != null) {
                listean1!!.onSuccess(selectedItem, filelist.pickedFileList)
            }
        }
    }

    fun setListean(listean: Listean?) {
        listean1 = listean
    }
}
