package com.antonageev.weatherapp;

import android.os.Bundle;

import com.antonageev.weatherapp.presenters.BasePresenter;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class PresenterManager {

    private static final String KEY_PRESENTER_ID = "presenterId";
    private static PresenterManager instance;
    private Map<Long, BasePresenter> presenters;
    private AtomicLong currentId;

    private PresenterManager() {
        presenters = new HashMap<>();
        currentId = new AtomicLong();
    }

    public static PresenterManager getInstance() {
        if (instance == null) instance = new PresenterManager();
        return instance;
    }

    public void savePresenter(BasePresenter<?, ?> presenter, Bundle outState) {
        long presenterId = currentId.incrementAndGet();
        presenters.put(presenterId, presenter);
        outState.putLong(KEY_PRESENTER_ID, presenterId);
    }

    public <P extends BasePresenter<?, ?>> P restorePresenter(Bundle saveInstanceState) {
        long presenterId = saveInstanceState.getLong(KEY_PRESENTER_ID, -1);
        P presenter = (P) presenters.get(presenterId);
        presenters.remove(presenterId);
        return presenter;
    }

}
