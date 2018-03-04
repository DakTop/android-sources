package com.example.mvp.presenter;

import android.text.TextUtils;

import com.example.mvp.model.User;
import com.example.mvp.presenter.interfaces.control.MainActivityPresenter;
import com.example.mvp.presenter.interfaces.view.MainActivityCallback;

/**
 * presenter层主要处理业务逻辑，写一些业务逻辑层的代码，真正的隔离了view层和model层。
 * 它与model层的联系比较简单，与view层的通信必须通过一个协议来完成，这个协议就是presenter层暴露给
 * View层的MainActivityPresenter接口，和View层暴露给presenter层的MainActivityCallback接口。这样就通过
 * 接口的形势将业务逻辑层和View层进行隔离，两者之间谁也不知道谁的实现，只是根据需要进行接口调用。
 *
 * 这个类进行业务逻辑的实现。而View层的更新是在Activity中。
 * <p>
 * Created by runTop on 2018/3/1.
 */
public class MainActivityPresenterImpl implements MainActivityPresenter {

    private MainActivityCallback mainActivityCallback;
    private User user;

    public MainActivityPresenterImpl(MainActivityCallback mainActivityCallback) {
        this.mainActivityCallback = mainActivityCallback;
    }

    @Override
    public void init() {
        user = new User();
    }


    @Override
    public void onDestory() {
        mainActivityCallback = null;
        user = null;
    }

    @Override
    public void loginClick(String name, String pwd) {
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(pwd)) {
            mainActivityCallback.loginFail("账号或密码不能为空");
        } else {
            //http请求....
            //解析数据....
            user.setId("123");
            user.setName("小明");
            mainActivityCallback.loginSuccess("用户名：" + user.getName() + "   id:" + user.getId());
        }
    }
}
