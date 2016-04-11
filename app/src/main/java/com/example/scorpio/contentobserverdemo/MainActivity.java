package com.example.scorpio.contentobserverdemo;

import android.content.ContentResolver;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        //监听系统短信

        ContentResolver resolver = getContentResolver();
        
        //注册一个内容观察者观察短信数据库
        resolver.registerContentObserver(Uri.parse("content://sms/"),true,new MyContentObserver(new Handler()));
    }
    
    /*内容观察者*/
    class MyContentObserver extends ContentObserver{

        private static final String TAG = "MyContentObserver";

        /**
         * Creates a content observer.
         *
         * @param handler The handler to run {@link #onChange} on, or null if none.
         */
        public MyContentObserver(Handler handler) {
            super(handler);
        }
        
        /*当被监听的内容发生改变时回调*/

        @Override
        public void onChange(boolean selfChange) {
            Log.i(TAG,"短信改变了");
            Uri uri = Uri.parse("content://sms/outbox");
            
            //查询发信箱的内容
            Cursor cursor = getContentResolver().query(uri,new String[]{"address","date","body"},null,null,null);
            if (cursor != null && cursor.getCount() > 0){
                
                String address;
                long date;
                String body;
                while (cursor.moveToNext()){
                    address =cursor.getString(0);
                    date = cursor.getLong(1);
                    body = cursor.getString(2);
                    
                    Log.i(TAG,"号码："+address+",日期："+date+",内容:"+body);
                }
            }
            cursor.close();
        }
    }
}
