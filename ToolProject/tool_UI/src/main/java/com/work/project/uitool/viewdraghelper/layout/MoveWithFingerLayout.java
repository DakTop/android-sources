package com.work.project.uitool.viewdraghelper.layout;

import android.content.Context;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.work.project.uitool.R;

/**
 * 随手指移动的View
 * Created by runTop on 2017/11/14.
 */
public class MoveWithFingerLayout extends ViewGroup {
    private TextView tvOne;
    private TextView tvTwo;
    private ViewDragHelper viewDragHelper;

    public MoveWithFingerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        viewDragHelper = ViewDragHelper.create(this, 1.0f, new ViewDragHelperCallbck());
    }

    private class ViewDragHelperCallbck extends ViewDragHelper.Callback {

        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return true;
        }

        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            int leftBound = getPaddingLeft();
            int rightBound = getWidth() - child.getWidth() - leftBound;
            //限制tvOne的拖动范围
            if (tvOne == child) {
                leftBound -= 200;
                rightBound -= 200;
            }
            return Math.min(Math.max(left, leftBound), rightBound);
        }


        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            final int topBound = getPaddingTop();
            final int bottomBound = getHeight() - child.getHeight() - topBound;
            return Math.min(Math.max(top, topBound), bottomBound);
        }

        @Override
        public int getViewHorizontalDragRange(View child) {
            return getWidth();
        }

        @Override
        public int getViewVerticalDragRange(View child) {
            return getHeight();
        }
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
        tvOne = (TextView) findViewById(R.id.tvOne);
        //这里由于子View设置了点击事件，所以要想使tvOne也能拖动
        // 则必须设置getViewHorizontalDragRange和getViewVerticalDragRange
        //的返回值大于0，这里设置的是父容器的宽高
        tvOne.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "我是可以点击的", Toast.LENGTH_SHORT).show();
            }
        });
        tvTwo = (TextView) findViewById(R.id.tvTwo);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(heightMeasureSpec));
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        tvOne.layout(10, 10, tvOne.getMeasuredWidth() + 10, tvOne.getMeasuredHeight() + 10);
        tvTwo.layout(tvOne.getMeasuredWidth() + 20, 10, tvOne.getMeasuredWidth() + tvTwo.getMeasuredWidth() + 20, tvTwo.getMeasuredHeight() + 10);
    }
}
