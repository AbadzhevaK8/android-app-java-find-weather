package android.abadzheva.findweatherapp;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

public class MainActivity extends AppCompatActivity implements GetData.AsyncResponse{
    private static final String TAG = "MainActivity";
    private EditText cityField;
    private TextView cityName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cityField = findViewById(R.id.cityField);
        cityName = findViewById(R.id.cityName);
    }

    @Override
    public void processFinish(String output) {
        Log.d(TAG, "processFinish: " + output);
        try {
            JSONObject resultJSON = new JSONObject(output);
            JSONObject weather = resultJSON.getJSONObject("main");
            JSONObject sys = resultJSON.getJSONObject("sys");

            TextView temp1 = findViewById(R.id.temperature2);
            temp1.setText(weather.getString("temp"));

            TextView pressure2 = findViewById(R.id.pressure2);
            pressure2.setText(weather.getString("pressure"));

            TextView rise2 = findViewById(R.id.rise2);
            String timeSunrise = sys.getString("sunrise");
            long time = Long.parseLong(timeSunrise);
            long offset = Long.parseLong(resultJSON.getString("timezone"));
            OffsetDateTime offsetDateTime = Instant.ofEpochSecond(time).atOffset(ZoneOffset.ofTotalSeconds((int) offset));
            rise2.setText(offsetDateTime.toLocalTime().toString());

            TextView set2 = findViewById(R.id.set2);
            String timeSet = sys.getString("sunset");
            time = Long.parseLong(timeSet);
            offsetDateTime = Instant.ofEpochSecond(time).atOffset(ZoneOffset.ofTotalSeconds((int) offset));
            set2.setText(offsetDateTime.toLocalTime().toString());

////=====================================================================
//            long time = Long.parseLong(timeSunrise);
//            long offset = Long.parseLong(resultJSON.getString("timezone"));
//            OffsetDateTime offsetDateTime = Instant.ofEpochSecond(time).atOffset(ZoneOffset.ofTotalSeconds((int) offset));
//            Log.d(TAG, "testTIme: " + offsetDateTime.toLocalTime());
//======================================================================

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void searchCity(View v) {
        URL url = buildUrl(cityField.getText().toString());
        cityName.setText(cityField.getText().toString());
        new GetData(this).execute(url);
        cityField.onEditorAction(EditorInfo.IME_ACTION_DONE);
    }


    private URL buildUrl (String city) {
        String BASE_URL = "https://api.openweathermap.org/data/2.5/weather";
        String PARAM_CITY = "q";
        String PARAM_APPID = "appid";
        String appid_value = "a4c0639c9f32776a6b3816e0ffa34d26";
        String PARAM_METRIC = "units";
        String metric_val = "metric";

        Uri.Builder builtUri = Uri.parse(BASE_URL).buildUpon().appendQueryParameter(PARAM_CITY, city).appendQueryParameter(PARAM_APPID, appid_value).appendQueryParameter(PARAM_METRIC, metric_val);
        URL url = null;

        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }
}