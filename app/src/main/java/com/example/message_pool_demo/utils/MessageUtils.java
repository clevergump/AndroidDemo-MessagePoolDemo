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

    public static void recycleMessage(MyMessage msg) {
        Log.i(Constant.TAG, "recycle " + msg.getMessageName());
        msg.recycle();
        int msgPoolSize = getMessagePoolSize();
        MyMessage sPool = getMessagePool();
        if (msgPoolSize > 0) {
            MyMessage sPoolNext = getNextMessage(sPool);
            Log.i(Constant.TAG, "      Current msgPoolSize = " + msgPoolSize + ", sPool = " + sPool + ", sPool.next = " + sPoolNext);
        } else {
            Log.i(Constant.TAG, "      Current msgPoolSize = " + msgPoolSize + ", sPool = " + sPool);
        }
    }

    public static MyMessage obtainMessage(String msgName) {
        MyMessage msg = MyMessage.obtain(msgName);
        MyMessage sPool = getMessagePool();
        int msgPoolSize = getMessagePoolSize();
        Log.i(Constant.TAG, "      Current msgPoolSize = "+ msgPoolSize +", sPool = " + sPool);
        return msg;
    }

    public static MyMessage getMessagePool() {
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