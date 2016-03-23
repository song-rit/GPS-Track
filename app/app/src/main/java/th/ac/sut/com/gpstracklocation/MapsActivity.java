package th.ac.sut.com.gpstracklocation;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.appdatasearch.GetRecentContextCall;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import th.ac.sut.com.gpstracklocation.constant.Constant;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private double latitude;
    private double longitude;
    private LocationManager locationManager;
    private OkHttpClient client;
    private Button button;
    private ProgressDialog progressDialog;

    private static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        client = new OkHttpClient();
        client.setConnectTimeout(15, TimeUnit.SECONDS); // connect timeout
        client.setReadTimeout(15, TimeUnit.SECONDS);    // socket timeout

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = new ProgressDialog(MapsActivity.this);
                progressDialog.setMessage("กำลังบันทึก");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();
                JSONObject JSONCriteria = new JSONObject();
//                try {
//                    JSONCriteria.put("latitude", latitude);
//                    JSONCriteria.put("longitude", longitude);
//                    Request request = HTTPPostAsynchronous(Constant.getSaveLocation , JSONCriteria.toString());
//                    client.newCall(request).enqueue(new Callback() {
//                        @Override
//                        public void onFailure(Request request, IOException e) {
//
//                        }
//
//                        @Override
//                        public void onResponse(Response response) throws IOException {
//
//                        }
//                    });
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
            }
        });

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(final GoogleMap googleMap) {

        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();

                String str = "Latitude: " + location.getLatitude() + " \nLongitude: " + location.getLongitude();
                Toast.makeText(getBaseContext(), str, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        mMap = googleMap;

        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                3,   // 3 sec
                100, locationListener);


        LatLng latLng = new LatLng(latitude, longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(16.0f));
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));

        //UI setting
        UiSettings ui = mMap.getUiSettings();
        ui.setZoomControlsEnabled(true);
        ui.setZoomGesturesEnabled(true);
        ui.setRotateGesturesEnabled(true);
        ui.setCompassEnabled(true);
        ui.setTiltGesturesEnabled(true);
        ui.setScrollGesturesEnabled(true);
        ui.setMyLocationButtonEnabled(true);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);


        }
//        timerDelayRemoveDialog(googleMap, 10000);

        mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {

                LatLng latLng = new LatLng(latitude, longitude);
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(16.0f));
//                CameraUpdate center=
//                        CameraUpdateFactory.newLatLng(sydney);
//                mMap.moveCamera(center);
//                CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);
//                mMap.moveCamera(center);
//                mMap.animateCamera(zoom);


//                mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//                mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
//                mMap.animateCamera( CameraUpdateFactory.zoomTo( 16.0f ) );
                System.out.println("Click");

                return true;
            }
        });
    }

    public void timerDelayRemoveDialog(final GoogleMap googleMap, long time) {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                System.out.println("#######-Hello World-#########");
                mMap = googleMap;
                LatLng sydney = new LatLng(16.0779105, 104);
                mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));

                mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(16.0f));

            }
        }, time);
    }

    Request HTTPPostAsynchronous(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        return request;
    }
}
