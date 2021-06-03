package com.example.match_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.CarrierConfigManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.crypto.spec.GCMParameterSpec;


public class Login03Activity extends AppCompatActivity {
    private Button button1;
    private TextView txtResult;
    double longitude = 0, latitude = 0;
    List<Address> address = null;
    HashSet<String> addr = new HashSet<>(50);



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login03);
        checkDangerousPermissions();
        button1 = findViewById(R.id.button1);
        txtResult = findViewById(R.id.txtResult);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startLocationService();
                getAddress();

                Iterator<String> iter = addr.iterator();
                while(iter.hasNext()){
                    txtResult.append(iter.next()+"\n");
                }
            }
        });
    }

    private void startLocationService() {
        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        GPSListener gpsListener = new GPSListener();
        long minTime = 1000;
        float minDistance = 100;

        try {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }

            manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTime, minDistance, gpsListener);
            Location lastLocation=manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            if(lastLocation != null){
                latitude = lastLocation.getLatitude();  // 위도
                longitude = lastLocation.getLongitude();  // 경도
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void getAddress() {
        Geocoder geocoder = new Geocoder(this);
        for (int i=0; i<7; i++) {

            for (int j=0; j<7; j++) {
                try {
/*                 address = geocoder.getFromLocation(latitude, longitude, 1);
                 String msg = address.get(0).getAdminArea()+" "+address.get(0).getSubLocality()+" "+address.get(0).getThoroughfare();
                 txtResult.append(msg);*/
                    double latitudeA = latitude;
                    double longitudeA = longitude;

                    latitudeA = (latitudeA - 0.01*(3-i));
                    longitudeA = (longitudeA - 0.01*(3-j));

                    txtResult.append(longitudeA+","+latitudeA+"\n");

                    address = geocoder.getFromLocation(latitudeA, longitudeA, 1);
                    String msg = address.get(0).getAdminArea() + " " + address.get(0).getSubLocality() + " " + address.get(0).getThoroughfare();


                    addr.add(msg.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    } //getAddress()

    private class GPSListener implements LocationListener{

        @Override
        public void onLocationChanged(@NonNull Location location) {
            Double latitude = location.getLatitude();  // 위도
            Double longitude = location.getLongitude();  // 경도
        }
    }
    private void checkDangerousPermissions() {
        String[] permissions = {
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_WIFI_STATE
        };

        int permissionCheck = PackageManager.PERMISSION_GRANTED;
        for (int i = 0; i < permissions.length; i++) {
            permissionCheck = ContextCompat.checkSelfPermission(this, permissions[i]);
            if (permissionCheck == PackageManager.PERMISSION_DENIED) {
                break;
            }
        }

        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
        } else {
            Toast.makeText(this, "권한 설정 필요함", Toast.LENGTH_LONG).show();
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[0])) {
                Toast.makeText(this, "권한 설명 필요함.", Toast.LENGTH_LONG).show();
            } else {
                ActivityCompat.requestPermissions(this, permissions, 1);
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 1) {
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, permissions[i] + " 권한이 승인됨.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, permissions[i] + " 권한이 승인되지 않음.", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}


