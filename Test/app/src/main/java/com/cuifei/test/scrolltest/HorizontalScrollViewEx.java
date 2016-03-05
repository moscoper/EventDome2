package com.cuifei.test.scrolltest;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Scroller;

/**
 * Created by cuifei on 16/2/23.
 */
public class HorizontalScrollViewEx extends LinearLayout {

    private  int mChildrenSize;
    private  int mChildrenWidth;
    private  int mChildrenIndex;

    private int mLastX = 0;
    private int mLastY = 0;

    private int mLastXIntercept = 0;
    private int mLastYIntercept=  0;

    private Scroller mScroll;

    private VelocityTracker mVelocityTracker;


//    int mLastEventX;
//    int mLastEventY;
//    Context mContext;

//    Scroller scroller;



    public HorizontalScrollViewEx(Context context) {
        super(context);
        init();
    }

    public HorizontalScrollViewEx(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public HorizontalScrollViewEx(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        if (mScroll == null){
            mScroll = new Scroller(getContext());
            mVelocityTracker = VelocityTracker.obtain();
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        boolean inrtercepter = false;
        int x = (int) ev.getX();
        int y = (int) ev.getY();
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                inrtercepter = false;
                if (!mScroll.isFinished()){
                    mScroll.abortAnimation();
                    inrtercepter = true;
                }
                break;
            case MotionEvent.ACTION_MOVE:

                int detalX = x - mLastXIntercept;
                int detalY = y - mLastYIntercept;

                if (Math.abs(detalX) > Math.abs(detalY)){
                    inrtercepter = true;
                }else {
                    inrtercepter = false;
                }



                break;
            case MotionEvent.ACTION_UP:
                inrtercepter = false;
                break;
        }

        mLastX =x;
        mLastY = y;
        mLastYIntercept = y;
        mLastXIntercept = x;

        return inrtercepter;
//        return super.onInterceptTouchEvent(ev);
    }

    int x  ;
    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        int x = (int) ev.getX();
        int y = (int) ev.getY();
        switch (ev.getAction()){

            case MotionEvent.ACTION_DOWN:
                if (mScroll.isFinished()){
                    mScroll.abortAnimation();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                int deltaX = x - mLastX;
                int deltaY = y - mLastY;
                scrollBy(-deltaX,0);
                System.out.println("======onTouchEvent==x==="+x);
                break;
            case MotionEvent.ACTION_UP:

                int scrollX = getScrollX();
                mVelocityTracker.computeCurrentVelocity(1000);
                float xVelocity = mVelocityTracker.getXVelocity();
                if (Math.abs(xVelocity) >= 50){
                    mChildrenIndex = xVelocity>0?mChildrenIndex - 1:mChildrenIndex+1;
                }else{
                    mChildrenIndex = (scrollX + mChildrenWidth /2)/mChildrenWidth;

                }

                mChildrenIndex = Math.max(0,Math.min(mChildrenIndex,mChildrenSize - 1));
                int dx = mChildrenIndex * mChildrenWidth - scrollX;
                smoothScrollTo(dx,0);
                mVelocityTracker.clear();

                break;
        }

        mLastX = x;
        mLastY = y;

        return true;
    }

    @Override
    public void computeScroll() {
//        super.computeScroll();
        if (mScroll.computeScrollOffset()){
            scrollTo(mScroll.getCurrX(), mScroll.getCurrY());
            postInvalidate();
        }

    }

    private void smoothScrollTo(int destX , int destY){
        mScroll.startScroll(getScrollX(), 0 ,destX,0,500);
        invalidate();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int measuredWidth = 0;
        int measuredHeight = 0;

        final int childCount = getChildCount();
        measureChildren(widthMeasureSpec,heightMeasureSpec);

        int widthSpaceSize = MeasureSpec.getSize(widthMeasureSpec);

        int widthSpaceMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSpaceSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightSpaceMode = MeasureSpec.getMode(heightMeasureSpec);

        if (childCount == 0){
            setMeasuredDimension(0,0);
        }else if (widthSpaceMode == MeasureSpec.AT_MOST && heightSpaceMode == MeasureSpec.AT_MOST){
            final View childView = getChildAt(0);
            measuredHeight = childView.getMeasuredHeight();
            measuredWidth = childCount * childView.getMeasuredWidth();

            setMeasuredDimension(measuredWidth,measuredHeight);
        }else if (heightSpaceMode == MeasureSpec.AT_MOST){
            final View childView = getChildAt(0);
            measuredHeight = childView.getMeasuredHeight();
//            measuredWidth = childCount * childView.getMeasuredWidth();

            setMeasuredDimension(widthSpaceSize,measuredHeight);
        }else if (widthSpaceMode == MeasureSpec.AT_MOST){
            final View childView = getChildAt(0);
//            measuredHeight = childView.getMeasuredHeight();
            measuredWidth = childCount * childView.getMeasuredWidth();

            setMeasuredDimension(measuredWidth,heightSpaceSize);
        }

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childLeft = 0;
        final int childCcount = getChildCount();

        mChildrenSize = childCcount;

        for (int i = 0; i< childCcount;i++){
            final View childView = getChildAt(i);
            if (childView.VISIBLE != View.GONE){
                final int childWidth = childView.getMeasuredWidth();
                mChildrenWidth = childWidth;

                childView.layout(childLeft,0,childLeft + childWidth,childView.getMeasuredHeight());

                childLeft += childWidth;
            }
        }
    }
}






















