package ir.sajjadyosefi.android.tsr;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Sample {
    String apiUrl = "http://test.sajjadyosefi.ir";
    Context context;
    String json = "";

    public Sample(Context context) {
        this.context = context;
    }

    public String execute() throws IOException{
        URL url = new URL(apiUrl);
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
            Toast.makeText(context, connection.getResponseCode() + "", Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText(context, connection.getResponseCode() + "", Toast.LENGTH_LONG).show();
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
