package com.antonageev.weatherapp;

public final class SettingsHandler {

    private static SettingsHandler instance = null;

    private static final Object mon = new Object(); //pojo

    private boolean darkTheme;
    private boolean MetersPerSecondChecked;
    private boolean KmPerHourChecked;
    private boolean CelsChecked;
    private boolean FhChecked;

    public void setDarkTheme(boolean darkTheme) {
        this.darkTheme = darkTheme;
    }

    public boolean isDarkTheme() {
        return darkTheme;
    }

    public boolean isMetersPerSecondChecked() {
        return MetersPerSecondChecked;
    }

    public void setMetersPerSecondChecked(boolean metersPerSecondChecked) {
        MetersPerSecondChecked = metersPerSecondChecked;
    }

    public boolean isKmPerHourChecked() {
        return KmPerHourChecked;
    }

    public void setKmPerHourChecked(boolean kmPerHourChecked) {
        KmPerHourChecked = kmPerHourChecked;
    }

    public boolean isCelsChecked() {
        return CelsChecked;
    }

    public void setCelsChecked(boolean celsChecked) {
        CelsChecked = celsChecked;
    }

    public boolean isFhChecked() {
        return FhChecked;
    }

    public void setFhChecked(boolean fhChecked) {
        FhChecked = fhChecked;
    }

    private SettingsHandler(){
        darkTheme = false;
        MetersPerSecondChecked = true;
        KmPerHourChecked = false;
        CelsChecked = true;
        FhChecked = false;
    }

    public static SettingsHandler getInstance(){
        synchronized (mon){
            if (instance == null){
                instance = new SettingsHandler();
            }
            return instance;
        }
    }
}
