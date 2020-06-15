package com.antonageev.weatherapp.ui.settings;

final class SettingsHandler {

    private static SettingsHandler instance = null;

    private static final Object mon = new Object(); //pojo

    private boolean darkTheme;
    private boolean MetersPerSecondChecked;
    private boolean KmPerHourChecked;
    private boolean CelsiusChecked;
    private boolean FhChecked;

    void setDarkTheme(boolean darkTheme) {
        this.darkTheme = darkTheme;
    }

    boolean isDarkTheme() {
        return darkTheme;
    }

    boolean isMetersPerSecondChecked() {
        return MetersPerSecondChecked;
    }

    void setMetersPerSecondChecked(boolean metersPerSecondChecked) {
        MetersPerSecondChecked = metersPerSecondChecked;
    }

    boolean isKmPerHourChecked() {
        return KmPerHourChecked;
    }

    void setKmPerHourChecked(boolean kmPerHourChecked) {
        KmPerHourChecked = kmPerHourChecked;
    }

    boolean isCelsiusChecked() {
        return CelsiusChecked;
    }

    void setCelsiusChecked(boolean celsiusChecked) {
        CelsiusChecked = celsiusChecked;
    }

    boolean isFhChecked() {
        return FhChecked;
    }

    void setFhChecked(boolean fhChecked) {
        FhChecked = fhChecked;
    }

    private SettingsHandler(){
        darkTheme = false;
        MetersPerSecondChecked = true;
        KmPerHourChecked = false;
        CelsiusChecked = true;
        FhChecked = false;
    }

    static SettingsHandler getInstance(){
        synchronized (mon){
            if (instance == null){
                instance = new SettingsHandler();
            }
            return instance;
        }
    }
}
