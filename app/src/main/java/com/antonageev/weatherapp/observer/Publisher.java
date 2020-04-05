package com.antonageev.weatherapp.observer;

import com.antonageev.weatherapp.Parcel;

import java.util.ArrayList;
import java.util.List;

public class Publisher {
    private List<Observer> observers;

    public Publisher(){
        observers = new ArrayList<>();
    }

    public void subscribe(Observer observer){
        observers.add(observer);
    }

    public void unsubscribe(Observer observer){
        observers.remove(observer);
    }

    public void notify(Parcel parcel){
        for (Observer o: observers){
            o.updateFields(parcel);
        }
    }
}
