package com.example.user.apptest.abstract_classes;

import android.app.Fragment;
import android.support.v4.app.FragmentActivity;

/**
 * Created by User on 23.07.2016.
 */
public abstract class AbstractTabFragment extends android.support.v4.app.Fragment {
    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
