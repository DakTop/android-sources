package com.example.mvp.presenter.interfaces.control;

import com.example.mvp.presenter.base.IPresenter;

/**
 * Created by runTop on 2018/3/2.
 */
public interface MainActivityPresenter extends IPresenter {
    void init();

    void onDestory();

    void loginClick(String name, String pwd);

}
