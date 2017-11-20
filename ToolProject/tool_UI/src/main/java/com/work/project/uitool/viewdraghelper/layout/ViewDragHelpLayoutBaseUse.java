package com.work.project.uitool.viewdraghelper.layout;

import android.content.Context;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

/**
 * ViewDragHelp使用
 * 更详细可参考：
 * https://juejin.im/entry/5768e7f1816dfa005475f2d0
 * http://www.cnblogs.com/lqstayreal/p/4500219.html
 * Created by runTop on 2017/11/14.
 */
public class ViewDragHelpLayoutBaseUse extends ViewGroup {
    private TextView tvOne;
    private TextView tvTwo;
    private ViewDragHelper viewDragHelper;

    public ViewDragHelpLayoutBaseUse(Context context, AttributeSet attrs) {
        super(context, attrs);

        /**
         * 第一个参数必须是当前自定义的ViewGroup
         * 第二个参数代表能够识别的最小的滑动距离，一般设置为1.0f，也可以取系统的值ViewConfiguration.get(context).getScaledTouchSlop();
         * 第三个参数是各种手势操作的回调,用来处理拖动的位置等相关操作
         */
        viewDragHelper = ViewDragHelper.create(this, 1.0f, new ViewDragHelperCallbck());
        viewDragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_ALL);
    }

    private class ViewDragHelperCallbck extends ViewDragHelper.Callback {
        /**
         * 传递当前触摸的子View实例，如果当前的子View需要进行拖拽移动返回true
         *
         * @param child
         * @param pointerId
         * @return 直接返回true，拦截所有的VIEW
         */
        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return true;
        }

        /**
         * left就代表子view的横向实时移动的坐标。返回值就是最终确定的移动的位置。
         * 我们要让view滑动的范围在我们的layout之内
         * 实际上就是判断如果这个坐标在layout之内 那我们就返回这个坐标值。
         * 如果这个坐标在layout的边界处 那我们就只能返回边界的坐标给他。不能让他超出这个范围
         * 除此之外就是如果你的layout设置了padding的话，也可以让子view的活动范围在padding之内的.
         *
         * @param child
         * @param left
         * @param dx    代表在X轴上拖拽的距离
         * @return
         */
        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            //取得左边界的坐标
            final int leftBound = getPaddingLeft();
            //取得右边界的坐标
            final int rightBound = getWidth() - child.getWidth() - leftBound;
            //这个地方的含义就是 如果left的值 在leftbound和rightBound之间 那么就返回left
            //如果left的值 比 leftbound还要小 那么就说明 超过了左边界 那我们只能返回给他左边界的值
            //如果left的值 比rightbound还要大 那么就说明 超过了右边界，那我们只能返回给他右边界的值
            return Math.min(Math.max(left, leftBound), rightBound);
        }

        /**
         * 纵向原理同横向
         */
        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            final int topBound = getPaddingTop();
            final int bottomBound = getHeight() - child.getHeight() - topBound;
            return Math.min(Math.max(top, topBound), bottomBound);
        }

        /**
         * ACTION_DOWN或ACTION_POINTER_DOWN事件发生时如果触摸到监听的边缘会调用此方法。
         * edgeFlags的取值为EDGE_LEFT、EDGE_TOP、EDGE_RIGHT、EDGE_BOTTOM的组合。
         *
         * @param edgeFlags
         * @param pointerId
         */
        @Override
        public void onEdgeTouched(int edgeFlags, int pointerId) {
        }

        /**
         * ACTION_MOVE事件发生时，检测到开始在某些边缘有拖动的手势（这里的边缘是指距离边缘一段固定的距离，在这个距离内的滑动才能触发这个方法的回调），
         * 并且没有锁定边缘，会调用此方法，只会调用一次。
         * edgeFlags取值为EDGE_LEFT、EDGE_TOP、EDGE_RIGHT、EDGE_BOTTOM的组合。
         * 可在此手动调用captureChildView()触发从边缘拖动子View的效果，可以完成侧滑功能。
         *
         * @param edgeFlags
         * @param pointerId
         */
        @Override
        public void onEdgeDragStarted(int edgeFlags, int pointerId) {
            viewDragHelper.captureChildView(tvOne, pointerId);
        }

        /**
         * 返回true表示锁定edgeFlags对应的边缘，锁定后的那些边缘在ACTION_MOVE事件发生时，会过滤掉不正常的拖动手势。
         * 举个左侧侧滑的例子：假如在此方法中锁定了左侧侧滑（即判断edgeFlags为EDGE_LEFT，则返回true），当手指从左侧向右侧滑动时，
         * 在边缘的侧滑的有效固定距离内，假如在Y轴上滑动的距离大于x轴上滑动距离，则不会回调onEdgeDragStarted()方法，
         * 这样就过滤掉了错误的侧滑手势。更详细可以参看ViewDragHelper文档解释图。
         * 默认返回false不锁定给定的边缘。edgeFlags的取值为EDGE_LEFT、EDGE_TOP、EDGE_RIGHT、EDGE_BOTTOM其中之一。
         *
         * @param edgeFlags
         * @return
         */
        @Override
        public boolean onEdgeLock(int edgeFlags) {
            if (edgeFlags == ViewDragHelper.EDGE_LEFT)
                return true;
            else return false;
        }

        /**
         * 拖动状态改变时会调用此方法，状态state有STATE_IDLE、STATE_DRAGGING、STATE_SETTLING三种取值。
         *
         * @param state
         */
        @Override
        public void onViewDragStateChanged(int state) {
            switch (state) {
                case ViewDragHelper.STATE_IDLE://所有的View处于静止空闲状态
                    break;
                case ViewDragHelper.STATE_DRAGGING://某个View正在被用户拖动（用户正在与设备交互）
                    break;
                case ViewDragHelper.STATE_SETTLING://某个View正在安置状态中（用户并没有交互操作），就是自动滚动的过程
                    break;
            }
        }

        /**
         * 正在被拖动的View或者自动滚动的View的位置改变时会调用此方法。
         *
         * @param changedView
         * @param left        changedView位置改变后，最终的x坐标
         * @param top         changedView位置改变后，最终的y坐标
         * @param dx          changedView位置改变后，最终x轴上移动的距离
         * @param dy          changedView位置改变后，最终y轴上移动的距离
         */
        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {

        }

        /**
         * tryCaptureViewForDrag()成功捕获到子View时会调用此方法。
         *
         * @param capturedChild
         * @param activePointerId
         */
        @Override
        public void onViewCaptured(View capturedChild, int activePointerId) {

        }

        /**
         * 拖动View松手时（processTouchEvent()的ACTION_UP）或被父View拦截事件时（processTouchEvent()的ACTION_CANCEL）会调用此方法。
         * xvel/yvel为离开屏幕时各方向每秒运动的速率，单位px
         *
         * @param releasedChild
         * @param xvel
         * @param yvel
         */
        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {

        }

        /**
         * 返回给定的child在相应的方向上可以被拖动的最远距离，默认返回0。
         * ACTION_DOWN发生时，若触摸点处的child消费了事件(比如child设置了点击事件)，
         * 并且想要在某个方向上可以被拖动，就要在对应方法里返回大于0的数。它与clampViewPositionHorizontal
         * 方法是有区别的，clampViewPositionHorizontal方法是实时监听被拖动的child的位置，并检测是否是否到达边界。
         *
         * @param child
         * @return
         */
        @Override
        public int getViewHorizontalDragRange(View child) {
            return getWidth();
        }

        /**
         * 同getViewHorizontalDragRange
         *
         * @param child
         * @return
         */
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
        tvOne = new TextView(getContext());
        //这里由于子View设置了点击事件，所以要想使tvOne也能拖动
        // 则必须设置getViewHorizontalDragRange和getViewVerticalDragRange
        //的返回值大于0，这里设置的是父容器的宽高
        tvOne.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "我是可以点击的", Toast.LENGTH_SHORT).show();
            }
        });
        tvTwo = new TextView(getContext());
        this.addView(tvOne);
        this.addView(tvTwo);
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
