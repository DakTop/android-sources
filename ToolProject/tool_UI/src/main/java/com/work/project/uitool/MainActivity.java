package com.work.project.uitool;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.work.project.uitool.viewdraghelper.activity.ViewDragHelperContentActivity;

/**
 * 各资源入口
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * ViewDragHelp内容
     *
     * @param view
     */
    public void jumpToViewDragHelp(View view) {
        this.startActivity(new Intent(this, ViewDragHelperContentActivity.class));
    }
}
