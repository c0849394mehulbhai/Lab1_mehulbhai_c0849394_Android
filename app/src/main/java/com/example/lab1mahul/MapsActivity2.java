package com.example.lab1mahul;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.lab1mahul.databinding.ActivityMaps2Binding;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MapsActivity2 extends FragmentActivity implements OnMapReadyCallback, SeekBar.OnSeekBarChangeListener{

    SeekBar polygonSeekbar, polylineSeekbar;

    Button btnDraw, btnClear;
    private GoogleMap gMap;
    private ActivityMaps2Binding binding;

    Polygon polygon = null;
    List<LatLng> latLngList = new ArrayList<>();
    List<Marker> markerList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps2);

        btnDraw = findViewById(R.id.btnDraw);
        btnClear = findViewById(R.id.btnClear);

        polylineSeekbar = findViewById(R.id.seekBarPolyLine);
        polygonSeekbar = findViewById(R.id.seekBarPolygon);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        btnDraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (polygon != null) {
                    polygon.remove();
                }
                PolygonOptions polygonOptions = new PolygonOptions().clickable(true).addAll(latLngList);
                polygon = gMap.addPolygon(polygonOptions);
                polygon.setStrokeColor(Color.RED);
                polygon.setFillColor(Color.GREEN);

            }
        });

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (polygon != null) polygon.remove();
                for (Marker m : markerList) {
                    m.remove();
                }
                latLngList.clear();
                markerList.clear();
            }
        });


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;
        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng latLng) {
                MarkerOptions markerOptions = new MarkerOptions().position(latLng);
                Marker marker = gMap.addMarker(markerOptions);
                latLngList.add(latLng);
                markerList.add(marker);
            }
        });

        gMap.getUiSettings().setZoomControlsEnabled(true);
        if (markerList.size() == 0) {
            LatLngBounds boundsNorthAmerica = new LatLngBounds(new LatLng(43.273909, -127.120020), new LatLng(43.273909, -68.409081));
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(boundsNorthAmerica, 10);
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    gMap.moveCamera(cameraUpdate);
                }
            }, 100);
        }

        gMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {

                if (polygon != null) polygon.remove();
                for (Marker marker : markerList) marker.remove();
                markerList.clear();
                latLngList.clear();

            }
        });

        gMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
                Boolean isFound = false;
                for (Marker mPoint : markerList) {
                    if (Math.abs(mPoint.getPosition().latitude - marker.getPosition().latitude) < 0.05 && Math.abs(marker.getPosition().longitude - marker.getPosition().longitude) < 0.05) {
                        isFound = true;
                    }
                }
                if (isFound == false) {
                    marker.remove();
                }
                return false;
            }
        });
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        final int min = 0;
        final int max = 255;
        final int random = new Random().nextInt((max - min) + 1) + min;
        final int random1 = new Random().nextInt((max - min) + 1) + min;

        float[] hsvColor = {random, random, 0};
        hsvColor[2] = 360f * i / i;

        float[] hsvColor1 = {random1, 0, random1};
        hsvColor1[1] = 360f * i / i;

        switch (seekBar.getId()){
            case R.id.seekBarPolyLine:
                if(polygon != null) {
                    polygon.setStrokeColor(Color.HSVToColor(hsvColor1));
                }
                break;
            case R.id.seekBarPolygon:
                if (polygon != null) {
                    polygon.setFillColor(Color.HSVToColor(hsvColor1));
                }
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}