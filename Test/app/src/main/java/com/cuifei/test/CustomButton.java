package com.cuifei.test;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.TextView;

/**
 * Created by cuifei on 16/2/17.
 */
public class CustomButton extends TextView {

    public static String TAG  ="CustomButton";
    public CustomButton(Context context) {
        super(context);
    }

    public CustomButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                Log.i(TAG, "======onTouchEvent======ACTION_DOWN==");
                break;
            case MotionEvent.ACTION_MOVE:
                Log.i(TAG, "======onTouchEvent======ACTION_MOVE==");
                break;
            case MotionEvent.ACTION_UP:
                Log.i(TAG, "======onTouchEvent======ACTION_UP==");
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        switch (event.getAction()){
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
        return super.dispatchTouchEvent(event);
    }

}
