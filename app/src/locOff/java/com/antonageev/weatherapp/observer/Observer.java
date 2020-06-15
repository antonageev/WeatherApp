package com.antonageev.weatherapp.observer;

import com.antonageev.weatherapp.Parcel;

public interface Observer {
    void updateFields(Parcel parcel);
}
