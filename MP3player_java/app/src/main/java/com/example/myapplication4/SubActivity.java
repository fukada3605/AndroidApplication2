package com.example.myapplication4;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SubActivity extends AppCompatActivity {

        //楽曲変更ボタン
        Button skipBtn;
        Button backBtn;

        int n=0;
        static String TAG ="Test menu";

        //再生ボタン
        Button playBtn;
        SeekBar positionBar[]=new SeekBar[1000];
        SeekBar volumeBar;
        TextView textmp;
        TextView elapsedTimeLabel[]=new TextView[1000];
        TextView remainingTimeLabel[]=new TextView[1000];

        //mp配列に変更し複数の曲に対応
        MediaPlayer mp[]=new MediaPlayer[1000];
        int totalTime[]=new int[1000];
        Intent intent;
        String name_1[]=new String[1000];
        String music[]=new String[1000];
        String nameMp;

        List <String> mpList=new ArrayList<String>();
        int number=0;
        int i=0;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_sub);

            intent=getIntent();
            nameMp=intent.getStringExtra("Text");
            mpList = intent.getStringArrayListExtra("list");

            playBtn = findViewById(R.id.playBtn);
            skipBtn = findViewById(R.id.skipBtn);
            backBtn = findViewById(R.id.backBtn);
            textmp= findViewById(R.id.mpname);
            playBtn.setBackgroundResource(R.drawable.stop);


            for(String str:mpList) {
                try {
                    name_1[i]=str;
                    music[i]=setMusic(name_1[i]);
                    mp[i]=new MediaPlayer();
                    mp[i].setDataSource(name_1[i]);
                    mp[i].prepare();
                    mp[i].setLooping(true);
                    mp[i].seekTo(0);
                    mp[i].setVolume(0.5f, 0.5f);
                    totalTime[i] = mp[i].getDuration();
                    positionBar[i] = findViewById(R.id.positionBar);
                    positionBar[i].setMax(totalTime[i]);
                    remainingTimeLabel[i] = findViewById(R.id.remainingTimeLabel);
                    elapsedTimeLabel[i] = findViewById(R.id.elapsedTimeLabel);
                    i++;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            while(!(nameMp.equals(name_1[number]))){
                number++;
            }
            mp[number].start();

            backBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    mp[number].pause();
                    if (number <= 0) {
                        number = i - 1;
                    } else {
                        number--;
                    }
                    mp[number].seekTo(0);
                    mp[number].start();
                    playBtn.setBackgroundResource(R.drawable.stop);

                }
            });
            skipBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    mp[number].pause();
                    if(!(number<i-1)) {
                        number = 0;
                    } else {
                        number++;
                    }
                    mp[number].seekTo(0);
                    mp[number].start();
                    playBtn.setBackgroundResource(R.drawable.stop);


                }
            });



            positionBar[number].setOnSeekBarChangeListener(
                    new SeekBar.OnSeekBarChangeListener() {
                        @Override
                        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                            if (fromUser) {
                                mp[number].seekTo(progress);
                                positionBar[number].setProgress(progress);
                            }
                        }

                        @Override
                        public void onStartTrackingTouch(SeekBar seekBar) {

                        }

                        @Override
                        public void onStopTrackingTouch(SeekBar seekBar) {

                        }
                    }
            );
            // 音量調節
            volumeBar = findViewById(R.id.volumeBar);
            volumeBar.setOnSeekBarChangeListener(
                    new SeekBar.OnSeekBarChangeListener() {
                        @Override
                        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                            float volumeNum = progress / 100f;
                            mp[number].setVolume(volumeNum, volumeNum);
                        }

                        @Override
                        public void onStartTrackingTouch(SeekBar seekBar) {

                        }

                        @Override
                        public void onStopTrackingTouch(SeekBar seekBar) {

                        }
                    }
            );

            // Thread (positionBar・経過時間ラベル・残り時間ラベルを更新する)
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (mp[number] != null) {
                        try {

                            Message msg = new Message();
                            msg.what = mp[number].getCurrentPosition();
                            handler.sendMessage(msg);
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {}
                    }
                }
            }).start();



        }

    Handler handler = new Handler(new Handler.Callback(){
            @Override
            public boolean handleMessage(Message msg) {

                textmp.setText(music[number]);
                int currentPosition = msg.what;
                // 再生位置を更新
                positionBar[number].setProgress(currentPosition);
                // 経過時間ラベル更新
                String elapsedTime = createTimeLabel(currentPosition);
                elapsedTimeLabel[number].setText(elapsedTime);

                // 残り時間ラベル更新
               String remainingTime = createTimeLabel(totalTime[number]);
                remainingTimeLabel[number].setText(remainingTime);

                positionBar[number].setMax(totalTime[number]);

                return true;
            }
        });


        public String createTimeLabel(int time) {
            String timeLabel = "";
            int min = time / 1000 / 60;
            int sec = time / 1000 % 60;

            timeLabel = min + ":";
            if (sec < 10) timeLabel += "0";
            timeLabel += sec;

            return timeLabel;
        }

        public void playBtnClick(View view) {
            if (!mp[number].isPlaying()) {
                // 停止中
                mp[number].start();
                playBtn.setBackgroundResource(R.drawable.stop);

            } else {
                // 再生中
                mp[number].pause();
                playBtn.setBackgroundResource(R.drawable.play);
            }
        }
        @Override
        protected void onDestroy() {
            super.onDestroy();
            if(mp[number]!=null){
                mp[number].pause();
            }
            finish();
        }

        //Pathから音楽ファイル名( xxx.mp3 など)を抽出
        public String setMusic(String str){
            int len=str.length();
            if (len==0)return null;
            String[] ar=str.split("");
            len--;
            while(true){
                if(ar[len].equals("/")){
                    break;
                }
                len--;
            }
            str=str.substring(len);
            return str;
        }

        //メニュー表示
        public boolean onCreateOptionsMenu(Menu menu){
            getMenuInflater().inflate(R.menu.main,menu);
            return true;
        }
        public boolean onOptionsItemSelected(MenuItem item){
            switch (item.getItemId()){
                case R.id.menu_settings:
                    break;
                case R.id.menu_help:
                    break;
                case R.id.menu_back:
                    finish();
                    break;
            }
            return true;
        }
}
