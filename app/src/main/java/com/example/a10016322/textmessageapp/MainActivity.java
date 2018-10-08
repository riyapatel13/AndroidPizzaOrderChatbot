package com.example.a10016322.textmessageapp;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    BroadcastReceiver broadcastReceiver;
    TextView sender, message;
    static String phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.RECEIVE_SMS}, 1);
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.SEND_SMS}, 1);

        sender = (TextView)(findViewById(R.id.sender_id));
        message = (TextView)(findViewById(R.id.message_id));

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Bundle bundle = intent.getExtras();
                Object[] pdus = (Object[])(bundle.get("pdus"));
                SmsMessage[] smsMessages = new SmsMessage[pdus.length];

                for (int i=0; i<pdus.length; i++)
                {
                    smsMessages[i] = SmsMessage.createFromPdu((byte[])(pdus[i]), bundle.getString("format"));
                }

                message.setText(smsMessages[0].getDisplayMessageBody());
                phoneNumber = smsMessages[0].getOriginatingAddress();
                sender.setText(phoneNumber);

                String textMessage = "hello this is a robot";
                Handler handler = new Handler();
                handler.postDelayed(runnable(textMessage),5000); //5000 = delay 5 seconds

            }
        };

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.provider.Telephony.SMS_RECEIVED");
        registerReceiver(broadcastReceiver, intentFilter);
    }

    public static Runnable runnable(final String message)
    {
        return new Runnable() {
            @Override
            public void run() {
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(phoneNumber, null, /*whatever message you want - depends on the case*/message, null, null);
            }
        };
    }


}
