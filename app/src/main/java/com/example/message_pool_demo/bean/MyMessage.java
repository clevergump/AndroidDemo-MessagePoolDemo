package com.example.message_pool_demo.bean;

import android.os.Bundle;
import android.os.Handler;
import android.os.Messenger;
import android.util.Log;

import com.example.message_pool_demo.Constant;


/**
 * This is a simplified version of {@link android.os.Message android.os.Message},
 * just to research the message pool design of the {@link android.os.Message android.os.Message}
 * class. Most unrelated methods in {@link android.os.Message android.os.Message} class are deleted
 * and some of the related are modified in order to accomplish this research, the main modifications
 * are as follows:
 * 1. Add a String field {@link #msgName msgName} standing for the name of the current MyMessage object.
 * 2. Add a String param and log printing codes to the constructor. See
 * {@link #MyMessage(String msgName) MyMessage(String msgName)}.
 * 3. Modify the {@link #toString() toString()} method by returning the msgName of the MyMessage object.
 * 4. Add a String param and log printing codes to the obtain() method. See
 * {@link #obtain(String) obtain(String)}.
 * <p/>
 * <p/>
 * 这是 Android SDK中 {@link android.os.Message android.os.Message} 类的一个简化版, 主要是为了研究
 * 该 Message类中消息池的设计机制, 所以将与该研究无关的方法全部删除, 而将某些方法的实现细节进行了适当的
 * 修改, 包括但不限于以下几点:
 * 1. 增加了字段 String {@link #msgName msgName}, 表示 MyMessage对象的名称.
 * 2. 将构造方法更改为携带有参数 msgName的形式并在其内部增加了日志输出语句, 请见
 *    {@link #MyMessage(String) MyMessage(String)}.
 * 3. 将 {@link #toString() toString()} 方法更改为返回 MyMessage对象中 msgName字段的值.
 * 4. 将 obtain()方法更改为带有一个String参数的 {@link #obtain(String) obtain(String)} 并在其内部
 *    增加了日志输出语句.
 */
public final class MyMessage {
    /**
     * User-defined message code so that the recipient can identify
     * what this message is about. Each {@link Handler} has its own name-space
     * for message codes, so you do not need to worry about yours conflicting
     * with other handlers.
     */
    public int what;

    public int arg1;

    public int arg2;

    public Object obj;

    /**
     * Optional Messenger where replies to this message can be sent.  The
     * semantics of exactly how this is used are up to the sender and
     * receiver.
     */
    public Messenger replyTo;

    /*package*/ int flags;

    /*package*/ long when;

    /*package*/ Bundle data;

    /*package*/ Handler target;

    /*package*/ Runnable callback;

    // sometimes we store linked lists of these things
    /*package*/ MyMessage next;

    private static final Object sPoolSync = new Object();
    private static MyMessage sPool;
    private static int sPoolSize = 0;
    // name of this MyMessage object. 表示该消息对象的名称.
    private String msgName;

    private static final int MAX_POOL_SIZE = 10;

    /**
     * Return a new Message instance from the global pool. Allows us to
     * avoid allocating new objects in many cases.
     *
     * @param msgName name of the newly created MyMessage object if sPool is null.
     */
    public static MyMessage obtain(String msgName) {
        synchronized (sPoolSync) {
            if (sPool != null) {
                MyMessage m = sPool;
                sPool = m.next;
                m.next = null;
                sPoolSize--;
                Log.i(Constant.TAG, "REUSE message " + m.msgName);
                return m;
            }
        }
        return new MyMessage(msgName);
    }

    /**
     * Return a Message instance to the global pool.  You MUST NOT touch
     * the Message after calling this function -- it has effectively been
     * freed.
     */
    public void recycle() {
        clearForRecycle();

        synchronized (sPoolSync) {
            if (sPoolSize < MAX_POOL_SIZE) {
                next = sPool;
                sPool = this;
                sPoolSize++;
            } else {
                Log.e(Constant.TAG, "      Message pool is full. Recycle fails.");
            }
        }
    }

    /**
     * Return the name of this MyMessage object instead of the original implementation way of the
     * {@link android.os.Message android.os.Message} class to make it clearer that which objects
     * are currently separately referenced to by the sPool and sPool.next fields.
     * <p/>
     * 将 toString()方法返回该消息对象的名称 msgName, 这样能更加直观地看出 sPool 和 sPool.next这
     * 两个引用当前各指向哪个 MyMessage对象.
     */
    @Override
    public String toString() {
        return msgName;
    }

    /*package*/ void clearForRecycle() {
        flags = 0;
        what = 0;
        arg1 = 0;
        arg2 = 0;
        obj = null;
        replyTo = null;
        when = 0;
        target = null;
        callback = null;
        data = null;
    }

    /**
     * Constructor (but the preferred way to get a Message is to call {@link #obtain(String msgName) MyMessage.obtain(msgName)}).
     */
    public MyMessage(String msgName) {
        this.msgName = msgName;
        Log.i(Constant.TAG, "create message " + msgName);
    }

    /**
     * Obtain the name of this MyMessage object
     *
     * @return the name of this MyMessage object
     */
    public String getMessageName() {
        return msgName;
    }
}