package com.antonageev.weatherapp;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.antonageev.weatherapp.broadcastreceivers.NetworkStateReceiver;

import com.antonageev.weatherapp.dagger.DaggerAppCompatActivityModule;
import com.antonageev.weatherapp.dagger.DaggerNetModule;
import com.antonageev.weatherapp.dagger.DaggerPreferencesModule;
import com.antonageev.weatherapp.dagger.DaggerSharedViewModelComponent;
import com.antonageev.weatherapp.dagger.DaggerViewModelModule;
import com.antonageev.weatherapp.dagger.SharedViewModelComponent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

public class MainActivity extends AppCompatActivity {

    public static String KEY_DARK_THEME = "keyDarkTheme";

    public static SharedViewModelComponent viewModelComponent;

    private static final String TAG = MainActivity.class.getSimpleName();
    NetworkStateReceiver networkStateReceiver = new NetworkStateReceiver();
    ConnectivityChecker connectivityChecker;

    public static final String WEATHER_FORECAST_INTENT_FILTER = "com.antonageev.weatherapp.forecast";
    public static final String LOCATION_INTENT_FILTER = "com.antonageev.weatherapp.location";

    private AppBarConfiguration mAppBarConfiguration;

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModelComponent = DaggerSharedViewModelComponent.builder()
                .daggerAppCompatActivityModule(new DaggerAppCompatActivityModule(this))
                .daggerViewModelModule(new DaggerViewModelModule())
                .daggerPreferencesModule(new DaggerPreferencesModule())
                .daggerNetModule(new DaggerNetModule())
                .build();

        setContentView(R.layout.activity_main);
        initNotificationChannel();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        setVisibilityOfMapMenuItem(navigationView);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_cities, R.id.nav_settings, R.id.nav_map)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        initTokenFromFirebaseForTesting();

        connectivityChecker = new ConnectivityChecker(this);

        registerReceiver(networkStateReceiver, new IntentFilter("com.antonageev.weatherapp.NetworkStateChange"));
    }

    public static SharedViewModelComponent getViewModelComponent() {
        return viewModelComponent;
    }



    private void setVisibilityOfMapMenuItem(NavigationView navigationView) {
        Menu menu = navigationView.getMenu();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            menu.findItem(R.id.nav_map).setVisible(true);
        } else {
            menu.findItem(R.id.nav_map).setVisible(false);
        }
    }


    private void initTokenFromFirebaseForTesting() {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("MainActivity", "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();

                        // Log and toast
                        String msg =  token;
                        Log.d("MainActivity", msg);
                    }
                });
    }

    private void initNotificationChannel(){
       if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("1", "LBC", importance);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(networkStateReceiver);
    }

}
