package ir.sajjadyosefi.android.tsr;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;


import java.io.IOException;

import androidx.annotation.Nullable;

public class MyBackgroundService extends Service {

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

//        new Handler(Looper.getMainLooper()).post(new Runnable() {
//            @Override
//            public void run() {
//                Toast.makeText(context,"My Awesome service toast...",Toast.LENGTH_SHORT).show();
//            }
//        });

//        new Handler(Looper.getMainLooper()).post(new Runnable() {
//            @Override
//            public void run() {
//                Sample sample = new Sample(context);
//                try {
//                    sample.execute();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();
//                }
//            }
//        });


        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Looper.prepare();
                            Sample sample = new Sample(context);
                            try {
                                sample.execute();
                            } catch (IOException e) {
                                e.printStackTrace();
//                                Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();
                            }
                    Looper.loop();
                } catch (Exception error) {
                    error.printStackTrace();
                }


//                try  {
//                    ((Activity)context).runOnUiThread(new Runnable()
//                    {
//                        public void run()
//                        {
//                            Sample sample = new Sample(context);
//                            try {
//                                sample.execute();
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                                Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();
//                            }
//                        }
//                    });
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
            }
        });
        thread.start();



        return START_STICKY;
    }

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
