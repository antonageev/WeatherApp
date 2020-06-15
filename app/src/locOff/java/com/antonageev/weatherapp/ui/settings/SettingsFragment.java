package com.antonageev.weatherapp.ui.settings;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.Switch;

import com.antonageev.weatherapp.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;

public class SettingsFragment extends Fragment {

    private final int SIGN_IN_REQUEST_CODE = 99;
    private final String TAG = SettingsFragment.class.getSimpleName();

    private Switch switchDarkTheme;
    private RadioButton windMetersPerSecond;
    private RadioButton windKmPerHour;
    private RadioButton tempCels;
    private RadioButton tempFh;
    private SignInButton signInButton;
    private MaterialButton signOutButton;

    private MaterialTextView nameTextView;
    private MaterialTextView emailTextView;

    private SettingsHandler settingsHandler;

    GoogleSignInClient mGoogleSignInClient;
    GoogleSignInAccount account;
    private String serverClientId = "646907682960-m9059oujrp7avicla49l4nn2oo45lpt4.apps.googleusercontent.com";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        settingsHandler = SettingsHandler.getInstance();
        initViews(view);
        setListeners();

        googleSignInInit();

        account = GoogleSignIn.getLastSignedInAccount(requireActivity());
        updateUI(account);

        switchDarkTheme.setChecked(settingsHandler.isDarkTheme());
        setTextToSwitcher();
        windMetersPerSecond.setChecked(settingsHandler.isMetersPerSecondChecked());
        windKmPerHour.setChecked(settingsHandler.isKmPerHourChecked());
        tempCels.setChecked(settingsHandler.isCelsiusChecked());
        tempFh.setChecked(settingsHandler.isFhChecked());
    }

    private void googleSignInInit() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken(serverClientId)
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso);
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, SIGN_IN_REQUEST_CODE);
    }

    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(requireActivity(), new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        updateUI(null);
                    }
                });
    }

    private void updateUI(GoogleSignInAccount account) {
        if (account == null){
            signInButton.setVisibility(View.VISIBLE);
            signOutButton.setVisibility(View.INVISIBLE);
            nameTextView.setVisibility(View.INVISIBLE);
            emailTextView.setVisibility(View.INVISIBLE);
        } else {
            signInButton.setVisibility(View.INVISIBLE);
            signOutButton.setVisibility(View.VISIBLE);

            nameTextView.setVisibility(View.VISIBLE);
            String name = account.getGivenName() + " " + account.getFamilyName();
            nameTextView.setText(name);

            emailTextView.setVisibility(View.VISIBLE);
            String mail = account.getEmail();
            emailTextView.setText(mail);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SIGN_IN_REQUEST_CODE) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            account = completedTask.getResult(ApiException.class);
            updateUI(account);
        } catch (ApiException e) {
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            e.printStackTrace();
            updateUI(null);
        }
    }

    private void initViews(View view){
        switchDarkTheme = view.findViewById(R.id.switchDarkTheme);
        windMetersPerSecond = view.findViewById(R.id.radioButtonMS);
        windKmPerHour = view.findViewById(R.id.radioButtonKmHour);
        tempCels = view.findViewById(R.id.radioButtonCelsius);
        tempFh = view.findViewById(R.id.radioButtonFahrenheit);
        signInButton = view.findViewById(R.id.sign_in_button);
        nameTextView = view.findViewById(R.id.nameTextView);
        emailTextView = view.findViewById(R.id.emailTextView);

        signOutButton = view.findViewById(R.id.sign_out_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
    }

    private void setListeners(){
        switchDarkTheme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTextToSwitcher();
                settingsHandler.setDarkTheme(switchDarkTheme.isChecked());
            }
        });
        windMetersPerSecond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (windMetersPerSecond.isChecked()) {
                    settingsHandler.setMetersPerSecondChecked(true);
                    settingsHandler.setKmPerHourChecked(false);
                }
            }
        });
        windKmPerHour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (windKmPerHour.isChecked()){
                    settingsHandler.setKmPerHourChecked(true);
                    settingsHandler.setMetersPerSecondChecked(false);
                }
            }
        });
        tempCels.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tempCels.isChecked()){
                    settingsHandler.setCelsiusChecked(true);
                    settingsHandler.setFhChecked(false);
                }
            }
        });
        tempFh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tempFh.isChecked()){
                    settingsHandler.setFhChecked(true);
                    settingsHandler.setCelsiusChecked(false);
                }
            }
        });
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });
    }



    private void setTextToSwitcher() {
        if (switchDarkTheme.isChecked()) {
            switchDarkTheme.setText(R.string.textSwitchDarkThemeOn);
        } else {
            switchDarkTheme.setText(R.string.textSwitchDarkThemeOff);
        }
    }
}
