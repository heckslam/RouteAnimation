package com.example.user.apptest.direction;

import java.util.List;

/**
 * Created by ruslanaliev on 24.07.16.
 */
public interface DirectionFinderListener {
    void onDirectionFinderStart();
    void onDirectionFinderSuccess(List<Route> routes);
}
