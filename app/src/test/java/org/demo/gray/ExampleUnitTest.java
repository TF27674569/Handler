package org.demo.gray;

import org.demo.handler.Handler;
import org.demo.handler.Looper;
import org.demo.handler.Message;
import org.demo.handler.Platform;
import org.demo.handler.PlatformUtil;

public class ExampleUnitTest implements Runnable {

    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handlerMessage(Message msg) {
            System.out.println("线程thread:" + Thread.currentThread().getName() + "  message:" + msg);
        }
    };

    public static void main(String[] args) {
        PlatformUtil.setPlatform(Platform.JAVA);
        System.out.println("启动线程 线程thread:" + Thread.currentThread().getName());
        Looper.prepareMainLooper();
        ExampleUnitTest exampleUnitTest = new ExampleUnitTest();
        new Thread(exampleUnitTest, "test").start();
        Looper.loop();
        System.out.println("线程退出");
    }

    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {

            Message message = new Message();
            message.obj = " 第" + i + "条信息";

            handler.sendMessageDelayed(message, i * 1000);
        }

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Looper.getMainLooper().quit();
    }
}