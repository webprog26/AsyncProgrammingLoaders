package com.example.webprog26.asynctaskloader.async_task_loader;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.os.Handler;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by webprog26 on 18.12.2016.
 */

public class BitcoinExchangeRateLoader extends android.support.v4.content.AsyncTaskLoader<Double> {

    public static final String TAG = "ExchangeRateLoader";

    private Double mExchangeRate = null;
    private long    mRefreshinterval;
    private Handler mHandler;
    private String mCurrency;

    private final Runnable refreshRunnable = new Runnable() {
        @Override
        public void run() {
            onContentChanged();
        }
    };

    BitcoinExchangeRateLoader(Context ctx, String currency,int refreshinterval) {
        super(ctx);
        this.mRefreshinterval = refreshinterval;
        this.mHandler = new Handler();
        this.mCurrency= currency;
    }

    @Override
    protected void onStartLoading() {
        Log.i(TAG, "Starting");
        if (mExchangeRate != null) {
            // If we currently have a result available, deliver it
            // immediately.
            deliverResult(mExchangeRate);
        }
        if (takeContentChanged() || mExchangeRate == null) {
            // If the mData has changed since the last time it was loaded
            // or is not currently available, start a load.
            forceLoad();
        }
    }


    @Override
    protected void onStopLoading() {
        Log.i(TAG, "Stopping");
        // Attempt to cancel the current load task if possible.
        cancelLoad();
    }

    @Override
    public void onCanceled(Double data) {
        onContentChanged();
    }


    @Override
    protected void onReset() {
        super.onReset();
        Log.i(TAG, "Reset");
        // Ensure the loader is stopped
        onStopLoading();
        // At this point we can release the resources associated with 'apps'
        // if needed.
        if (mExchangeRate != null) {
            mExchangeRate = null;
        }
        // Stop monitoring for changes.
        mHandler.removeCallbacks(refreshRunnable);
    }

    @Override
    public void deliverResult(Double result) {
        this.mExchangeRate = result;
        super.deliverResult(result);
    }

    @Override
    public Double loadInBackground() {
        Double result = null;
        Log.i(TAG, "Refreshing the rate in background");
        try {
            StringBuilder builder = new StringBuilder();
            URL url = new URL("https://blockchain.info/ticker");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            // Starts the query
            conn.connect();
            int responseCode = conn.getResponseCode();
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), "UTF-8"));
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            JSONObject obj = new JSONObject(builder.toString());
            result = obj.getJSONObject(mCurrency).getDouble("last");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    protected void onForceLoad() {
        super.onForceLoad();
        mHandler.removeCallbacks(refreshRunnable);
        if (!isReset())
            mHandler.postDelayed(refreshRunnable, mRefreshinterval);
    }

}
