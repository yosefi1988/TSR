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



import com.google.gson.Gson;

import java.io.IOException;

import androidx.annotation.Nullable;
import ir.sajjadyosefi.android.tsr.model.SmsModel;

public class SMSBackgroundService extends Service {

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
                            HttpConnection httpConnection = new HttpConnection(context);
                            try {
                                String txt = intent.getStringExtra("text");
                                String sender = intent.getStringExtra("sender");

                                String result = httpConnection.execute(checkNumber(txt,sender),getType(txt));

                                Gson gson = new Gson();
                                String resulttext = result.replace("\\\"","\"");
                                ServerResponseBase exception = gson.fromJson(resulttext.substring(1, resulttext.length() - 1), ServerResponseBase.class);

                                if (exception.getTubelessException().getCode() == (-55) || exception.getTubelessException().getCode() == (-54)) {
//                                    Thread thread = new Thread(new Runnable() {
//                                        @Override
//                                        public void run() {
//                                            try {
//                                                Looper.prepare();
//                                                try {
                                                    sendSms(intent,sender,exception.getTubelessException().getMessage());
                                                    Toast.makeText(context, "send:" + exception.getTubelessException().getMessage(), Toast.LENGTH_SHORT).show();
//                                                }catch (Exception ex){
//                                                    Toast.makeText(context, "err:" + ex.getMessage(), Toast.LENGTH_SHORT).show();
//                                                }
//                                                Looper.loop();
//                                            } catch (Exception error) {
//                                                error.printStackTrace();
//                                            }
//
//                                        }
//                                    });
//                                    thread.start();
                                }
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

    private void sendSms(Intent intent, String item, String txt) {
        PendingIntent pi= PendingIntent.getActivity(getApplicationContext(), 0, intent,0);
        SmsManager sms= SmsManager.getDefault();
        sms.sendTextMessage(item, null, txt, pi,null);
    }

    private String checkNumber(String txt, String sender) {
        String senderClean = sender.replace("+98", "0");
        Gson gson = new Gson();
        SmsModel smsModel = gson.fromJson(txt,SmsModel.class);

        if (smsModel.getM().equals(senderClean)) {
            return txt;
        } else {
            smsModel.setM(senderClean);
            return gson.toJson(smsModel);
        }
    }

    public enum SMSTYPE {UNKNOWN,TSRSMS, TSRSMS_WITHTYPE, TSRSMS_CONFIRM, TSRSMS_RESET}

    public static SMSTYPE getType(String strMessage){
        if(strMessage.contains("{\"d\":\"")) {
            if(strMessage.contains(",\"t\":\"")) {
                if(strMessage.contains(",\"t\":\"c")) {
                    return SMSTYPE.TSRSMS_CONFIRM;
                }
                if(strMessage.contains(",\"t\":\"r")) {
                    return SMSTYPE.TSRSMS_RESET;
                }
                return SMSTYPE.TSRSMS_WITHTYPE;
            }
            return SMSTYPE.TSRSMS;
        }
        return SMSTYPE.UNKNOWN;
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
