package com.example.user.apptest.abstract_classes;


import android.support.v4.app.Fragment;


public abstract class AbstractTabFragment extends Fragment {
    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
