package com.example.vctor.findme;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

/**
 * Created by Víctor on 26/04/2017.
 */

public class BackgroundTask extends AsyncTask<String, Void, String>
{
    public AsyncResponse delegate = null;
    public static int var;

    public BackgroundTask(AsyncResponse delegate)
    {
        this.delegate = delegate;
    }
    public interface AsyncResponse
    {
        void processFinish(String output);
    }

    @Override
    protected void onPreExecute()
    {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {
        return null;
    }
    // Function to check if there is a working internet connection available

    public static boolean isNetworkAvailable( Context a) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) a.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
