package com.sagar.stormy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.webkit.ConsoleMessage.MessageLevel.LOG;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String apiKey = "5f8112da8cbb3f229389827205d2160e";
        double latitude = 37.8267;
        double longitude = -122.4233;
        String forecastURL = "https://api.darksky.net/forecast/" +apiKey + "/" +latitude + "," +longitude;

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(forecastURL)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    Log.v(TAG, response.body().string());
                    if(response.isSuccessful()){
                        Log.v(TAG, response.body().string());
                    }
                    else{
                        alertUserAboutError();
                    }
                } catch (IOException e) {
                    Log.e(TAG, "IO Exception");
                }
            }
        });


    }

    private void alertUserAboutError() {

    }
}
