package com.sagar.stormy;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.webkit.ConsoleMessage.MessageLevel.LOG;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();

    private CurrentWeather currentWeather;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView darkSky = findViewById(R.id.darkskyAttribution);
        darkSky.setMovementMethod(LinkMovementMethod.getInstance());

        String apiKey = "5f8112da8cbb3f229389827205d2160e";
        double latitude = 37.8267;
        double longitude = -122.4233;
        String forecastURL = "https://api.darksky.net/forecast/" +apiKey + "/" +latitude + "," +longitude;


        if(isNetwrokAvailable()) {
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
                        String jsonData = response.body().string();
                        Log.v(TAG, jsonData);
                        if (response.isSuccessful()) {
                            currentWeather = getCurrentDetails(jsonData);
                        } else {
                            alertUserAboutError();
                        }
                    } catch (IOException e) {
                        Log.e(TAG, "IO Exception", e);
                    } catch (JSONException e){
                        Log.e(TAG, "JSON Excetion caught", e);
                    }
                }
            });
        }

    }

    private CurrentWeather getCurrentDetails(String jsonData) throws JSONException{
        JSONObject forecast = new JSONObject(jsonData);

        String timezone = forecast.getString("timezone");
        Log.i(TAG, "From JSON:" + timezone);


        JSONObject currently = new JSONObject("currently");

        CurrentWeather currentWeather = new CurrentWeather();

        currentWeather.setHumidity(currently.getDouble("humidity"));
        currentWeather.setTime(currently.getLong("time"));
        currentWeather.setIcon(currently.getString("icon"));
        currentWeather.setLocationLabel("Alcatraz Island, CA");
        currentWeather.setPrecipChance(currently.getDouble("precipChance"));
        currentWeather.setSummary(currently.getString("summary"));
        currentWeather.setSummary(currently.getString("temperature"));
        currentWeather.setTimeZone(timezone);

        Log.d(TAG, currentWeather.getFormattedTime());

        return currentWeather;
    }

    private boolean isNetwrokAvailable() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        boolean isAvaialable = false;

        if(networkInfo != null && networkInfo.isConnected()){
            isAvaialable = true;
        }
        else{
            Toast.makeText(this, R.string.network_unavailable_msg, Toast.LENGTH_LONG).show();
        }
        return isAvaialable;
    }

    private void alertUserAboutError() {
        AlertDialogFragment dialog = new AlertDialogFragment();
        dialog.show(getFragmentManager(), "error_dialog");
    }
}
