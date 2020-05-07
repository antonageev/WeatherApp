package com.antonageev.weatherapp.ui.map;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.navigation.Navigation;

import com.antonageev.weatherapp.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

public class MyMapFragment extends SupportMapFragment implements OnMapReadyCallback {

    GoogleMap mGoogleMap;

    @Override
    public void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    private void setUpMapIfNeeded() {
        if (mGoogleMap == null) {
            getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        mGoogleMap.setMyLocationEnabled(true);
        mGoogleMap.getUiSettings().setZoomControlsEnabled(true);
        try {
            LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            Location loc = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
            if (loc != null){
                LatLng target = new LatLng(loc.getLatitude(), loc.getLongitude());
                moveCamera(target, 15F);
            }
        } catch (SecurityException | NullPointerException e){
            e.printStackTrace();
            Navigation.findNavController(requireActivity(), R.id.nav_host_fragment).navigate(R.id.nav_home);
        }
    }

    private void moveCamera(LatLng target, float zoom){
        if (target == null || zoom < 1 ) return;
        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(target, zoom));
    }
}
