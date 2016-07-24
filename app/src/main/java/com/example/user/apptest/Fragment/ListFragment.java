package com.example.user.apptest.Fragment;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.user.apptest.R;
import com.example.user.apptest.abstract_classes.AbstractTabFragment;
import com.example.user.apptest.animation.LatLngInterpolator;
import com.example.user.apptest.animation.MarkerAnimation;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Random;


public class ListFragment extends AbstractTabFragment implements OnMapReadyCallback {

    private Context context;
    protected View view;
    private static int counter = 1;



    public ListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_list, container, false);
        initMap();
        return view;
    }

    public static ListFragment getInstance(Context context){
        Bundle args = new Bundle();
        ListFragment listFragment = new ListFragment();
        listFragment.setArguments(args);
        listFragment.setContext(context);
        listFragment.setTitle(context.getString(R.string.tab_list));
        return listFragment;
    }


    public void setContext(Context context) {
        this.context = context;
    }

    private void initMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        final Random random = new Random();


        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(55, 55))
                .zoom(4)
                .build();
        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
        googleMap.moveCamera(cameraUpdate);
        googleMap.addMarker(new MarkerOptions().position(new LatLng(55, 55)));
        googleMap.getUiSettings().setAllGesturesEnabled(true);
        googleMap.getUiSettings().setMapToolbarEnabled(false);

        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

                Marker marker = googleMap.addMarker(new MarkerOptions().position(new LatLng(55, 55)).title("Маркер №" + counter));
                LatLngInterpolator latLngInterpolator = new LatLngInterpolator.Spherical();
                MarkerAnimation.animateMarkerToICS(marker, new LatLng(random.nextInt(57), random.nextInt(57)), latLngInterpolator);
                counter++;
            }
        });


    }
}
