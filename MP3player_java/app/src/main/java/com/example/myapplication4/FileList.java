package com.example.myapplication4;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileList extends AppCompatActivity {
    private String sdPath = getExternalStoragePath();
    List<String> pickedFileList = new ArrayList<String>();
    ListView lv;
    Intent isintent;
    Intent intent1;
    private MyAsyncTask myAsyncTask;
    static TextView tv;
    final Context context=this;
    Menu menu1;
    private String filetype;


    static String TAG ="Test menu";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv=findViewById(R.id.listTest);
        myAsyncTask=new MyAsyncTask(this);
        myAsyncTask.setListean(createListean());
        isintent=getIntent();
        filetype=isintent.getStringExtra("EndName");
        myAsyncTask.setEndWith(filetype);
        myAsyncTask.execute(new File(sdPath));

        try{
            Class.forName("android.os.AsyncTask");
        }catch (ClassNotFoundException e){}

    }

    // 外部ストレージ(SDカード)のパスを取得する。
    public static String getExternalStoragePath() {
        String path;
        // MOTOROLA 対応
        path = System.getenv("EXTERNAL_ALT_STORAGE");
        if (path != null) {
            return path;
        }
        // Samsung 対応
        path = System.getenv("EXTERNAL_STORAGE2");
        if (path != null) {
            return path;
        }
        // 旧 Samsung + 標準 対応
        path = System.getenv("EXTERNAL_STORAGE");
        if (path == null) {
            path = Environment.getExternalStorageDirectory().getPath();
        }
        // HTC 対応
        File file = new File(path + "/ext_sd");
        if (file.exists()) {
            path = file.getPath();
        }
        // その他機種
        return path;
    }

    protected void onDestroy() {
        myAsyncTask.setListean(null);
        super.onDestroy();
        finish();
    }

    private MyAsyncTask.Listean createListean()  {

        return new MyAsyncTask.Listean() {
            @Override
            public void onSuccess(String fpstr,List<String> list) {
                intent1=new Intent(context,SubActivity.class);
                intent1.putExtra("Text",fpstr);
                intent1.putStringArrayListExtra("list", (ArrayList<String>) list);
                startActivity(intent1);
            }

        };

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
            case R.id.ViewPt7:
                myAsyncTask.SETpt(7);
                myAsyncTask.onPostExecute("true");
                break;
            case R.id.ViewPt14:
                myAsyncTask.SETpt(14);
                myAsyncTask.onPostExecute("true");
                break;
            case R.id.ViewPt21:
                myAsyncTask.SETpt(21);
                myAsyncTask.onPostExecute("true");
                break;
            case R.id.ViewPt28:
                myAsyncTask.SETpt(28);
                myAsyncTask.onPostExecute("true");
                break;
            case R.id.ViewPt35:
                myAsyncTask.SETpt(35);
                myAsyncTask.onPostExecute("true");
                break;
            case R.id.ViewPt42:
                myAsyncTask.SETpt(42);
                myAsyncTask.onPostExecute("true");
                break;

        }
        return true;
    }
}
