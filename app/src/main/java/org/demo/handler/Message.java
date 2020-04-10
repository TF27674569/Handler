package org.demo.handler;


/**
 * create by TIAN FENG on 2020/4/8
 */
public class Message {

    public Handler target;
    public long when;
    public Runnable callback;
    public Message next;
    public Object obj;


    @Override
    public String toString() {
        return "Message{" +
                ", when=" + when +
                ", obj=" + obj +
                '}';
    }
}
