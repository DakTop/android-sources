package com.example.mvp.view;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mvp.R;
import com.example.mvp.presenter.MainActivityPresenterImpl;
import com.example.mvp.presenter.interfaces.view.MainActivityCallback;
import com.example.mvp.view.base.BaseActivity;

public class MainActivity extends BaseActivity implements MainActivityCallback, View.OnClickListener {

    private EditText etName;
    private EditText etPwd;
    private Button btnLogin;
    private MainActivityPresenterImpl mainActivityPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //
        mainActivityPresenter = new MainActivityPresenterImpl(this);
        //
        etName = findViewById(R.id.et_name);
        etPwd = findViewById(R.id.et_pwd);
        btnLogin = findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(this);
        //
        mainActivityPresenter.init();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                mainActivityPresenter.loginClick(etName.getText().toString().trim(), etPwd.getText().toString().trim());
                break;
        }
    }

    @Override
    public void loginSuccess(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void loginFail(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        mainActivityPresenter.onDestory();
        mainActivityPresenter = null;
        super.onDestroy();
    }
}
