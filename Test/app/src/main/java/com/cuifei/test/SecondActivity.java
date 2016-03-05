package com.cuifei.test;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.cuifei.test.scrolltest.CoustemScrollView;

import java.util.ArrayList;

/**
 * Created by cuifei on 16/1/10.
 */
public class SecondActivity extends Activity implements CoustemScrollView.GiveUpOnTouchEventListener{

    private ListView listView;
    private CoustemScrollView coustemScrollView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_layut);
        coustemScrollView = (CoustemScrollView) findViewById(R.id.custemScroll);
        coustemScrollView.setGiveUpOnTouchEventListener(this);
        listView = (ListView) findViewById(R.id.content);
        creatList();

    }
    public void creatList(){
        ArrayList<String> datas = new ArrayList<String>();

        for (int i = 0; i < 150 ; i++){
            datas.add(i+" item");
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.list_item,R.id.name,datas);
        listView.setAdapter(adapter);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        android.os.Process.killProcess(android.os.Process.myPid());
    }

    @Override
    public boolean onGiveUpOnTouchEvent(MotionEvent event) {

        if (listView.getFirstVisiblePosition() == 0){
            View view = listView.getChildAt(0);
            if (view != null && view.getTop()>=0){
                return true;
            }
        }
        return false;
    }
}
