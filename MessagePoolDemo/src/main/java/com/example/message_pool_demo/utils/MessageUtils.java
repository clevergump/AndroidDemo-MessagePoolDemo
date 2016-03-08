package com.example.message_pool_demo.utils;

import android.util.Log;

import com.example.message_pool_demo.Constant;
import com.example.message_pool_demo.bean.MyMessage;

import java.lang.reflect.Field;

/**
 * This is a utility class of the {@link MyMessage MyMessage} class.
 * MyMessage的工具类
 */
public class MessageUtils {

    /**
     * 将给定的消息对象回收到消息池中.
     * @param msg
     */
    public static void recycleMessage(MyMessage msg) {
        Log.i(Constant.TAG, "recycle " + msg.getMessageName());
        msg.recycle();
        int msgPoolSize = getMessagePoolSize();
        MyMessage sPool = getMessageFromPool();
        if (msgPoolSize > 0) {
            MyMessage sPoolNext = getNextMessage(sPool);
            Log.i(Constant.TAG, "      Current msgPoolSize = " + msgPoolSize + ", sPool = " + sPool + ", sPool.next = " + sPoolNext);
        } else {
            Log.i(Constant.TAG, "      Current msgPoolSize = " + msgPoolSize + ", sPool = " + sPool);
        }
    }

    /**
     * 获取一个消息对象. 优先使用消息池中已有的消息对象, 如果消息池为空, 才会去创建消息对象.
     * @param msgName
     * @return
     */
    public static MyMessage obtainMessage(String msgName) {
        MyMessage msg = MyMessage.obtain(msgName);
        MyMessage sPool = getMessageFromPool();
        int msgPoolSize = getMessagePoolSize();
        Log.i(Constant.TAG, "      Current msgPoolSize = "+ msgPoolSize +", sPool = " + sPool);
        return msg;
    }

    /**
     * 获取 Message类中私有的 sPool字段的值.
     * @return
     */
    public static MyMessage getMessageFromPool() {
        try {
            Field sPoolField = MyMessage.class.getDeclaredField("sPool");
            sPoolField.setAccessible(true);
            MyMessage sPool = (MyMessage) sPoolField.get(null);
            return sPool;
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取消息池中当前存放的消息引用的数量
     * @return
     */
    public static int getMessagePoolSize() {
        try {
            Field sPoolField = MyMessage.class.getDeclaredField("sPoolSize");
            sPoolField.setAccessible(true);
            Integer sPoolSize = (Integer) sPoolField.get(null);
            return sPoolSize;
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 获取给定消息对象所指向的下一个消息对象, 也就是 msg.next 的值.
     * @param msg
     * @return
     */
    public static MyMessage getNextMessage(MyMessage msg) {
        MyMessage next = null;
        try {
            Field nextField = MyMessage.class.getDeclaredField("next");
            nextField.setAccessible(true);
            next = (MyMessage) nextField.get(msg);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return next;
    }
}