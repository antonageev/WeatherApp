package com.antonageev.weatherapp.ui.cities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.antonageev.weatherapp.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;

public class DialogCustomFragment extends DialogFragment {

    private TextInputEditText editTextCity;

    private MaterialTextView message;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_dialog, null);
        editTextCity = getParentFragmentManager().getPrimaryNavigationFragment().getView().findViewById(R.id.editTextCity);
        String city = editTextCity.getText().toString();
        String alertMessage = getResources().getString(R.string.cityNotFoundExclamation, city);
        message = view.findViewById(R.id.dialog_message);
        message.setText(alertMessage);
        view.findViewById(R.id.dialog_negative).setOnClickListener(listener);
        view.findViewById(R.id.dialog_positive).setOnClickListener(listener);
        return view;
    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dismiss();
            String message = ((MaterialButton)v).getText().toString();
            try {
                SelectCityFragment scf = (SelectCityFragment) getParentFragmentManager().getPrimaryNavigationFragment();
                if (scf != null){
                    scf.onDialogResult(message);
                }
            } catch (ClassCastException e){
                e.printStackTrace();
            }

        }
    };

}
