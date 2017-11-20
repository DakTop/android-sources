package com.work.project.uitool.viewdraghelper.layout;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

/**
 * 侧滑效果
 * Created by runTop on 2017/11/16.
 */
public class SideslipLayout extends FrameLayout {
    private ViewDragHelper viewDragHelper;
    //侧滑的范围
    private int range = 0;
    private View viewContent;
    private View viewMenu;
    private boolean menuOpen = false;

    public SideslipLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        viewDragHelper = ViewDragHelper.create(this, 1.0f, new ViewDragHelperCallbck());
        viewDragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_LEFT);
    }

    private class ViewDragHelperCallbck extends ViewDragHelper.Callback {

        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return true;
        }

        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            if (left <= 0 )
                return 0;
            else if (left >= range) {
                return range;
            } else {
                return left;
            }
        }

        @Override
        public int getViewHorizontalDragRange(View child) {
            return range;
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            float currX = releasedChild.getX();
            if (currX > range * 0.3) {
                viewDragHelper.settleCapturedViewAt(range, releasedChild.getTop());
            } else {
                viewDragHelper.settleCapturedViewAt(0, releasedChild.getTop());
            }
            invalidate();
        }

        @Override
        public void onEdgeDragStarted(int edgeFlags, int pointerId) {
            viewDragHelper.captureChildView(viewMenu, pointerId);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        range = (int) (w * 0.7);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return viewDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        viewDragHelper.processTouchEvent(event);
        return true;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        viewMenu = getChildAt(0);
        viewContent = getChildAt(1);
    }

    @Override
    public void computeScroll() {
        if (viewDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }
}
