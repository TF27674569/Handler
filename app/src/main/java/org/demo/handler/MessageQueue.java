package org.demo.handler;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * create by TIAN FENG on 2020/4/8
 */
public class MessageQueue {

    private String TAG = "MessageQueue";

    private final ReentrantLock LOCK = new ReentrantLock();
    private final Condition mCondition = LOCK.newCondition();

    // msg 头
    private Message mMessages;

    // 是否允许退出
    private boolean mQuitAllowed;

    // 是否退出
    private boolean mQuitting;

    MessageQueue(boolean quitAllowed) {
        mQuitAllowed = quitAllowed;
    }


    boolean enqueueMessage(Message msg, long when) {
        try {
            LOCK.lock();

            // 退出之后不允许添加
            if (mQuitting) {
                return false;
            }

            Message p = mMessages;
            msg.when = when;

            if (p == null || when == 0 || when < p.when) {
                // 往下压链表
                msg.next = mMessages;
                // 更新链表头
                mMessages = msg;
            } else {

                // 这时需要往后找，找到一个合适的位置链上
                Message prev;
                for (; ; ) { // 编译后比 while(true) 快
                    // 前一个
                    prev = p;
                    //下一个
                    p = p.next;
                    // 如果到最后一个
                    // 或者时间比现在这个小  800(target)    1000(current)  将target放在current前面即可
                    if (p == null || when < p.when) {
                        break;
                    }
                }

                // 将 1000(current) 放在 800(target) 后面
                msg.next = p;
                // 将 800(target)  放在 之前current前面的一个
                prev.next = msg;
                // mMessages 保存的是头 ，头部不通处理
            }


            // debug open
            /*Message p1 = mMessages;
            while (true) {
                Log.d(TAG, "time:" + p1);
                if (p1 == null) {
                    break;
                }
                p1 = p1.next;
            }*/

            // 插入后唤醒
            waitWake();

        } catch (Exception e) {
            PlatformUtil.e(TAG, "enqueueMessage: err" + e.getMessage());
            return false;
        } finally {
            LOCK.unlock();
        }

        return true;
    }

    /**
     * 记得加锁记得唤醒
     */
    Message next() {
        long nextWaitTimeoutMillis = 0;
        for (; ; ) {
            try {
                LOCK.lockInterruptibly();

                // 模仿nativePollOnce
                waitOnce(nextWaitTimeoutMillis);

                // 退出之后不需要遍历
                if (mQuitting) {
                    return null;
                }

                // 没有消息
                if (mMessages == null) {
                    //  下一轮轮询一直阻塞
                    nextWaitTimeoutMillis = -1;
                    continue;
                }

                final long now = PlatformUtil.currentTimeMillis();
                Message msg = mMessages;
                if (now < msg.when) {
                    // 等待 msg.when - now 这么长的时间
                    nextWaitTimeoutMillis = msg.when - now;
                } else {
                    mMessages = msg.next;
                    return msg;
                }
            } catch (Exception e) {
                PlatformUtil.e(TAG, "next: err" + e.getMessage());
                return null;
            } finally {
                LOCK.unlock();
            }
        }
    }


    private void waitWake() {
        // 唤醒阻塞队列
        mCondition.signal();
    }

    private void waitOnce(long timeMilli) throws InterruptedException {

        // 0 直接返回 不挂起线程
        if (timeMilli == 0) {
            return;
        }

        // > 0 等待这个 > 0的值到了之后再 返回
        if (timeMilli > 0) {
            mCondition.await(timeMilli, TimeUnit.MILLISECONDS);
            return;
        }

        // < 0 (-1)  一直挂起
        mCondition.await();
    }

    public void quit() {
        if (!mQuitAllowed && PlatformUtil.isAndroid()) {
            throw new IllegalStateException("Main thread not allowed to quit.");
        }

        LOCK.lock();
        // 防止并发重复 waitWake
        if (mQuitting) {
            return;
        }
        mQuitting = true;
        try {
            waitWake();
        } finally {
            LOCK.unlock();
        }


    }
}
