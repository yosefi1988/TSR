package ir.sajjadyosefi.android.tsr;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import static ir.sajjadyosefi.android.tsr.MainActivity.TAG;

public class SMSReceiver extends BroadcastReceiver {

    public static final String pdu_type = "pdus";

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        SmsMessage[] msgs;
        String strMessage = "";
        String sender = "";
        String format = bundle.getString("format");
        Object[] pdus = (Object[]) bundle.get(pdu_type);
        if (pdus != null) {
            boolean isVersionM =(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M);
            msgs = new SmsMessage[pdus.length];

            for (int i = 0; i < msgs.length; i++) {
                if (isVersionM) {
                    msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i], format);
                } else {
                    msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                }
                // Build the message to show.
//                strMessage += "SMS from " + msgs[i].getOriginatingAddress();
                sender = msgs[i].getOriginatingAddress();
//                strMessage += " :" ;
                strMessage += msgs[i].getMessageBody() + "\n";
            }

            Log.d(TAG, "onReceive: " + strMessage);

            if (SMSBackgroundService.getType(strMessage) != SMSBackgroundService.SMSTYPE.UNKNOWN) {
//            if(strMessage.contains("{\"d\":\"")) {
//                Toast.makeText(context, strMessage, Toast.LENGTH_LONG).show();
                Intent i = new Intent(context, SMSBackgroundService.class);
                i.putExtra("text",strMessage);
                i.putExtra("sender",sender);
                context.startService(i);
            }
        }
    }
}