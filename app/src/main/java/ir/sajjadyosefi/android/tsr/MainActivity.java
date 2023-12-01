package ir.sajjadyosefi.android.tsr;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.internal.Primitives;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ir.sajjadyosefi.android.tsr.model.SmsModel;

import static android.Manifest.permission.READ_PHONE_STATE;
import static android.Manifest.permission.RECEIVE_SMS;
import static android.Manifest.permission.SEND_SMS;
import static ir.sajjadyosefi.android.tsr.SMSBackgroundService.getType;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "TSR";
//    String[] strArr = {RECEIVE_SMS,READ_PHONE_STATE,SEND_SMS};
    String[] strArr = {RECEIVE_SMS,READ_PHONE_STATE,SEND_SMS};
    Context context;

    @Override
    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        if (i == 101) {
            try {
                if (iArr[0] == 0) {
//                    if(checkIfSimIsPresent(this)){
                        if (isNetworkAvailable().booleanValue()) {
                            Toast.makeText(this,"Ready",Toast.LENGTH_LONG).show();
                        }else {
                            Toast.makeText(this,"برای ادامه به اینترنت متصل شوید",Toast.LENGTH_LONG).show();
                        }
//                    }else {
//                        Toast.makeText(this,"برای ادامه باید از سیم کارت استفاده کنید",Toast.LENGTH_LONG).show();
//                    }
                    return;
                }
                Toast.makeText(this, "برای ادامه به این دسترسی احتیاج است", Toast.LENGTH_LONG).show();
            } catch (ArrayIndexOutOfBoundsException e) {
                e.printStackTrace();
            } catch (RuntimeException e2) {
                e2.printStackTrace();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;

        try {
            getSupportActionBar().hide();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
//        Timber.tag(TAG);
        setContentView(R.layout.activity_main);
        ((Button)findViewById(R.id.buttonSend)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                List<String> numbers = Arrays.asList(((EditText)findViewById(R.id.editTextNumbers))
                        .getText()
                        .toString()
                        .replaceAll(" ", "")
                        .replaceAll("\n", ",")
                        .split("\\s*,\\s*"));
                String[] array = numbers.toArray(new String[numbers.size()]);

                Intent i = new Intent(context, BulkSMSBackgroundService.class);
                Bundle bundle = new Bundle();

                bundle.putStringArray("numbers", array);
                bundle.putString("text", String.valueOf(((EditText)findViewById(R.id.editTextText)).getText()));
                i.putExtras(bundle);
                context.startService(i);

                ((EditText) findViewById(R.id.editTextNumbers)).setText("");
            }
        });
        init();

//
//        Thread thread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    Looper.prepare();
//                    HttpConnection httpConnection = new HttpConnection(context);
//                    try {
//                        String txt = ("{\"d\":\"ba3299b92dd7d04d\",\"m\":\"\",\"t\":\"r\",\"u\":\"\"}");
//                        String sender = ("09149771335");
//                        String result = httpConnection.execute(checkNumber(txt,sender),getType(txt));
//
//                        Gson gson = new Gson();
//                        String resulttext = result.replace("\\\"","\"");
//                        ServerResponseBase exception = gson.fromJson(resulttext.substring(1, resulttext.length() - 1), ServerResponseBase.class);
//
//                        if (exception.getTubelessException().getCode() == (-55) || exception.getTubelessException().getCode() == (-54)) {
//                            send
//                        }
//                    } catch (IOException e) {
//                        e.printStackTrace();
////                                Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();
//                    }
//                    Looper.loop();
//                } catch (Exception error) {
//                    error.printStackTrace();
//                }
//
//            }
//        });
//        thread.start();




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

    private void init() {
        if (hasMainActivityPermission()) {
//            if(checkIfSimIsPresent(this)){
                if (isNetworkAvailable().booleanValue()) {
                    Toast.makeText(this,"Ready",Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(this,"برای ادامه به اینترنت متصل شوید",Toast.LENGTH_LONG).show();
                }
//            }else {
//                Toast.makeText(this,"برای ادامه باید از سیم کارت استفاده کنید",Toast.LENGTH_LONG).show();
//            }
        }else {
            ActivityCompat.requestPermissions(this,strArr,101);
            Toast.makeText(this,"برای ادامه به این دسترسی احتیاج است",Toast.LENGTH_LONG).show();
        }
    }

    private boolean checkIfSimIsPresent(Context mContext) {
        //        TelephonyManager telMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
//        int simState = telMgr.getSimState();
//        switch (simState) {
//            case TelephonyManager.SIM_STATE_ABSENT:
//                // do something
//                break;
//            case TelephonyManager.SIM_STATE_NETWORK_LOCKED:
//                // do something
//                break;
//            case TelephonyManager.SIM_STATE_PIN_REQUIRED:
//                // do something
//                break;
//            case TelephonyManager.SIM_STATE_PUK_REQUIRED:
//                // do something
//                break;
//            case TelephonyManager.SIM_STATE_READY:
//                // do something
//                break;
//            case TelephonyManager.SIM_STATE_UNKNOWN:
//                // do something
//                break;
//        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            SubscriptionManager sManager = (SubscriptionManager) mContext.getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                SubscriptionInfo infoSim1 = sManager.getActiveSubscriptionInfoForSimSlotIndex(0);
                SubscriptionInfo infoSim2 = sManager.getActiveSubscriptionInfoForSimSlotIndex(1);
                if (infoSim1 != null || infoSim2 != null) {
                    return true;
                }
            }
        } else {
            TelephonyManager telephonyManager = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
            if (telephonyManager.getSimState() != TelephonyManager.SIM_STATE_ABSENT){
                return true;
            }
        }
        return false;
    }
    private boolean hasMainActivityPermission() {
        for (int i = 0; i < 1; i++) {
            if (ContextCompat.checkSelfPermission(this, strArr[i]) != 0) {
                return false;
            }
        }
        return true;
    }
    private Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean z = true;
        if (Build.VERSION.SDK_INT >= 23) {
            Network activeNetwork = connectivityManager.getActiveNetwork();
            if (activeNetwork == null) {
                return false;
            }
            NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork);
            if (
                    networkCapabilities == null || (
                            !networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) &&
                            !networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) &&
                            !networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) &&
                            !networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH))) {
                z = false;
            }
            return Boolean.valueOf(z);
        }
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetworkInfo == null || !activeNetworkInfo.isConnected()) {
            z = false;
        }
        return Boolean.valueOf(z);
    }

}