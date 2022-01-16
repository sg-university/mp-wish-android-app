package com.binus.mp.wish.views.activities.menufragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.binus.mp.wish.R;
import com.binus.mp.wish.apis.PostApi;
import com.binus.mp.wish.contracts.Result;
import com.binus.mp.wish.controllers.Controller;
import com.binus.mp.wish.models.Auth;
import com.binus.mp.wish.models.Post;
import com.binus.mp.wish.views.activities.RequestLocationPermission;
import com.binus.mp.wish.views.adapters.FeedAdapter;
import com.huawei.hmf.tasks.OnFailureListener;
import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.hmf.tasks.Task;
import com.huawei.hms.common.ApiException;
import com.huawei.hms.common.ResolvableApiException;
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

import java.io.IOException;
import java.util.List;
import java.util.Vector;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FeedFragment extends Fragment {
    List<Post> listPost;
    TextView textViewLoading, textViewLocation;
    ProgressDialog dialog;
    Auth auth;
    View viewFeedFragment;

    public static final String TAG = "LocationUpdatesCallback";
    LocationCallback mLocationCallback;
    LocationRequest mLocationRequest;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private SettingsClient mSettingsClient;


    public FeedFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_feed, container, false);
        viewFeedFragment = view;

        textViewLocation = view.findViewById(R.id.fragment_feed_text_view_location);
        auth = Auth.getInstance();

        initLocationService();
        requestLocationUpdatesWithCallback();

        dialog = new ProgressDialog(view.getContext());
        dialog.setMessage("Please wait for a moment...");
        dialog.setCancelable(false);
        dialog.setInverseBackgroundForced(false);
//        dialog.show();

        listPost = new Vector<Post>();
        textViewLoading = view.findViewById(R.id.welcomeTxt);
        Controller<PostApi> controller = new Controller<>(PostApi.class);
        Call<Result<List<Post>>> call = controller.getApi().readAll();
        call.enqueue(new Callback<Result<List<Post>>>() {
            @Override
            public void onResponse(Call<Result<List<Post>>> call, Response<Result<List<Post>>> response) {
                if (response.isSuccessful()) {
                    Result<List<Post>> posts = response.body();
                    Log.i("FeedActivity", "post0 count : " + posts.getContent().size());
                    setListPost(posts.getContent());
                    setRecyclerView(view);
                } else {
                    Log.i("FeedActivity", "Error : " + response.code());
//                    txtView.setText("Success");
                }
                dialog.hide();
            }

            @Override
            public void onFailure(Call<Result<List<Post>>> call, Throwable t) {
                t.printStackTrace();
                dialog.hide();
            }
        });
        return view;
    }


    public void initLocation() throws IOException {
        List<Location> locations = auth.getLocations();
        Location location = locations.get(0);
        Double latitude = location.getLatitude();
        Double longitude = location.getLongitude();
//        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
//        List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1) ;
//        String cityName = addresses.get(0).getAddressLine(0);
//        String stateName = addresses.get(0).getAddressLine(1);
//        String countryName = addresses.get(0).getAddressLine(2);
        String locationText = String.format("Lat: %s\nLong: %s", latitude.toString(), longitude.toString());

        textViewLocation.setText(locationText);
    }


    public void initLocationService() {
        RequestLocationPermission.requestLocationPermission(getContext());
        // Button click listeners
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());
        mSettingsClient = LocationServices.getSettingsClient(getContext());
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
                        for (Location location : locations) {
                            Toast.makeText(getContext(),
                                    "onLocationResult location[Longitude,Latitude,Accuracy]:" + location.getLongitude()
                                            + "," + location.getLatitude() + "," + location.getAccuracy(), Toast.LENGTH_LONG).show();
                        }
                        auth.setLocations(locations);
                        try {
                            initLocation();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
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
                    Toast.makeText(getContext(), "removeLocationUpdatesWithCallback onSuccess", Toast.LENGTH_LONG).show();
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
                                    Toast.makeText(getContext(), "requestLocationUpdatesWithCallback onSuccess", Toast.LENGTH_LONG).show();
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
                            try {
                                //When the startResolutionForResult is invoked, a dialog box is displayed, asking you to open the corresponding permission.
                                ResolvableApiException rae = (ResolvableApiException) e;
                                rae.startResolutionForResult((Activity) getContext(), 0);
                            } catch (IntentSender.SendIntentException sie) {
                                Log.e(TAG, "PendingIntent unable to execute request.");
                            }
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

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                Log.i(TAG, "onRequestPermissionsResult: apply LOCATION PERMISSION successful");
            } else {
                Log.i(TAG, "onRequestPermissionsResult: apply LOCATION PERMISSSION  failed");
            }
        }

        if (requestCode == 2) {
            if (grantResults.length > 2 && grantResults[2] == PackageManager.PERMISSION_GRANTED
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                Log.i(TAG, "onRequestPermissionsResult: apply ACCESS_BACKGROUND_LOCATION successful");
            } else {
                Log.i(TAG, "onRequestPermissionsResult: apply ACCESS_BACKGROUND_LOCATION  failed");
            }
        }
    }


    private void setRecyclerView(View view) {
        RecyclerView rv = view.findViewById(R.id.feedRV);
        Log.i("FeedActivity", "post2 count : " + listPost.size());
        FeedAdapter adapter = new FeedAdapter(listPost);

        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(view.getContext()));
        rv.setAdapter(adapter);

        Auth auth = Auth.getInstance();

        textViewLoading.setText("Welcome " + auth.getAccount().getName());
    }

    private void setListPost(List<Post> posts) {
        this.listPost = posts;
        Log.i("FeedActivity", "post count : " + listPost.size());
    }
}