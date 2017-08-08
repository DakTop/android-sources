package com.toolproject.runtop.myapplication;


import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class SecondActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        Bundle b = getIntent().getExtras();
        Toast.makeText(this, b.getString("Type"), Toast.LENGTH_SHORT).show();
    }

    public void exitSign(View v) {
        MainActivity.is_login = false;
        finish();
    }
}
