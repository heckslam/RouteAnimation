package com.example.user.apptest.adapter;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.support.v13.app.FragmentStatePagerAdapter;

import com.example.user.apptest.abstract_classes.AbstractTabFragment;
import com.example.user.apptest.Fragment.ListFragment;
import com.example.user.apptest.Fragment.MapFragment;

import java.util.HashMap;
import java.util.Map;

public class TabAdapter extends FragmentStatePagerAdapter {

    private Map<Integer, AbstractTabFragment> tabs;
    private Context context;

    public TabAdapter(Context context, FragmentManager fm) {
        super(fm);
        this.context = context;
        initTabsMap(context);
    }



    @Override
    public CharSequence getPageTitle(int position) {
        return tabs.get(position).getTitle();
    }


    @Override
    public Fragment getItem(int position) {
        return tabs.get(position);

    }

    @Override
    public int getCount() {
        return tabs.size();
    }

    private void initTabsMap(Context context) {
        tabs = new HashMap<>();
        tabs.put(0, MapFragment.getInstance(context));
        tabs.put(1, ListFragment.getInstance(context));
    }
}