package android.abadzheva.findweatherapp;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class GetData extends AsyncTask <URL, Void, String> {

    private static final String TAG = "GetData";

    protected String getResponseFromHttpGetUrl (URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream is = urlConnection.getInputStream();
            Scanner sc = new Scanner(is);
            sc.useDelimiter("\\A");
            boolean hasInput = sc.hasNext();
            String resFromUrl;
            if (hasInput) {
                resFromUrl = sc.next();
                return resFromUrl;
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

    protected interface AsyncResponse {
        void processFinish(String output);
    }
    public AsyncResponse delegate;

    public GetData (AsyncResponse delegate){
        this.delegate = delegate;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        Log.d(TAG, "onPreExecute: called");
    }

    @Override
    protected String doInBackground(URL[] url) {
        Log.d(TAG, "doInBackground: called");
        URL urlQuery = url[0];
        String result = null;
        try {
            result = getResponseFromHttpGetUrl(urlQuery);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    protected void onPostExecute(String result) {
//        super.onPostExecute(o);
        Log.d(TAG, "onPostExecute: called");
        Log.d(TAG, "onPostExecute: " + result);
        delegate.processFinish(result);
    }
}
