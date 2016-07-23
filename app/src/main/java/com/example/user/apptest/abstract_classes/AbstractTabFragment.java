package com.example.user.apptest.abstract_classes;

import android.app.Fragment;

/**
 * Created by User on 23.07.2016.
 */
public abstract class AbstractTabFragment extends Fragment {
    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
