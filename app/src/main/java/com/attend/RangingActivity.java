package com.attend;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;

import com.comix.overwatch.HiveProgressView;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.Collection;

public class RangingActivity extends Activity implements BeaconConsumer {
    protected static final String TAG = "RangingActivity";
    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;
    public static ClassList classObject;
    private BeaconManager beaconManager;
    private Integer countDown = 0;
    private String id1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranging);

        Intent intent = getIntent();
        classObject = (ClassList) intent.getSerializableExtra("classObject");
        id1 = classObject.getBluetooth_address();

        beaconManager = BeaconManager.getInstanceForApplication(this);
        beaconManager.bind(this);
        checkPermission();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        beaconManager.unbind(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (beaconManager.isBound(this)) beaconManager.setBackgroundMode(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (beaconManager.isBound(this)) beaconManager.setBackgroundMode(false);
    }

    @Override
    public void onBeaconServiceConnect() {
        beaconManager.setRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                countDown += 1;
                Log.i("RangingActivity", "Search for beacon in progress");
                //TODO: change if condition to countDown >= 60
                if(countDown >= 60) {
                    String connectionStatus = "false";
                    Intent intent = new Intent(RangingActivity.this, MarkAttendance.class);
                    intent.putExtra("connectionStatus", connectionStatus);
                    intent.putExtra("classObject", classObject);
                    startActivity(intent);
                    finish();
                }
                if (beacons.size() > 0) {
                    Beacon firstBeacon = beacons.iterator().next();
                    String beaconName = firstBeacon.getId1().toString();
                    Double distance = firstBeacon.getDistance();
                    Log.i("RangingActivity", "The first beacon " + beaconName + " is about " + distance + " meters away.");
                    if(beaconName.equals(id1) && distance < 5) {
                        String connectionStatus = "true";
                        Intent intent = new Intent(RangingActivity.this, MarkAttendance.class);
                        intent.putExtra("connectionStatus", connectionStatus);
                        intent.putExtra("classObject", classObject);
                        startActivity(intent);
                        finish();
                    }
                }
            }

        });

        try {
            beaconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));
        } catch (RemoteException e) {
        }
    }

    @TargetApi(23)
    private void checkPermission(){
        // Android M Permission check 
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("This app needs location access");
                builder.setMessage("Please grant location access so this app can detect beacons.");
                builder.setPositiveButton(android.R.string.ok, null);
                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    public void onDismiss(DialogInterface dialog) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_COARSE_LOCATION);
                        }
                    }
                });

                builder.show();
            }
            else{
                HiveProgressView hiveProgressView = (HiveProgressView) findViewById(R.id.hiveProgressView);
                hiveProgressView.setVisibility(View.VISIBLE);
            }
        }
        Log.d("Permission", "checkPermission: Permission Checked");
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_COARSE_LOCATION: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("MainActivity Error", "coarse location permission granted");
                    HiveProgressView hiveProgressView = (HiveProgressView) findViewById(R.id.hiveProgressView);
                    hiveProgressView.setVisibility(View.VISIBLE);
                } else {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Functionality limited");
                    builder.setMessage("Since location access has not been granted, this app will not be able to discover beacons when in the background.");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {

                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            String connectionStatus = "false";
                            Intent intent = new Intent(RangingActivity.this, MarkAttendance.class);
                            intent.putExtra("connectionStatus", connectionStatus);
                            intent.putExtra("classObject", classObject);
                            startActivity(intent);
                            finish();
                        }

                    });
                    builder.show();
                }
                return;
            }
        }
    }

    @Override
    public void onBackPressed() {

    }
}