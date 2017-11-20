package com.work.project.uitool.viewdraghelper.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.work.project.uitool.R;

/**
 * 拖动效果
 */
public class ViewDragHelperContentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_drag_helper_content);
    }

    /**
     * 随手指移动的view
     *
     * @param view
     */
    public void moveWithFinger(View view) {
        startActivity(new Intent(this, MoveWithFingerActivity.class));
    }

    /**
     * 侧滑效果
     *
     * @param view
     */
    public void sideslipping(View view) {
        startActivity(new Intent(this, SideslipActivity.class));
    }
}
