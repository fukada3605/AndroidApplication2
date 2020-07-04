package com.example.myapplication4

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.io.IOException
import java.util.*

class SubActivity : AppCompatActivity() {
    //楽曲変更ボタン
    var skipBtn: Button? = null
    var backBtn: Button? = null
    var n = 0

    //再生ボタン
    var playBtn: Button? = null
    var positionBar = arrayOfNulls<SeekBar>(1000)
    var volumeBar: SeekBar? = null
    var textmp: TextView? = null
    var elapsedTimeLabel = arrayOfNulls<TextView>(1000)
    var remainingTimeLabel = arrayOfNulls<TextView>(1000)

    //mp配列に変更し複数の曲に対応
    var mp = arrayOfNulls<MediaPlayer>(1000)
    var totalTime = IntArray(1000)
    var name_1 = arrayOfNulls<String>(1000)
    var music = arrayOfNulls<String>(1000)
    var nameMp: String? = null
    var mpList: List<String> = ArrayList()
    var number = 0
    var i = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sub)
        nameMp = intent.getStringExtra("Text")
        mpList = intent.getStringArrayListExtra("list")
        playBtn = findViewById(R.id.playBtn)
        skipBtn = findViewById(R.id.skipBtn)
        backBtn = findViewById(R.id.backBtn)
        textmp = findViewById(R.id.mpname)
        playBtn?.setBackgroundResource(R.drawable.stop)
        for (str in mpList) {
            try {
                name_1[i] = str
                music[i] = setMusic(name_1[i])
                mp[i] = MediaPlayer()
                mp[i]!!.setDataSource(name_1[i])
                mp[i]!!.prepare()
                mp[i]!!.isLooping = true
                mp[i]!!.seekTo(0)
                mp[i]!!.setVolume(0.5f, 0.5f)
                totalTime[i] = mp[i]!!.duration
                positionBar[i] = findViewById(R.id.positionBar)
                positionBar[i]?.setMax(totalTime[i])
                remainingTimeLabel[i] = findViewById(R.id.remainingTimeLabel)
                elapsedTimeLabel[i] = findViewById(R.id.elapsedTimeLabel)
                i++
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        while (nameMp != name_1[number]) {
            number++
        }
        mp[number]!!.start()
        backBtn?.setOnClickListener(View.OnClickListener {
            mp[number]!!.pause()
            if (number <= 0) {
                number = i - 1
            } else {
                number--
            }
            mp[number]!!.seekTo(0)
            mp[number]!!.start()
            playBtn?.setBackgroundResource(R.drawable.stop)
        })
        skipBtn?.setOnClickListener(View.OnClickListener {
            mp[number]!!.pause()
            if (number >= i - 1) {
                number = 0
            } else {
                number++
            }
            mp[number]!!.seekTo(0)
            mp[number]!!.start()
            playBtn?.setBackgroundResource(R.drawable.stop)
        })
        positionBar[number]!!.setOnSeekBarChangeListener(
                object : OnSeekBarChangeListener {
                    override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                        if (fromUser) {
                            mp[number]!!.seekTo(progress)
                            positionBar[number]!!.progress = progress
                        }
                    }

                    override fun onStartTrackingTouch(seekBar: SeekBar) {}
                    override fun onStopTrackingTouch(seekBar: SeekBar) {}
                }
        )
        // 音量調節
        volumeBar = findViewById(R.id.volumeBar)
        volumeBar?.setOnSeekBarChangeListener(
                object : OnSeekBarChangeListener {
                    override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                        val volumeNum = progress / 100f
                        mp[number]!!.setVolume(volumeNum, volumeNum)
                    }

                    override fun onStartTrackingTouch(seekBar: SeekBar) {}
                    override fun onStopTrackingTouch(seekBar: SeekBar) {}
                }
        )

        // Thread (positionBar・経過時間ラベル・残り時間ラベルを更新する)
        Thread(Runnable {
            while (mp[number] != null) {
                try {
                    val msg = Message()
                    msg.what = mp[number]!!.currentPosition
                    handler.sendMessage(msg)
                    Thread.sleep(1000)
                } catch (e: InterruptedException) {
                }
            }
        }).start()
    }

    var handler = Handler(Handler.Callback { msg ->
        textmp!!.text = music[number]
        val currentPosition = msg.what
        // 再生位置を更新
        positionBar[number]!!.progress = currentPosition
        // 経過時間ラベル更新
        val elapsedTime = createTimeLabel(currentPosition)
        elapsedTimeLabel[number]!!.text = elapsedTime

        // 残り時間ラベル更新
        val remainingTime = createTimeLabel(totalTime[number])
        remainingTimeLabel[number]!!.text = remainingTime
        positionBar[number]!!.max = totalTime[number]
        true
    })

    fun createTimeLabel(time: Int): String {
        var timeLabel: String? = ""
        val min = time / 1000 / 60
        val sec = time / 1000 % 60
        timeLabel = "$min:"
        if (sec < 10) timeLabel += "0"
        timeLabel += sec
        return timeLabel
    }

    fun playBtnClick(view: View?) {
        if (!mp[number]!!.isPlaying) {
            // 停止中
            mp[number]!!.start()
            playBtn!!.setBackgroundResource(R.drawable.stop)
        } else {
            // 再生中
            mp[number]!!.pause()
            playBtn!!.setBackgroundResource(R.drawable.play)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mp[number] != null) {
            mp[number]!!.pause()
        }
        finish()
    }

    //Pathから音楽ファイル名( xxx.mp3 など)を抽出
    fun setMusic(str: String?): String? {
        var str = str
        var len = str!!.length
        if (len == 0) return null
        val ar = str.split("").toTypedArray()
        len--
        while (true) {
            if (ar[len] == "/") {
                break
            }
            len--
        }
        str = str.substring(len)
        return str
    }

    //メニュー表示
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_settings -> {
            }
            R.id.menu_help -> {
            }
            R.id.menu_back -> finish()
        }
        return true
    }

    companion object {
        var TAG = "Test menu"
    }
}