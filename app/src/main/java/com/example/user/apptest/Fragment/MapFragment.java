package com.example.user.apptest.Fragment;


import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.user.apptest.R;
import com.example.user.apptest.abstract_classes.AbstractTabFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends AbstractTabFragment {

    private Context context;


    public MapFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    public static MapFragment getInstance(Context context){
        Bundle args = new Bundle();
        MapFragment mapFragment = new MapFragment();
        mapFragment.setArguments(args);
        mapFragment.setContext(context);
        return mapFragment;
    }


    public void setContext(Context context) {
        this.context = context;
    }

}
