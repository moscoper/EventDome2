package com.cuifei.test.scrolltest;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.LinearLayout;

import java.util.NoSuchElementException;

/**
 * Created by cuifei on 16/3/5.
 */
public class CoustemScrollView extends LinearLayout {

  private static final String TAG = "CoustemScrollView";

  public interface GiveUpOnTouchEventListener {
    boolean onGiveUpOnTouchEvent(MotionEvent event);
  }

  private View headerView;
  private View contentView;

  private GiveUpOnTouchEventListener giveUpOnTouchEventListener;

  private int mLastX;
  private int mLastY;

  private int headerHeight;
  private int originalHeaderHeight;

  private int mStatus = STATUS_EXPANDED;
  public static final int STATUS_EXPANDED = 1;
  public static final int STATUS_COLLAPSED = 2;

  private int mtouchSlop;

  private int mInterceptX;
  private int mInterceptY;

  // 用来控制滑动角度，仅当角度a满足如下条件才进行滑动：tan a = deltaX / deltaY > 2
  private static final int TAN = 2;

  private boolean isInitData = false;
  private boolean isScroll = true;

  private boolean isDisAllowInterceptTouchEventOnHeade = true;

  public CoustemScrollView(Context context) {
    super(context);
  }

  public CoustemScrollView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public CoustemScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  public void setGiveUpOnTouchEventListener(GiveUpOnTouchEventListener listener) {
    giveUpOnTouchEventListener = listener;
  }

  public void setDisAllowInterceptTouchEventOnHeade(boolean isDisAllowInterceptTouchEventOnHeade) {
    this.isDisAllowInterceptTouchEventOnHeade = isDisAllowInterceptTouchEventOnHeade;
  }

  @Override public void onWindowFocusChanged(boolean hasWindowFocus) {
    super.onWindowFocusChanged(hasWindowFocus);

    if (hasWindowFocus && (headerView == null || contentView == null)) {
      initData();
    }
  }

  public int getHeaderHeight() {
    return headerHeight;
  }

  public void setHeaderHeight(int height) {

    if (!isInitData) {
      initData();
    }

    if (height < 0) {
      height = 0;
    } else if (height > originalHeaderHeight) {
      height = originalHeaderHeight;
    }

    if (height == 0) {
      mStatus = STATUS_COLLAPSED;
    } else {
      mStatus = STATUS_EXPANDED;
    }

    if (headerView != null && headerView.getLayoutParams() != null) {
      headerView.getLayoutParams().height = height;
      headerView.requestLayout();

      headerHeight = height;
    } else {
      Log.d(TAG, "null LayoutParams when setHeaderHeight");
    }
  }

  public void initData() {
    int headerId = getResources().getIdentifier("header", "id", getContext().getPackageName());
    int contentId = getResources().getIdentifier("content", "id", getContext().getPackageName());

    if (headerId != 0 && contentId != 0) {
      headerView = findViewById(headerId);
      contentView = findViewById(contentId);
      headerHeight = headerView.getHeight();
      originalHeaderHeight = headerHeight;
      mtouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();

      if (originalHeaderHeight > 0) {
        isInitData = true;
      }
    } else {
      throw new NoSuchElementException(
          "Did your view with id \\\"header\\\" or \\\"content\\\" exists?");
    }
  }

  @Override public boolean onInterceptTouchEvent(MotionEvent ev) {

    boolean intercept = false;

    int x = (int) ev.getX();
    int y = (int) ev.getY();
    switch (ev.getAction()) {

      case MotionEvent.ACTION_DOWN:

        mLastX = x;
        mLastY = y;
        mInterceptX = x;
        mInterceptY = y;
        intercept = false;

        break;
      case MotionEvent.ACTION_MOVE:
        int dealtX = x - mInterceptX;
        int dealtY = y - mInterceptY;

        if (isDisAllowInterceptTouchEventOnHeade && y < headerHeight) {
          intercept = false;
        } else if (Math.abs(dealtX) >= Math.abs(dealtY)) {
          intercept = false;
        } else if (mStatus == STATUS_EXPANDED && dealtY < -mtouchSlop) {
          intercept = true;
        } else if (giveUpOnTouchEventListener != null) {
          if (giveUpOnTouchEventListener.onGiveUpOnTouchEvent(ev) && dealtY > mtouchSlop) {
            intercept = true;
          }
        }

        break;
      case MotionEvent.ACTION_UP:
        intercept = false;
        mInterceptX = mInterceptY = 0;

        break;
    }

    return intercept && isScroll;
  }

  @Override public boolean onTouchEvent(MotionEvent event) {

    if (!isScroll) {
      return true;
    }
    int x = (int) event.getX();
    int y = (int) event.getY();

    switch (event.getAction()) {
      case MotionEvent.ACTION_DOWN:

        break;

      case MotionEvent.ACTION_MOVE:

        int detalX = x - mLastX;
        int detalY = y - mLastY;
        headerHeight = headerHeight + detalY;

        setHeaderHeight(headerHeight);
        break;

      case MotionEvent.ACTION_UP:

        int destHeight = 0;

        if (headerHeight < originalHeaderHeight * 0.5) {
          destHeight = 0;
          mStatus = STATUS_COLLAPSED;
        } else {
          destHeight = originalHeaderHeight;
          mStatus = STATUS_EXPANDED;
        }

        smoothSetHeaderHeight(headerHeight, destHeight, 500);

        break;
    }

    mLastX = x;
    mLastY = y;

    return true;
  }

  public int getOriginalHeaderHeight() {
    return originalHeaderHeight;
  }

  public void setOriginalHeaderHeight(int originalHeaderHeight) {
    this.originalHeaderHeight = originalHeaderHeight;
  }

  public void smoothSetHeaderHeight(final int from, final int to, long duration) {
    smoothSetHeaderHeight(from, to, duration, false);
  }

  public void smoothSetHeaderHeight(final int from, final int to, long duration,
      final boolean modifiyOriginHeaderHeight) {

    final int framCount = (int) ((duration / 1000.f * 30) + 1);

    final float paration = (to - from) / (float) framCount;

    new Thread("smoothSetHeaderHeight") {
      @Override public void run() {

        for (int i = 0; i < framCount; i++) {
          final int height;
          if (framCount == i - 1) {
            height = to;
          } else {
            height = (int) (from + (paration * i));
          }

          post(new Runnable() {
            @Override public void run() {
              setHeaderHeight(height);
            }
          });

          try {
            sleep(10);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        }

        if (modifiyOriginHeaderHeight) {
          setOriginalHeaderHeight(to);
        }
      }
    }.start();
  }


}



































