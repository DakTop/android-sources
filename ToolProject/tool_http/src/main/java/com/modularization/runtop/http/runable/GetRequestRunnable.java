package com.modularization.runtop.http.runable;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.modularization.runtop.http.MainActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * Created by runTop on 2018/3/14.
 */

public class GetRequestRunnable implements Runnable {
    private Handler handler;

    public GetRequestRunnable(Handler handler) {
        this.handler = handler;
    }

    @Override
    public void run() {
        HttpURLConnection connection = null;
        try {
            URL url = new URL("https://www.baidu.com");
            //第一步、调用URL类中openConnection方法获得HttpURLConnection对象。
            connection = (HttpURLConnection) url.openConnection();
            //第二步、使用下面方法设置满意的请求属性包括请求头信息。
            connection.setRequestMethod("GET");//设置请求方式
            connection.setConnectTimeout(8000);//设置链接超时时间
            connection.setReadTimeout(8000);//设置读取的超时时间
            //第三步、调用connect方法链接远程资源,该方法还可用于向服务器查询头信息（header information）。
            connection.setAllowUserInteraction(false);
            connection.connect();
            //第四步、与服务器建立链接后可以查询头信息。getHeaderFields返回一个包含头部所有字段的map对象。
            Map<String, List<String>> headMap = connection.getHeaderFields();
            //第五步、访问资源数据，使用 getInputStream 方法获取一个输入流用以读取信息。
            InputStream in = connection.getInputStream();//获取服务器返回的输入流
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            //String str = stringBuilder.toString();//响应结果
            Message message = new Message();
            message.what = MainActivity.GetRequest;
            Bundle b = new Bundle();
            b.putString(MainActivity.GetResponse, "get请求响应code：" + connection.getResponseCode());
            message.setData(b);
            handler.sendMessage(message);
            in.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //关闭http链接
            if (connection != null)
                connection.disconnect();
        }
    }
}
