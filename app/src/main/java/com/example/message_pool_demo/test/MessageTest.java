package com.example.message_pool_demo.test;

import android.util.Log;

import com.example.message_pool_demo.Constant;
import com.example.message_pool_demo.bean.MyMessage;
import com.example.message_pool_demo.utils.MessageUtils;

import java.util.LinkedList;
import java.util.List;


public class MessageTest {

    /**
     * 用于测试消息池对象消息对象的回收与重复利用.
     */
    public static void testMessagePoolRecycleAndReuse() {
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

    /**
     * 用于测试并验证消息池的容量上限是10, 当消息池中已经存放了10个消息引用后, 就不能再回收消息对象了.
     */
    public static void testMessagePoolUpperLimit() {
        Log.i(Constant.TAG, " ");
        Log.i(Constant.TAG, "***************************************************************************");
        Log.i(Constant.TAG, " ");
        List<MyMessage> msgList = new LinkedList<MyMessage>();

        // create 11 messages. 创建 11个消息对象.(而消息池的最大容量是10, 也就是最多只能回收10个消息对象,
        // 第11个消息对象无法回收)
        for (int i = 1; i < 12; i++) {
            msgList.add(MessageUtils.obtainMessage("msg"+i));
        }

        // recycle all the 11 messages created above, the 11th message recycle will fail since the
        // message pool size is full at that time (the message pool capacity is 10).
        // 回收先前创建的 11个消息对象, 其中前10个消息对象都能被消息池回收, 由于尝试回收第11个消息对象时,
        // 消息池已满, 所以第11个消息对象就无法被消息池回收了.
        for (MyMessage msg : msgList) {
            MessageUtils.recycleMessage(msg);
        }
    }
}