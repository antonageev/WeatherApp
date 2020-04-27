package com.antonageev.weatherapp;

import java.util.HashMap;
import java.util.Map;

public class MapWeatherLinks {

    public static String getLinkFromMap (int key){
        Map <Integer, String> weatherPictureLinks = new HashMap<>();
        weatherPictureLinks.put(2, "https://images.unsplash.com/photo-1472145246862-b24cf25c4a36?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1051&q=80");
        weatherPictureLinks.put(3, "https://images.unsplash.com/photo-1501691223387-dd0500403074?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=564&q=80");
        weatherPictureLinks.put(5, "https://images.unsplash.com/photo-1501691223387-dd0500403074?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=564&q=80");
        weatherPictureLinks.put(6, "https://images.unsplash.com/photo-1516715094483-75da7dee9758?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=967&q=80");
        weatherPictureLinks.put(7, "https://images.unsplash.com/photo-1485236715568-ddc5ee6ca227?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=633&q=80");
        weatherPictureLinks.put(8, "https://images.unsplash.com/photo-1419833173245-f59e1b93f9ee?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1050&q=80");
        return weatherPictureLinks.get(key);
    }



}
