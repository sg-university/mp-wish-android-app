package com.binus.mp.wish.services;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.binus.mp.wish.models.Auth;
import com.binus.mp.wish.views.activities.RequestLocationPermission;
import com.huawei.hmf.tasks.OnFailureListener;
import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.hmf.tasks.Task;
import com.huawei.hms.common.ApiException;
import com.huawei.hms.location.FusedLocationProviderClient;
import com.huawei.hms.location.LocationAvailability;
import com.huawei.hms.location.LocationCallback;
import com.huawei.hms.location.LocationRequest;
import com.huawei.hms.location.LocationResult;
import com.huawei.hms.location.LocationServices;
import com.huawei.hms.location.LocationSettingsRequest;
import com.huawei.hms.location.LocationSettingsResponse;
import com.huawei.hms.location.LocationSettingsStatusCodes;
import com.huawei.hms.location.SettingsClient;

import java.util.List;

public class LocationService extends Service {

    public static final String TAG = "LocationUpdatesCallback";
    LocationCallback mLocationCallback;
    LocationRequest mLocationRequest;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private SettingsClient mSettingsClient;

    Auth auth;

    /**
     * Requests a location update and calls back on the specified Looper thread.
     */
    private void requestLocationUpdatesWithCallback() {
        try {
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
            builder.addLocationRequest(mLocationRequest);
            LocationSettingsRequest locationSettingsRequest = builder.build();
            // Before requesting location update, invoke checkLocationSettings to check device settings.
            Task<LocationSettingsResponse> locationSettingsResponseTask = mSettingsClient.checkLocationSettings(locationSettingsRequest);
            locationSettingsResponseTask.addOnSuccessListener(new OnSuccessListener<LocationSettingsResponse>() {
                @Override
                public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                    Log.i(TAG, "check location settings success");
                    mFusedLocationProviderClient
                            .requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.getMainLooper())
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(LocationService.this, "requestLocationUpdatesWithCallback onSuccess", Toast.LENGTH_LONG).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(Exception e) {
                                    Log.e(TAG,
                                            "requestLocationUpdatesWithCallback onFailure:" + e.getMessage());
                                }
                            });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(Exception e) {
                    Log.i(TAG, "checkLocationSetting onFailure:" + e.getMessage());
                    int statusCode = ((ApiException) e).getStatusCode();
                    switch (statusCode) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
//                            try {
//                                //When the startResolutionForResult is invoked, a dialog box is displayed, asking you to open the corresponding permission.
//                                ResolvableApiException rae = (ResolvableApiException) e;
//                                rae.startResolutionForResult(MainActivity.this, 0);
//                            } catch (IntentSender.SendIntentException sie) {
//                                Log.e(TAG, "PendingIntent unable to execute request.");
//                            }
                            break;
                        default:
                            break;
                    }
                }
            });
        } catch (Exception e) {
            Log.i(TAG, "requestLocationUpdatesWithCallback exception:" + e.getMessage());
        }
    }

    /**
     * Removed when the location update is no longer required.
     */
    private void removeLocationUpdatesWithCallback() {
        try {
            Task<Void> voidTask = mFusedLocationProviderClient.removeLocationUpdates(mLocationCallback);
            voidTask.addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(LocationService.this, "removeLocationUpdatesWithCallback onSuccess", Toast.LENGTH_LONG).show();
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(Exception e) {
                            Log.e(TAG, "removeLocationUpdatesWithCallback onFailure:" + e.getMessage());
                        }
                    });
        } catch (Exception e) {
            Log.i(TAG, "removeLocationUpdatesWithCallback exception:" + e.getMessage());
        }
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        requestLocationUpdatesWithCallback();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        removeLocationUpdatesWithCallback();
    }

    @Override
    public IBinder onBind(Intent intent) {
        auth = Auth.getInstance();
        RequestLocationPermission.requestLocationPermission(this);
        // Button click listeners
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        mSettingsClient = LocationServices.getSettingsClient(this);
        mLocationRequest = new LocationRequest();
        // Sets the interval for location update (unit: Millisecond)
        mLocationRequest.setInterval(5000);
        // Sets the priority
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        if (null == mLocationCallback) {
            mLocationCallback = new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    if (locationResult != null) {
                        List<Location> locations = locationResult.getLocations();
                        auth.setLocations(locations);
                    }
                }

                @Override
                public void onLocationAvailability(LocationAvailability locationAvailability) {
                    if (locationAvailability != null) {
                        boolean flag = locationAvailability.isLocationAvailable();
                        Log.i(TAG, "onLocationAvailability isLocationAvailable:" + flag);
                    }
                }
            };
        }
        return null;
    }
}