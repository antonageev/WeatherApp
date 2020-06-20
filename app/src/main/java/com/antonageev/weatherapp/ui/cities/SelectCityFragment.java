package com.antonageev.weatherapp.ui.cities;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.antonageev.weatherapp.MainActivity;
import com.antonageev.weatherapp.OnCityAdapterItemClickListener;
import com.antonageev.weatherapp.PresenterManager;
import com.antonageev.weatherapp.R;
import com.antonageev.weatherapp.SharedViewModel;
import com.antonageev.weatherapp.presenters.SelectCityPresenter;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import javax.inject.Inject;

/**
 * A simple {@link Fragment} subclass.
 */
public class SelectCityFragment extends Fragment implements SelectCityView {

    private final String TAG = this.getClass().getSimpleName();

    private SelectCityPresenter selectCityPresenter;

    private MaterialButton findButton;
    private TextInputEditText editTextCity;
    private RecyclerView recyclerView;

    private DialogCustomFragment dlgCustom;

    private boolean mDualPane;

    @Inject
    SharedViewModel sharedViewModel;

    @Inject
    SharedPreferences sharedPreferences;

    private OnCityAdapterItemClickListener onCityAdapterItemClickListener = new OnCityAdapterItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            Snackbar.make(view, getResources().getString(R.string.snackBarSure), BaseTransientBottomBar.LENGTH_SHORT).
                    setAction(getResources().getString(R.string.confirm), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            selectCityPresenter.setSelectedCityByPosition(position);
                            showMainFragment();
                        }
                    }).show();
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_select_city, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MainActivity.getViewModelComponent().injectToSelectCityFragment(this);

        if (savedInstanceState == null) {
            selectCityPresenter = new SelectCityPresenter(onCityAdapterItemClickListener);
        } else {
            selectCityPresenter = PresenterManager.getInstance().restorePresenter(savedInstanceState);
        }

        dlgCustom = new DialogCustomFragment();

        initViews(view);
        setListeners();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        PresenterManager.getInstance().savePresenter(selectCityPresenter, outState);
    }

    @Override
    public void onPause() {
        super.onPause();
        selectCityPresenter.unbindView(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        selectCityPresenter.bindView(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mDualPane = (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE);

        initRecyclerView();

        if (mDualPane) Navigation.findNavController(requireActivity(), R.id.nav_host_fragment).navigate(R.id.nav_home);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.remove_context:
                selectCityPresenter.deleteSelectedCity();
                return true;
        }

        return super.onContextItemSelected(item);
    }

    private void initRecyclerView() {
        LinearLayoutManager lt = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(lt);
        recyclerView.hasFixedSize();
        recyclerView.setAdapter(selectCityPresenter.presenterGetAdapter());
    }

    private void showMainFragment() {
        Navigation.findNavController(requireActivity(), R.id.nav_host_fragment).navigate(R.id.nav_home);
    }

    private void initViews(View view){
        findButton = view.findViewById(R.id.findButton);
        editTextCity = view.findViewById(R.id.editTextCity);
        recyclerView = view.findViewById(R.id.recyclerView);
    }

    private void setListeners(){
        findButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectCityPresenter.presenterUpdateWeatherData(editTextCity.getText().toString());
            }
        });
    }

    public void onDialogResult(String resultDialog){
        Toast.makeText(getActivity(),"selected button: " + resultDialog, Toast.LENGTH_SHORT).show();
    }
}
