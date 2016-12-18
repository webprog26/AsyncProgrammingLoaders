package com.example.webprog26.asynctaskloader.async_task_loader;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.webprog26.asynctaskloader.R;

/**
 * Created by webprog26 on 18.12.2016.
 */

public class BitcoinExchangeRateFragment extends Fragment implements LoaderManager.LoaderCallbacks<Double>{

    private static final String TAG = BitcoinExchangeRateLoader.TAG;

    public static final int BITCOIN_EURO_EXRATE_LOADER_ID = 1;
    private static final String CURRENNCY_KEY="currency";
    private static final String REFRESH_INTERNAL="internal";

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i(TAG,"onCreateView created");
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.bitcoin_exchange_rate_fragment_layout,
                container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.i(TAG,"Activity created");
        super.onActivityCreated(savedInstanceState);
        LoaderManager lm = getActivity().getSupportLoaderManager();
        Bundle bundle = new Bundle();
        bundle.putString(CURRENNCY_KEY,"EUR");
        bundle.putInt(REFRESH_INTERNAL, 5000);
        lm.initLoader(BITCOIN_EURO_EXRATE_LOADER_ID, bundle, BitcoinExchangeRateFragment.this);
    }

    @Override
    public Loader<Double> onCreateLoader(int id, Bundle args) {
        Loader res = null;
        Log.i(TAG, "LoaderCallbacks.onCreateLoader[" + id + "]");
        switch (id) {
            case BITCOIN_EURO_EXRATE_LOADER_ID:
                res = new BitcoinExchangeRateLoader(getActivity(),
                        args.getString(CURRENNCY_KEY),
                        args.getInt(REFRESH_INTERNAL));
                break;
        }
        return res;
    }

    @Override
    public void onLoadFinished(Loader<Double> loader, Double data) {

        Log.i("BitcoinExchangeRate", "LoaderCallbacks.onLoadFinished[" + loader.getId() + "]");
        switch (loader.getId()) {
            case BITCOIN_EURO_EXRATE_LOADER_ID:
                // Initialize recycler view
                TextView tv  = (TextView) getView().findViewById(R.id.temperature);
                tv.setText(data.toString());
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<Double> loader) {

    }
}
