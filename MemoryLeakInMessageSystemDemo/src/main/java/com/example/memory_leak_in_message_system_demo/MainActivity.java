package com.example.memory_leak_in_message_system_demo;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.exit_message_loop_demo.R;

import java.lang.ref.WeakReference;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "SendNullTargetedMessageDemo";
    private static final int FLAG_MSG1 = 1;
    private static final int FLAG_MSG2 = 2;
    private static final int FLAG_MSG3 = 3;
    public static final int DELAY_TIME_MILLIS = 10000;
    private static MyHandler sHandler;

    // Non-static anonymous inner class may leads to Activity instance's memory leak.
    // Activity中定义的非静态匿名内部类易造成Activity对象在内存中的泄露.
    private Handler nonStaticInnerHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Log.i(TAG, "nonStaticHandler Receive msg" + msg.what);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        asyncSendMessageTest();
    }

    private void init() {
        sHandler = new MyHandler(this);
    }

    /**
     * Send messages asynchronously, that is sending in another thread.
     * 发送异步消息的测试方法.
     */
    private void asyncSendMessageTest() {
        new Thread(new RunnableImpl(MainActivity.this)).start();

        // Non-static anonymous inner class leading to memory leak demo.
        // 非静态匿名内部类导致内存泄露的例子.
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message msg3 = Message.obtain();
                msg3.what = FLAG_MSG3;
                nonStaticInnerHandler.sendMessageDelayed(msg3, DELAY_TIME_MILLIS);
            }
        }).start();
    }

    private static class MyHandler extends Handler {

        private WeakReference<Activity> weakRef;

        public MyHandler(Activity activity) {
            weakRef = new WeakReference<Activity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            Activity activity = weakRef.get();
            if (activity != null) {
                Log.i(TAG, "Receive msg" + msg.what);
            }
        }
    }

    private static class RunnableImpl implements Runnable {

        private WeakReference<Activity> weakRef;

        public RunnableImpl(Activity activity) {
            weakRef = new WeakReference<Activity>(activity);
        }

        @Override
        public void run() {
            Activity activity = weakRef.get();
            if (activity != null) {
                // Obtain a message object with its target left null.
                // 获取一个 Message对象 msg1, 此时它内部的 target字段为 null.
                Message msg1 = Message.obtain();
                msg1.what = FLAG_MSG1;
                // The msg1.target will be internally set to the Handler object that sending it,
                // that is sHandler.
                // 在 Handler的 sendMessage()方法内部会将 msg1的target设置为该Handler对象. 这样即使一个
                // Message对象没有设置 target也没关系, 只要用一个 Handler对象将它发送出去, 该 Handler对象
                // 会自动设置它自己为该 Message对象的 target.
                sHandler.sendMessageDelayed(msg1, DELAY_TIME_MILLIS);

                // Obtain another message object with its target left null.
                // 再次获取一个 Message对象 msg2, 此时它内部的 target字段也为 null.
                Message msg2 = Message.obtain();
                msg2.what = FLAG_MSG2;
                // The msg2.target will be internally set to the Handler object that sending it,
                // that is sHandler.
                // 在 Handler的 sendMessage()方法内部会将 msg2的target设置为该Handler对象. 这样即使一个
                // Message对象没有设置 target也没关系, 只要用一个 Handler对象将它发送出去, 该 Handler对象
                // 会自动设置它自己为该 Message对象的 target.
                sHandler.sendMessageDelayed(msg2, DELAY_TIME_MILLIS);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (sHandler != null) {
            sHandler.removeCallbacksAndMessages(null);
        }
    }
}