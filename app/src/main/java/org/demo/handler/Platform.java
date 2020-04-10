package org.demo.handler;

import android.os.SystemClock;
import android.util.Log;

/**
 * create by TIAN FENG on 2020/4/10
 */
public enum Platform {

    JAVA() {
        @Override
        public void d(String tag, String msg) {
            System.out.println(tag + ":" + msg);
        }

        @Override
        public void e(String tag, String msg) {
            d(tag, msg);
        }

        @Override
        public long currentTimeMillis() {
            return System.currentTimeMillis();
        }
    },
    ANDROID() {
        @Override
        public void d(String tag, String msg) {
            Log.d(tag, msg);
        }

        @Override
        public void e(String tag, String msg) {
            Log.e(tag, msg);
        }

        @Override
        public long currentTimeMillis() {
            return SystemClock.uptimeMillis();
        }
    };


    public abstract void d(String tag, String msg);

    public abstract void e(String tag, String msg);

    public abstract long currentTimeMillis();

}
