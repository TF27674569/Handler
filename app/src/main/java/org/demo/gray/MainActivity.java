package org.demo.gray;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import org.demo.handler.Handler;
import org.demo.handler.Looper;
import org.demo.handler.Message;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends BaseActivity implements Handler.Callback {

    private static String TAG = "MainActivity";

    private volatile Handler mHandler = new Handler(Looper.getMainLooper(), this) {
        @Override
        public void handlerMessage(Message msg) {
            Log.e(TAG, "线程: " + Thread.currentThread().getName() + "    内容：" + msg.obj.toString());
        }
    };

    ExecutorService service = Executors.newFixedThreadPool(10);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    long time = 0;

    @Override
    public boolean handlerMessage(Message msg) {
        time = System.currentTimeMillis() - time;
        Log.e(TAG, " 内容：" + msg.obj.toString() + "   time:" + time);
        time = System.currentTimeMillis();
        return false;
    }


    int j = 0;

    public void click(View view) {
        for (int i = 0; i < 10; i++) {
            service.execute(new Runnable() {
                @Override
                public void run() {
                    Message message = new Message();
                    message.obj = " 第" + ++j + "条信息";

                    boolean b = mHandler.sendMessage(message);
                    Log.e(TAG, "添加消息: " + message.obj + "   isSuccess:" + b);

                }
            });
        }

        if (j >= 30) {
            Looper.getMainLooper().quit();
        }
    }
}
