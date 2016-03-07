package com.example.message_pool_demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.message_pool_demo.test.MessageTest;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MessageTest.testMessagePoolRecycleAndReuse();
        MessageTest.testMessagePoolUpperLimit();
    }
}