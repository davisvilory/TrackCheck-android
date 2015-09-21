package beteam.viloco.trackcheck.util;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;

import beteam.viloco.trackcheck.activity.BaseClass;
import beteam.viloco.trackcheck.repositorios.LogErrorRepository;

public class GPSTracker extends Service implements LocationListener {

    private final Context mContext;

    boolean isGPSEnabled = false;
    boolean isNetworkEnabled = false;
    boolean canGetLocation = false;

    Location location;
    double latitude;
    double longitude;

    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 5; // 5 meters
    private static final long MIN_TIME_BW_UPDATES = 1000 * 30; // 30 seconds
    protected LocationManager locationManager;

    public GPSTracker(Context context) {
        this.mContext = context;
        getLocation();
    }

    public Location getLocation() {
        try {
            locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);

            Criteria criteria = new Criteria();
            criteria.setPowerRequirement(Criteria.NO_REQUIREMENT); // Chose your desired power consumption level.
            criteria.setAccuracy(Criteria.ACCURACY_FINE); // Choose your accuracy requirement.
            criteria.setSpeedRequired(true); // Chose if speed for first location fix is required.
            criteria.setAltitudeRequired(false); // Choose if you use altitude.
            criteria.setBearingRequired(false); // Choose if you use bearing.
            criteria.setCostAllowed(false); // Choose if this provider can waste money :-)

            String proname = locationManager.getBestProvider(criteria, true);
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                // no network provider is enabled
            } else {
//                if (isNetworkEnabled) {
//                    locationManager.requestLocationUpdates(proname, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
//                    if (locationManager != null) {
//                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
//                        if (location != null) {
//                            latitude = location.getLatitude();
//                            longitude = location.getLongitude();
//                            canGetLocation = true;
//                        }
//                    }
//                }
//                if (isGPSEnabled) {
                    locationManager.requestLocationUpdates(proname, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    if (locationManager != null) {
                        location = locationManager.getLastKnownLocation(proname);
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                            canGetLocation = true;
                        }
                    }
//                }
            }
        } catch (SecurityException ex) {
            LogErrorRepository.BuildLogError(ex, mContext);
            BaseClass.ToastAlert("No se han proporcionado los accesos necesarios", mContext);
        }

        return location;
    }

    public void stopUsingGPS() {
        try {
            if (locationManager != null) {
                locationManager.removeUpdates(GPSTracker.this);
            }
        } catch (SecurityException ex) {
            LogErrorRepository.BuildLogError(ex, mContext);
            BaseClass.ToastAlert("No se han proporcionado los accesos necesarios", mContext);
        }
    }

    public double getLatitude() {
        if (location != null) {
            latitude = location.getLatitude();
        }
        return latitude;
    }

    public double getLongitude() {
        if (location != null) {
            longitude = location.getLongitude();
        }
        return longitude;
    }

    public boolean isGPSEnabled() {
        return this.isGPSEnabled;
    }

    public boolean canGetLocation() {
        return this.canGetLocation;
    }

    public void showSettingsGPSAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

        // Setting Dialog Title
        alertDialog.setTitle("Habilitar GPS");

        // Setting Dialog Message
        alertDialog.setMessage("GPS no esta habilitado. Por favor habilite el GPS");

        // On pressing Settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mContext.startActivity(intent);
            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    @Override
    public void onLocationChanged(Location location) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

}