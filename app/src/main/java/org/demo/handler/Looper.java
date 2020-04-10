package org.demo.handler;



/**
 * create by TIAN FENG on 2020/4/8
 */
public final class Looper {

    MessageQueue mQueue;
    private static final ThreadLocal<Looper> sThreadLocal = new ThreadLocal<>();
    private static Looper sMainLooper;

    private Looper(boolean quitAllowed) {
        mQueue = new MessageQueue(quitAllowed);
    }

    public static void prepare() {
        prepare(true);
    }

    private static void prepare(boolean quitAllowed) {
        if (sThreadLocal.get() != null) {
            throw new RuntimeException("Only one Looper may be created per thread");
        }
        sThreadLocal.set(new Looper(quitAllowed));
    }


    public static void prepareMainLooper() {
        prepare(false);
        synchronized (android.os.Looper.class) {
            if (sMainLooper != null) {
                throw new IllegalStateException("The main Looper has already been prepared.");
            }
            sMainLooper = myLooper();
        }
    }

    public static Looper myLooper() {
        return sThreadLocal.get();
    }


    public static Looper getMainLooper() {
        synchronized (Looper.class) {
            return sMainLooper;
        }
    }

    public void quit() {
        mQueue.quit();
    }

    public static void loop(){
        final Looper me = myLooper();

        // 必须先启动prepare 创建一个looper对象
        if (me == null) {
            throw new RuntimeException("No Looper; Looper.prepare() wasn't called on this thread.");
        }
        final MessageQueue queue = me.mQueue;

        for (;;) {
            Message msg = queue.next();

            if (msg == null) {
                return;
            }
            msg.target.dispatchMessage(msg);
        }
    }
}
