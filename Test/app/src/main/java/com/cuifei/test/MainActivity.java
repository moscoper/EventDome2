package com.cuifei.test;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends Activity {
    public static String str ="second";
    public static String TAG = "MainActivity";
    private ListView listView;
    private ListView listView1;
    private ListView listView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.listView);
        listView1 = (ListView) findViewById(R.id.listView1);
        listView2 = (ListView) findViewById(R.id.listView2);

        creatList();

    }



    public void creatList(){
        ArrayList<String> datas = new ArrayList<String>();

        for (int i = 0; i < 50 ; i++){
            datas.add(i+" item");
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.list_item,R.id.name,datas);
        listView.setAdapter(adapter);
        listView1.setAdapter(adapter);
        listView2.setAdapter(adapter);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();


    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                Log.i(TAG, "======dispatchTouchEvent======ACTION_DOWN==");
                break;
            case MotionEvent.ACTION_MOVE:
                Log.i(TAG, "======dispatchTouchEvent======ACTION_MOVE==");
                break;
            case MotionEvent.ACTION_UP:
                Log.i(TAG, "======dispatchTouchEvent======ACTION_UP==");
                break;
        }
        return super.dispatchTouchEvent(ev);
    }
//
//
//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        switch (event.getAction()){
//            case MotionEvent.ACTION_DOWN:
//                Log.i(TAG, "======onTouchEvent======ACTION_DOWN==");
//                break;
//            case MotionEvent.ACTION_MOVE:
//                Log.i(TAG, "======onTouchEvent======ACTION_MOVE==");
//                break;
//            case MotionEvent.ACTION_UP:
//                Log.i(TAG, "======onTouchEvent======ACTION_UP==");
//                break;
//        }
//        return super.onTouchEvent(event);
//    }



}
