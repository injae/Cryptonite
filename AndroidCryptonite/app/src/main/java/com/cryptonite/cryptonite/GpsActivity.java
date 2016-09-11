package com.cryptonite.cryptonite;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import Function.GpsInfo;

public class GpsActivity extends Activity implements OnMapReadyCallback {

    private Button btnSubmitLoc;
    private Activity activity = this;

    // GPSTracker class
    private GpsInfo gps;
    public GoogleMap mgoogleMap;
    private Marker marker;
    private Circle circle;

    static final LatLng SEOUL = new LatLng(37.56, 126.97);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps);

        btnSubmitLoc = (Button) findViewById(R.id.btn_start);

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



        // GPS 정보를 보여주기 위한 이벤트 클래스 등록
        btnSubmitLoc.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                gps.stopUsingGPS();
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        this.mgoogleMap = googleMap;

        try {

            gps = new GpsInfo(GpsActivity.this, activity, btnSubmitLoc,this);

            mgoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom( SEOUL, 15));

            mgoogleMap.animateCamera(CameraUpdateFactory.zoomTo(12), 2000, null);

            // GPS 사용유무 가져오기
            if (gps.isGetLocation()) {
                double latitude = gps.getLatitude();
                double longitude = gps.getLongitude();
                float accuracy = gps.getAccuracy();

                LatLng loc = new LatLng(latitude,longitude);
                mgoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc,18));

                marker = mgoogleMap.addMarker(new MarkerOptions().position(loc));
                circle = mgoogleMap.addCircle(new CircleOptions().center(loc).radius((1500/accuracy)).fillColor(0x30ff0000).strokeColor(Color.BLACK).strokeWidth(2));


            } else {
                // GPS 를 사용할수 없으므로
                gps.showSettingsAlert();
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    public void removeMarker(){
        if(marker!=null) {
            marker.remove();
            circle.remove();
        }
    }

    public void addMarker(MarkerOptions marker,CircleOptions circle){
        this.marker = mgoogleMap.addMarker(marker);
        this.circle = mgoogleMap.addCircle(circle);
    }
}

