package com.example.message_pool_demo.test;

import android.util.Log;

import com.example.message_pool_demo.Constant;
import com.example.message_pool_demo.bean.MyMessage;
import com.example.message_pool_demo.utils.MessageUtils;

import java.util.LinkedList;
import java.util.List;


public class MessageTest {

    public static void testMessagePoolReuse() {
        MyMessage msgA = MessageUtils.obtainMessage("msgA");
        MyMessage msgB = MessageUtils.obtainMessage("msgB");
        MyMessage msgC = MessageUtils.obtainMessage("msgC");

        MessageUtils.recycleMessage(msgA);
        MessageUtils.recycleMessage(msgB);
        MessageUtils.recycleMessage(msgC);

        MyMessage msgD = MessageUtils.obtainMessage("msgD");
        MyMessage msgE = MessageUtils.obtainMessage("msgE");
        MyMessage msgF = MessageUtils.obtainMessage("msgF");
    }

    public static void testMessagePoolUpperLimit() {
        Log.i(Constant.TAG, " ");
        Log.i(Constant.TAG, "***************************************************************************");
        Log.i(Constant.TAG, " ");
        List<MyMessage> msgList = new LinkedList<MyMessage>();

        // create 11 messages.
        for (int i = 1; i < 12; i++) {
            msgList.add(MessageUtils.obtainMessage("msg"+i));
        }

        // recycle all the 11 messages created above, the 11th message recycle will fail since the
        // message pool size is full at that time (the message pool capacity is 10).
        for (MyMessage msg : msgList) {
            MessageUtils.recycleMessage(msg);
        }
    }
}