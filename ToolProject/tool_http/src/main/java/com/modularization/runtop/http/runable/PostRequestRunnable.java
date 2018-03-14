package com.modularization.runtop.http.runable;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.modularization.runtop.http.MainActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

/**
 * Created by runTop on 2018/3/14.
 */
public class PostRequestRunnable implements Runnable {
    private Handler handler;

    public PostRequestRunnable(Handler handler) {
        this.handler = handler;
    }

    @Override
    public void run() {
        HttpURLConnection connection = null;
        try {
            URL url = new URL("https://www.baidu.com");
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");//设置请求方式
            connection.setConnectTimeout(8000);//设置链接超时时间
            connection.setReadTimeout(8000);//设置读取的超时时间
            connection.setUseCaches(false);//post请求不能缓存，需要手动设置为false
            connection.setDoOutput(true);//默认是只能向服务器获取内容，不能向服务器输出内容，这里需要设置为true
            //在使用 POST 命令时，并不需要在 URL 中添加任何参数，而是从 URLConnection 中获取输出流，
            // 并将"名-值"对写入该流中。当然，仍然需要对这些值进行URL编码，并用&字符将它们隔开。
            OutputStream out = connection.getOutputStream();
            String data = "passwd=" + URLEncoder.encode("123", "UTF-8") +
                    "&number=" + URLEncoder.encode("456", "UTF-8");
            out.write(data.getBytes());
            out.flush();
            out.close();
            connection.connect();
            //
            Map<String, List<String>> headMap = connection.getHeaderFields();
            InputStream in = connection.getInputStream();//获取服务器返回的输入流
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
//            response = stringBuilder.toString();
            Message message = new Message();
            message.what = MainActivity.PostRequest;
            Bundle b = new Bundle();
            b.putString(MainActivity.PostResponse, "post请求响应code：" + connection.getResponseCode());
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
