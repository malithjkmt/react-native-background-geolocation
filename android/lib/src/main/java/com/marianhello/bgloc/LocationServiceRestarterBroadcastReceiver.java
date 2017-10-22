package com.marianhello.bgloc;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import org.json.JSONException;

import com.marianhello.bgloc.Config;
import com.marianhello.bgloc.LocationService;
import com.marianhello.bgloc.data.DAOFactory;
import com.marianhello.bgloc.data.ConfigurationDAO;

public class LocationServiceRestarterBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = LocationServiceRestarterBroadcastReceiver.class.getName();

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "Received Location service stoped");

        ConfigurationDAO dao = DAOFactory.createConfigurationDAO(context);
        Config config = null;

        try {
            config = dao.retrieveConfiguration();
        } catch (JSONException e) {
            Log.d(TAG, "Error in ConfigurationDAO.retrieveConfiguration");
            //noop
        }

        if (config == null) {
            Log.d(TAG, "ConfigurationDAO.retrieveConfiguration is null");
            return;
        }

        Intent locationServiceIntent = new Intent(context, LocationService.class);
        locationServiceIntent.addFlags(Intent.FLAG_FROM_BACKGROUND);
        locationServiceIntent.putExtra("config", config);

        context.startService(locationServiceIntent);
        Log.d(TAG, "Location service Restarted");
    }
}
