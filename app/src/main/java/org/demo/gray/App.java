package org.demo.gray;

import android.app.Application;
import android.util.Log;
import org.demo.handler.Looper;

/**
 * create by TIAN FENG on 2020/4/9
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepareMainLooper();

                Log.e("App", "测试 线程  run: " + Thread.currentThread().getName());

                Looper.loop();

                Log.e("App", "线程退出");

            }
        },"main").start();
    }
}
