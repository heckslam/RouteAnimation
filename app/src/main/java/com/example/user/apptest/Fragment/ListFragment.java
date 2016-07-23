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
public class ListFragment extends AbstractTabFragment {

    private Context context;


    public ListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    public static ListFragment getInstance(Context context){
        Bundle args = new Bundle();
        ListFragment listFragment = new ListFragment();
        listFragment.setArguments(args);
        listFragment.setContext(context);
        return listFragment;
    }


    public void setContext(Context context) {
        this.context = context;
    }

}
