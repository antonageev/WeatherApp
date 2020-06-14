package com.antonageev.weatherapp.presenters;

public abstract class BasePresenter<M, V> {
    protected M model;
    protected V view;

    public void bindView(V view) {
        this.view = view;
        if (setupDone()) updateView();
    }

    public void unbindView(V view) {
        if (this.view == view) this.view = null;
    }

    public abstract void updateView();

    private boolean setupDone() {
        if (view != null && model != null) return true;
        return false;
    }

    public void setModel(M model) {
        this.model = model;
        if (setupDone()) updateView();
    }
}
