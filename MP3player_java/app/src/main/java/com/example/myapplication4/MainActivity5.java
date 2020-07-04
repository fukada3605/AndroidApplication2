package com.example.myapplication4;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity5 extends AppCompatActivity {
    final Context context=this;
    Button mp3btn;
    Button wavbtn;
    Intent intent2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_file_type);
        mp3btn=findViewById(R.id.button);
        wavbtn= findViewById(R.id.button2);
        mp3btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSend(mp3btn);
            }
        });
        wavbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSend(wavbtn);
            }
        });
    }

    public void onSend(Button button){
        intent2=new Intent(getApplication(), FileList.class);;
        intent2.putExtra("EndName",button.getText());
        startActivity(intent2);
    }

    protected void onDestroy() {
        super.onDestroy();
        finish();
    }

    //メニュー表示
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case R.id.menu_help:
                break;
            case R.id.menu_back:
                finish();
                break;
        }
        return true;
    }
}