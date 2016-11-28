package org.grupovialibre.dev.appluchar;

import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.grupovialibre.dev.appluchar.entities.Report;

import java.util.ArrayList;
import java.util.List;

public class CurrentReportsMapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    private List<Report> reports;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_reports_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        // handle intent extras
        Bundle extras = getIntent().getExtras();
        if(extras != null)
        {
            if (extras.getParcelableArrayList("locations") != null) {
                reports = extras.getParcelableArrayList("locations");

                if(reports!=null){

                    //Build locations


                }

            }
        }




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
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        // Add a marker in Sydney and move the camera
        LatLng colombia = new LatLng(5.088637,-74.196760);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(colombia, 5));


        if(reports!=null && !reports.isEmpty()){

            if(reports.size()==1){
                // set 1 marker
                Report reporte = reports.get(0);

                String[] loc = reporte.getLocation().split(",");
                String lat = loc[0];
                String lng = loc[1];

                LatLng neoLatLng = new LatLng(Double.parseDouble(lat),Double.parseDouble(lng));

                mMap.addMarker(new MarkerOptions()
                        .position(neoLatLng)
                        .snippet(reporte.getUserName()+"-"+reporte.getDate())
                        .title(reporte.getDescription())
                );

                mMap.addCircle(new CircleOptions()
                        .center(neoLatLng)
                        .radius(50)
                        .strokeColor(Color.RED)
                        .strokeWidth(2f)
                        .fillColor(Color.argb(50, 200, 20, 20)));

                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(neoLatLng, 16));

            }else{
                //set markers
                for (Report reporte : reports){

                    String[] loc = reporte.getLocation().split(",");
                    String lat = loc[0];
                    String lng = loc[1];
                    LatLng neoLatLng = new LatLng(Double.parseDouble(lat),Double.parseDouble(lng));

                    mMap.addMarker(new MarkerOptions()
                            .position(neoLatLng)
                            .snippet(reporte.getUserName()+"-"+reporte.getDate())
                            .title(reporte.getDescription())
                    );

                }
            }



        }

    }
}
