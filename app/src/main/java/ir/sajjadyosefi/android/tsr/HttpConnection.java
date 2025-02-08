package ir.sajjadyosefi.android.tsr;

import android.content.Context;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpConnection {
    String apiUrlConfirm        = "https://test.sajjadyosefi.ir/Api/User/confirmUserMobile";
    String apiUrlReset          = "https://test.sajjadyosefi.ir/Api/User/resetPassword";


    Context context;


    public HttpConnection(Context context) {
        this.context = context;
    }

    public String execute(String json, SMSBackgroundService.SMSTYPE smsType) throws IOException{
        URL url ;

//        apiUrlReset = "http://192.168.1.3:80/Api/User/resetPassword" ;

        if (smsType.equals(SMSBackgroundService.SMSTYPE.TSRSMS_CONFIRM))
            url =  new URL(apiUrlConfirm);
        else if (smsType.equals(SMSBackgroundService.SMSTYPE.TSRSMS_RESET))
            url =  new URL(apiUrlReset);
        else
            url =  new URL(apiUrlConfirm);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
        connection.connect();

        PrintStream ps = new PrintStream(connection.getOutputStream());
        ps.print(json);
        ps.close();

        if (connection.getResponseCode() != 200){
            Toast.makeText(context, connection.getResponseCode() + " " + json, Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText(context, connection.getResponseCode() + " " + json, Toast.LENGTH_LONG).show();
        }

        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(),"UTF-8"));
        StringBuilder stringBuilder = new StringBuilder();
        String line;

        while ((line = br.readLine())  != null){
            stringBuilder.append(line);
        }
        br.close();

        return stringBuilder.toString();
    }
}
