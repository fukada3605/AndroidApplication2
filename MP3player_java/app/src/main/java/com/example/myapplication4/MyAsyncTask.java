package com.example.myapplication4;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.io.File;
import java.util.List;

public class MyAsyncTask extends AsyncTask<File, Void, String>   {
    Context context;
    String selectedItem;
    Listean listean;
    String endWITH="mp3";

    FileList filelist =new FileList();
    private int pt=21;

    public MyAsyncTask(Context context) {
        this.context = context;
    }
        @Override
        protected String doInBackground (File...params){
        pickOnDir(params[0]);
        return "hoge";
    }
    public void setEndWith(String str){
        endWITH=str;
    }

    public void pickOnDir (File dir){
        Log.d("情報","/sdcard/Download");
        File dir1=new File("/sdcard/Download");
        File[] files = dir1.listFiles();
        Log.d("情報","1");
        if (files != null) {
            Log.d("情報","2");
            for (int j = 0; j < files.length; j++) {
                if (files[j].isDirectory()) {
                    pickOnDir(files[j]);
                } else if (files[j].isFile()) {
                    if (files[j].getName().endsWith(endWITH)) {
                        filelist.pickedFileList.add(files[j].getAbsolutePath());
                    } else {
                    }
                } else {
                }
            }
            return;
        }
    }
    public void SETpt(int x){
        pt=x;
    }
    @Override
    protected void onPostExecute(String results) {

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.context,R.layout.listview, filelist.pickedFileList){

            public View getView(int position, View convertView, ViewGroup parent) {
                TextView view = (TextView)super.getView(position, convertView, parent);
                view.setTextSize( pt );
                return view;
            }

        };

        filelist.lv =  ((Activity) this.context).findViewById(R.id.listView);
        filelist.lv.setAdapter(adapter);

        filelist.lv.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 選択した項目をTextViewにキャストした後、Stringにキャストする
                selectedItem = (String)filelist.lv.getItemAtPosition(position);

                if(listean!=null){
                    listean.onSuccess(selectedItem,filelist.pickedFileList);
                }

            }

        });

    }
    void setListean(Listean listen) {
        this.listean = listen;
    }

    //インターフェースでつなぎの役割を担い
    //onSuccessメソッドでファイル名を送信
    interface Listean {
        void onSuccess(String fpstr ,List<String> list);
    }

}