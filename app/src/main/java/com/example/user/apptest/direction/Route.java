package com.example.user.apptest.direction;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

/**
 * Created by ruslanaliev on 24.07.16.
 */
public class Route {
    public com.example.user.apptest.direction.Distance distance;
    public com.example.user.apptest.direction.Duration duration;
    public String endAddress;
    public LatLng endLocation;
    public String startAddress;
    public LatLng startLocation;

    public List<LatLng> points;

}
