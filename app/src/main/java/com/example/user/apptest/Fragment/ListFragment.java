package com.example.user.apptest.Fragment;


import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.user.apptest.R;
import com.example.user.apptest.abstract_classes.AbstractTabFragment;
import com.example.user.apptest.adapter.MyAdapter;
import com.example.user.apptest.direction.DirectionFinder;
import com.example.user.apptest.direction.DirectionFinderListener;
import com.example.user.apptest.direction.Route;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;


public class ListFragment extends AbstractTabFragment implements OnMapReadyCallback, DirectionFinderListener {

    private static int counter = 1;

    private Button btnFindPath;
    private EditText etOrigin;
    private EditText etDestination;

    private Context context;
    protected View view;
    // Keep track of our markers
    private List<Marker> markers = new ArrayList<Marker>();

    private GoogleMap mMap;
    private final Handler mHandler = new Handler();

    private Marker selectedMarker;

    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;



    public ListFragment() {
        // Required empty public constructor
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


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_list, container, false);


        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        btnFindPath = (Button) view.findViewById(R.id.button);
        etOrigin = (EditText) view.findViewById(R.id.etPoint1);
        etDestination = (EditText) view.findViewById(R.id.etPoint2);

        RecyclerView mRecyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new MyAdapter();
        mRecyclerView.setAdapter(mAdapter);

        btnFindPath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendRequest();
            }
        });

        return view;
    }

    private void sendRequest() {
        String origin = etOrigin.getText().toString() + " махачкала";
        String destination = etDestination.getText().toString() + " махачкала";
        if (origin.isEmpty()) {
            Toast.makeText(context, "Пожалуйста введите отправную точку", Toast.LENGTH_SHORT).show();
            return;
        }
        if (destination.isEmpty()) {
            Toast.makeText(context, "Пожалуйста введите конечную точку", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            new DirectionFinder(this, origin, destination).execute();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private Animator animator = new Animator();

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.getUiSettings().setAllGesturesEnabled(true);
        mMap.getUiSettings().setMapToolbarEnabled(false);

    }

    @Override
    public void onDirectionFinderStart() {
        clearMarkers();
    }

    @Override
    public void onDirectionFinderSuccess(List<Route> routes) {
        for (Route route : routes) {
            addMarkerToMap(route.startLocation);
            for (int i = 0; i < route.points.size(); i++) {
                addMarkerHidden(route.points.get(i));
            }
            addMarkerToMap(route.endLocation);
        }

        animator.startAnimation(true);
    }


    public class Animator implements Runnable {

        private static final int ANIMATE_SPEEED = 1500;
        private static final int ANIMATE_SPEEED_TURN = 1000;
        private static final int BEARING_OFFSET = 20;

        private final Interpolator interpolator = new LinearInterpolator();

        int currentIndex = 0;

        float tilt = 90;

        long start = SystemClock.uptimeMillis();

        LatLng endLatLng = null;
        LatLng beginLatLng = null;

        boolean showPolyline = false;

        private Marker trackingMarker;

        public void reset() {

            start = SystemClock.uptimeMillis();
            currentIndex = 0;
            endLatLng = getEndLatLng();
            beginLatLng = getBeginLatLng();

        }

        public void stop() {
            trackingMarker.remove();
            mHandler.removeCallbacks(animator);

        }

        public void initialize(boolean showPolyLine) {
            reset();
            this.showPolyline = showPolyLine;

            if (showPolyLine) {
                polyLine = initializePolyLine();
            }

            // We first need to put the camera in the correct position for the first run (we need 2 markers for this).....
            LatLng markerPos = markers.get(0).getPosition();
            LatLng secondPos = markers.get(1).getPosition();

            setupCameraPositionForMovement(markerPos, secondPos);

        }

        private void setupCameraPositionForMovement(LatLng markerPos,
                                                    LatLng secondPos) {



            float bearing = bearingBetweenLatLngs(markerPos,secondPos);

            trackingMarker = mMap.addMarker(new MarkerOptions().position(markerPos)
                    .title("тайтл"));


            CameraPosition cameraPosition =
                    new CameraPosition.Builder()
                            .target(markerPos)
                            .bearing(bearing + BEARING_OFFSET)
                            .tilt(90)
                            .zoom(mMap.getCameraPosition().zoom >=16 ? mMap.getCameraPosition().zoom : 16)
                            .build();

            mMap.animateCamera(
                    CameraUpdateFactory.newCameraPosition(cameraPosition),
                    ANIMATE_SPEEED_TURN,
                    new GoogleMap.CancelableCallback() {

                        @Override
                        public void onFinish() {
                            System.out.println("finished camera");
                            animator.reset();
                            Handler handler = new Handler();
                            handler.post(animator);
                        }

                        @Override
                        public void onCancel() {
                            System.out.println("cancelling camera");
                        }
                    }
            );
        }

        private Polyline polyLine;
        private PolylineOptions rectOptions = new PolylineOptions();


        private Polyline initializePolyLine() {
            //polyLinePoints = new ArrayList<LatLng>();
            rectOptions.add(markers.get(0).getPosition());
            return mMap.addPolyline(rectOptions);
        }

        /**
         * Add the marker to the polyline.
         */
        private void updatePolyLine(LatLng latLng) {
            List<LatLng> points = polyLine.getPoints();
            points.add(latLng);
            polyLine.setPoints(points);
        }


        public void stopAnimation() {
            animator.stop();
        }

        public void startAnimation(boolean showPolyLine) {
            if (markers.size()>2) {
                animator.initialize(showPolyLine);
            }
        }


        @Override
        public void run() {

            long elapsed = SystemClock.uptimeMillis() - start;
            double t = interpolator.getInterpolation((float)elapsed/ANIMATE_SPEEED);

//			LatLng endLatLng = getEndLatLng();
//			LatLng beginLatLng = getBeginLatLng();

            double lat = t * endLatLng.latitude + (1-t) * beginLatLng.latitude;
            double lng = t * endLatLng.longitude + (1-t) * beginLatLng.longitude;
            LatLng newPosition = new LatLng(lat, lng);

            trackingMarker.setPosition(newPosition);

            if (showPolyline) {
                updatePolyLine(newPosition);
            }

            if (t< 1) {
                mHandler.postDelayed(this, 16);
            } else {

                System.out.println("Move to next marker.... current = " + currentIndex + " and size = " + markers.size());
                // imagine 5 elements -  0|1|2|3|4 currentindex must be smaller than 4
                if (currentIndex<markers.size()-2) {

                    currentIndex++;

                    endLatLng = getEndLatLng();
                    beginLatLng = getBeginLatLng();


                    start = SystemClock.uptimeMillis();

                    LatLng begin = getBeginLatLng();
                    LatLng end = getEndLatLng();

                    float bearingL = bearingBetweenLatLngs(begin, end);


                    CameraPosition cameraPosition =
                            new CameraPosition.Builder()
                                    .target(end) // changed this...
                                    .bearing(bearingL  + BEARING_OFFSET)
                                    .tilt(tilt)
                                    .zoom(mMap.getCameraPosition().zoom)
                                    .build();


                    mMap.animateCamera(
                            CameraUpdateFactory.newCameraPosition(cameraPosition),
                            ANIMATE_SPEEED_TURN,
                            null
                    );

                    start = SystemClock.uptimeMillis();
                    mHandler.postDelayed(animator, 16);

                } else {
                    currentIndex++;
                    stopAnimation();
                }

            }
        }




        private LatLng getEndLatLng() {
            return markers.get(currentIndex+1).getPosition();
        }

        private LatLng getBeginLatLng() {
            return markers.get(currentIndex).getPosition();
        }
    };


    private Location convertLatLngToLocation(LatLng latLng) {
        Location loc = new Location("someLoc");
        loc.setLatitude(latLng.latitude);
        loc.setLongitude(latLng.longitude);
        return loc;
    }

    private float bearingBetweenLatLngs(LatLng begin,LatLng end) {
        Location beginL= convertLatLngToLocation(begin);
        Location endL= convertLatLngToLocation(end);

        return beginL.bearingTo(endL);
    }


    /**
     * Adds a marker to the map.
     */
    public void addMarkerToMap(LatLng latLng) {
        Marker marker = mMap.addMarker(new MarkerOptions().position(latLng)
                .title("Шаг " + String.valueOf(counter)));
        counter++;
        markers.add(marker);
    }

    /**
     * Adds hidden markers to the map.
     */
    public void addMarkerHidden(LatLng latLng) {
        Marker marker = mMap.addMarker(new MarkerOptions().position(latLng)
                .title("Шаг " + String.valueOf(counter))
                .alpha(0.0f));
        counter++;
        markers.add(marker);
    }



    /**
     * Clears all markers from the map.
     */
    public void clearMarkers() {
        mMap.clear();
        markers.clear();
        counter = 1;
    }

}