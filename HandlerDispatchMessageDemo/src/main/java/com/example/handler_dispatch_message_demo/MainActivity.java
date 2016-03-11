package com.example.handler_dispatch_message_demo;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "HandlerDispatchMessageDemo";
    private static final int MSG1 = 1;
    private static final int MSG2 = 2;
    private static final int MSG3 = 3;

    private MyHandler mHandlerWithNullCallback;
    private MyHandler mHandlerWithNonNullCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initData();
        sendAndDispatchMsgTest();
    }

    private void initData() {
        mHandlerWithNullCallback = new MyHandler();
        mHandlerWithNonNullCallback = new MyHandler(MyHandler.getCallback(true));
    }

    private void sendAndDispatchMsgTest() {
        // The message's what filed is zero by default. So the message will be msg0.
        // And msg0.callback != null, so only msg0.callback.run() method will be executed.

        // 发送的该消息由于没有设置 what字段的值, 所以会使用默认值0. 该消息就是 msg0.
        // 由于该消息 msg0的 callback字段不为 null, 所以只有 msg0.callback.run()方法会被执行.
        Runnable r = new MessageCallback();
        mHandlerWithNullCallback.post(r);


        /*************************************************************************************/
        // msg1.callback == null, but this handler's mCallback != null && this handler's
        // mCallback.handleMessage(Message) returns true. So only this handler's mCallback.handleMessage(Message)
        // method will be executed.

        // 发送的 msg1的 callback字段为null, 但该 Handler的 mCallback字段不为 null, 且该字段内的
        // 回调方法的返回值为 true, 这样就屏蔽了 Handler自身的那个 handleMessage(Message)方法的执行.
        // 所以, 只有该 Handler对象的 mCallback.handleMessage(Message)方法会被执行.
        Message msg1 = mHandlerWithNonNullCallback.obtainMessage(MSG1);
        mHandlerWithNonNullCallback.sendMessage(msg1);


        /*************************************************************************************/
        // msg2.callback == null, but this handler's mCallback != null && this handler's
        // mCallback.handleMessage(Message) returns false, So both this handler's mCallback.handleMessage(Message)
        // and this handler's handleMessage(Message) method will be executed.

        // 发送的 msg2的 callback字段为null, 但该 Handler的 mCallback字段不为 null, 且该字段内的
        // 回调方法的返回值为 false, 所以该 Handler的 mCallback.handleMessage(Message) 和
        // handleMessage(Message) 这两个方法都会被执行.
        mHandlerWithNonNullCallback = new MyHandler(MyHandler.getCallback(false));
        Message msg2 = mHandlerWithNonNullCallback.obtainMessage(MSG2);
        mHandlerWithNonNullCallback.sendMessage(msg2);

        /*************************************************************************************/
        // both msg3.callback and this handler's mCallback are null, so only the handleMessage(Message)
        // method of this handler will be executed.

        // 由于 msg3.callback 和该 Handler的 mCallback字段都为 null, 所以只有该 Handler的 handleMessage(Message)
        // 方法会被执行.
        Message msg3 = mHandlerWithNullCallback.obtainMessage(MSG3);
        mHandlerWithNullCallback.sendMessage(msg3);
    }

    private static class MyHandler extends Handler {
        private static HandlerCallbackTrueImpl sHandlerCallbackTrueImpl;
        private static HandlerCallbackFalseImpl sHandlerCallbackFalseImpl;

        public MyHandler() {
        }

        public MyHandler(Callback callback) {
            super(callback);
        }

        /**
         * Get the Handler.Callback interface's instance. If the parameter disiredReturnValue is
         * true, then {@link #sHandlerCallbackTrueImpl} will be created and returned, otherwise
         * {@link #sHandlerCallbackFalseImpl} will be created and returned.
         *
         * 获取 Handler.Callback 这个接口的一个实现类对象. 如果传入的参数为 true, 那么会返回一个非 null的
         * {@link #sHandlerCallbackTrueImpl}. 如果为 false, 那么会返回一个非 null 的
         * {@link #sHandlerCallbackFalseImpl}.
         *
         * @param disiredReturnValue the return result of the method handleMessage(Message) in
         *                           {@link Handler.Callback} interface's implementation that you desire.
         *                           你想要让 {@link Handler.Callback}接口的实现类中 handleMessage(Message)
         *                           方法返回的结果.
         * @return a {@link Handler.Callback} interface's instance. 返回一个 {@link Handler.Callback}接口的实现类对象
         */
        public static Handler.Callback getCallback (boolean disiredReturnValue) {
            if (disiredReturnValue) {
                if (sHandlerCallbackTrueImpl == null) {
                    sHandlerCallbackTrueImpl = new HandlerCallbackTrueImpl();
                }
                return sHandlerCallbackTrueImpl;
            } else {
                if (sHandlerCallbackFalseImpl == null) {
                    sHandlerCallbackFalseImpl = new HandlerCallbackFalseImpl();
                }
                return sHandlerCallbackFalseImpl;
            }
        }

        /**
         * Override the method in the superclass to add indicating logs.
         * 重写父类(也就是 {@link Handler}类)中的该方法, 然后在该方法的前后分别加上标识性的log语句.
         * @param msg
         */
        @Override
        public void dispatchMessage(Message msg) {
            Log.i(TAG, "-----Start dispatching message msg" + msg.what);
            super.dispatchMessage(msg);
            Log.i(TAG, "-----Finish dispatching message msg" + msg.what);
            Log.i(TAG, " ");
        }

        @Override
        public void handleMessage(Message msg) {
            Log.i(TAG, "The message is handled by Handler.handleMessage()");
        }

        static class HandlerCallbackFalseImpl implements Handler.Callback {
            @Override
            public boolean handleMessage(Message msg) {
                Log.i(TAG, "The message is handled by Handler.mCallback.handleMessage()");
                return false;
            }
        }

        static class HandlerCallbackTrueImpl implements Handler.Callback {
            @Override
            public boolean handleMessage(Message msg) {
                Log.i(TAG, "The message is handled by Handler.mCallback.handleMessage()");
                return true;
            }
        }
    }

    private class MessageCallback implements Runnable {
        @Override
        public void run() {
            Log.i(TAG, "The message is handled by Message.callback.run()");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mHandlerWithNullCallback != null) {
            mHandlerWithNullCallback.removeCallbacksAndMessages(null);
        }
        if (mHandlerWithNonNullCallback != null) {
            mHandlerWithNonNullCallback.removeCallbacksAndMessages(null);
        }
    }
}