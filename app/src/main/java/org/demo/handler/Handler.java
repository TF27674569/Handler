package org.demo.handler;

/**
 * create by TIAN FENG on 2020/4/8
 */
public class Handler {

    private String TAG = "Handler";

    public interface Callback {
        boolean handlerMessage(Message msg);
    }

    private Callback mCallback;
    private MessageQueue mQueue;
    private Looper mLooper;

    public Handler() {
        this(Looper.myLooper());
    }


    public Handler(Looper looper) {
        this(looper, null);
    }

    public Handler(Looper looper, Callback callback) {

        mLooper = looper;

        if (mLooper == null) {
            throw new RuntimeException(
                    "Can't create handler inside thread that has not called Looper.prepare()");
        }

        mCallback = callback;
        mQueue = mLooper.mQueue;
    }


    public boolean post(Runnable r) {
        return postDelayed(r, 0);
    }


    public boolean postDelayed(Runnable r, long delayMillis) {
        Message message = new Message();
        message.callback = r;
        return sendMessageDelayed(message, delayMillis);
    }


    public boolean sendMessage(Message msg) {
        return sendMessageDelayed(msg, 0);
    }

    public final boolean sendMessageDelayed(Message msg, long delayMillis) {
        if (delayMillis < 0) {
            delayMillis = 0;
        }
        return sendMessageAtTime(msg, PlatformUtil.currentTimeMillis() + delayMillis);
    }

    public boolean sendMessageAtTime(Message msg, long uptimeMillis) {
        MessageQueue queue = mQueue;
        if (queue == null) {
            PlatformUtil.e(TAG, "sendMessageAtTime: queue is null!!");
            return false;
        }
        return enqueueMessage(queue, msg, uptimeMillis);
    }

    private boolean enqueueMessage(MessageQueue queue, Message msg, long uptimeMillis) {
        msg.target = this;
        return queue.enqueueMessage(msg, uptimeMillis);
    }


    void dispatchMessage(Message msg) {
        if (msg.callback != null) {
            msg.callback.run();
            return;
        }

        if (mCallback == null || !mCallback.handlerMessage(msg)) {
            handlerMessage(msg);
        }
    }

    public void handlerMessage(Message msg) {

    }


}
