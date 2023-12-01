package ir.sajjadyosefi.android.tsr;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.telephony.SmsManager;
import android.widget.Toast;


import java.io.IOException;
import java.util.ArrayList;

import androidx.annotation.Nullable;

public class BulkSMSBackgroundService extends Service {

    Context context;
    Handler handler;

    @Override
    public void onCreate() {
        super.onCreate();
        this.context = getApplicationContext();
        handler = new Handler();

    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int x = super.onStartCommand(intent, flags, startId);

        String[] numbers = intent.getExtras().getStringArray("numbers");
        String txt = intent.getExtras().getString("text");

        for (String item:numbers) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Looper.prepare();
                        try {
                            sendSms(intent,item,txt);
                            Toast.makeText(context, "send:" + item, Toast.LENGTH_SHORT).show();
                        }catch (Exception ex){
                            Toast.makeText(context, "err:" + ex.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        Looper.loop();
                    } catch (Exception error) {
                        error.printStackTrace();
                    }

                }
            });
            thread.start();
        }


        return START_STICKY;
    }

    private void sendSms(Intent intent, String item, String txt) {
        PendingIntent pi= PendingIntent.getActivity(getApplicationContext(), 0, intent,0);
        SmsManager sms= SmsManager.getDefault();
        sms.sendTextMessage(item, null, txt, pi,null);
    }

    public enum SMSTYPE {UNKNOWN,TSRSMS, TSRSMS_WITHTYPE, TSRSMS_CONFIRM, TSRSMS_RESET}

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
