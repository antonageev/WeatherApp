package com.antonageev.weatherapp.ui.map;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
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
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        setHasOptionsMenu(true);
        return super.onCreateView(layoutInflater, viewGroup, bundle);
    }

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

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.map_fragment_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        final int id = item != null ? item.getItemId() : 0;

        // Map type - Normal
        if (id == R.id.menu_map_mode_normal) {
            mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        }
        // Map type - Satellite
        if (id == R.id.menu_map_mode_satellite) {
            mGoogleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
            return true;
        }
        // Map type - Terrain
        if (id == R.id.menu_map_mode_terrain) {
            mGoogleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
