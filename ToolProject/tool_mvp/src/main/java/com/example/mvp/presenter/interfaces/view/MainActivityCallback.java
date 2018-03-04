package com.example.mvp.presenter.interfaces.view;

import com.example.mvp.presenter.base.IViewCallback;

/**
 * 需要相应的Activity来实现此接口,处理业务逻辑层的presenter处理完数据，
 * 通过调用此接口中的方法把数据传递给View。
 * Created by runTop on 2018/3/2.
 */
public interface MainActivityCallback extends IViewCallback {

    void loginSuccess(String msg);

    void loginFail(String msg);
}
